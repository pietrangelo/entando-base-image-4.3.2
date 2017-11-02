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
package com.agiletec.plugins.jpforum.aps.system.services.statistics;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.IStatisticManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

public class TestStatisticManager extends JpforumBaseTestCase {

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

		assertEquals(0, this._statisticManager.getViews(thread.getCode()));
		this._statisticManager.viewThread("fake_session_id",thread.getCode());
		java.lang.Thread.sleep(500);
		assertEquals(1, this._statisticManager.getViews(thread.getCode()));
		this._threadManager.deleteThread(thread.getCode());
		assertEquals(0, this._statisticManager.getViews(thread.getCode()));
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

	private void init() {
		this._statisticManager = (IStatisticManager) this.getService(JpforumSystemConstants.STATISTIC_MANAGER);
		this._threadManager = (IThreadManager) this.getService(JpforumSystemConstants.THREAD_MANAGER);
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

	private IStatisticManager _statisticManager;
	private IThreadManager _threadManager;
	private static final String SECTION = "a";

}
