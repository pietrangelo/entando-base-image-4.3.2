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

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import twitter4j.Twitter;

/**
 * Twitter describes the twitter credentials needed
 * @author entando
 */
public class TwitterCredentials extends AbstractCredentials {

    public TwitterCredentials(String accessToken, String secret) {
        this.setAccessToken(accessToken);
        this.setTokenSecret(secret);
    }
	
    public TwitterCredentials(String accessToken) {
        this(accessToken, null);
    }

    @Override
    public String getProvider() {
        return JpSocialSystemConstants.PROVIDER_TWITTER;
    }

	public Twitter getClient() {
		return _client;
	}

	public final void setClient(Twitter client) {
		this._client = client;
	}
	
	private Twitter _client;
}
