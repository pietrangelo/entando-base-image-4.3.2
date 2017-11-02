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
package org.entando.entando.plugins.jpsocial.aps.system;

public class AccessTokenInfo {

	public String getAccessToken() {
		return _accessToken;
	}
	public void setAccessToken(String accessToken) {
		this._accessToken = accessToken;
	}
	
	public Integer getExpires() {
		return _expires;
	}
	public void setExpires(Integer expires) {
		this._expires = expires;
	}
	
	public Long getStart() {
		return _start;
	}
	public void setStart(Long start) {
		this._start = start;
	}

  public String getRefreshToken() {
    return _refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this._refreshToken = refreshToken;
  }
  
	private String _accessToken;
	private Integer _expires;
	private Long _start;
  // Optional, Google only
  private String _refreshToken;
}
