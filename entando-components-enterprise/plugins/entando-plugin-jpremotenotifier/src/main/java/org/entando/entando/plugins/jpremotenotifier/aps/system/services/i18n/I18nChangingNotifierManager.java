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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.i18n;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.i18n.event.RemoteLabelChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.i18n.event.RemoteLabelChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class I18nChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteLabelChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(I18nChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromLabelChanging(RemoteLabelChangingEvent event) {
		try {
			_logger.debug("Event received (label): {}", event.getEventID());
			((IManager) this.getI18nManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.i18n.II18nManager.addLabelGroup(..))")
	public void notifyAdd() {
		//System.out.println("Notifying event: add");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.i18n.II18nManager.updateLabelGroup(..))")
	public void notifyUpdate() {
		//System.out.println("Notifying event: update");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.i18n.II18nManager.deleteLabelGroup(..))")
	public void notifyDelete() {
		//System.out.println("Notifying event: delete");
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (label)!");
		RemoteLabelChangingEvent remoteEvent = new RemoteLabelChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	private II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	
	private II18nManager _i18nManager;

}
