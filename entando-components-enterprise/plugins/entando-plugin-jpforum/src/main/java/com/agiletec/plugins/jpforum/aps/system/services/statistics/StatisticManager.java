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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.event.SessionEvent;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.event.SessionObserver;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

@Aspect
public class StatisticManager extends AbstractService implements IStatisticManager, SessionObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(StatisticManager.class);
	
	@Override
	public void init() throws Exception {
		this.setSessionViews(new HashMap<String, Set<Integer>>());
		this.loadMostOnlineUsers();
		_logger.debug("{} is ready", this.getClass().getName());
	}

	@Override
	public void updateFromSessionEvent(SessionEvent sessionEvent) {
		String sessionId = sessionEvent.getSessionId();
		if (sessionEvent.getOperationCode() == SessionEvent.OPERATION_CREATED) {
			this.getSessionViews().put(sessionId, new HashSet<Integer>());
			//System.out.println("added session " + sessionId + "  SIZE:" + this.getSessionViews().size());
		} else if (sessionEvent.getOperationCode() == SessionEvent.REMOVE_DESTROYED) {
			//System.out.println("removed session " + sessionId + " SIZE:" + this.getSessionViews().size());
			this.getSessionViews().remove(sessionId);
			this.getGuestSessions().remove(sessionId);
		}
	}


	@Override
	public void viewThread(String sessionId, int threadId) throws ApsSystemException {
		try {
			if (!this.getSessionViews().containsKey(sessionId) || !this.getSessionViews().get(sessionId).contains(threadId)) {
				this.getStatisticDAO().addView(threadId);
				if (!this.getSessionViews().containsKey(sessionId)) {
					this.getSessionViews().put(sessionId, new HashSet<Integer>());
				}
				this.getSessionViews().get(sessionId).add(threadId);
			}
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta statistiche per thread {}", threadId, t);
			throw new ApsSystemException("Errore in aggiunta statistiche per thread " + threadId, t);
		}
	}

	@Override
	public int getViews(int postCode) throws ApsSystemException {
		int views = 0;
		try {
			views = this.getStatisticDAO().loadViews(postCode);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta statistiche per post {}", postCode, t);
			throw new ApsSystemException("Errore in aggiunta statistiche per post", t);
		}
		return views;
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.deleteThread(..)) && args(key)")
	public void deleteThreadStatistics(Object key) throws ApsSystemException {
		int threadCode = (Integer)key;
		try {
			this.getStatisticDAO().deleteThreadStatistics(threadCode);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta statistiche per post", t);
			throw new ApsSystemException("Errore in aggiunta statistiche per post", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.addThread(..)) && args(key)")
	public void prepareThreadStatistic(Object key) throws ApsSystemException {
		Thread thread = (Thread) key;
		try {
			this.getStatisticDAO().createRecord(thread.getCode());
		} catch (Throwable t) {
			_logger.error("Errore in creazione record statistiche per thread ", t);
			throw new ApsSystemException("Errore in creazione record statistiche per thread ", t);
		}
	}

	protected MostOnlineUsersRecord loadMostOnlineUsers() throws ApsSystemException {
		MostOnlineUsersRecord record = null;
		try {
			record = this.getStatisticDAO().loadMostOnlineUsers();
			if (null == record) {
				record = new MostOnlineUsersRecord();
				record.setCount(0);
				record.setRecordDate(new Date());
				this.getStatisticDAO().addMostOnlineUsers(record);
			}
			this.setMostOnlineUsersRecord(record);
		} catch (Throwable t) {
			_logger.error("Error loading the most online users record", t);
			throw new ApsSystemException("Error loading the most online users record", t);
		}
		return record;
	}

	private void updateMostUserOnlineRecord() {
		int count = this.getUsersOnline().size();
		if (count > this.getMostOnlineUsersRecord().getCount()) {
			MostOnlineUsersRecord record = new MostOnlineUsersRecord();
			record.setCount(count);
			record.setRecordDate(new Date());
			this.getStatisticDAO().updateMostOnlineUsers(record);
			this.setMostOnlineUsersRecord(record);
		}
	}

	@Override
	public Collection<String> getUsersOnline() {
		return this.getActiveSessions().values();
	}

	protected boolean isValidUser(UserDetails user) {
		boolean valid = false;
		if (null != user) {
			if (this.getAuthorizationManager().isAuthOnRole(user, JpforumSystemConstants.ROLE_FORUM_USER)) {
				valid = true;
			} else if (this.getAuthorizationManager().isAuthOnRole(user, JpforumSystemConstants.ROLE_SECTION_MODERATOR)) {
				valid = true;
			} else if (this.getAuthorizationManager().isAuthOnRole(user, JpforumSystemConstants.ROLE_SUPERUSER)) {
				valid = true;
			}
		}
		return valid;
	}

	public void addActiveSession(String sessionId, UserDetails user) {
		if (this.isValidUser(user)) {
			this.getActiveSessions().put(sessionId, user.getUsername());
			if (this.getGuestSessions().contains(sessionId)) {
				this.getGuestSessions().remove(sessionId);
			}
			this.updateMostUserOnlineRecord();
		} else {
			this.getGuestSessions().add(sessionId);
		}
	}


	public void notifySessionEvent(int opearation, String sessionId) {
		//System.out.println("NOTIFIED");
		SessionEvent event = new SessionEvent();
		event.setSessionId(sessionId);
		event.setOperationCode(opearation);
		this.notifyEvent(event);

	}

	public void removeActiveSession(String sessionId) {
		this.getActiveSessions().remove(sessionId);
	}

	protected IStatisticDAO getStatisticDAO() {
		return _statisticDAO;
	}
	public void setStatisticDAO(IStatisticDAO statisticDAO) {
		this._statisticDAO = statisticDAO;
	}

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

	public Map<String, Set<Integer>> getSessionViews() {
		return _sessionViews;
	}
	public void setSessionViews(Map<String, Set<Integer>> sessionViews) {
		this._sessionViews = sessionViews;
	}

	public Map<String, String> getActiveSessions() {
		return _activeSessions;
	}
	public void setActiveSessions(Map<String, String> activeSessions) {
		this._activeSessions = activeSessions;
	}


	public MostOnlineUsersRecord getMostOnlineUsersRecord() {
		return _mostOnlineUsersRecord;
	}
	public void setMostOnlineUsersRecord(MostOnlineUsersRecord mostOnlineUsersRecord) {
		this._mostOnlineUsersRecord = mostOnlineUsersRecord;
	}

	public Set<String> getGuestSessions() {
		return _guestSessions;
	}
	public void setGuestSessions(Set<String> guestSessions) {
		this._guestSessions = guestSessions;
	}

	private Map<String, Set<Integer>> _sessionViews;
	private Map<String, String> _activeSessions = new HashMap<String, String>();
	private Set<String> _guestSessions = new HashSet<String>();
	private IStatisticDAO _statisticDAO;
	private MostOnlineUsersRecord _mostOnlineUsersRecord;
	private IAuthorizationManager _authorizationManager;

}
