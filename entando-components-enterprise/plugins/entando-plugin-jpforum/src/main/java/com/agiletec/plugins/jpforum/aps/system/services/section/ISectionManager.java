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
package com.agiletec.plugins.jpforum.aps.system.services.section;

import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISectionManager extends ITreeNodeManager {

	/**
	 * Restituisce una sezione in base all'identificativo
	 * @param code l'identificativo della sezione
	 * @return
	 */
	public Section getSection(String code);
	
	/**
	 * Aggiunge una sezione 
	 * L'elemento aggiunto finisce in coda agli altri dello stesso livello
	 * @param section L'oggetto da aggiungere
	 * @throws ApsSystemException in caso di errore
	 */
	public void addSection(Section section) throws ApsSystemException;

	/**
	 * Elimina una sezione e i gruppi e i moderatori associati
	 * @param code l'identificativo della sezione da eliminare
	 * @throws ApsSystemException
	 */
	public void deleteSection(String code) throws ApsSystemException;

	/**
	 * Aggiorna una sezione
	 * @param section la sezione da aggiornare
	 * @throws ApsSystemException
	 */
	public void updateSection(Section section)  throws ApsSystemException;

	/**
	 * Muove una sezione all'intero dell'albero.
	 * @param code
	 * @param moveUp
	 * @return
	 * @throws ApsSystemException
	 */
	public boolean moveSection(String code, boolean moveUp) throws ApsSystemException;
	
	/**
	 * Restituisce la lista di utenti, calcolata a runtime, abilitati a moderare la sezione
	 * @param section
	 * @return
	 * @throws ApsSystemException
	 */
	public List<String> getModerators(Section section) throws ApsSystemException;


}
