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

/**
 *
 * @author entando
 */
public class YoutubeCredentials  extends AbstractCredentials {
 
  public YoutubeCredentials(String singleUseToken) {
    this.setAccessToken(singleUseToken);
    this._sessionToken = false;
  }

  public void setSessionToken(String sessionToken) {
    setAccessToken(sessionToken);
    this._sessionToken = true;
  }
  
  @Override
  public String getProvider() {
    return JpSocialSystemConstants.PROVIDER_YOUTUBE;
  }

  public boolean isSessionToken() {
    return _sessionToken;
  }
  
  private boolean _sessionToken;
  
}
