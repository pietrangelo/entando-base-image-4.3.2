/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsalesforce.apsadmin;

import javax.servlet.http.HttpSession;

import org.entando.entando.plugins.jpsalesforce.PluginConfigTestUtils;
import org.entando.entando.plugins.jpsalesforce.SalesforceTestSettings;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;

public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase implements SalesforceTestSettings {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		// restore original configuration
		_salesforceManager.updateConfiguration(_config);
	}
	
	private void init() throws Exception {
		this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._salesforceManager = (ISalesforceManager) this.getService(JpSalesforceConstants.SALESFORCE_MANAGER);
		
		try {
			_config = _salesforceManager.getConfig();
			SalesforceConfig testConfig = getConfigForTest();
			_salesforceManager.updateConfiguration(testConfig);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new Exception(t.getCause());
		}
	}
	
	protected SalesforceAccessDescriptor getAccessDescriptorInSession() throws Throwable {
		HttpSession session = this.getRequest().getSession();
		SalesforceConfig config = this.getSalesforceManager().getConfig();

		SalesforceAccessDescriptor sad = (SalesforceAccessDescriptor) session.getAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD);
		if (null == sad) {
			sad = this.getSalesforceManager().login(config);
			session.removeAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD);
			session.setAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD, sad);
		}
		assertNotNull(sad);
		assertNotNull(sad.getAccessToken());
		assertNotNull(sad.getInstaceUrl());
		return sad;
	}
	
	protected boolean abortTest() {
		Logger logger = LoggerFactory.getLogger(ApsAdminPluginBaseTestCase.class);
		boolean abort = (!(null != getSalesforceManager()
				&& null != getSalesforceManager().getConfig()
				&& getSalesforceManager().getConfig().isValidForUnmannedLogin()));
		
		if (abort) {
			logger.warn("\n**** SKIPPING TEST BECAUSE OF INVALID CONFIGURATION (edit the SalesforceTestSettings.java interface) ****");
		}
		return abort;
	}
	
	/**
	 * Create the configuration for the test
	 * @return
	 */
	private SalesforceConfig getConfigForTest() {
		final String CONFIG_TEMPLATE = "<config><username>%s</username><oid>%s</oid><app_secret>%s</app_secret><app_id>%s</app_id><auth_url_token>%s</auth_url_token><auth_url_new>%s</auth_url_new><api_version>%s</api_version><security_token>%s</security_token><password>%s</password></config>";
		String xml = String.format(CONFIG_TEMPLATE, 
				USER_ID,
				OID,
				CLIENT_SECRET,
				CLIENT_ID,
				TOKEN_ENDPOINT,
				LOGIN_ENDPOINT,
				VERSION,
				SECURITY_TOKEN,
				USER_PWD);
		SalesforceConfig config = new SalesforceConfig(xml);
		assertTrue(config.isValidForUnmannedLogin());
		return config;
	}
	
	public final static String JPSALESFORCE_FORM_WIDGETCODE = "jpsalesforce_publish_form";
	
	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}

	// original config
	private SalesforceConfig _config;
	
	private IPageManager _pageManager = null;
	private ConfigInterface _configManager = null;
	private ISalesforceManager _salesforceManager = null;
	
}
