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

package org.entando.entando.plugins.jpmultisite.apsadmin.common;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.MultisiteTestHelper;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.MultisiteConfigTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteSwitchAction extends ApsAdminBaseTestCase {
	
	//TODO ADD MORE TESTS

	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteSwitchAction.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void init() throws ApsSystemException {
		_logger.info(this.getClass().getName() + " init");
		IMultisiteManager multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
		assertNotNull(multisiteManager);
		this._multisiteManager = multisiteManager;
		MultisiteTestHelper.addMultisiteForTesting(_multisiteManager);
	}
	
	
	public void testSwitch() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmultisite", "switchMultisite");
		Map<String, String> params = new HashMap<String, String>();
		params.put("multisiteCode", "");
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.initAction("/do/jpmultisite", "switchMultisite");
		params = new HashMap<String, String>();
		params.put("multisiteCode", "test");
		this.addParameters(params);
		result = this.executeAction();
		assertEquals("multisite", result);
	}
	
	public void testSwitch_2() throws Throwable {
		this.setUserOnSession("admin@asd");
		this.initAction("/do/jpmultisite", "switchMultisite");
		Map<String, String> params = new HashMap<String, String>();
		params.put("multisiteCode", "");
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals("userNotAllowed", result);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		MultisiteTestHelper.deleteAllMultisite(_multisiteManager);
	}

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new MultisiteConfigTestUtils();
	}

	
	private IMultisiteManager _multisiteManager;

}
