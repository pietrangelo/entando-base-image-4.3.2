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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.guifragment;

import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.guifragment.event.GuiFragmentChangedEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.guifragment.event.RemoteFragmentChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.guifragment.event.RemoteFragmentChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class GuiFragmentChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteFragmentChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(GuiFragmentChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromFragmentChanging(RemoteFragmentChangingEvent event) {
		try {
			_logger.debug("Event received (Fragment): {}", event.getEventID());
			ApsProperties properties = event.getParameters();
			String code = (String) properties.get("code");
			Integer operationCode = null;
			try {
				operationCode = Integer.parseInt((String)properties.get("operationCode"));
			} catch (Throwable t) {
				_logger.error("Errore in parsing operationCode {}", properties.get("operationCode"), t);
				return;
			}
			
			//@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#guiFragment.code)")
			this.getCacheInfoManager().flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, "GuiFragment_" + code);
			//@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'GuiFragment_uniqueByWidgetTypeGroup,GuiFragment_codesByWidgetTypeGroup'")//TODO improve group handling
			this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, "GuiFragment_uniqueByWidgetTypeGroup");
			this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, "GuiFragment_codesByWidgetTypeGroup");
			
			//raise local event
			GuiFragmentChangedEvent localEvent = new GuiFragmentChangedEvent();
			localEvent.setGuiFragment(this.getGuiFragmentManager().getGuiFragment(code));
			localEvent.setOperationCode(operationCode);
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager.addGuiFragment(..)) && args(guiFragment)")
	public void notifyAdd(GuiFragment guiFragment) {
		//System.out.println("Notifying event: add");
		if (null == guiFragment || null == guiFragment.getCode()) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("code", guiFragment.getCode());
		properties.put("operationCode", Integer.toString(GuiFragmentChangedEvent.INSERT_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager.updateGuiFragment(..)) && args(guiFragment)")
	public void notifyUpdate(GuiFragment guiFragment) {
		//System.out.println("Notifying event: update");
		if (null == guiFragment || null == guiFragment.getCode()) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("code", guiFragment.getCode());
		properties.put("operationCode", Integer.toString(GuiFragmentChangedEvent.UPDATE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager.deleteGuiFragment(..)) && args(code)")
	public void notifyDelete(String code) {
		//System.out.println("Notifying event: delete");
		if (null == code) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("code", code);
		properties.put("operationCode", Integer.toString(GuiFragmentChangedEvent.REMOVE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (Fragment)!");
		RemoteFragmentChangingEvent remoteEvent = new RemoteFragmentChangingEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected ICacheInfoManager getCacheInfoManager() {
		return _cacheInfoManager;
	}
	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this._cacheInfoManager = cacheInfoManager;
	}
	
	protected IGuiFragmentManager getGuiFragmentManager() {
		return _guiFragmentManager;
	}
	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this._guiFragmentManager = guiFragmentManager;
	}
	
	private ICacheInfoManager _cacheInfoManager;
	private IGuiFragmentManager _guiFragmentManager;
	
}
