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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteNotifingThread extends Thread {

	private static final Logger _logger =  LoggerFactory.getLogger(RemoteNotifingThread.class);
	
	public RemoteNotifingThread(RemoteNotifyManager remoteNotMan, ApsRemoteEvent remoteEvent) {
		this.remoteNotMan = remoteNotMan;
		this.remoteEvent = remoteEvent;
	}
	
	public void run() {
		try {
			remoteNotMan.startSendEvent(remoteEvent);
			_logger.debug("Remote notifier up and running");
		} catch (Throwable t) {
			_logger.error("Errore in notificazione eventi remoto", t);
		}
	}
	
	private ApsRemoteEvent remoteEvent;
	private RemoteNotifyManager remoteNotMan;
	
}
