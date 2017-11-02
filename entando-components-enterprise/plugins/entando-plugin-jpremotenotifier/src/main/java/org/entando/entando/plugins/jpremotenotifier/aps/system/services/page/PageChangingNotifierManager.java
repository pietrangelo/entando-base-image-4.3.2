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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.page;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.page.event.RemotePageChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.page.event.RemotePageChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.util.ApsProperties;

@Aspect
public class PageChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemotePageChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(PageChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.error("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromPageChanging(RemotePageChangingEvent event) {
		try {
			System.out.println("Event received (page): " + event.getEventID());
			ApsProperties properties = event.getParameters();
			String pageCode = (String) properties.get("pageCode");
			((IManager) this.getPageManager()).refresh();
			IPage page = this.getPageManager().getPage(pageCode);
			PageChangedEvent localEvent = new PageChangedEvent();
			localEvent.setPage(page);
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	private ApsProperties buildRemoteEventProperties(IPage page) {
		ApsProperties properties = new ApsProperties();
		properties.put("pageCode", page.getCode());
		return properties;
	}

	
	private ApsProperties buildRemoteEventProperties(String pageCode) {
		ApsProperties properties = new ApsProperties();
		properties.put("pageCode", pageCode);
		return properties;
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.addPage(..)) && args(page)")
	public void notifyAdd(IPage page) {
		//System.out.println("Notifying event: Add");
		ApsProperties properties = this.buildRemoteEventProperties(page);
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.updatePage(..)) && args(page)")
	public void notifyUpdate(IPage page) {
		//System.out.println("Notifying event: Update");
		ApsProperties properties = this.buildRemoteEventProperties(page);
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.deletePage(..)) && args(pageCode, ..)")
	public void notifyDelete(String pageCode) {
		//System.out.println("Notifying event: Delete");
		ApsProperties properties = this.buildRemoteEventProperties(pageCode);
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.movePage(..)) && args(pageCode, ..)")
	public void notifyMove(String pageCode) {
		//System.out.println("Notifying event: move");
		ApsProperties properties = this.buildRemoteEventProperties(pageCode);
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.joinWidget(..)) && args(pageCode, ..)")
	public void notifyJoinWidget(String pageCode) {
		//System.out.println("Notifying event: JoinWidget");
		ApsProperties properties = this.buildRemoteEventProperties(pageCode);
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.page.IPageManager.removeWidget(..)) && args(pageCode, ..)")
	public void notifyRemoveWidget(String pageCode) {
		//System.out.println("Notifying event: notifyRemoveWidget");
		ApsProperties properties = this.buildRemoteEventProperties(pageCode);
		this.notifyRemoteEvent(properties);
	}

	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Inivio notifica evento (page)");
		RemotePageChangingEvent remoteEvent = new RemotePageChangingEvent();
		if (null != properties) {
			remoteEvent.setParameters(properties);
		}
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	public IPageManager getPageManager() {
		return _pageManager;
	}

	private IPageManager _pageManager;

}
