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
package org.entando.entando.plugins.jpwebformremotenotifier.aps.system.services.form;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebformremotenotifier.aps.system.services.form.event.RemoteFormChangingEvent;
import org.entando.entando.plugins.jpwebformremotenotifier.aps.system.services.form.event.RemoteFormChangingObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class WebformChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteFormChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(WebformChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.info("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromFormChanging(RemoteFormChangingEvent event) {
		System.out.println("Received notification (webforms): " + event.getEventID() );
		try {
			((IManager) this.getFormManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.plugins.jpwebform.aps.system.services.form.FormManager.saveNotifierConfig(..))")
	public void notifySaveNotifierConfig() {
		notifyFormChanging(null);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.plugins.jpwebform.aps.system.services.form.FormManager.generateNewVersionType(..))")
	public void notifyGenerateNewVersionType() {
		notifyFormChanging(null);
	}
	
	
	// org.entando.entando.plugins.jpwebform.aps.system.services.form.FormManager.generateWorkStepUserGui(String, String, boolean)
	@AfterReturning(
			pointcut="execution(* org.entando.entando.plugins.jpwebform.aps.system.services.form.FormManager.generateWorkStepUserGui(..))")
	public void notifyGenerateWorkStepUserGui() {
		notifyFormChanging(null);
	}
	
	protected void notifyFormChanging(ApsProperties properties) {
		System.out.println("Sending notification (webforms)!");
		RemoteFormChangingEvent remoteEvent = new RemoteFormChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IFormManager getFormManager() {
		return formManager;
	}

	public void setFormManager(IFormManager formManager) {
		this.formManager = formManager;
	}

	
	
	private IFormManager formManager;
}
