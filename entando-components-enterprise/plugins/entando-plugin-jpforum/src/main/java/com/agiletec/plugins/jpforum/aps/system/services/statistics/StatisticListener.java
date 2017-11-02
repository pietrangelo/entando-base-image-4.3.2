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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.event.SessionEvent;

public class StatisticListener implements HttpSessionAttributeListener, HttpSessionListener {

	protected UserDetails extractUserFromSession(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		UserDetails user = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return user;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		if (attributeName.equals(SystemConstants.SESSIONPARAM_CURRENT_USER)) {
			UserDetails user = this.extractUserFromSession(event);
			if (null != user) {

				this.addEntry(event.getSession().getId(), user, event.getSession());
			}
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		if (attributeName.equals(SystemConstants.SESSIONPARAM_CURRENT_USER)) {
			this.removeEntry(event.getSession().getId(), event.getSession());
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		if (attributeName.equals(SystemConstants.SESSIONPARAM_CURRENT_USER)) {
			UserDetails user = this.extractUserFromSession(event);
			if (null != user) {
				this.addEntry(event.getSession().getId(), user, event.getSession());
			}
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		UserDetails user = this.extractUserFromSession(event);
		if (null != user) {
			this.addEntry(event.getSession().getId(), user, event.getSession());
		}
		ApplicationContext cx =  WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
		IStatisticManager statisticManager = (IStatisticManager) cx.getBean(JpforumSystemConstants.STATISTIC_MANAGER);
		((StatisticManager)statisticManager).notifySessionEvent(SessionEvent.OPERATION_CREATED, event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		ApplicationContext cx =  WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
		IStatisticManager statisticManager = (IStatisticManager) cx.getBean(JpforumSystemConstants.STATISTIC_MANAGER);
		((StatisticManager)statisticManager).notifySessionEvent(SessionEvent.REMOVE_DESTROYED, event.getSession().getId());
		this.removeEntry(event.getSession().getId(), event.getSession());
	}

	private void addEntry(String sessionId, UserDetails user, HttpSession session) {
		ApplicationContext cx =  WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		IStatisticManager statisticManager = (IStatisticManager) cx.getBean(JpforumSystemConstants.STATISTIC_MANAGER);
		((StatisticManager)statisticManager).addActiveSession(sessionId, user);
	}

	private void removeEntry(String sessionId, HttpSession session) {
		ApplicationContext cx =  WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		IStatisticManager statisticManager = (IStatisticManager) cx.getBean(JpforumSystemConstants.STATISTIC_MANAGER);
		((StatisticManager)statisticManager).removeActiveSession(sessionId);
	}
}
