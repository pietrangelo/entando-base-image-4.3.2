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

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

public class PostIndexLoaderThread extends Thread {

	private static final Logger _logger =  LoggerFactory.getLogger(PostIndexLoaderThread.class);

	public PostIndexLoaderThread(IForumIndexerDAO indexerDAO,	IForumSearchManager searchManager, IThreadManager manager) {
		this._forumIndexerDAO = indexerDAO;
		this._forumSearchManager = searchManager;
		this._threadManager = manager;
	}

	@Override
	public void run() {
		LastReloadInfo reloadInfo = new LastReloadInfo();
		try {
			this.loadNewIndex();
			reloadInfo.setResult(LastReloadInfo.ID_SUCCESS_RESULT);
		} catch (Throwable t) {
			reloadInfo.setResult(LastReloadInfo.ID_FAILURE_RESULT);
			_logger.error("error in run", t);
		} finally {
			reloadInfo.setDate(new Date());
			this.getForumSearchManager().notifyEndingIndexLoading(reloadInfo, this.getForumIndexerDAO());
			this.getForumSearchManager().sellOfQueueEvents();
		}
	}

	private void loadNewIndex() throws Throwable {
		
		try {
			List<Integer> posts = this.getThreadManager().getPosts();
			for (int i = 0; i < posts.size(); i++) {
				int code = posts.get(i).intValue();
				Post post = this.getThreadManager().getPost(code);
				if (post != null) {
					this.getForumIndexerDAO().add(post);
					_logger.debug("Indicizzato post {}", post.getCode());
				}
			}
			_logger.debug("Indicizzazione post effettuata");
		} catch (Throwable t) {
			_logger.error("error in loadNewIndex", t);
			throw t;
		}
	}

	public void setForumIndexerDAO(IForumIndexerDAO forumIndexerDAO) {
		this._forumIndexerDAO = forumIndexerDAO;
	}
	public IForumIndexerDAO getForumIndexerDAO() {
		return _forumIndexerDAO;
	}

	public void setForumSearchManager(IForumSearchManager forumSearchManager) {
		this._forumSearchManager = forumSearchManager;
	}
	public IForumSearchManager getForumSearchManager() {
		return _forumSearchManager;
	}

	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	public IThreadManager getThreadManager() {
		return _threadManager;
	}

	private IThreadManager _threadManager;
	private IForumIndexerDAO _forumIndexerDAO;
	private IForumSearchManager _forumSearchManager;
}
