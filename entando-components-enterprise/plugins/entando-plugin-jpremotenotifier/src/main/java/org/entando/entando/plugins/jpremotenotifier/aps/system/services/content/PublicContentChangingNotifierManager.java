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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.content;

import com.agiletec.aps.system.common.IManager;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.content.event.RemotePublicContentChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.content.event.RemotePublicContentChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingObserver;

@Aspect
public class PublicContentChangingNotifierManager extends AbstractNotifierInterceptorManager 
		implements RemotePublicContentChangingObserver, RemoteEntityTypeChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(PublicContentChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromEntityTypeChanging(RemoteEntityTypeChangingEvent event) {
		try {
			ApsProperties properties = event.getParameters();
			String managerName = (String) properties.get("manager");
			if (null == managerName || !((IManager) this.getContentManager()).getName().equals(managerName)) {
				return;
			}
			((IManager)this.getContentManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@Override
	public void updateFromPublicContentChanging(RemotePublicContentChangingEvent event) {
		try {
			//System.out.println("evento: " + event);
			//System.out.println("Event received (content): " + event.getEventID());
			//System.out.println("PARAMS: " + event.getParameters());
			//Decode properties and build local event
			ApsProperties properties = event.getParameters();
			String contentId = (String) properties.get("contentId");
			int operationCode = Integer.parseInt((String)properties.get("operationCode"));
			Content content = this.getContentManager().loadContent(contentId, false);
			if (null == content) {
				return;
			}
			String cacheKey = JacmsSystemConstants.CONTENT_CACHE_PREFIX + contentId;
			this.getCacheInfoManager().flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, cacheKey);
			String groupsCvs = CmsCacheWrapperManager.getContentCacheGroupsToEvictCsv(contentId, content.getTypeCode());
			String[] groupNames = groupsCvs.split(",");
			for (int i = 0; i < groupNames.length; i++) {
				String groupName = groupNames[i];
				this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, groupName);
			}
			//raise local event
			PublicContentChangedEvent localEvent = new PublicContentChangedEvent();
			localEvent.setContent(content);
			localEvent.setOperationCode(operationCode);
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("error in updateFromPublicContentChanging", t);
			throw new RuntimeException("errore in updateFromPublicContentChanging");
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.insertOnLineContent(..)) && args(content)")
	public void notifyInsertOnLineContent(Content content) {
		//System.out.println("Notifying event: InsertOnLineContent per " + content.getId());
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("contentId", content.getId());
		properties.put("operationCode", Integer.toString(PublicContentChangedEvent.INSERT_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.removeOnLineContent(..)) && args(content)")
	public void notifyRemoveOnLineContent(Content content) {
		//System.out.println("Notifying event: notifyRemoveOnLineContent per " + content.getId());
		ApsProperties properties = new ApsProperties();
		properties.put("contentId", content.getId());
		properties.put("operationCode", Integer.toString(PublicContentChangedEvent.REMOVE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		//System.out.println("Sending notification (content)");
		RemotePublicContentChangingEvent remoteEvent = new RemotePublicContentChangingEvent();
		if (null != properties) {
			remoteEvent.setParameters(properties);
		}
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected ICacheInfoManager getCacheInfoManager() {
		return cacheInfoManager;
	}
	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this.cacheInfoManager = cacheInfoManager;
	}
	
	private IContentManager _contentManager;
	private ICacheInfoManager cacheInfoManager;
	
}
