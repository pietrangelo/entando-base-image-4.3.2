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

import org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteNotifyTroubleshootingAlarmThread extends Thread {

	private static final Logger _logger =  LoggerFactory.getLogger(RemoteNotifyTroubleshootingAlarmThread.class);
	
	public RemoteNotifyTroubleshootingAlarmThread(RemoteNotifyManager remoteNotMan, ApsRemoteEvent remoteEvent, Site remoteSite, int retCode) {
		this.remoteNotMan = remoteNotMan;
		this.remoteEvent = remoteEvent;
		this._retCode = retCode;
		this._remoteSite = remoteSite;
	}

	public void run() {
		try {
			remoteNotMan.sendAlarm(_remoteSite, this.remoteEvent, _retCode);
		} catch (Throwable t) {
			_logger.error("Errore in notificazione eventi remoto", t);
		}
	}

	public void setRemoteSite(Site remoteSite) {
		this._remoteSite = remoteSite;
	}

	public Site getRemoteSite() {
		return _remoteSite;
	}

	public void setRetCode(int retCode) {
		this._retCode = retCode;
	}

	public int getRetCode() {
		return _retCode;
	}

	private ApsRemoteEvent remoteEvent;
	private RemoteNotifyManager remoteNotMan;

	private Site _remoteSite;
	private int _retCode;

}

