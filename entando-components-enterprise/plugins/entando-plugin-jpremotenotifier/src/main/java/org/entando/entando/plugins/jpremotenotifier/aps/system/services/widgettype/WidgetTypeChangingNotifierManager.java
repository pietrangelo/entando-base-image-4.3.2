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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.widgettype;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.guifragment.event.RemoteFragmentChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.guifragment.event.RemoteFragmentChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.widgettype.event.RemoteWidgetTypeChangingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class WidgetTypeChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteFragmentChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(WidgetTypeChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromFragmentChanging(RemoteFragmentChangingEvent event) {
		try {
			_logger.debug("Event received (widget type): {}", event.getEventID());
			/*
			ApsProperties properties = event.getParameters();
			String code = (String) properties.get("code");
			Integer operationCode = null;
			try {
				operationCode = Integer.parseInt((String)properties.get("operationCode"));
			} catch (Throwable t) {
				_logger.error("Errore in parsing operationCode {}", properties.get("operationCode"), t);
				return;
			}
			*/
			((IManager) this.getWidgetTypeManager()).refresh();
			((IManager) this.getPageManager()).refresh();
			/*
			//raise local event
			WidgetTypeChangedEvent localEvent = new WidgetTypeChangedEvent();
			localEvent.setWidgetTypeCode(code);
			localEvent.setOperationCode(operationCode);
			this.notifyEvent(localEvent);
			*/
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@Deprecated
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.addShowletType(..)) && args(widgetType)")
	public void notifyAddShowlet(WidgetType widgetType) {
		this.notifyAdd(widgetType);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.addWidgetType(..)) && args(widgetType)")
	public void notifyAdd(WidgetType widgetType) {
		//System.out.println("Notifying event: add -> " + widgetType);
		if (null == widgetType || null == widgetType.getCode()) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("code", widgetType.getCode());
		properties.put("operationCode", Integer.toString(WidgetTypeChangedEvent.INSERT_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.updateShowletType(..))")// && args(widgetTypeCode)")
	public void updateShowletType(/*String widgetTypeCode*/) {
		this.notifyUpdate(/*widgetTypeCode*/);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.updateShowletTypeTitles(..))")// && args(widgetTypeCode)")
	public void updateShowletTypeTitles(/*String widgetTypeCode*/) {
		this.notifyUpdate(/*widgetTypeCode*/);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.updateWidgetType(..))")// && args(widgetTypeCode)")
	public void notifyUpdate(/*String widgetTypeCode*/) {
		//System.out.println("Notifying event: update");
		//if (null == widgetTypeCode) {
		//	return;
		//}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		//properties.put("code", widgetTypeCode);
		properties.put("operationCode", Integer.toString(WidgetTypeChangedEvent.UPDATE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.deleteShowletType(..)) && args(widgetTypeCode)")
	public void notifyDeleteShowlet(String widgetTypeCode) {
		this.notifyDelete(widgetTypeCode);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.widgettype.WidgetTypeManager.deleteWidgetType(..)) && args(widgetTypeCode)")
	public void notifyDelete(String widgetTypeCode) {
		if (null == widgetTypeCode) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("code", widgetTypeCode);
		properties.put("operationCode", Integer.toString(WidgetTypeChangedEvent.REMOVE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (widget type)!");
		RemoteWidgetTypeChangingEvent remoteEvent = new RemoteWidgetTypeChangingEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	private IPageManager _pageManager;
	private IWidgetTypeManager _widgetTypeManager;
	
}
