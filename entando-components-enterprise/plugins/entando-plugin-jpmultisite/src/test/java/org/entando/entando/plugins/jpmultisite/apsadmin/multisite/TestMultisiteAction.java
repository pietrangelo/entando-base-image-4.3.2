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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.MultisiteTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteAction extends ApsAdminBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteAction.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		this._multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		MultisiteTestHelper.addMultisiteForTesting(_multisiteManager);
	}


	private String saveNewMultisite(String username, Multisite multisite) throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("strutsAction", "1");
		params.put("code", multisite.getCode());
		params.put("url", multisite.getUrl());
		params.put("titles_it", "titolo");
		params.put("titles_en", "title");
		params.put("descriptions_it", "descrizione");
		params.put("descriptions_en", "description");
		params.put("multisiteAdminPassword", "adminadmin");
		String result = this.executeSaveMultisite(username, params);
		return result;
	}

	private String executeSaveMultisite(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmultisite/Multisite", "save");
		this.addParameters(params);
		return this.executeAction();
	}

	public void testNew() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpmultisite/Multisite", "new");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testSaveDelete() throws Throwable {
		Multisite test = new Multisite("test", null, null, "http://127.0.0.1", null, null, "test");
		String result = this.saveNewMultisite("admin", test);
		assertEquals(Action.SUCCESS, result);
		UserDetails user = _userManager.getUser("admin@test");
		assertNotNull(user);
		assertEquals("admin@test", user.getUsername());
		Multisite loadMultisite = _multisiteManager.loadMultisite("test");
		assertNotNull(loadMultisite);

		this.setUserOnSession("admin");
		this.initAction("/do/jpmultisite/Multisite", "delete");
		Map<String, String> params = new HashMap<String, String>();
		params.put("strutsAction", "4");
		params.put("code", "test");
		this.addParameters(params);
		result = executeAction();
		assertEquals(Action.SUCCESS, result);
		loadMultisite = _multisiteManager.loadMultisite("test");
		assertNull(loadMultisite);
		user = _userManager.getUser("admin@test");
		assertNull(user);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		MultisiteTestHelper.deleteAllMultisite(_multisiteManager);
	}

	private ICategoryManager _categoryManager;
	private IMultisiteManager _multisiteManager;
	private IUserManager _userManager;

}
