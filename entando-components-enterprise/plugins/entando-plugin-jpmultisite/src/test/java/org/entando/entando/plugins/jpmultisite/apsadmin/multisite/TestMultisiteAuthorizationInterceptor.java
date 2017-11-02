/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.multisite;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteAuthorizationInterceptor extends ApsAdminBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteAuthorizationInterceptor.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_multisiteManager = (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.getRequest());
	}
	
	
	
	public void testSuccessSwitch() throws Throwable {
		super.setUp();
		String multisiteCode = (String) this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		assertNull(multisiteCode);
		String result = this.executeSwitch("admin");
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testSuccessSwitch_2() throws Throwable {
		addMultisites();
		_multisiteConfigTestUtils = new MultisiteConfigTestUtils("http://test.com");
		super.setUp();
		String multisiteCode = (String) this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		assertNull(multisiteCode);
		String result = this.executeSwitch("admin@asd2");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeSwitch(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do", "switchMultisite");
		String result = super.executeAction();
		return result;
	}

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return _multisiteConfigTestUtils;
	}
	
	private void addMultisites() throws ApsSystemException {
		ApsProperties titles = new ApsProperties();
		titles.put("en", "test_title");
		titles.put("it", "test_title");
		ApsProperties descriptions = new ApsProperties();
		descriptions.put("en", "test_descriptions");
		descriptions.put("it", "test_descriptions");
		Multisite multisite = new Multisite("asd", titles, descriptions, "http://test.com", null, null, "asd");
		if (_multisiteManager.loadMultisite("asd") == null) {
			User user = new User();
			user.setUsername("admin@asd");
			user.setPassword("admin@asd");
			_multisiteManager.addMultisite(multisite, user);
		}
		multisite = new Multisite("asd2", titles, descriptions, "http://testp.com", null, null, "asd2");
		if (_multisiteManager.loadMultisite("asd2") == null) {
			User user = new User();
			user.setUsername("admin@asd2");
			user.setPassword("admin@asd2");
			_multisiteManager.addMultisite(multisite, user);
		}
		assertEquals(2, _multisiteManager.loadMultisites().size());
	}
	
	@Override
	protected void tearDown() throws Exception {
		List<String> loadMultisites = this._multisiteManager.loadMultisites();
		for (String code : loadMultisites) {
			_logger.debug("tear down - deleting {}", code);
			this._multisiteManager.deleteMultisite(code);
		}
	}

	private MultisiteConfigTestUtils _multisiteConfigTestUtils = new MultisiteConfigTestUtils();
	
	private IMultisiteManager _multisiteManager;

}
