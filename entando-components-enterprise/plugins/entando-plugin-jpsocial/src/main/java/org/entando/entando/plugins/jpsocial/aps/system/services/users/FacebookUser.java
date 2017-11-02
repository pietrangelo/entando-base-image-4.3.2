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
package org.entando.entando.plugins.jpsocial.aps.system.services.users;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;

public class FacebookUser extends AbstractSocialUser {
	
	public FacebookUser(String username, String screenname) {
		super();
		this.setUsername(username);
		this.setScreenName(screenname);
	}
	
	@Override
	public String getProvider() {
		return JpSocialSystemConstants.PROVIDER_FACEBOOK;
	}
	
}
