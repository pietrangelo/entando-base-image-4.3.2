package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface SocialPostChangedObserver extends ObserverService {
	
	public void updateFromSocialPostChanged(SocialPostChangedEvent event);
	
}
