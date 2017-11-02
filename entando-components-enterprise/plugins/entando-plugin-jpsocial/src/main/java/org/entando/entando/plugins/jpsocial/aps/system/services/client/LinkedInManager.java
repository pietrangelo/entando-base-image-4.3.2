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

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;

/**
 * @author S.Loru
 */
public class LinkedInManager extends AbstractService implements ILinkedInManager {

	private static final Logger _logger =  LoggerFactory.getLogger(LinkedInManager.class);

	@Override
	public void init() {
	}

	@Override
	public void share(String text, LinkedInAccessToken accessToken) {
		ConfigInterface configManager = (ConfigInterface) this.getConfigManager();
		LinkedInApiClient client = createLinkedInClient(accessToken);
		
		client.postNetworkUpdate(text);
		_logger.debug("YOUSHARE: {}", text);
		System.out.println("youshare: " + text);
	}

	@Override
	public String getAuthenticationURL(String redirectUrl) throws ApsSystemException {
		ConfigInterface configManager = (ConfigInterface) this.getConfigManager();
		String appBaseUrl = configManager.getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuilder url = new StringBuilder("");
		String baseUrl = configManager.getParam(SystemConstants.PAR_APPL_BASE_URL);
		if (null != redirectUrl && !"".equals(redirectUrl.trim())) {
			// make sure to include the servlet in the request
			if (redirectUrl.indexOf(JpSocialSystemConstants.SERVLET_LINKEDIN) < 0) {
				redirectUrl = redirectUrl.replace(baseUrl, baseUrl.concat(JpSocialSystemConstants.SERVLET_LINKEDIN.concat("/")));
				url.append(redirectUrl);
			} else {
				// ok append as is
				url.append(redirectUrl);
			}
		} else {
			// use default!
			url.append(baseUrl);
			url.append("LinkedInEnforcer");
		}
		return url.toString();
	}

	@Override
	public LinkedInApiClient createLinkedInClient(LinkedInAccessToken accessToken) {
		LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(this.getConsumerKey(), this.getConsumerSecret());
		LinkedInApiClient client = factory.createLinkedInApiClient(accessToken);
		return client;
	}

	@Override
	public LinkedInOAuthService createLinkedInOAuthService() {
		LinkedInOAuthService oauthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(this.getConsumerKey(), this.getConsumerSecret());
		return oauthService;
	}

	private String getConsumerKey() {
		return this.getConfigManager().getParam(JpSocialSystemConstants.LINKEDIN_CONSUMER_KEY_PARAM_NAME);
	}

	private String getConsumerSecret() {
		return this.getConfigManager().getParam(JpSocialSystemConstants.LINKEDIN_CONSUMER_SECRET_PARAM_NAME);
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	private ConfigInterface _configManager;
}
