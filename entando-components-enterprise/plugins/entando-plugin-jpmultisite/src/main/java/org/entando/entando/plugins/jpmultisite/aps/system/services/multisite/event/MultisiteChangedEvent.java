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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;


public class MultisiteChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((MultisiteChangedObserver) srv).updateFromMultisiteChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return MultisiteChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public Multisite getMultisite() {
		return _multisite;
	}
	public void setMultisite(Multisite multisite) {
		this._multisite = multisite;
	}

	private Multisite _multisite;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
