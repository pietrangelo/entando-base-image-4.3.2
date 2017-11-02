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

import com.agiletec.aps.system.common.AbstractService;

public abstract class AbstractNotifierInterceptorManager extends AbstractService {
	
	protected IRemoteNotifyManager getRemoteNotifyManager() {
		return _remoteNotifyManager;
	}
	public void setRemoteNotifyManager(IRemoteNotifyManager remoteNotifyManager) {
		this._remoteNotifyManager = remoteNotifyManager;
	}
	
	private IRemoteNotifyManager _remoteNotifyManager;
	
}
