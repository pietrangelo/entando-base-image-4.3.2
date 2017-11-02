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
package com.agiletec.plugins.jpforum.aps.system.services.statistics;

import java.util.Collection;
import java.util.Set;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia del servizio di gestione statistiche forum.
 * Il bean che implementa l'interfaccia ha scope session.
 * Ogni volta che viene visitato un thread viene segnata la visita e
 * l'identificatovo del thread viene salvato in un Set in modo tale
 * da segnare la visita solo una volta per sessione.
 * Tramote AOP viene creato il record delle visite in fase di creazione del
 * thread e viene eliminato in fase di eliminazione del thread.
 *
 */
public interface IStatisticManager {

	/**
	 * Aggiunge, se necessario, una visita al thread.
	 * @param threadId
	 * @throws ApsSystemException
	 */
	public void viewThread(String sessionId, int threadId) throws ApsSystemException;

	/**
	 * restituisce il numero di vilte che il thread è stato visitato
	 * @param postCode
	 * @return
	 * @throws ApsSystemException
	 */
	public int getViews(int threadId) throws ApsSystemException;

	public Collection<String> getUsersOnline();

	public MostOnlineUsersRecord getMostOnlineUsersRecord();

	public Set<String> getGuestSessions();
}
