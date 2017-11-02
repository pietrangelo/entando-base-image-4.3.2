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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.group;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.group.event.RemoteGroupChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.group.event.RemoteGroupChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class GroupChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteGroupChangingObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GroupChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromGroupChanging(RemoteGroupChangingEvent event) {
		try {
			((IManager)this.getGroupManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(pointcut="execution(* com.agiletec.aps.system.services.group.IGroupManager.addGroup(..))")
	public void notifyAddGroup() {
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(pointcut="execution(* com.agiletec.aps.system.services.group.IGroupManager.removeGroup(..))")
	public void notifyRemoveGroup() {
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(pointcut="execution(* com.agiletec.aps.system.services.group.IGroupManager.updateGroup(..))")
	public void notifyUpdateGroup() {
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		RemoteGroupChangingEvent remoteEvent = new RemoteGroupChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	public IGroupManager getGroupManager() {
		return _groupManager;
	}
	
	private IGroupManager _groupManager;
	
}