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
public class GoogleCredentials extends AbstractCredentials {

  public GoogleCredentials(String accessToken, String refreshToken, String developerKey) {
    this.setAccessToken(accessToken);
    this.setRefreshToken(refreshToken);
    if (null != developerKey && !"".equals(developerKey.trim())) {
      this.setDeveloperKey(developerKey.trim());
    }
    this._sessionToken = false;
  }

  @Override
  public String getProvider() {
    return JpSocialSystemConstants.PROVIDER_GOOGLE;
  }

  public void setSessionToken(String sessionToken) {
    this.setAccessToken(sessionToken);
    this._sessionToken = false;
  }
  
  public String getDeveloperKey() {
    return _developerKey;
  }

  public String getRefreshToken() {
    return _refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this._refreshToken = refreshToken;
  }
  
  public void setDeveloperKey(String developerKey) {
    this._developerKey = developerKey;
  }

  public boolean isSessionToken() {
    return _sessionToken;
  }
  
  private boolean _sessionToken;
  private String _developerKey;
  private String _refreshToken;

  
}
