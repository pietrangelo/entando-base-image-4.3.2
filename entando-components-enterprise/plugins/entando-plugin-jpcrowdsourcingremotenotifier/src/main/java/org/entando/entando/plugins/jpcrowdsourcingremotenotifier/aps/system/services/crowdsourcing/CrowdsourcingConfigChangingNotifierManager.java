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
package org.entando.entando.plugins.jpcrowdsourcingremotenotifier.aps.system.services.crowdsourcing;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpcrowdsourcingremotenotifier.aps.system.services.crowdsourcing.event.RemoteConfigChangingEvent;
import org.entando.entando.plugins.jpcrowdsourcingremotenotifier.aps.system.services.crowdsourcing.event.RemoteConfigChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;


@Aspect
public class CrowdsourcingConfigChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteConfigChangingObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CrowdsourcingConfigChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());	
	}

	@Override
	public void updateFromConfigChanging(RemoteConfigChangingEvent event) {
		System.out.println("Receiving (crowdsourcing): " + event.getEventID());
		try {
			((IManager) this.getIdeaManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager.updateConfig(..))")
	public void notifyUpdateConfig() {
		notifyCrowdConfigChanging(null);
	}
	
	protected void notifyCrowdConfigChanging(ApsProperties properties) {
		System.out.println("Sending notification (crowdsourcing)!");
		RemoteConfigChangingEvent remoteEvent = new RemoteConfigChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IIdeaManager getIdeaManager() {
		return _ideaManager;
	}

	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	private IIdeaManager _ideaManager; 
}
