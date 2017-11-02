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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.contentmodel;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.contentmodel.event.RemoteContentModelChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.contentmodel.event.RemoteContentModelChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.event.ContentModelChangedEvent;

@Aspect
public class RemoteContentModelChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteContentModelChangingObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(RemoteContentModelChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromContentModelChanging(RemoteContentModelChangingEvent event) {
		try {
			_logger.debug("Event received (contentModel): {}", event.getEventID());
			((IManager) this.getContentModelManager()).refresh();
			ApsProperties properties = event.getParameters();
			String contentModelId = (String) properties.get("contentModelId");
			String operationCode = (String) properties.get("operationCode");
			ContentModelChangedEvent localEvent = new ContentModelChangedEvent();
			ContentModel contentModel = this.getContentModelManager().getContentModel(new Long(contentModelId).longValue());
			localEvent.setContentModel(contentModel);
			localEvent.setOperationCode(new Integer(operationCode).intValue());
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto contentModel", t);
		}	
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager.addContentModel(..)) && args(model)")
	public void notifyAdd(ContentModel model) {
		_logger.debug("rotifica evento remoto addContentModel");
		ApsProperties properties = this.buildRemoteEventProperties(model, ContentModelChangedEvent.INSERT_OPERATION_CODE);
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager.removeContentModel(..)) && args(model)")
	public void notifyRemove(ContentModel model) {
		_logger.debug("rotifica evento remoto removeContentModel");
		ApsProperties properties = this.buildRemoteEventProperties(model, ContentModelChangedEvent.REMOVE_OPERATION_CODE);
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager.updateContentModel(..)) && args(model)")
	public void notifyUpdate(ContentModel model) {
		_logger.debug("rotifica evento remoto updateContentModel");
		ApsProperties properties = this.buildRemoteEventProperties(model, ContentModelChangedEvent.UPDATE_OPERATION_CODE);
		this.notifyRemoteEvent(properties);
	}
	
	private ApsProperties buildRemoteEventProperties(ContentModel model, int operationCode) {
		ApsProperties properties = new ApsProperties();
		properties.put("contentModelId", new Long(model.getId()).toString());
		properties.put("operationCode", new Integer(operationCode).toString());
		return properties;
	}

	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Sending notification (contentModel)");
		RemoteContentModelChangingEvent remoteEvent = new RemoteContentModelChangingEvent();
		if (null != properties) {
			remoteEvent.setParameters(properties);
		}
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	public IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}

	private IContentModelManager _contentModelManager;

}
