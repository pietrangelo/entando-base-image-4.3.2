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
package org.entando.entando.plugins.jpjasperremotenotifier.aps.system.services.jasper;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasperremotenotifier.aps.system.services.jasper.event.RemoteConfigChangingEvent;
import org.entando.entando.plugins.jpjasperremotenotifier.aps.system.services.jasper.event.RemoteConfigChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;


@Aspect
public class JasperConfigChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteConfigChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperConfigChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromConfigChanging(RemoteConfigChangingEvent event) {
		System.out.println("Received notification (jasper)!");
		try {
			((IManager) this.getJasperServerManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager.updateJasperConfig(..))")
	public void notifyUpdateJasperConfig() {
		notifyJasperConfigChanging(null);
	}
	
	protected void notifyJasperConfigChanging(ApsProperties properties) {
		System.out.println("Sending notification (jasper)!");
		RemoteConfigChangingEvent remoteEvent = new RemoteConfigChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}

	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	private IJasperServerManager _jasperServerManager;
}
