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
package com.agiletec.plugins.jpforum.aps.system.services.thread.attach;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.IAttachManager;

public class TestAttachManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetAttachDiskFolder() {
		String attachDiskFolder = _attachManager.getAttachDiskFolder();
		//System.out.println(attachDiskFolder);
		assertNotNull(attachDiskFolder);
	}

	public void testSaveAttachs() throws Throwable {
		List<Attach> userAttachs = _attachManager.getAttachs("admin");
		assertEquals(0, userAttachs.size());
		
		List<Attach> postAttachs = _attachManager.getAttachs(1);
		assertEquals(0, postAttachs.size());
		
		List<Attach> attachs = new ArrayList<Attach>();
		Attach attach = this.createTestAttach(1);
		attachs.add(attach);
		_attachManager.saveAttachs(attachs);
		
		userAttachs = _attachManager.getAttachs("admin");
		assertEquals(1, userAttachs.size());

		postAttachs = _attachManager.getAttachs(1);
		assertEquals(1, postAttachs.size());

		_attachManager.deleteAttach(1, "entando_logo.jpg");
		
		userAttachs = _attachManager.getAttachs("admin");
		assertEquals(0, userAttachs.size());

		postAttachs = _attachManager.getAttachs(1);
		assertEquals(0, postAttachs.size());
	}
	
	public void testDeleteAttachsOnDeletePost() throws Throwable {
		//CREATE THREAD
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
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
		
		List<Attach> userAttachs = _attachManager.getAttachs("admin");
		assertEquals(0, userAttachs.size());
		List<Attach> postAttachs = _attachManager.getAttachs(postCode);
		assertEquals(0, postAttachs.size());
		
		List<Attach> attachs = new ArrayList<Attach>();
		Attach attach = this.createTestAttach(postCode);
		attachs.add(attach);
		_attachManager.saveAttachs(attachs);
		
		userAttachs = _attachManager.getAttachs("admin");
		assertEquals(1, userAttachs.size());

		postAttachs = _attachManager.getAttachs(postCode);
		assertEquals(1, postAttachs.size());
		
		this._threadManager.deletePost(postCode);
		
		postAttachs = _attachManager.getAttachs(postCode);
		assertEquals(0, postAttachs.size());
		
	}
	
	private Attach createTestAttach(int postCode) throws Throwable {
		File file = new File("target/test/entando_logo.jpg");
		Attach attach = new Attach();
		attach.setFileName(file.getName());
		attach.setInputStream(new FileInputStream(file));
		attach.setPostCode(postCode);
		attach.setUserName("admin");
		return attach;
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
	
	private Post createTestPost(Thread thread) {
		Post post = new Post();
		post.setText("body");
		post.setTitle("title");
		post.setThreadId(thread.getCode());
		post.setUsername(thread.getUsername());
		return post;
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
		this._attachManager.deleteAttachs("admin");
		Map<Integer, List<Integer>> threads = _threadManager.getThreads(SECTION);
		if (!threads.isEmpty()) {
			Integer threadCode = Collections.max(threads.keySet());
			this._threadManager.deleteThread(threadCode);
		}
		super.tearDown();
	}

	private void init() {
		_threadManager = (IThreadManager) this.getService(JpforumSystemConstants.THREAD_MANAGER);
		_attachManager = (IAttachManager) this.getService(JpforumSystemConstants.ATTACH_MANAGER);
	}

	private IAttachManager _attachManager;
	private IThreadManager _threadManager;
	private static final String SECTION = "a";
}
