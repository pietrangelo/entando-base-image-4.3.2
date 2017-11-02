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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.category;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.category.event.RemoteCategoryChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.category.event.RemoteCategoryChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class CategoryChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteCategoryChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CategoryChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromCategoryChanging(RemoteCategoryChangingEvent event) {
		try {
			_logger.debug("Event received (category): {}", event.getEventID());
			((IManager) this.getCategoryManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.category.ICategoryManager.addCategory(..))")
	public void notifyAdd() {
		//System.out.println("Notifying event: add");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.category.ICategoryManager.updateCategory(..))")
	public void notifyUpdate() {
		//System.out.println("Notifying event: update");
		this.notifyRemoteEvent(null);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.category.ICategoryManager.deleteCategory(..))")
	public void notifyDelete() {
		//System.out.println("Notifying event: delete");
		this.notifyRemoteEvent(null);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (category)!");
		RemoteCategoryChangingEvent remoteEvent = new RemoteCategoryChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	private ICategoryManager _categoryManager;	

}
