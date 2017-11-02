/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface NotifyChangedObserver extends ObserverService {
	
	public void updateFromNotifyChanged(NotifyChangedEvent event);
	
}
