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

import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;

/**
 *
 * @author entando
 */
public abstract class AbstractCredentials implements ISocialUser {

  @Override
  public abstract String getProvider();

  /**
   * Not supported
   *
   * @return
   */
  @Override
  public String getScreenName() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getAccessToken() {
    return _accessToken;
  }

  public void setAccessToken(String accessToken) {
    this._accessToken = accessToken;
  }

  public String getTokenSecret() {
    return _tokenSecret;
  }

  public void setTokenSecret(String tokenSecret) {
    this._tokenSecret = tokenSecret;
  }
  
  private String _accessToken;
  private String _tokenSecret;
  
}
