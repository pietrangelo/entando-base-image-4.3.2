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
package com.agiletec.plugins.jpforum.aps.system.services.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

public class TestThreadManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testAddThread() throws Throwable {
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
		
		Thread thread = this.createTestThread("admin");
		this._threadManager.addThread(thread);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(1, threads.size());
		
		List<Integer> threadList = new ArrayList<Integer>();
		threadList.addAll(threads.keySet());
		Integer testThreadCode = threadList.get(0);
		
		Thread testTread = _threadManager.getThread(testThreadCode);
		assertNotNull(testTread);
		assertEquals(1, testTread.getPostCodes().size());
		assertEquals(SECTION, testTread.getSectionId());
		assertEquals("admin", testTread.getUsername());
		assertNotNull(thread.getPost());
		
		assertEquals(2, _threadManager.getUserThreads("admin").size());
		assertEquals(2, _threadManager.getUserPosts("admin").size());
		
		//CLEAN
		this._threadManager.deleteThread(testThreadCode);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
	}
	
	public void testUpdateThread() throws Throwable {
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
		//CREATE THREAD
		Thread thread = this.createTestThread("admin");
		this._threadManager.addThread(thread);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(1, threads.size());
		assertTrue(thread.isOpen());
		assertFalse(thread.isPin());
		
		int threadCode = thread.getCode();
		
		thread.setOpen(false);
		this._threadManager.updateThread(thread);
		thread = this._threadManager.getThread(threadCode);
		assertFalse(thread.isOpen());
		assertFalse(thread.isPin());

		thread.setPin(true);
		this._threadManager.updateThread(thread);
		thread = this._threadManager.getThread(threadCode);
		assertFalse(thread.isOpen());
		assertTrue(thread.isPin());

		thread.setPin(false);
		thread.setOpen(true);
		this._threadManager.updateThread(thread);
		thread = this._threadManager.getThread(threadCode);
		assertFalse(thread.isPin());
		assertTrue(thread.isOpen());
		
		this._threadManager.deleteThread(threadCode);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
	}
	
	public void testPostOperations() throws Throwable {
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
		//CREATE THREAD
		Thread thread = this.createTestThread("admin");
		this._threadManager.addThread(thread);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(1, threads.size());
		int threadCode = thread.getCode();
		//TEST ADD
		Post post = this.createTestPost(thread);
		this._threadManager.addPost(post);
		int postCode = post.getCode();
		thread = _threadManager.getThread(threadCode);
		List<Integer> postCodes = thread.getPostCodes();
		assertEquals(2, postCodes.size());
		
		Post addedPost = this._threadManager.getPost(postCode);
		assertEquals(post.getText(), addedPost.getText());
		assertEquals(post.getTitle(), post.getTitle());
		assertEquals(post.getThreadId(), addedPost.getThreadId());
		assertEquals(post.getUsername(), addedPost.getUsername());
		assertEquals(post.getPostDate(), addedPost.getPostDate());
		//TEST UPDATE
		addedPost.setText("mod_text");
		addedPost.setTitle("mod_title");
		this._threadManager.updatePost(addedPost);
		
		addedPost = this._threadManager.getPost(postCode);
		assertEquals("mod_text", _markupParser.XMLToMarkup(addedPost.getText()));
		assertEquals("mod_title", addedPost.getTitle());
		assertEquals(post.getThreadId(), addedPost.getThreadId());
		assertEquals(post.getUsername(), addedPost.getUsername());
		assertEquals(post.getPostDate(), addedPost.getPostDate());
		//TEST DELETE
		this._threadManager.deletePost(addedPost.getCode());
		thread = _threadManager.getThread(threadCode);
		postCodes = thread.getPostCodes();
		assertEquals(1, postCodes.size());
		this._threadManager.deleteThread(threadCode);
		threads = _threadManager.getThreads(SECTION);
		assertEquals(0, threads.size());
	}
	
	private Post createTestPost(Thread thread) {
		Post post = new Post();
		post.setText("body");
		post.setTitle("title");
		post.setThreadId(thread.getCode());
		post.setUsername(thread.getUsername());
		return post;
	}

	private Thread createTestThread(String username) {
		Thread thread = new Thread();
		thread.setOpen(true);
		thread.setPin(false);
		thread.setSectionId(SECTION);
		thread.setUsername(username);
		thread.setPost(this.createTestPost(username));
		return thread;
	}

	private Post createTestPost(String username) {
		Post post = new Post();
		post.setPostDate(new Date());
		post.setUsername(username);
		post.setTitle("test_title");
		post.setText("test_text");
		return post;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		if (!threads.isEmpty()) {
			Integer threadCode = Collections.max(threads.keySet());
			this._threadManager.deleteThread(threadCode);
		}
	}

	private void init() {
		this._threadManager = (IThreadManager) this.getService(JpforumSystemConstants.THREAD_MANAGER);
		this._markupParser = (IMarkupParser) this.getService(JpforumSystemConstants.MARKUP_PARSER);
	}
	
	private IThreadManager _threadManager;
	private IMarkupParser _markupParser;
	private static final String SECTION = "a";
}

