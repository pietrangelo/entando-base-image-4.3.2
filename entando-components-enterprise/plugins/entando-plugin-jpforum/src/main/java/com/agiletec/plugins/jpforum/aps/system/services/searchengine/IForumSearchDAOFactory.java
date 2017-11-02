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

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IForumSearchDAOFactory {

	/**
	 * Inizializzazione della classe factory.
	 * @throws Exception In caso di errore.
	 */
	public void init() throws Exception;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di indicizzazione.
	 * @param newIndex Discrimina la costruzione.
	 * @return La classe dao delegata alle operazioni di indicizzazione.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IForumIndexerDAO getIndexer(boolean newIndex) throws ApsSystemException;
	
	public IForumIndexerDAO getIndexer(boolean newIndex, String subDir) throws ApsSystemException;
	
	/**
	 * Restituisce la classe dao delegata alle operazioni di ricerca.
	 * @return La classe dao delegata alle operazioni di ricerca.
	 * @throws ApsSystemException In caso nella errore.
	 */
	public IForumSearcherDAO getSearcher() throws ApsSystemException;
	
	public IForumSearcherDAO getSearcher(String subDir) throws ApsSystemException;
	
	
	public void updateSubDir(String newSubDirectory) throws ApsSystemException;
	
	public void deleteSubDirectory(String subDirectory);

}
