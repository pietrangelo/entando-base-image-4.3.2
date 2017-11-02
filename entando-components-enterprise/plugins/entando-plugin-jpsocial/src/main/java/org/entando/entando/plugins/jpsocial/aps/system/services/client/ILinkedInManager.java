/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;

/**
 *
 * @author work
 */
public interface ILinkedInManager {
	
	public String getAuthenticationURL(String redirectUrl) throws ApsSystemException;
	
	public void share(String text, LinkedInAccessToken accessToken);
	
	public LinkedInApiClient createLinkedInClient(LinkedInAccessToken accessToken);

	public LinkedInOAuthService createLinkedInOAuthService();
	
}
