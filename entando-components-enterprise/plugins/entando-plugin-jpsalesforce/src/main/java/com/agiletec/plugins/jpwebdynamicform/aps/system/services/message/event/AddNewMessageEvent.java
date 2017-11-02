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

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * @author E.Santoboni - M.Diana
 */
public class AddNewMessageEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((AddNewMessageObserver) srv).updateFromNewMessage(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return AddNewMessageObserver.class;
	}
	
	public Message getMessage() {
		return _message;
	}
	public void setMessage(Message message) {
		this._message = message;
	}
	
	public String getUserEmail() {
		return _userEmail;
	}
	public void setUserEmail(String userEmail) {
		this._userEmail = userEmail;
	}
	
	private Message _message;
	private String _userEmail;
	
}
