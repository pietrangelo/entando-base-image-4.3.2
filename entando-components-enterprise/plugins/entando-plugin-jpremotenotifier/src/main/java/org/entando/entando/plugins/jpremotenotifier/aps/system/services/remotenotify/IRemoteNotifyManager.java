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

import com.agiletec.aps.util.ApsProperties;

public interface IRemoteNotifyManager {
	
	public void sendEvent(ApsRemoteEvent remoteEvent);
	
	public void startSendEvent(ApsRemoteEvent remoteEvent);
	
	public ApsRemoteEvent getRemoteEvent(ApsProperties properties);
	
	public static final String REMOTE_NOTIFY_THREAD_NAME = "RemoteNotifing_";
	
	public static final String REMOTE_NOTIFY_TROUBLESHOOTING_THREAD_NAME = "RemoteNotifing_Troubleshooting_";
	
	public static final String EMAIL_ALARM_RECIPIENTS = "jpremotenotifier_alarm_recipients";
	
	public static final String EMAIL_ALARM_SENDER_CODE = "jpremotenotifier_alarm_senderCode";
	
	public static final String EMAIL_ALARM_DELAY = "jpremotenotifier_alarm_delay";
	
}
