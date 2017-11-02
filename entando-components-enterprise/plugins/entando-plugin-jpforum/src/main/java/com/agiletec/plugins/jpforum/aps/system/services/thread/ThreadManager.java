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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.IAttachManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.event.PostOperationEvent;

public class ThreadManager extends AbstractService implements IThreadManager {

	private static final Logger _logger =  LoggerFactory.getLogger(ThreadManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void addThread(Thread thread) throws ApsSystemException {
		try {
			String markup = thread.getPost().getText();
			String xml = this.getMarkupParser().markupToXML(markup);
			thread.getPost().setText(xml);
			int code = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			thread.setCode(code);
			thread.getPost().setCode(code);
			thread.getPost().setPostDate(new Date());
			thread.getPost().setThreadId(code);
			thread.getPost().setUsername(thread.getUsername());
			this.getThreadDAO().insertThread(thread);
			this.notifyPostChanging(thread.getPost(), PostOperationEvent.INSERT_OPERATION_CODE, null);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta thread", t);
			throw new ApsSystemException("Errore in aggiunta thread.", t);
		}
	}

	@Override
	public void deleteThread(int code) throws ApsSystemException {
		try {
			Thread thread = this.getThread(code);
			this.getThreadDAO().removeThread(code);

			Post post = new Post();
			post.setCode(code);
			this.notifyPostChanging(post, PostOperationEvent.REMOVE_THREAD_OPERATION_CODE, thread.getPostCodes());
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione thread", t);
			throw new ApsSystemException("Errore in eliminazione thread.", t);
		}
	}

	@Override
	public Thread getThread(int code) throws ApsSystemException {
		Thread thread = null;
		try {
			thread = this.getThreadDAO().loadThread(code);
		} catch (Throwable t) {
			_logger.error("Errore in recuero thread {}", code, t);
			throw new ApsSystemException("Errore in recuero thread " + code, t);
		}
		return thread;
	}

	@Override
	public Post getPost(int code) throws ApsSystemException {
		Post post = null;
		try {
			post = this.getThreadDAO().loadPost(code);
		} catch (Throwable t) {
			_logger.error("Errore in recuero post {}", code, t);
			throw new ApsSystemException("Errore in recuero post " + code, t);
		}
		return post;
	}

	@Override
	public void updatePost(Post post) throws ApsSystemException {
		try {
			String markup = post.getText();
			String xml = this.getMarkupParser().markupToXML(markup);
			post.setText(xml);
			this.getThreadDAO().updatePost(post);
			this.notifyPostChanging(post, PostOperationEvent.UPDATE_OPERATION_CODE, null);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento post {}", post.getCode(), t);
			throw new ApsSystemException("Errore in aggiornamento post " + post.getCode(), t);
		}
	}

	@Override
	public void deletePost(int code) throws ApsSystemException {
		try {
			this.getThreadDAO().deletePost(code);
			Post post = new Post();
			post.setCode(code);
			this.notifyPostChanging(post, PostOperationEvent.REMOVE_OPERATION_CODE, null);
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione post {}", code, t);
			throw new ApsSystemException("Errore in eliminazione post " + code , t);
		}
	}

	@Override
	public void addPost(Post post) throws ApsSystemException {
		try {
			String markup = post.getText();
			String xml = this.getMarkupParser().markupToXML(markup);
			post.setText(xml);

			int code = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			post.setCode(code);
			post.setPostDate(new Date());
			this.getThreadDAO().insertPost(post);
			this.notifyPostChanging(post, PostOperationEvent.INSERT_OPERATION_CODE, null);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta post", t);
			throw new ApsSystemException("Errore in aggiunta post.", t);
		}
	}

	@Override
	public void updateThread(Thread thread) throws ApsSystemException {
		try {
			this.getThreadDAO().updateThread(thread);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento post", t);
			throw new ApsSystemException("Errore in aggiornamento post.", t);
		}
	}

	@Override
	public Map<Integer, List<Integer>> getThreads(String sectionCode)	throws ApsSystemException {
		Map<Integer, List<Integer>> threads = new HashMap<Integer, List<Integer>>();
		try {
			threads = this.getThreadDAO().loadThreadsForSection(sectionCode);
		} catch (Throwable t) {
			_logger.error("Errore in recupero mappa threads-posts per la sezione {}", sectionCode, t);
			throw new ApsSystemException("Errore in recupero mappa threads-posts per la sezione " + sectionCode, t);
		}
		return threads;
	}

	@Override
	public List<Integer> getThreads(String sectionCode, Boolean pin) throws ApsSystemException {
		List<Integer> threads = new ArrayList<Integer>();
		try {
			threads = this.getThreadDAO().loadThreadsForSection(sectionCode, pin);
		} catch (Throwable t) {
			_logger.error("Errore in recupero identificativi threads per la sezione {}", sectionCode, t);
			throw new ApsSystemException("Errore in recupero identificativi threads per la sezione " + sectionCode, t);
		}
		return threads;
	}

	@Override
	public List<Integer> getUserPosts(String username) throws ApsSystemException {
		List<Integer> posts = new ArrayList<Integer>();
		try {
			posts = this.getThreadDAO().getUserPosts(username);
		} catch (Throwable t) {
			_logger.error("Errore in recupero identificativi posts per utente {}", username, t);
			throw new ApsSystemException("Errore in recupero identificativi posts per utente" + username, t);
		}
		return posts;
	}

	@Override
	public List<Integer> getUserThreads(String username) throws ApsSystemException {
		List<Integer> threads = new ArrayList<Integer>();
		try {
			threads = this.getThreadDAO().getUserThreads(username);
		} catch (Throwable t) {
			_logger.error("Errore in recupero identificativi threads per utente {}", username, t);
			throw new ApsSystemException("Errore in recupero identificativi threads per utente" + username, t);
		}
		return threads;
	}

	@Override
	public List<String> getUsersWithPosts() throws ApsSystemException {
		List<String> usernames = new ArrayList<String>();
		try {
			usernames = this.getThreadDAO().loadAllUsers();
		} catch (Throwable t) {
			_logger.error("Error loading the list of users from posts", t);
			throw new ApsSystemException("Error loading the list of users from posts", t);
		}
		return usernames;
	}

	@Override
	public List<Integer> getPosts() throws ApsSystemException {
		List<Integer> posts = new ArrayList<Integer>();
		try {
			posts = this.getThreadDAO().getPosts();
		} catch (Throwable t) {
			_logger.error("Errore in recupero identificativi posts", t);
			throw new ApsSystemException("Errore in recupero identificativi posts ", t);
		}
		return posts;
	}

	@Override
	public List<Integer> getPosts(String sectionId) throws ApsSystemException {
		List<Integer> posts = new ArrayList<Integer>();
		try {
			posts = this.getThreadDAO().getPosts(sectionId);
		} catch (Throwable t) {
			_logger.error("Error loading the posts under the section {}", sectionId, t);
			throw new ApsSystemException("Error loading the posts under the section " + sectionId, t);
		}
		return posts;
	}

	private void notifyPostChanging(Post post, int operationCode, List<Integer> postCodes) throws ApsSystemException {
		PostOperationEvent event = new PostOperationEvent();
		event.setOperationCode(operationCode);
		event.setPost(post);
		event.setPostCodes(postCodes);
		this.notifyEvent(event);
	}

	public void setThreadDAO(IThreadDAO threadDAO) {
		this._threadDAO = threadDAO;
	}
	protected IThreadDAO getThreadDAO() {
		return _threadDAO;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	public void setMarkupParser(IMarkupParser markupParser) {
		this._markupParser = markupParser;
	}
	protected IMarkupParser getMarkupParser() {
		return _markupParser;
	}

	public void setAttachManager(IAttachManager attachManager) {
		this._attachManager = attachManager;
	}
	protected IAttachManager getAttachManager() {
		return _attachManager;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IThreadDAO _threadDAO;
	private IMarkupParser _markupParser;
	private IAttachManager _attachManager;
}
