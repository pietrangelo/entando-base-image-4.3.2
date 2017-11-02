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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify;

import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.util.ApsProperties;

public abstract class ApsRemoteEvent extends ApsEvent {
	
	public abstract String getEventID();
	
	public abstract boolean isReloadConfigEvent();
	
	public ApsProperties getParameters() {
		return _properties;
	}
	
	public void setParameters(ApsProperties properties) {
		this._properties = properties;
	}
	
	private ApsProperties _properties;
	
	public static final String EVENT_ID_PARAM_NAME = "eventId";
	
}
