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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.event;

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * @author E.Santoboni - M.Diana
 */
public interface AddNewMessageObserver extends ObserverService {
	
	public void updateFromNewMessage(AddNewMessageEvent event);
	
}
