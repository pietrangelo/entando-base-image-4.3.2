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
package com.agiletec.plugins.jpforum.aps.system.services.config;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.config.ForumConfig;
import com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager;

public class TestConfigManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() {
		_configManager = (IConfigManager) this.getService(JpforumSystemConstants.CONFIG_MANAGER);
	}
	
	public void testInit() {
		assertNotNull(_configManager);
		assertEquals(3, _configManager.getPostsPerPage());
		assertEquals("Nickname", _configManager.getProfileNickAttributeName());
		assertEquals("PFL", _configManager.getProfileTypecode());
	}
	
	public void updateConfig() throws Throwable {
		this.restoreConfig(10, 11, "NICK", "CIT");
		assertEquals(10, _configManager.getPostsPerPage());
		assertEquals(11, _configManager.getAttachsPerPost());
		assertEquals("NICK", _configManager.getProfileNickAttributeName());
		assertEquals("CIT", _configManager.getProfileTypecode());
	}

	@Override
	protected void tearDown() throws Exception {
		this.restoreConfig(3, 2, "Nickname", "PFL");
		super.tearDown();
	}

	private void restoreConfig(int postsPerPage, int attachsPerPost, String profileNickAttributeName, String profileTypecode) throws Exception {
		ForumConfig config = new ForumConfig();
		config.setPostsPerPage(postsPerPage);
		config.setAttachsPerPost(attachsPerPost);
		config.setProfileNickAttributeName(profileNickAttributeName);
		config.setProfileTypecode(profileTypecode);
		this._configManager.updateConfig(config);
	}

	private IConfigManager _configManager;
	
}
