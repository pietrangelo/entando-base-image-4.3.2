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
package org.entando.entando.plugins.jpsocial.aps.tags;

import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.services.user.UserDetails;

public abstract class AbstractSocialLoginTag extends TagSupport {
	
	protected String getDevLoginUrl() {
		StringBuilder devLogUrl = new StringBuilder(this.getCurrentUrl());
		if (this.getCurrentUrl().contains("?")) {
			devLogUrl.append("&loginrequest=");
			devLogUrl.append(this.getDomain());
		} else {
			devLogUrl.append("?loginrequest=");
			devLogUrl.append(this.getDomain());
		}
		return devLogUrl.toString();
	}
	
	protected abstract UserDetails getFakeUser();
	
	protected abstract String getDomain();
	
	public String getCurrentUrl() {
		return _currentUrl;
	}
	public void setCurrentUrl(String currentUrl) {
		this._currentUrl = currentUrl;
	}
	
	private String _currentUrl;
	
}
