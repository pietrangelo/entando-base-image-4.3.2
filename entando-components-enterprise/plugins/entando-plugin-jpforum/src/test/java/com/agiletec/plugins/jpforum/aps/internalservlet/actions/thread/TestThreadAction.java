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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.thread;

import java.util.List;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.thread.ThreadAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.opensymphony.xwork2.Action;

public class TestThreadAction extends JpforumApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNewThread() throws Throwable {
		String sectionCode = "";
		String currentUser ="";
		//NO USER
		sectionCode = "b";
		String result = this.executeNewThread(currentUser, sectionCode);
		assertEquals("jpforumlogin", result);

		//user without permissions
		currentUser = "mainEditor";
		result = this.executeNewThread(currentUser, sectionCode);
		assertEquals("jpforumlogin", result);
		
		//ok
		currentUser = FORUM_USER;
		result = this.executeNewThread(currentUser, sectionCode);
		List<String> errors = (List<String>) this.getAction().getActionErrors();
		assertEquals(0, errors.size());
		
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testViewThread() throws Throwable {
		String result = this.executeViewThrad(1);
		assertEquals(Action.SUCCESS, result);
		
	}

	public void testChangeStatus() throws Throwable {
		Thread thread = this._threadManager.getThread(1);
		assertTrue(thread.isOpen());
		//Change status by forum user
		String result = this.executeChangeStatus(FORUM_USER, 1);
		assertEquals("jpforumlogin", result);
		
		result = this.executeChangeStatus(FORUM_MODERATOR, 1);
		assertEquals(Action.SUCCESS, result);	
		
		thread = this._threadManager.getThread(1);
		assertFalse(thread.isOpen());
	}

	public void testChangePin() throws Throwable {
		Thread thread = this._threadManager.getThread(1);
		assertFalse(thread.isPin());
		//Change status by forum user
		String result = this.executeChangePin(FORUM_USER, 1);
		assertEquals("jpforumlogin", result);
		
		result = this.executeChangePin(FORUM_MODERATOR, 1);
		assertEquals(Action.SUCCESS, result);	
		
		thread = this._threadManager.getThread(1);
		assertTrue(thread.isPin());
	}

	public void testThrashThread() throws Throwable {
		Thread thread = this._threadManager.getThread(1);
		//Change status by forum user
		String result = this.executeTrashThread(FORUM_USER, 1);
		assertEquals("jpforumlogin", result);
		
		result = this.executeTrashThread(FORUM_MODERATOR, 1);
		assertEquals(Action.SUCCESS, result);	
		ThreadAction action = (ThreadAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.DELETE, action.getStrutsAction());
		assertEquals(thread.getCode(), action.getThread());
	}
	
	
	private String executeNewThread(String currentUser, String sectionCode) throws Throwable {
		if (null != currentUser && currentUser.trim().length() > 0) {
			this.setUserOnSession(currentUser);
		}
		this.initAction(NS, "newThread");
		this.addParameter("section", sectionCode);		
		return this.executeAction();
	}

	private String executeViewThrad(int thread) throws Throwable {
		this.initAction(NS, "viewThread");
		this.addParameter("thread", thread);		
		return this.executeAction();
	}

	private String executeChangeStatus(String username, int thread) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "changeStatus");
		this.addParameter("thread", thread);		
		return this.executeAction();
	}

	private String executeChangePin(String username, int thread) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "changePin");
		this.addParameter("thread", thread);		
		return this.executeAction();
	}

	private String executeTrashThread(String username, int thread) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "trashThread");
		this.addParameter("thread", thread);		
		return this.executeAction();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Section section = this._sectionManager.getSection(SECTION);
		this._sectionManager.updateSection(section);
		Thread thread = this._threadManager.getThread(1);
		thread.setOpen(true);
		thread.setPin(false);
		this._threadManager.updateThread(thread);
		this.waitNotifyingThread();
		super.tearDown();
	}

	private void init() {
		this._threadManager = (IThreadManager) this.getService(JpforumSystemConstants.THREAD_MANAGER);
		this._sectionManager = (ISectionManager) this.getService(JpforumSystemConstants.SECTION_MANAGER);
	}
	
	private IThreadManager _threadManager;
	private ISectionManager _sectionManager;
	private static final String SECTION = "b";
	private static final String NS = "/do/jpforum/Front/Thread";
}

