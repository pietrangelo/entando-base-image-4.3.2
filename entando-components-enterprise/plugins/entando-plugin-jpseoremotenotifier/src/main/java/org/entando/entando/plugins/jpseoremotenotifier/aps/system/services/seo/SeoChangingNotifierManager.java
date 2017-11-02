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
package org.entando.entando.plugins.jpseoremotenotifier.aps.system.services.seo;

import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.event.SeoChangedEvent;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.event.SeoChangedObserver;
import org.entando.entando.plugins.jpseoremotenotifier.aps.system.services.seo.event.RemoteSeoChangingEvent;
import org.entando.entando.plugins.jpseoremotenotifier.aps.system.services.seo.event.RemoteSeoChangingObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;

@Aspect
public class SeoChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteSeoChangingObserver, SeoChangedObserver{

	private static final Logger _logger =  LoggerFactory.getLogger(SeoChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromSeoChanging(RemoteSeoChangingEvent event) {
		System.out.println("Received event (SEO) :" + event.getEventID());
		try {
			((IManager) this.getMappingManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Error detected while refreshing mapping in response of an external event", t);
		}
	}

	@Override
	public void updateFromSeoChanged(SeoChangedEvent event) {
		//System.out.println("Sending notification (SEO)!");
		RemoteSeoChangingEvent remoteEvent = new RemoteSeoChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public ISeoMappingManager getMappingManager() {
		return _mappingManager;
	}
	public void setMappingManager(ISeoMappingManager mappingManager) {
		this._mappingManager = mappingManager;
	}
	
	private ISeoMappingManager _mappingManager;
	
}
