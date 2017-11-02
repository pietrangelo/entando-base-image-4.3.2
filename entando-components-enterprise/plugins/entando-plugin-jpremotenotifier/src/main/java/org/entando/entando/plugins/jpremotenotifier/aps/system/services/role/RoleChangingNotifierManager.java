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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.role;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.role.event.RemoteRoleChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.role.event.RemoteRoleChangingObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class RoleChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteRoleChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(RoleChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.info("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromRoleChanging(RemoteRoleChangingEvent event) {
		try {
			System.out.println("Event received (roles): " + event.getEventID());
			((IManager)this.getRoleManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.addRole(..))")
	public void notifyAddRole() {
		//System.out.println("Notifying event: notifyAddRole");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.updateRole(..))")
	public void notifyUpdateRole() {
		//System.out.println("Notifying event: notifyUpdateRole");
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.removeRole(..))")
	public void notifyDeleteRole() {
		//System.out.println("Notifying event: notifyDeleteRole");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.removePermission(..))")
	public void notifyRemovePermission() {
		//System.out.println("Notifying event: removePermission");
		this.notifyRemoteEvent(null);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.updatePermission(..))")
	public void notifyUpdatePermission() {
		//System.out.println("Notifying event: notifyUpdatePermission");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.role.IRoleManager.addPermission(..))")
	public void notifyAddPermission() {
		//System.out.println("Notifying event: notifyAddPermission");
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Sending notification (roles)!");
		RemoteRoleChangingEvent remoteEvent = new RemoteRoleChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}

	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	public IRoleManager getRoleManager() {
		return _roleManager;
	}

	private IRoleManager _roleManager;
}
