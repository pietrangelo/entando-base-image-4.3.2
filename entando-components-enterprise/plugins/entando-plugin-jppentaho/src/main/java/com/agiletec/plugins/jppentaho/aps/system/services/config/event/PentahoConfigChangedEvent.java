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
package com.agiletec.plugins.jppentaho.aps.system.services.config.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * @author E.Santoboni
 */
public class PentahoConfigChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((PentahoConfigChangedObserver) srv).updateFromConfigChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return PentahoConfigChangedObserver.class;
	}
	
}
