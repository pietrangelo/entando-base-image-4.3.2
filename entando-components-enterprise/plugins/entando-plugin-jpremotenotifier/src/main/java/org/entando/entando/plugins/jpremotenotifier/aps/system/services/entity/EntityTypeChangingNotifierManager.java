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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class EntityTypeChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteEntityTypeChangingObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(EntityTypeChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromEntityTypeChanging(RemoteEntityTypeChangingEvent event) {
		try {
			_logger.debug("Event received (Entity type): {}", event.getEventID());
			ApsProperties properties = event.getParameters();
			String manager = (String) properties.get("manager");
			if (null == manager) {
				return;
			}
			IManager bean = super.getBeanFactory().getBean(manager, IManager.class);
			if (null != bean) {
				bean.refresh();
			}
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.IEntityTypesConfigurer.addEntityPrototype(..)) && args(entityType,..)")
	public void notifyAdd(JoinPoint joinPoint, IApsEntity entityType) {
		//System.out.println("Notifying event: add");
		IManager manager = (IManager) joinPoint.getTarget();
		ApsProperties properties = new ApsProperties();
		properties.put("manager", manager.getName());
		properties.put("entityTypeCode", entityType.getTypeCode());
		properties.put("operationCode", Integer.toString(EntityTypesChangingEvent.INSERT_OPERATION_CODE));
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.IEntityTypesConfigurer.updateEntityPrototype(..)) && args(entityType,..)")
	public void notifyUpdate(JoinPoint joinPoint, IApsEntity entityType) {
		//System.out.println("Notifying event: update");
		IManager manager = (IManager) joinPoint.getTarget();
		ApsProperties properties = new ApsProperties();
		properties.put("manager", manager.getName());
		properties.put("entityTypeCode", entityType.getTypeCode());
		properties.put("operationCode", Integer.toString(EntityTypesChangingEvent.UPDATE_OPERATION_CODE));
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.IEntityTypesConfigurer.removeEntityPrototype(..)) && args(entityTypeCode,..)")
	public void notifyDelete(JoinPoint joinPoint, String entityTypeCode) {
		//System.out.println("Notifying event: delete");
		IManager manager = (IManager) joinPoint.getTarget();
		ApsProperties properties = new ApsProperties();
		properties.put("manager", manager.getName());
		properties.put("entityTypeCode", entityTypeCode);
		properties.put("operationCode", Integer.toString(EntityTypesChangingEvent.REMOVE_OPERATION_CODE));
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (Entity type)!");
		RemoteEntityTypeChangingEvent remoteEvent = new RemoteEntityTypeChangingEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
}