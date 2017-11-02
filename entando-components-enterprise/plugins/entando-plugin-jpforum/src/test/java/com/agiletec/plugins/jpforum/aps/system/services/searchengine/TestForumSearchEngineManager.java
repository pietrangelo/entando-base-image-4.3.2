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
package com.agiletec.plugins.jpforum.aps.system.services.searchengine;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

public class TestForumSearchEngineManager extends JpforumBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.cleanThreads();
	}
	
	public void testReload() throws Throwable {
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
		Thread thread = this.createTestThread("admin", SECTION);
		this._threadManager.addThread(thread);
		this.waitNotifyingThread();
		threads = _threadManager.getThreads(SECTION);
		assertEquals(1, threads.size());
		Set<String> userGroups = new HashSet<String>();
		userGroups.add(Group.FREE_GROUP_NAME);
		List<Integer> posts = this._searchManager.searchPosts("solitudine", userGroups);
		assertNotNull(posts);
		assertEquals(1, posts.size());
		
		posts = this._searchManager.searchPosts("matrimonio", userGroups);
		assertNotNull(posts);
		assertEquals(1, posts.size());
	}
	/*
	TODO to check
	public void testSearchWrongGroups() throws Throwable {
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(ADMIN_SECTION);
		assertEquals(0, threads.size());
		Thread thread = this.createTestThread("admin", ADMIN_SECTION);
		this._threadManager.addThread(thread);
		this.waitNotifyingThread();
		threads = _threadManager.getThreads(ADMIN_SECTION);
		assertEquals(1, threads.size());
		Set<String> userGroups = new HashSet<String>();
		userGroups.add(Group.FREE_GROUP_NAME);
		List<Integer> posts = this._searchManager.searchPosts("solitudine", userGroups);
		assertNotNull(posts);
		assertEquals(1, posts.size());
		
		posts = this._searchManager.searchPosts("matrimonio", userGroups);
		assertNotNull(posts);
		assertEquals(1, posts.size());
	}
	*/
	private Thread createTestThread(String username, String section) {
		Thread thread = new Thread();
		thread.setOpen(true);
		thread.setPin(false);
		thread.setSectionId(section);
		thread.setUsername(username);
		thread.setPost(this.createTestPost(username));
		return thread;
	}

	private Post createTestPost(String username) {
		Post post = new Post();
		post.setPostDate(new Date());
		post.setUsername(username);
		post.setTitle("il matrimonio");
		post.setText("Se temete la solitudine, non sposatevi");
		return post;
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.cleanThreads();
		super.tearDown();
	}
	
	private void init() {
		this._threadManager = (IThreadManager) this.getService(JpforumSystemConstants.THREAD_MANAGER);
		this._searchManager = (IForumSearchManager) this.getService(JpforumSystemConstants.SEARCH_MANAGER);
	}
	
	protected void cleanThreads() throws Exception {
		Map<Integer, List<Integer>> threads1 = _threadManager.getThreads(ADMIN_SECTION);
		if (!threads1.isEmpty()) {
			Integer threadCode = Collections.max(threads1.keySet());
			this._threadManager.deleteThread(threadCode);
		}
		this.waitNotifyingThread();
		Map<Integer, List<Integer>> threads2 = _threadManager.getThreads(SECTION);
		if (!threads2.isEmpty()) {
			Integer threadCode = Collections.max(threads2.keySet());
			this._threadManager.deleteThread(threadCode);
		}
		this.waitNotifyingThread();
	}
	
	private IForumSearchManager _searchManager;
	private IThreadManager _threadManager;
	private static final String SECTION = "a";
	private static final String ADMIN_SECTION = "s";
	
}