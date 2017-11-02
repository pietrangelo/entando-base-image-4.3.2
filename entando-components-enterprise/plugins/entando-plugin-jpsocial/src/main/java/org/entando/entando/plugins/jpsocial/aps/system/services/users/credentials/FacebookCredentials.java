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

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;

/**
 *
 * @author entando
 */
public class FacebookCredentials extends AbstractCredentials {

  public FacebookCredentials(FacebookClient facebookClient) {
    this._client = facebookClient;
  }
  public FacebookCredentials(String accessToken) {
	  this.setAccessToken(accessToken);
	  this.setClient(new DefaultFacebookClient(accessToken));
  }
  
  @Override
  public String getProvider() {
    return JpSocialSystemConstants.PROVIDER_FACEBOOK;
  }

  public FacebookClient getClient() {
    return _client;
  }

  public void setClient(FacebookClient client) {
    this._client = client;
  }
  
  private FacebookClient _client;
}
