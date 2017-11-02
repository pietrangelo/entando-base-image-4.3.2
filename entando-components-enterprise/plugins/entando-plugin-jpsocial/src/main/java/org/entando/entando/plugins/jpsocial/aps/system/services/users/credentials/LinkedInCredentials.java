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
package org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials;

import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;

/**
 * @author S.Loru
 */
public class LinkedInCredentials extends AbstractCredentials {

	public LinkedInCredentials(LinkedInAccessToken accessToken) {
		this.setLinkedInAccessToken(accessToken);
		this._sessionToken = false;
	}

	public void setSessionToken(String sessionToken) {
		setAccessToken(sessionToken);
		this._sessionToken = true;
	}

	@Override
	public String getProvider() {
		return JpSocialSystemConstants.PROVIDER_LINKEDIN;
	}

	public boolean isSessionToken() {
		return _sessionToken;
	}

	public LinkedInAccessToken getLinkedInAccessToken() {
		return linkedInAccessToken;
	}

	public void setLinkedInAccessToken(LinkedInAccessToken linkedInAccessToken) {
		this.linkedInAccessToken = linkedInAccessToken;
	}
	private LinkedInAccessToken linkedInAccessToken;
	private boolean _sessionToken;
}
