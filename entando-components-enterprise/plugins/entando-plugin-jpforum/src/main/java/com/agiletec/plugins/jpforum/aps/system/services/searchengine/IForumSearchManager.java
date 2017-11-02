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

import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

public interface IForumSearchManager {

	public void addToIndex(Post post) throws ApsSystemException;
	
	public void updateIndexElement(Post post) throws ApsSystemException;
	
	public void removeFromIndex(int code) throws ApsSystemException;
	
	/**
	 * Restituisce una lista di identificativi
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> searchPosts(String word, Set<String> userGroups) throws ApsSystemException;

	public Thread startReloadPostReferences() throws ApsSystemException;
	
    public static final int ID_STATE_READY = 0;
    public static final int ID_RELOAD_INDEX_IN_PROGRESS = 1;
    
	public void notifyEndingIndexLoading(LastReloadInfo reloadInfo,	IForumIndexerDAO forumIndexerDAO);

	public void sellOfQueueEvents();
}
