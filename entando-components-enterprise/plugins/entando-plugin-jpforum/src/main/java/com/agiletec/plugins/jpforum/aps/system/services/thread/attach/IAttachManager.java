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

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IAttachManager {
	
	public void saveAttachs(List<Attach> attachs) throws ApsSystemException;
	
	/**
	 * Restituisce una lista di allegati in base al codice del post
	 * @param code
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Attach> getAttachs(int code) throws ApsSystemException;
	
	public List<Attach> getAttachs(String username) throws ApsSystemException;

	public void deleteAttachs(int code) throws ApsSystemException;
	
	public void deleteAttachs(String username) throws ApsSystemException;
	
	public void deleteAttach (int code, String filename) throws ApsSystemException;

	public String getAttachDiskFolder();
	
	public int getAttachFolderMaxSize();
	
}
