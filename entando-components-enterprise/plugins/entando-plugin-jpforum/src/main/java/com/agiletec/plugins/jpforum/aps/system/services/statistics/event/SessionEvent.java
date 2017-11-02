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
package com.agiletec.plugins.jpforum.aps.system.services.statistics.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

public class SessionEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((SessionObserver) srv).updateFromSessionEvent(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return SessionObserver.class;
	}
	
	public String getSessionId() {
		return _sessionId;
	}
	public void setSessionId(String sessionId) {
		this._sessionId = sessionId;
	}

	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	private String _sessionId;
	private int _operationCode;
	
	public static final int OPERATION_CREATED = 1;
	public static final int REMOVE_DESTROYED = 2;
	
}