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

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class TwitterManager extends TwitterCookieConsumerManager
		implements ITwitterManager {

	private static final Logger _logger =  LoggerFactory.getLogger(TwitterManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	protected void release() {
		super.release();
	}

	@Override
	public String tweet(String msg, String accessToken, String accessTokenSecret) throws ApsSystemException {
		ConfigInterface configManager = (ConfigInterface) this.getConfigManager();
		String consumerKey = configManager.getParam(JpSocialSystemConstants.TWITTER_CONSUMER_KEY_PARAM_NAME);
		String consumerSecret = configManager.getParam(JpSocialSystemConstants.TWITTER_CONSUMER_SECRET_PARAM_NAME);
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Status updatedStatus = null;
		try {
			if (null != consumerKey && null != consumerSecret) {
				builder.setDebugEnabled(true);
				builder.setOAuthAccessToken(accessToken);
				builder.setOAuthAccessTokenSecret(accessTokenSecret);
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerSecret);

				OAuthAuthorization auth = new OAuthAuthorization(builder.build());
				Twitter twitter = new TwitterFactory().getInstance(auth);
				updatedStatus = twitter.updateStatus(msg);
			}
			String id =  Long.toString(updatedStatus.getUser().getId()) + "_" + Long.toString(updatedStatus.getId());
			return id;
		} catch (Throwable t) {
			_logger.error("Error tweeting a message", t);
			throw new ApsSystemException("Error tweeting", t);
		}
	}

	@Override
	public String tweet(Twitter client, String message) throws ApsSystemException{
		String id = "";
		try {
			Status updateStatus = client.updateStatus(message);
			id = Long.toString(updateStatus.getId());
		} catch (Throwable t) {
			_logger.error("Error tweeting", t);
			throw new ApsSystemException("Error tweeting", t);
		}
		return id;
	}

	@Override
	public String getAuthenticationURL(String redirectUrl) throws ApsSystemException {
		ConfigInterface configManager = (ConfigInterface) this.getConfigManager();
		String appBaseUrl = configManager.getParam(SystemConstants.PAR_APPL_BASE_URL);

		try {
			StringBuilder url = new StringBuilder(appBaseUrl);
			url.append("Twitter?");
			url.append(JpSocialSystemConstants.TW_REDIRECT_URL);
			url.append("=");
			if (null != redirectUrl && !"".equals(redirectUrl.trim())) {
				redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
				url.append(redirectUrl);
			} else {
				url.append(appBaseUrl);
				url.append(JpSocialSystemConstants.BACKEND_ACTION);
			}
			return url.toString();
		} catch (Throwable t) {
			_logger.error("Error getting AuthenticationURL", t);
			throw new ApsSystemException("Error getting AuthenticationURL", t);
		}
	}

	@Override
	public Twitter createClient() {
		String consumerKey = this.getConfigManager().getParam(JpSocialSystemConstants.TWITTER_CONSUMER_KEY_PARAM_NAME);
		String consumerSecret = this.getConfigManager().getParam(JpSocialSystemConstants.TWITTER_CONSUMER_SECRET_PARAM_NAME);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

	@Override
	public Twitter createClient(String accessToken, String tokenSecret) {
		String consumerKey = this.getConfigManager().getParam(JpSocialSystemConstants.TWITTER_CONSUMER_KEY_PARAM_NAME);
		String consumerSecret = this.getConfigManager().getParam(JpSocialSystemConstants.TWITTER_CONSUMER_SECRET_PARAM_NAME);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(tokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

	@Override
	public ISocialUser createSocialCredentials(String accessToken, String tokenSecret) {
		TwitterCredentials credentials = new TwitterCredentials(accessToken, tokenSecret);
		credentials.setClient(createClient(accessToken, tokenSecret));
		return credentials;
	}

	@Override
	public ISocialUser createSocialCredentials(String accessToken) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getLinkBySocialId(String id) throws ApsSystemException{
		String userId = JpSocialSystemUtils.getUserIdBySocialId(id);
		String postId = JpSocialSystemUtils.getPostIdBySocialId(id);
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(postId)){
			throw new ApsSystemException("Cannot userId not found for this facebook id" + id);
		}
		if(StringUtils.isBlank(postId)){
			throw new ApsSystemException("Cannot postId not found for this facebook id" + id);
		}
		return "https://www.twitter.com/" + userId +"/status/" + postId;
	}




}
