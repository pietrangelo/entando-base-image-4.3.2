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
package org.entando.entando.plugins.jpsalesforce.aps.system;

import org.entando.entando.plugins.jpsalesforce.aps.ApsPluginManagerTestCase;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.ISalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.IPortalSubscriptionManager;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormBinding;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;

public class JpSalesforceBaseTestCase extends ApsPluginManagerTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		try {
			// init services
			this._salesforceManager = (ISalesforceManager) super.getService(JpSalesforceConstants.SALESFORCE_MANAGER);
			this._messageManager = (IMessageManager) super.getService(JpwebdynamicformSystemConstants.MESSAGE_MANAGER);
			this._salesforceFormManager = (ISalesforceFormManager) this.getService(JpSalesforceConstants.SALESFORCE_FORM_MANAGER);
			this._configInterface = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}
	
	protected FormConfiguration getFormProfileBindingConfiguration() throws Throwable {
		FormConfiguration cfg = new FormConfiguration("myGreatCampaign", "PFL");
		cfg.getRemainingLeadFields().add("lang");
		cfg.getRemainingLeadFields().add("mail");
		cfg.getRemainingLeadFields().add("birth");
		cfg.getRemainingLeadFields().add("firstname");
		cfg.getRemainingProfileFields().add(EMAIL);
		cfg.getRemainingProfileFields().add(BIRTHDATE);
		cfg.getRemainingProfileFields().add(LANGUAGE);
		cfg.getRemainingProfileFields().add(FULLNAME);
		cfg.setDescription("test description");
		// TODO mandatory fields?
		// 0 - fullname
		ApsProperties labels = getLabels("0");
		FormBinding bind = new FormBinding("firstname", FULLNAME, true, labels, 0);
		cfg.addFieldAssociation(bind);
		// 1 - language
		labels = getLabels("1");
		bind = new FormBinding("lang", LANGUAGE, true, labels, 1);
		cfg.addFieldAssociation(bind);
		// 2 - email
		labels = getLabels("2");
		bind = new FormBinding("mail", EMAIL, true, labels, 2);
		cfg.addFieldAssociation(bind);
		// 3 - birthdate
		labels = getLabels("3");
		bind = new FormBinding("birth", BIRTHDATE, true, labels, 3);
		cfg.addFieldAssociation(bind);
		return cfg;
	}
	
	protected void insertConfiguration() throws Throwable {
		try {
			_configInterface.updateConfigItem(IPortalSubscriptionManager.ENTANDO_SUBSCRIPTION_CONFIG_ITEM, "");
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private ApsProperties getLabels(String id) {
		ApsProperties prop = new ApsProperties();
		prop.put("en", "label en ".concat(id));
		prop.put("it", "label it ".concat(id));
		return prop;
	}
	
	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}

	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}
	
	public IMessageManager getMessageManager() {
		return _messageManager;
	}

	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}

	public ISalesforceFormManager getSalesforceFormManager() {
		return _salesforceFormManager;
	}

	public void setSalesforceFormManager(ISalesforceFormManager salesforceFormManager) {
		this._salesforceFormManager = salesforceFormManager;
	}
	
	public ConfigInterface getConfigInterface() {
		return _configInterface;
	}

	public void setConfigInterface(ConfigInterface configInterface) {
		this._configInterface = configInterface;
	}

	private ISalesforceManager _salesforceManager;
	private IMessageManager _messageManager;
	private ISalesforceFormManager _salesforceFormManager;
	private ConfigInterface _configInterface;
	
	
	public static final String EMAIL = "email";
	public static final String BIRTHDATE = "birthdate";
	public static final String LANGUAGE = "language";
	public static final String FULLNAME = "fullname";
}
