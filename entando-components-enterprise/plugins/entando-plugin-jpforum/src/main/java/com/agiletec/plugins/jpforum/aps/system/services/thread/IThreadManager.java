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

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IThreadManager {

	/**
	 * Aggiunge un thread ed il relativo un post. L'identificativo del thread e del post sarà lo stesso.
	 * @param thread
	 * @return
	 * @throws ApsSystemException
	 */
	public void addThread(Thread thread) throws ApsSystemException;

	/**
	 * Elimina un thread e tutti i post ad associati
	 * @param code
	 * @throws ApsSystemException
	 */
	public void deleteThread(int code) throws ApsSystemException;

	/**
	 * Aggiorna un thread (open e pin)
	 * @param thread
	 * @throws ApsSystemException
	 */
	public void updateThread(Thread thread) throws ApsSystemException;

	/**
	 * Recupera un thread
	 * @param code
	 * @return
	 * @throws ApsSystemException
	 */
	public Thread getThread(int code) throws ApsSystemException;

	/**
	 * Restituisce un post
	 * @param code
	 * @return
	 * @throws ApsSystemException
	 */
	public Post getPost(int code) throws ApsSystemException;

	/**
	 * Aggiunge un post
	 * @param post
	 * @throws ApsSystemException
	 */
	public void addPost(Post post) throws ApsSystemException;

	/**
	 * Aggiorna un post
	 * @param post
	 * @throws ApsSystemException
	 */
	public void updatePost(Post post) throws ApsSystemException;

	/**
	 * Elimina un post
	 * @param code
	 * @throws ApsSystemException
	 */
	public void deletePost(int code) throws ApsSystemException;

	/**
	 * Restituisce una mappa di identificativi [thread][Lista di posts] di una sezione
	 * @param sectionCode
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<Integer, List<Integer>> getThreads(String sectionCode) throws ApsSystemException;

	/**
	 * Restituisce una la lista dei threads di una sezione in base al parametro pin.
	 * I thread sono ordinati in base al post più recente
	 * @param sectionCode il codice della sezione
	 * @param pin boolen, can be null. se true restituisce esclusivamente i threads pinnati, se false restituisce solo i threads non pinnati
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getThreads(String sectionCode, Boolean pin) throws ApsSystemException;


	/**
	 * Restituisce la lista di identificativi di Threads aperti dall'utente specificato
	 * @param username
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getUserThreads(String username) throws ApsSystemException;

	/**
	 * Restituisce la lista di identificativi di posts creati dall'utente, threads compresi
	 * @param username
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getUserPosts(String username) throws ApsSystemException;

	public List<Integer> getPosts() throws ApsSystemException;

	/**
	 * Loads the post ids under the specified section, from the latest to the oldest
	 * @param sectionId
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getPosts(String sectionId) throws ApsSystemException;

	/**
	 * Load the list of all the usernames that published a post or a thread at least once
	 * @return
	 * @throws ApsSystemException
	 */
	public List<String> getUsersWithPosts() throws ApsSystemException;


	
//	public void saveAttachs(List<Attach> attachs) throws ApsSystemException;
//
//	public List<Attach> getAttachs(int code) throws ApsSystemException;
//	
//	public List<Attach> getAttachs(String username) throws ApsSystemException;
//
//	public void deleteAttachs(int code) throws ApsSystemException;
//	
//	public void deleteAttach (int code, String filename) throws ApsSystemException;
//
//	public String getAttachDiskFolder();
}
