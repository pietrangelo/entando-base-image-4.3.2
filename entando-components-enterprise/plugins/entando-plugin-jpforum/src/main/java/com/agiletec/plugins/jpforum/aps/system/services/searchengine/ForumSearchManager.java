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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.event.PostOperationEvent;
import com.agiletec.plugins.jpforum.aps.system.services.thread.event.PostOperationObserver;

public class ForumSearchManager extends AbstractService implements IForumSearchManager, PostOperationObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumSearchManager.class);

	@Override
	public void updateFromPostOperation(PostOperationEvent event) {
		if (this.getState() == ID_RELOAD_INDEX_IN_PROGRESS) {
			this.getPostChangedEventQueue().add(0, event);
		} else {
			this.manageEvent(event);
		}
	}
	
	@Override
	public Thread startReloadPostReferences() throws ApsSystemException {
		PostIndexLoaderThread loaderThread = null;
		if (this.getState() == ID_STATE_READY) {
			try {
				Random r = new Random();
				int x = r.nextInt(1000);
				String now = new Long(new Date().getTime()).toString();
				_newTempSubDirectory = "forumindexdir" + now;
				IForumIndexerDAO newIndexer = this.getFactory().getIndexer(true, _newTempSubDirectory);
				loaderThread = new PostIndexLoaderThread(newIndexer, this, this.getThreadManager());
				String threadName = NotifyManager.NOTIFYING_THREAD_NAME + "_" + RELOAD_THREAD_NAME_PREFIX + now;
				loaderThread.setName(threadName);
				this.setState(ICmsSearchEngineManager.STATUS_RELOADING_INDEXES_IN_PROGRESS);
				loaderThread.start();
				_logger.info("Ricaricamento indici avviato");
			} catch (Throwable t) {
				throw new ApsSystemException("Errore in aggiornamento referenze", t);
			}
		} else {
			_logger.info("Ricaricamento indici sospeso : stato {}", this.getState());
		}
		return loaderThread;
	}

	@Override
	public void notifyEndingIndexLoading(LastReloadInfo reloadInfo, IForumIndexerDAO forumIndexerDAO) {
		try {
			if (reloadInfo.getResult() == LastReloadInfo.ID_SUCCESS_RESULT) {
				IForumSearcherDAO newSearcherDAO = this.getFactory().getSearcher(this._newTempSubDirectory);
				this.getFactory().updateSubDir(_newTempSubDirectory);
				this.setForumIndexerDAO(forumIndexerDAO);
				this.setForumSearcherDAO(newSearcherDAO);
				this.setReloadInfo(reloadInfo);
			} else if (null != this._newTempSubDirectory) {
				this.getFactory().deleteSubDirectory(this._newTempSubDirectory);
			}
		} catch (Throwable t) {
			_logger.error("error reloading LastReloadInfo", t);
		} finally {
			this.setState(ICmsSearchEngineManager.STATUS_READY);
			this._newTempSubDirectory = null;
		}
	}

	@Override
	public void sellOfQueueEvents() {
		int size = this.getPostChangedEventQueue().size();
		if (size>0) {
			List<PostOperationEvent> events = new ArrayList<PostOperationEvent>();
			events.addAll(this.getPostChangedEventQueue());
			for (int i=0; i<size; i++) {
				PostOperationEvent event = events.get(i);
				this.manageEvent(event);
				this.getPostChangedEventQueue().remove(event);
			}
		}
	}

	private void manageEvent(PostOperationEvent event) {
		_logger.debug( "{} - ricezione evento codice {}",this.getClass().getName(), event.getOperationCode());
		Post post= event.getPost();
		try {
			switch (event.getOperationCode()) {
			case PostOperationEvent.INSERT_OPERATION_CODE:
				this.addToIndex(post);
				break;
			case PostOperationEvent.REMOVE_OPERATION_CODE:
				this.removeFromIndex(post.getCode());
				break;
			case PostOperationEvent.REMOVE_THREAD_OPERATION_CODE:
				List<Integer> codes = event.getPostCodes();
				if (null != codes && !codes.isEmpty()) {
					for (int i = 0; i < codes.size(); i++) {
						this.removeFromIndex(codes.get(i));
					}
				}
				break;
			case PostOperationEvent.UPDATE_OPERATION_CODE:
				this.updateIndexElement(post);
				break;
			case PostOperationEvent.REFRESH_SECTIONS_TREE:
				
				this.startReloadPostReferences();
				break;	
			}
		} catch (Throwable t) {
			_logger.error("Errore in notificazione evento", t);
		}
	}
	
	@Override
	public void init() throws Exception {
		this.setForumIndexerDAO(this.getFactory().getIndexer(false));
		this.setForumSearcherDAO(this.getFactory().getSearcher());
		_logger.debug("{}: ready", this.getClass().getName());
	}

	@Override
	public void addToIndex(Post post) throws ApsSystemException {
		try {
			this.getForumIndexerDAO().add(post);
		} catch (ApsSystemException e) {
			_logger.error("Errore in aggiunta post in fase di indicizzazione", e);
			throw e;
		}
	}

	@Override
	public void removeFromIndex(int code) throws ApsSystemException {
		try {
			this.getForumIndexerDAO().delete(IForumIndexerDAO.POST_CODE_FIELD_NAME, code);
		} catch (ApsSystemException e) {
			_logger.error("Errore nella cancellazione post", e);
			throw e;
		}
	}

	@Override
	public List<Integer> searchPosts(String word, Set<String> userGroups) throws ApsSystemException {
		List<Integer> postCodes = new ArrayList<Integer>();
		try {
			postCodes = this.getForumSearcherDAO().searchPost(word, userGroups);
		} catch (ApsSystemException e) {
			_logger.error("Errore in ricerca lista identificativi post", e);
			throw e;
		}
		return postCodes;
	}

	@Override
	public void updateIndexElement(Post post) throws ApsSystemException {
		try {
			this.removeFromIndex(post.getCode());
			this.addToIndex(post);
		} catch (ApsSystemException e) {
			_logger.error("Errore nell'aggiornamento di un post", e);
			throw e;
		}
	}

	public void setFactory(IForumSearchDAOFactory factory) {
		this._factory = factory;
	}
	public IForumSearchDAOFactory getFactory() {
		return _factory;
	}

	public void setForumIndexerDAO(IForumIndexerDAO forumIndexerDAO) {
		this._forumIndexerDAO = forumIndexerDAO;
	}
	public IForumIndexerDAO getForumIndexerDAO() {
		return _forumIndexerDAO;
	}

	public void setForumSearcherDAO(IForumSearcherDAO forumSearcherDAO) {
		this._forumSearcherDAO = forumSearcherDAO;
	}
	public IForumSearcherDAO getForumSearcherDAO() {
		return _forumSearcherDAO;
	}

	public void setState(int state) {
		this._state = state;
	}
	public int getState() {
		return _state;
	}

	public void setNewTempSubDirectory(String newTempSubDirectory) {
		this._newTempSubDirectory = newTempSubDirectory;
	}
	public String getNewTempSubDirectory() {
		return _newTempSubDirectory;
	}

	public void setReloadInfo(LastReloadInfo reloadInfo) {
		this._reloadInfo = reloadInfo;
	}
	public LastReloadInfo getReloadInfo() {
		return _reloadInfo;
	}

	public void setPostChangedEventQueue(List<PostOperationEvent> postChangedEventQueue) {
		this._postChangedEventQueue = postChangedEventQueue;
	}
	public List<PostOperationEvent> getPostChangedEventQueue() {
		return _postChangedEventQueue;
	}

	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	protected IThreadManager getThreadManager() {
		return _threadManager;
	}

	private IForumSearchDAOFactory _factory;
	private IForumIndexerDAO _forumIndexerDAO;
	private IForumSearcherDAO _forumSearcherDAO;
	private IThreadManager _threadManager;
	private int _state;
	private String _newTempSubDirectory;
	private LastReloadInfo _reloadInfo;
	private List<PostOperationEvent> _postChangedEventQueue = new ArrayList<PostOperationEvent>();

	public static final String RELOAD_THREAD_NAME_PREFIX = "RELOAD_FORUM_INDEX_";

}
