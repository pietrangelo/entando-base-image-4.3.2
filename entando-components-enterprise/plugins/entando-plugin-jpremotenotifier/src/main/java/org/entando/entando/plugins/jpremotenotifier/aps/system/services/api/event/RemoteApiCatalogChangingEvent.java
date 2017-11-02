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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.api.event;

import org.entando.entando.plugins.jpremotenotifier.aps.system.services.JpRemoteNotifierSystemConstants;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.ApsRemoteEvent;

import com.agiletec.aps.system.common.IManager;

public class RemoteApiCatalogChangingEvent extends ApsRemoteEvent {

	@Override
	public String getEventID() {
		return JpRemoteNotifierSystemConstants.API_CATALOG_CHANGING_REMOTE_EVENT_ID;
	}

	@Override
	public void notify(IManager srv) {
		((RemoteApiCatalogChangingObserver) srv).updateFromApiCatalogChange(this);
		
	}

	@Override
	public Class getObserverInterface() {
		return RemoteApiCatalogChangingObserver.class;
	}
	
	@Override
	public boolean isReloadConfigEvent() {
		return false;
	}
}
