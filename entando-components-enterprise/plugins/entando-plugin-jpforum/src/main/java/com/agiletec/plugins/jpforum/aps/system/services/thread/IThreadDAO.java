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

import java.util.List;
import java.util.Map;

public interface IThreadDAO {

	public void insertThread(Thread thread);

	public void removeThread(int code);

	public Map<Integer, List<Integer>> loadThreadsForSection(String sectionCode);

	public Thread loadThread(int code);

	public Post loadPost(int code);

	public List<Integer> loadThreadsForSection(String sectionCode, Boolean pin);

	public void insertPost(Post post);

	public void updatePost(Post post);

	public void deletePost(int code);

	/**
	 * aggiorna pin e open
	 * @param thread
	 */
	public void updateThread(Thread thread);

	public List<Integer> getUserPosts(String username);

	public List<Integer> getUserThreads(String username);

	public List<Integer> getPosts();

	public List<Integer> getPosts(String sectionId);

	public List<String> loadAllUsers();
}
