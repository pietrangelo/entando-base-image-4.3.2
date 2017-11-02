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
package com.agiletec.plugins.jpforum.aps.system.services.message;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IMessageManager {

	/**
	 * Salva un messaggio.
	 * Viene settato a true il valore toRead.
	 * @param message
	 * @throws ApsSystemException
	 */
	public void addMessage(Message message) throws ApsSystemException;

	public void deleteMessage(int code) throws ApsSystemException;
	
	public Message getMessage(int code) throws ApsSystemException;

	public Message readMessage(int code) throws ApsSystemException;
	
	/**
	 * La chiave della mappa è costituito dal tipo di messaggio, il valore è la lista dei codici dei messaggi
	 * @param recipient
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, List<Integer>> getIncomingMessages(String recipient) throws ApsSystemException;
	
	/**
	 * La chiave della mappa è costituito dal tipo di messaggio, il valore il conteggio dei messaggi ancora da leggere
	 * @param recipient
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getNewMessages(String recipient) throws ApsSystemException;
}
