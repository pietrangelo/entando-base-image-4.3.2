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
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISanctionManager {

	/**
	 * Aggiunge una sanzione
	 * @param sanction
	 * @throws ApsSystemException
	 */
	public void addSanction(Sanction sanction) throws ApsSystemException;

	/**
	 * Elimina una sanzione 
	 * @param id
	 * @throws ApsSystemException
	 */
	public void deleteSanction(int id) throws ApsSystemException;

	/**
	 * Restituisce una sanzione
	 * @param id
	 * @return
	 * @throws ApsSystemException
	 */
	public Sanction getSanction(int id) throws ApsSystemException;

	/**
	 * Restituisce la lista delle sanzioni applicate ad un utente
	 * @param username
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getSanctions(String username) throws ApsSystemException;

	/**
	 * Elimina tutte le sanzioni per un utente
	 * @param username
	 * @throws ApsSystemException
	 */
	public void deleteSanctions(String username) throws ApsSystemException;
	
	/**
	 * Verifica se un utente è sotto sanzione
	 * @param username
	 * @return
	 * @throws ApsSystemException
	 */
	public boolean isUnderSanction(String username) throws ApsSystemException;
}
