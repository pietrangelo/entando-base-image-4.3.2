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
package org.entando.entando.plugins.jpremotenotifier.aps.system.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.plugins.jpremotenotifier.aps.system.services.JpRemoteNotifierSystemConstants;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.ApsRemoteEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.IRemoteNotifyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.notify.INotifyManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class JpRemoteNotifierServlet extends HttpServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(JpRemoteNotifierServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    						throws ServletException, IOException {
		this.execute(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
							throws ServletException, IOException {
		this.execute(req, resp);
	}
	
	protected void execute(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException {
		_logger.debug("jpremotenotifier: incoming remote event");
		try {
			ApsProperties properties = new ApsProperties();
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				properties.setProperty(paramName, request.getParameter(paramName));
			}
			IRemoteNotifyManager remoteNotMan = (IRemoteNotifyManager) ApsWebApplicationUtils.getBean(JpRemoteNotifierSystemConstants.REMOTE_NOTIFIER_MANAGER, request);
			ApsRemoteEvent event = remoteNotMan.getRemoteEvent(properties);
			if (null != event) {
				_logger.debug("jpremotenotifier: received " + event.getEventID() + " event");
				if (event.isReloadConfigEvent()) {
					ApsWebApplicationUtils.executeSystemRefresh(request);
				} else {
					INotifyManager notifyManager = (INotifyManager) ApsWebApplicationUtils.getBean("NotifyManager", request);
					notifyManager.publishEvent(event);
				}
			} else {
				_logger.debug("jpremotenotifier: received NULL event");
			}
		} catch (Throwable t) {
			_logger.error("error in execute", t);
		}
	}
	
}