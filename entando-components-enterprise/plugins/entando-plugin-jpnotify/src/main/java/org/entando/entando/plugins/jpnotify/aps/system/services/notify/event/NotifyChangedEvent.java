/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.Notify;


public class NotifyChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((NotifyChangedObserver) srv).updateFromNotifyChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return NotifyChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public Notify getNotify() {
		return _notify;
	}
	public void setNotify(Notify notify) {
		this._notify = notify;
	}

	private Notify _notify;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
