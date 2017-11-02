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
package org.entando.entando.plugins.jppentahoremotenotifier.aps.system.services.pentaho;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jppentahoremotenotifier.aps.system.services.pentaho.event.RemoteConfigChangingEvent;
import org.entando.entando.plugins.jppentahoremotenotifier.aps.system.services.pentaho.event.RemoteConfigChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfig;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfigDOM;

@Aspect
public class PentahoChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteConfigChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		 _logger.debug("{} ready ", this.getClass().getName());
	}

	@Override
	public void updateFromConfigChanging(RemoteConfigChangingEvent event) {
		System.out.println("Receiving notification (penthao)!");
		try {
			ApsProperties properties = event.getParameters();
			String xmlConfig = (String) properties.get(CONFIG_KEY);
			if (null != xmlConfig) {
				PentahoConfigDOM configDOM = new PentahoConfigDOM();
				PentahoConfig config = configDOM.extractConfig(xmlConfig);
				this.getPentahoConfigManager().updateConfig(config);
			}
		} catch (Throwable t) {
			_logger.error("error in updateFromConfigChanging", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager.updateConfig(..)) && args(config, ..)")
	public void notifyUpdateConfig(PentahoConfig config) {
		try {
			String xml = new PentahoConfigDOM().createConfigXml(config);
			ApsProperties properties = new ApsProperties();
			properties.put(CONFIG_KEY, xml);
			notifyRemoteEvent(properties);
		} catch (Throwable t) {
			_logger.error("error in notifyUpdateConfig", t);
		}
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Sending notification (penthao)!");
		RemoteConfigChangingEvent remoteEvent = new RemoteConfigChangingEvent();
		if (null != properties) {
			remoteEvent.setParameters(properties);
		}
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IPentahoConfigManager getPentahoConfigManager() {
		return _pentahoConfigManager;
	}

	public void setPentahoConfigManager(IPentahoConfigManager pentahoConfigManager) {
		this._pentahoConfigManager = pentahoConfigManager;
	}

	private IPentahoConfigManager _pentahoConfigManager;
	
	public static final Object CONFIG_KEY = "pentahoConfig";

}
