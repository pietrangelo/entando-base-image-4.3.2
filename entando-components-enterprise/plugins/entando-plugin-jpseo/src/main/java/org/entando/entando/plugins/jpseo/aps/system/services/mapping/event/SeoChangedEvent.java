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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;


public class SeoChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((SeoChangedObserver) srv).updateFromSeoChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return SeoChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}

	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private int _operationCode;
	
	public final static int PAGE_CHANGED_EVENT = 1;
	public final static int CONTENT_CHANGED_EVENT = 2;
}
