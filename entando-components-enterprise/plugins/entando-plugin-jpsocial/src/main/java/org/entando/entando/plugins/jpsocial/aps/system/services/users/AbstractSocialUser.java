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

import com.agiletec.aps.system.services.user.AbstractUser;

public abstract class AbstractSocialUser extends AbstractUser implements ISocialUser {

	@Override
	public boolean isEntandoUser() {
		return false;
	} 
	
	@Override
	@Deprecated
	public boolean isJapsUser() {
		return this.isEntandoUser();
	} 
	
	@Override
	public String toString() {
		return this.getScreenName();
	}
	
	@Override
	public abstract String getProvider();
	
	@Override
	public String getScreenName() {
		return _screenname;
	}
	
	public void setScreenName(String screename) {
		this._screenname = screename;
	}
	
	private String _screenname;
}
