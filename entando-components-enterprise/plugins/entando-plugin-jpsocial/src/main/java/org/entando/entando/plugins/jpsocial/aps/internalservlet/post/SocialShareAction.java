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
package org.entando.entando.plugins.jpsocial.aps.internalservlet.post;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ISocialManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.restfb.FacebookClient;

/**
 * @author S.Loru
 */
public class SocialShareAction extends AbstractSocialFrontAction implements ISocialShareAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialShareAction.class);
	
	@Override
	public String intro() {
		this.setPostText(this.getTextToShare() + " " + this.getPermalink());
		return SUCCESS;
	}

	@Override
	public String cancel() {
		this.setRedirectUrl(JpSocialSystemUtils.addParameterToUrl(this.getRedirectUrl(), "social_status", "ko"));
		return SUCCESS;
	}

	@Override
	public String post() {
		String result = INPUT;
		try {
			if (null != this.getProvider()) {
				switch (JpSocialSystemConstants.PROVIDER.valueOf(this.getProvider())) {
					case facebook:
						result = postOnFacebook();
						break;
					case twitter:
						result = postOnTwitter();
						break;
				}
			} else {
				this.addActionError(this.getText("jpsocial.error.provider"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in post", t);
			this.addActionError(t.getMessage());
			return INPUT;
		}
		if(SUCCESS.equals(result)){
			this.setRedirectUrl(JpSocialSystemUtils.addParameterToUrl(this.getRedirectUrl(), "social_status", "ok"));
		}
		return result;
	}

	//FIXME handle exception
	private String postOnFacebook() throws ApsSystemException {
		FacebookCredentials credentials = null;
		credentials = JpSocialSystemUtils.getFacebookCredentialsFromCookie(this.getRequest());
		if (null != credentials) {
			FacebookClient client = credentials.getClient();
			String id = this.getFacebookManager().publishMessage(client, this.getPostText());
			if (StringUtils.isNotBlank(id)) {
				createAndAddSocialPost(id, this.getObjectId(), this.getService(), this.getPermalink(), this.getProvider(), this.getTextToShare() + " " + this.getPermalink(), this.getCurrentUser().getUsername());
			} else {
				this.addActionError(this.getText("jpsocial.error.post"));
				return INPUT;
			}
		} else {
			this.addActionError(this.getText("jpsocial.error.credentials.facebook"));
			return INPUT;
		}
		return SUCCESS;
	}

	//FIXME handle exception
	private String postOnTwitter() throws ApsSystemException {
		TwitterCredentials credentials;
		credentials = JpSocialSystemUtils.getTwitterCredentialsFromCookie(this.getRequest());
		if (null != credentials) {
			Twitter client = this.getTwitterManager().createClient(credentials.getAccessToken(), credentials.getTokenSecret());
			String id = this.getTwitterManager().tweet(client, this.getPostText());
			if (StringUtils.isNotBlank(id)) {
				createAndAddSocialPost(id, this.getObjectId(), this.getService(), this.getPermalink(), this.getProvider(), this.getPostText(), this.getCurrentUser().getUsername());
			} else {
				this.addActionError(this.getText("jpsocial.error.post"));
				return INPUT;
			}
		} else {
			this.addActionError(this.getText("jpsocial.error.credentials.twitter"));
			return INPUT;
		}
		return SUCCESS;
	}

	protected String getCallbackActionURL(String provider) {
		String baseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuilder url = null;
		try {
			url = new StringBuilder(baseUrl);
			url.append(JpSocialSystemConstants.SOCIAL_ACTION_NAMESPACE);
			url.append("socialLogin.action");
			url.append("?provider=" + provider);
			url.append("&callbackURL=" + this.getCallbackURL());
		} catch (Throwable t) {
			_logger.error("error in getCallbackActionURL", t);
		}
		return url.toString();
	}

	public String getProvider() {
		return _provider;
	}

	public void setProvider(String provider) {
		this._provider = provider;
	}

	public ISocialManager getSocialManager() {
		return _socialManager;
	}

	public void setSocialManager(ISocialManager socialManager) {
		this._socialManager = socialManager;
	}

	public String getCallbackURL() {
		return _callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this._callbackURL = callbackURL;
	}

	public String getTextToShare() {
		return _textToShare;
	}

	public void setTextToShare(String textToShare) {
		this._textToShare = textToShare;
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public String getPermalink() {
		return _permalink;
	}

	public void setPermalink(String permalink) {
		this._permalink = permalink;
	}

	public String getService() {
		return _service;
	}

	public void setService(String service) {
		this._service = service;
	}

	public String getObjectId() {
		return _objectId;
	}

	public void setObjectId(String objectId) {
		this._objectId = objectId;
	}

	public String getPostText() {
		return _postText;
	}

	public void setPostText(String postText) {
		this._postText = postText;
	}

	public String getRedirectUrl() {
		return _redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this._redirectUrl = redirectUrl;
	}
	private String _postText;
	private String _provider;
	private ISocialManager _socialManager;
	private String _callbackURL;
	private String _textToShare;
	private ConfigInterface _configManager;
	private String _permalink;
	private String _service;
	private String _objectId;
	private String _redirectUrl;
}
