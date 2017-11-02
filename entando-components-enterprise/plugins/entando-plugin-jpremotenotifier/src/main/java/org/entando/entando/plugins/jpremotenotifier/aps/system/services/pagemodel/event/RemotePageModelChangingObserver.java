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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.pagemodel.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface RemotePageModelChangingObserver extends ObserverService {
	
	public void updateFromPageModelChanging(RemotePageModelChangingEvent event);
	
}