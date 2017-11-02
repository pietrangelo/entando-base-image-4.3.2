package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;

/**
 *
 * @author work
 */
public interface ISocialBaseManager {

	/**
	 * Get the URL to initiate the authentication process; if no realive url is
	 * provided then we default to the authentication page of the plugin (common
	 * for all the providers)
	 *
	 * @param callbackUrl
	 * @return
	 */
	public String getAuthenticationURL(String callbackUrl) throws ApsSystemException;

	public ISocialUser createSocialCredentials(String accessToken);
	
	public String getLinkBySocialId(String id) throws ApsSystemException;

}
