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

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import org.apache.commons.lang.StringUtils;

public class RemoteNotifyManager extends AbstractService implements IRemoteNotifyManager {

	private static final Logger _logger =  LoggerFactory.getLogger(RemoteNotifyManager.class);
	
	@Override
	public void init() throws ApsSystemException {
		this.checkEventTypes();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		int delay = this.getAlarmDelay();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -delay);
		this.setLastNotify(cal.getTime());
		_logger.debug("{} ready. Initialized {} event types", this.getClass().getName(), this.getEventTypes().size());
	}
	
	private void checkEventTypes() throws ApsSystemException {
		Set<Object> keys = this.getEventTypes().keySet();
		Iterator<Object> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String className = (String) this.getEventTypes().getProperty(key);
			ApsRemoteEvent eventType = this.instantiateEventType(className);
			if (!eventType.getEventID().equals(key)) {
				throw new ApsSystemException("EventID '" + eventType.getEventID() + "' does not match the key " + key + className);
			}
		}
	}

	protected ApsRemoteEvent instantiateEventType(String className) throws ApsSystemException{
		try {
			Class eventClass = Class.forName(className);
			ApsRemoteEvent event = (ApsRemoteEvent) eventClass.newInstance();
			return event;
		} catch (Throwable t) {
			_logger.error("Error on instantiating class {}", className, t);
			throw new ApsSystemException("Error on instantiating class " + className, t);
		}
	}
	
	@Override
	public void sendEvent(ApsRemoteEvent remoteEvent) {
		RemoteNotifingThread notifingThread = null;
		try {
			notifingThread = new RemoteNotifingThread(this, remoteEvent);
			String threadName = IRemoteNotifyManager.REMOTE_NOTIFY_THREAD_NAME + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
			notifingThread.setName(threadName);
			notifingThread.start();
			_logger.debug("Notificazione remota avviata");
		} catch (Throwable t) {
			_logger.error("Error detected while delivering notification", t);
		}
	}

	@Override
	public void startSendEvent(ApsRemoteEvent remoteEvent) {
		List<Site> sites = this.getMultisiteManager().getParents();
		for (int i=0; i<sites.size(); i++) {
			Site site = sites.get(i);
			HttpClient client = new HttpClient();
			String url_action = site.getFullApplBaseURL() + "remoteEventNotify";
			PostMethod post = new PostMethod(url_action);
			post.addParameter(ApsRemoteEvent.EVENT_ID_PARAM_NAME, remoteEvent.getEventID());
			ApsProperties properties = remoteEvent.getParameters();
			if (null != properties) {
				Enumeration keys = properties.propertyNames();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					post.addParameter(key, properties.getProperty(key));
				}
			}
			int retVal = 0;
			try {
				retVal = client.executeMethod(post);
				_logger.debug("Notified Event {} to site {}", remoteEvent.getEventID(), site.getFullApplBaseURL());
			} catch (Throwable t) {
				_logger.error("Errore in chiamata a client {}", site.getFullApplBaseURL(), t);
			} finally {
				post.releaseConnection();
				if (retVal != 200) {
					String alarmString = "Notifica remota -- risultato:" + retVal + " su evento:" + remoteEvent.getEventID() + "  per chiamata effettuata da " + this.getMultisiteManager().getCurrentSite().getCode() + " verso " + site.getCode();
					_logger.warn("{}",alarmString);
					Date now = new Date();
					Calendar cal = Calendar.getInstance();
					int delay = this.getAlarmDelay();
					cal.setTime(this.getLastNotify());
					cal.add(Calendar.MINUTE, delay);
					Date date = cal.getTime();
					if (date.before(now)) {
						RemoteNotifyTroubleshootingAlarmThread alarmThread = new RemoteNotifyTroubleshootingAlarmThread(this, remoteEvent, site, retVal);
						String threadName = IRemoteNotifyManager.REMOTE_NOTIFY_TROUBLESHOOTING_THREAD_NAME + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
						alarmThread.setName(threadName);
						alarmThread.start();
						this.setLastNotify(now);
					}
				}
			}
		}
	}
	
	public void sendAlarm(Site remoteSite, ApsRemoteEvent event, int retCode) {
		try {
			String alarmString = "Notifica remota -- risultato:" + retCode + " su evento:" + event.getEventID() + "  per chiamata effettuata da " + this.getMultisiteManager().getCurrentSite().getCode() + " verso " + remoteSite.getCode();
			String[] recipientsTo = this.getAlarmRecipients();
			if (null == recipientsTo) {
				return;
			}
			String senderCode = this.getConfigManager().getParam(IRemoteNotifyManager.EMAIL_ALARM_SENDER_CODE);
			if (StringUtils.isBlank(senderCode)) {
				return;
			}
			this.getMailManager().sendMail(alarmString, "Errore sincronizzazione multisito", recipientsTo, null, null, senderCode);
		} catch (Throwable t) {
			_logger.error("Errore in invio email malfunzionamento notifiche multisito", t);
		}
	}
	
	@Override
	public ApsRemoteEvent getRemoteEvent(ApsProperties properties) {
		ApsRemoteEvent event = null;
		String eventIdString = properties.getProperty(ApsRemoteEvent.EVENT_ID_PARAM_NAME);
		String eventClassName = this.getEventTypes().getProperty(eventIdString);
		try {
			event = this.instantiateEventType(eventClassName);
			event.setParameters(properties);
		} catch (Throwable t) {
			_logger.error("Error on instantiating class {}", eventClassName, t);
			throw new RuntimeException("Error on instantiating class " + eventClassName, t);
		}
		return event;
	}
	
	protected String[] getAlarmRecipients() {
		String[] recipientsTo = null;
		String parameter = this.getConfigManager().getParam(IRemoteNotifyManager.EMAIL_ALARM_RECIPIENTS);
		if (null != parameter) {
			recipientsTo = parameter.split(",");
		}
		return recipientsTo;
	}
	
	protected Integer getAlarmDelay() {
		Integer delay = null;
		String parameter = this.getConfigManager().getParam(IRemoteNotifyManager.EMAIL_ALARM_DELAY);
		try {
			delay = Integer.parseInt(parameter);
		} catch (Exception e) {
			delay = 60; //default delay - 60 minutes
		}
		return delay;
	}
	
	protected IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}
	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}
	
	public void setLastNotify(Date lastNotify) {
		this._lastNotify = lastNotify;
	}
	public Date getLastNotify() {
		return _lastNotify;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected Properties getEventTypes() {
		return _eventTypes;
	}
	public void setEventTypes(Properties eventTypes) {
		this._eventTypes = eventTypes;
	}
	
	private IMultisiteManager _multisiteManager;
	private Date _lastNotify;
	private IMailManager _mailManager;
	private ConfigInterface _configManager;
	
	private Properties _eventTypes;	
	
}
