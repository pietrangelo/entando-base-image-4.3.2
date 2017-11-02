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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.user.UserDetails;

@Aspect
public class MessageManager extends AbstractService implements IMessageManager {

	private static final Logger _logger =  LoggerFactory.getLogger(MessageManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void addMessage(Message message) throws ApsSystemException {
		try {
			int code = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			message.setCode(code);
			message.setMessageDate(new Date());
			message.setToRead(true);
			this.getMessageDAO().insertMessage(message);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta messaggio", t);
            throw new ApsSystemException("Errore in aggiunta messaggio", t);
		}
	}

	@Override
	public void deleteMessage(int code) throws ApsSystemException {
		try {
			this.getMessageDAO().removeMessage(code);
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione messaggio", t);
            throw new ApsSystemException("Errore in eliminazione messaggio " + code, t);
		}
	}

	public void deleteMessages(String username) throws ApsSystemException {
		try {
			this.getMessageDAO().removeMessages(username);
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione messaggi", t);
			throw new ApsSystemException("Errore in eliminazione messaggi per " + username, t);
		}
	}

	@Override
	public Message getMessage(int code) throws ApsSystemException {
		Message message = null;
		try {
			message = this.getMessageDAO().loadMessage(code);
		} catch (Throwable t) {
			_logger.error("Errore in recupero messaggio", t);
            throw new ApsSystemException("Errore in recupero messaggio " + code, t);
		}
		return message;
	}
	
	@Override
	public Message readMessage(int code) throws ApsSystemException {
		Message message = null;
		try {
			message = this.getMessageDAO().readMessage(code);
		} catch (Throwable t) {
			_logger.error("Errore in lettura messaggio", t);
            throw new ApsSystemException("Errore in lettura messaggio " + code, t);
		}
		return message;
	}

	@Override
	public Map<String, List<Integer>> getIncomingMessages(String recipient)	throws ApsSystemException {
		Map<String, List<Integer>> messages = new HashMap<String, List<Integer>>();
		try {
			messages = this.getMessageDAO().loadIncomingMessages(recipient);
		} catch (Throwable t) {
			_logger.error("Errore in recupero messaggi", t);
            throw new ApsSystemException("Errore in recupero messaggi " + recipient, t);
		}
		return messages;
	}

	@Override
	public Map<String, Integer> getNewMessages(String recipient) throws ApsSystemException {
		Map<String, Integer> messages = new HashMap<String, Integer>();
		try {
			messages = this.getMessageDAO().loadNewMessages(recipient);
		} catch (Throwable t) {
			_logger.error("Errore in recupero nuovi messaggi", t);
			throw new ApsSystemException("Errore in recupero nuovi messaggi" + recipient, t);
		}
		return messages;
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void deleteSanctions(Object key) throws ApsSystemException {
		String username = "";
		if (key instanceof String) {
			username = key.toString();
		} else if (key instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) key;
			username = userDetails.getUsername();
		}
		this.deleteMessages(username);
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	
	public void setMessageDAO(IMessageDAO messageDAO) {
		this._messageDAO = messageDAO;
	}
	protected IMessageDAO getMessageDAO() {
		return _messageDAO;
	}

	private IMessageDAO _messageDAO;
	private IKeyGeneratorManager _keyGeneratorManager;

}
