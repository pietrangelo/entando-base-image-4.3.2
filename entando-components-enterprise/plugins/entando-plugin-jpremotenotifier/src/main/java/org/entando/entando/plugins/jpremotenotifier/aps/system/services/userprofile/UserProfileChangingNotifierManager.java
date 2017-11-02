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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.userprofile;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedEvent;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.entity.event.RemoteEntityTypeChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.userprofile.event.RemoteUserProfileChangingEvent;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.userprofile.event.RemoteUserProfileChangingObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class UserProfileChangingNotifierManager extends AbstractNotifierInterceptorManager 
		implements RemoteUserProfileChangingObserver, RemoteEntityTypeChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void updateFromEntityTypeChanging(RemoteEntityTypeChangingEvent event) {
		try {
			ApsProperties properties = event.getParameters();
			String managerName = (String) properties.get("manager");
			if (null == managerName || !((IManager) this.getUserProfileManager()).getName().equals(managerName)) {
				return;
			}
			Integer operationCode = null;
			try {
				operationCode = Integer.parseInt((String)properties.get("operationCode"));
			} catch (Throwable t) {
				_logger.error("Errore in parsing operationCode {}", properties.get("operationCode"), t);
				return;
			}
			if (operationCode != EntityTypesChangingEvent.INSERT_OPERATION_CODE) {
				this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfileTypes_cacheGroup");
			}
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@Override
	public void updateFromUserProfileChanging(RemoteUserProfileChangingEvent event) {
		try {
			_logger.debug("Event received (user profile): {}", event.getEventID());
			ApsProperties properties = event.getParameters();
			String username = (String) properties.get("username");
			Integer operationCode = null;
			try {
				operationCode = Integer.parseInt((String)properties.get("operationCode"));
			} catch (Throwable t) {
				_logger.error("Errore in parsing operationCode {}", properties.get("operationCode"), t);
				return;
			}
			this.getCacheInfoManager().flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, "UserProfile_" + username);
			//raise local event
			IUserProfile profile = this.getUserProfileManager().getProfile(username);
			if (null == profile) {
				return;
			}
			ProfileChangedEvent localEvent = new ProfileChangedEvent();
			localEvent.setProfile(profile);
			localEvent.setOperationCode(operationCode);
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.updateUser(..)) && args(user,..)")
	public void notifyUpdate(Object user) {
		//System.out.println("Notifying event: update");
		if (user == null) {
			return;
		}
		UserDetails userDetails = (UserDetails) user;
		Object profile = userDetails.getProfile();
		if (profile == null) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("username", userDetails.getUsername());
		properties.put("operationCode", Integer.toString(ProfileChangedEvent.UPDATE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void notifyDelete(Object key) {
		//System.out.println("Notifying event: delete");
		String username = null;
        if (key instanceof String) {
            username = key.toString();
        } else if (key instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) key;
            username = userDetails.getUsername();
        }
        if (username == null) {
			return;
		}
		ApsProperties properties = new ApsProperties();
		//System.out.println("content " + content);
		properties.put("username", username);
		properties.put("operationCode", Integer.toString(ProfileChangedEvent.REMOVE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		_logger.debug("Sending notification (User Profile)!");
		RemoteUserProfileChangingEvent remoteEvent = new RemoteUserProfileChangingEvent();
		remoteEvent.setParameters(properties);
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	protected ICacheInfoManager getCacheInfoManager() {
		return _cacheInfoManager;
	}
	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this._cacheInfoManager = cacheInfoManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private ICacheInfoManager _cacheInfoManager;
	private IUserProfileManager _userProfileManager;
	
}
