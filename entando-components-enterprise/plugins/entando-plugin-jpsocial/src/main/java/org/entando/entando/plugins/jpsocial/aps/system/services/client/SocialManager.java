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

package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;
import static org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER.facebook;
import static org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER.twitter;

/**
 * @author S.Loru
 */
public class SocialManager extends AbstractService implements ISocialManager {

	@Override
	public void init() throws Exception {
	}
	

	@Override
	public String getAuthenticationUrl(PROVIDER provider, String callbackURL) throws ApsSystemException{
		String url = "";
		ISocialBaseManager manager = extractManager(provider);
		url = manager.getAuthenticationURL(callbackURL);
		return url;
	}
	
	private ISocialBaseManager extractManager(PROVIDER provider){
		switch (provider){
			case facebook:
				return this.getFacebookManager();
			case twitter:
				return this.getTwitterManager();
		}
		return null;
	}

	@Override
	public String shareText(JpSocialSystemConstants.PROVIDER provider, String text) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public IFacebookManager getFacebookManager() {
		return _facebookManager;
	}

	public void setFacebookManager(IFacebookManager facebookManager) {
		this._facebookManager = facebookManager;
	}

	public ITwitterManager getTwitterManager() {
		return _twitterManager;
	}

	public void setTwitterManager(ITwitterManager twitterManager) {
		this._twitterManager = twitterManager;
	}

	private IFacebookManager _facebookManager;
	private ITwitterManager _twitterManager;
}
