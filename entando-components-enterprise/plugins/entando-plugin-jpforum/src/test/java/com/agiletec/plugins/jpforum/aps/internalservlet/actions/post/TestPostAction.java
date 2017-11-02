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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.post;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.post.PostAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.opensymphony.xwork2.Action;

public class TestPostAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testReplyPost() throws Throwable {
		String result = this.executeReplyPost("mainEditor", 1);
		assertEquals("jpforumlogin", result);

		result = this.executeReplyPost("", 1);
		assertEquals("jpforumlogin", result);
		
		result = this.executeReplyPost(FORUM_USER, 1);
		assertEquals(Action.SUCCESS, result);
	}

	public void testReplyPostWithClosedThread() throws Throwable {
		Thread thread = this._threadManager.getThread(1);
		thread.setOpen(false);
		_threadManager.updateThread(thread);
		
		String result = this.executeReplyPost(FORUM_USER, 1);
		assertEquals(Action.INPUT, result);
	}
	
	public void testReplyPostWithClosedSection() throws Throwable {
		Thread thread = this._threadManager.getThread(1);
		Section section = this._sectionManager.getSection(thread.getSectionId());
		section.setOpen(false);
		_sectionManager.updateSection(section);
		
		String result = this.executeReplyPost(FORUM_USER, 1);
		assertEquals(Action.INPUT, result);
		
	}

	public void testViewPost() throws Throwable {
		String result = this.executeViewPost(SystemConstants.GUEST_USER_NAME, 1);
		assertEquals(Action.SUCCESS, result);

		result = this.executeViewPost("", 1);
		assertEquals(Action.SUCCESS, result);

		//Visualizzazione di un post su sezione HIDDEN da parte di un utente senza i gruppi necessari
		Thread thread = this._threadManager.getThread(1);
		Section section = this._sectionManager.getSection(thread.getSectionId());
		section.getGroups().clear();
		section.addGroup("administrators");
		section.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_HIDDEN);
		_sectionManager.updateSection(section);
		result = this.executeViewPost("", 1);
		assertEquals(Action.INPUT, result);
		
		//Visualizzazione di un post su sezione HIDDEN da parte di un utente con i gruppi necessari
		result = this.executeViewPost("admin", 1);
		assertEquals(Action.SUCCESS, result);
		
		//Visualizzazione di un post su sezione READ ONLY da parte di un utente senza i gruppi necessari
		section.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_READONLY);
		_sectionManager.updateSection(section);
		result = this.executeViewPost("", 1);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testEditPost_1() throws Throwable {
		Post post = this.createTestPost(FORUM_USER, 1);
		this._threadManager.addPost(post);
		
		//EDIT DA PARTE DELL'UTENTE proprietario del post e thread aperto
		String result = this.executeEditPost(FORUM_USER, post.getCode());
		assertEquals(Action.SUCCESS, result);
		PostAction action = (PostAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		assertEquals(post.getTitle(), action.getTitle());
		assertEquals(post.getText(), action.getBody());
		assertEquals(post.getThreadId(), action.getThread());

		//EDIT DA PARTE DI DELL'UTENTE proprietario del post e thread chiuso
		Thread thread = this._threadManager.getThread(1);
		thread.setOpen(false);
		_threadManager.updateThread(thread);
		result = this.executeEditPost(FORUM_USER, post.getCode());
		assertEquals(Action.INPUT, result);

		//EDIT DA PARTE DI DELL'UTENTE proprietario del post e thread aperto e sezione chiusa
		thread = this._threadManager.getThread(1);
		thread.setOpen(true);
		_threadManager.updateThread(thread);
		Section section = this._sectionManager.getSection(thread.getSectionId());
		section.setOpen(false);
		_sectionManager.updateSection(section);
		result = this.executeEditPost(FORUM_USER, post.getCode());
		assertEquals(Action.INPUT, result);
		
		this._threadManager.deletePost(post.getCode());
	}
	
	public void testEditPost_2() throws Throwable {
		Post post = this.createTestPost(FORUM_USER, 1);
		this._threadManager.addPost(post);
		
		//EDIT DA PARTE di ADMIN
		String result = this.executeEditPost("admin", post.getCode());
		assertEquals(Action.SUCCESS, result);
		
		//EDIT DA PARTE di un moderatore  associato ALLA sezione
		result = this.executeEditPost(FORUM_MODERATOR, post.getCode());
		assertEquals(Action.SUCCESS, result);
		
		this._threadManager.deletePost(post.getCode());
	}
	
	public void testTrashPost() throws Throwable {
		Post post = this.createTestPost(FORUM_USER, 1);
		this._threadManager.addPost(post);
		
		//Trash DA PARTE DELL'UTENTE del post e thread aperto
		String result = this.executeTrashPost(FORUM_USER, post.getCode());
		assertEquals("jpforumlogin", result);

		//Trash DA PARTE DI UN MODERATORE ASSOCIATO
		Section section = this._sectionManager.getSection(SECTION);
		result = this.executeTrashPost(FORUM_MODERATOR, post.getCode());
		assertEquals(Action.SUCCESS, result);
		PostAction action = (PostAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.DELETE, action.getStrutsAction());
		assertEquals(post.getCode(), action.getPost());

		//Trash DA PARTE DI UN MODERATORE ASSOCIATO E SEZIONE CHIUSA
		section.setOpen(false);
		_sectionManager.updateSection(section);
		result = this.executeTrashPost(FORUM_MODERATOR, post.getCode());
		assertEquals(Action.INPUT, result);
				
		this._threadManager.deletePost(post.getCode());
	}

	public void testDeletePost() throws Throwable {
		Post post = this.createTestPost(FORUM_USER, 1);
		this._threadManager.addPost(post);
		
		Map<Integer, List<Integer>> threads = this._threadManager.getThreads(SECTION);
		List<Integer> posts = threads.get(1);
		assertEquals(2, posts.size());
		
		int strutsAction = ApsAdminSystemConstants.DELETE;
		String result = this.executeDeletePost("admin", post.getCode(), strutsAction);
		assertEquals(Action.SUCCESS, result);
		
		threads = this._threadManager.getThreads(SECTION);
		posts = threads.get(1);
		assertEquals(1, posts.size());
		
		this._threadManager.deletePost(post.getCode());
	}
	
	public void testSaveNewPost() throws Throwable {
		Map<Integer, List<Integer>> threads = this._threadManager.getThreads(SECTION);
		List<Integer> posts = threads.get(1);
		assertEquals(1, posts.size());
		
		String result = this.executeSaveNewPost(FORUM_USER, "title", "body", 1);
		assertEquals(Action.SUCCESS, result);
		
		threads = this._threadManager.getThreads(SECTION);
		posts = threads.get(1);
		assertEquals(2, posts.size());
	}

	public void testSaveNewPostWithErrors() throws Throwable {
		Map<Integer, List<Integer>> threads = this._threadManager.getThreads(SECTION);
		List<Integer> posts = threads.get(1);
		assertEquals(1, posts.size());
		
		String result = this.executeSaveNewPost(FORUM_USER, "", "", 1);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size()); 
		assertTrue(fieldErrors.containsKey("body"));
		
		threads = this._threadManager.getThreads(SECTION);
		posts = threads.get(1);
		assertEquals(1, posts.size());
	}
	
	private Post createTestPost(String username, int threadId) {
		Post post = new Post();
		post.setText("deleteMe");
		post.setTitle("deleteMe");
		post.setUsername(username);
		post.setThreadId(threadId);
		return post;
	}
	
	private String executeReplyPost(String username, int postId) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "replyPost");
		this.addParameter("post", postId);
		return this.executeAction();
	}

	private String executeViewPost(String username, int postId) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "viewPost");
		this.addParameter("post", postId);
		return this.executeAction();
	}

	private String executeEditPost(String username, int postId) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "editPost");
		this.addParameter("post", postId);
		return this.executeAction();
	}
	
	private String executeTrashPost(String username, int postId) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "trashPost");
		this.addParameter("post", postId);
		return this.executeAction();
	}

	private String executeDeletePost(String username, int postId, int strutsAction) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "deletePost");
		this.addParameter("strutsAction", strutsAction);
		this.addParameter("post", postId);
		return this.executeAction();
	}

	private String executeSaveNewPost(String username, String title, String body, int postId) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "savePost");
		this.setToken();
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("body", body);
		this.addParameter("title", title);
		this.addParameter("thread", postId);
		return this.executeAction();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Section section = this._sectionManager.getSection(SECTION);
		section.setOpen(true);
		section.getGroups().clear();
		section.addGroup("free");
		section.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_READONLY);
		this._sectionManager.updateSection(section);
		Thread thread = this._threadManager.getThread(1);
		thread.setOpen(true);
		thread.setPin(false);
		this._threadManager.updateThread(thread);
		Map<Integer, List<Integer>> threads = this._threadManager.getThreads(SECTION);
		List<Integer> posts = threads.get(1);
		for (int i = 0; i < posts.size(); i++) {
			int code = posts.get(i);
			//cancella tutti i posts tranne il thread
			if (code != 1) {
				this._threadManager.deletePost(code);
			}
		}
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
	private static final String NS = "/do/jpforum/Front/Post";
}
