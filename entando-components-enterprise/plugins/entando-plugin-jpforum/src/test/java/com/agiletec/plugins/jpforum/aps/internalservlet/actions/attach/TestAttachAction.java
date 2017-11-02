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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.attach;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.IAttachManager;
import com.opensymphony.xwork2.Action;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

public class TestAttachAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testDeleteAttach() throws Throwable {
		List<Attach> userAttachs = _attachManager.getAttachs("admin");
		assertEquals(0, userAttachs.size());
		String fileSeparator = System.getProperty("file.separator");
		String path = this._attachManager.getAttachDiskFolder() + "admin" + fileSeparator;
		FileUtils.copyFile(new File("target/test/entando_logo.jpg"), new File(path + "1_entando_logo.jpg"));
		userAttachs = _attachManager.getAttachs("admin");
		assertEquals(1, userAttachs.size());
		
		String result = this.executeDeleteAttach("admin");
		assertEquals(Action.SUCCESS, result);
		
		userAttachs = _attachManager.getAttachs("admin");
		assertEquals(0, userAttachs.size());
		
	}

	private String executeDeleteAttach(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction(NS, "deleteAttach");
		this.addParameter("post", 1);
		this.addParameter("name", "entando_logo.jpg");
		this.addParameter("strutsAction", ApsAdminSystemConstants.DELETE);
		return this.executeAction();
	}

	private void init() {
		_attachManager = (IAttachManager) this.getService(JpforumSystemConstants.ATTACH_MANAGER);
	}
	
	private IAttachManager _attachManager;
	public static final String NS = "/do/jpforum/Front/User/Attach";
}
