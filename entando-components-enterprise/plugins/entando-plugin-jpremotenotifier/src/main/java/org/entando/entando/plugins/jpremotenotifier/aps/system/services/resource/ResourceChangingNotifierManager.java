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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.resource.event.RemoteResourceChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.resource.event.RemoteResourceChangingObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

@Aspect
public class ResourceChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteResourceChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(ResourceChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromResourceChanging(RemoteResourceChangingEvent event) {
		System.out.println("Event received (resource): " + event.getEventID());
		try {
			boolean useSan = this.getMultisiteManager().isUseSan();
			if (!useSan) {
				ApsProperties properties = event.getParameters();
				String operationCodeParam = (String) properties.get("operationCode");
				int operationCode = Integer.parseInt(operationCodeParam);
				String serializedBean = (String) properties.get("bean");
				byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer(serializedBean);
				ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
				ResourceDataBeanObj obj = (ResourceDataBeanObj) objectIn.readObject(); 
				obj.setInputStream(new DataInputStream(new ByteArrayInputStream(obj.getByteArray())));
				ResourceInterface res = this.getResourceManager().createResourceType(obj.getResourceType());
				res.setDescr(obj.getDescr());
				res.setMainGroup(obj.getMainGroup());
				res.setCategories(obj.getCategories());
				if (operationCode == OPERATION_CODE_ADD) {
					res.saveResourceInstances(obj);
				} else if (operationCode == OPERATION_CODE_UPDATE) {
					//NOTHING TO DO
				} else if (operationCode == OPERATION_CODE_DELETE) {
					res.deleteResourceInstances();
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager.addResource(..)) && args(bean)")
	public void notifyAdd(ResourceDataBean bean) {
		//System.out.println("Notifying event: AddResource");
		ApsProperties properties = new ApsProperties();
		properties.put("operationCode", Integer.toString(OPERATION_CODE_ADD));
		String encodedResource = this.getEncodedResouceDataBean(bean);
		properties.put("bean", encodedResource);
		//System.out.println(encodedResource);
		this.notifyRemoteEvent(properties);
	}

	private String getEncodedResouceDataBean(ResourceDataBean bean) {
		String encodedResource = new String();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ous = new ObjectOutputStream(baos);
			ous.writeObject(new ResourceDataBeanObj(bean));
			ous.flush();
			ous.close();
			byte[] buf = baos.toByteArray();
			encodedResource = new sun.misc.BASE64Encoder().encode(buf);
		} catch (Throwable t) {
			_logger.error("errore in encoding risorsa", t);
			throw new RuntimeException("errore in encoding risorsa");
		}
		return encodedResource;
	}

	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Inivio notifica evento (resource)");
		RemoteResourceChangingEvent remoteEvent = new RemoteResourceChangingEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}

	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	public IResourceManager getResourceManager() {
		return _resourceManager;
	}

	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}
	public IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}

	private IResourceManager _resourceManager;
	private IMultisiteManager _multisiteManager;

	private static final int OPERATION_CODE_ADD = 1;
	private static final int OPERATION_CODE_UPDATE = 2;
	private static final int OPERATION_CODE_DELETE = 3;
}
