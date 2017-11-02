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
package com.agiletec.plugins.jpforum.apsadmin.config;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.config.ForumConfig;
import com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager;
import com.agiletec.plugins.jpforum.apsadmin.config.ConfigAction;
import com.opensymphony.xwork2.Action;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

public class TestConfigAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testEdit() throws Throwable {
		String result = this.executeEdit();
		assertEquals(Action.SUCCESS, result);
		ConfigAction action = (ConfigAction) this.getAction();
		assertEquals(3, action.getConfig().getPostsPerPage());
		assertEquals("Nickname", action.getConfig().getProfileNickAttributeName());
		assertEquals("PFL", action.getConfig().getProfileTypecode());
	}

	public void testSave() throws Throwable {
		String result = this.executeSave(5, 10, "NICK", "CIT");
		assertEquals(Action.SUCCESS, result);
		ConfigAction action = (ConfigAction) this.getAction();
		assertEquals(10, action.getConfig().getPostsPerPage());
		assertEquals("NICK", action.getConfig().getProfileNickAttributeName());
		assertEquals("CIT", action.getConfig().getProfileTypecode());
	}
	
	private String executeEdit() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "edit");
		return this.executeAction();
	}

	private String executeSave(int attachsPerPost, int postsPerPage, String profileNickAttributeName, String profileTypecode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "save");
		this.addParameter("config.postsPerPage", postsPerPage);
		this.addParameter("config.attachsPerPost", attachsPerPost);
		this.addParameter("config.profileNickAttributeName", profileNickAttributeName);
		this.addParameter("config.profileTypecode", profileTypecode);
		return this.executeAction();
	}

	private void init() {
		_configManager = (IConfigManager) this.getService(JpforumSystemConstants.CONFIG_MANAGER);
	}

	private void restoreConfig(int postsPerPage, String profileNickAttributeName, String profileTypecode) throws Exception {
		ForumConfig config = new ForumConfig();
		config.setPostsPerPage(postsPerPage);
		config.setProfileNickAttributeName(profileNickAttributeName);
		config.setProfileTypecode(profileTypecode);
		this._configManager.updateConfig(config);
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.restoreConfig(3, "Nickname", "PFL");
		super.tearDown();
	}
	
	private IConfigManager _configManager;
	private static final String NS = "/do/jpforum/Config";
}
