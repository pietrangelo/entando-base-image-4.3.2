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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.ParameterStyle;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthClient;
import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthResponseMessage;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpMessage;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpResponseMessage;
import org.entando.entando.plugins.jpsocial.aps.system.httpclient.TwitterHttpClient;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterCookieConsumer;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.TwitterUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.net.URI;

/**
 * A trivial consumer of the 'friends_timeline' service at Twitter.
 *
 * @author John Kristian
 */
public class MineTwitterConsumer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(MineTwitterConsumer.class);
	
//  protected final Logger log = Logger.getLogger(getClass().getName());
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		OAuthConsumer consumer = null;
		//log.info(" MineTwitterConsumer START");
		this.setRedirectUrl(request.getParameter(JpSocialSystemConstants.TW_REDIRECT_URL));
		ConfigInterface configInterface = (ConfigInterface) ApsWebApplicationUtils.getBean("BaseConfigManager", request);
		String devMode = configInterface.getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);
		String loginrequest = request.getParameter("loginrequest");
		if ("true".equalsIgnoreCase(devMode)) {
			_logger.info(" DEV MODE :: {} - auth url {}", devMode, this.getDevLoginUrl());
			this.setRedirectUrl(this.getDevLoginUrl());
			if (this.getDomain().equals(loginrequest)) {
				UserDetails userDetails = this.getFakeUser();
				request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
			}
		} else {
			ITwitterCookieConsumer twitterConsumerManager =
					(ITwitterCookieConsumer) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CONSUMER_MANAGER, request);
			try {
				consumer = twitterConsumerManager.getTwitterConsumer(request);
				OAuthAccessor accessor = twitterConsumerManager.getAccessor(request, response, consumer);
				OAuthClient client = new OAuthClient(new TwitterHttpClient());
				OAuthResponseMessage result = client.access(accessor.newRequestMessage(OAuthMessage.GET,
						"https://api.twitter.com/1.1/account/verify_credentials.json", null), ParameterStyle.AUTHORIZATION_HEADER);
				int status = result.getHttpResponse().getStatusCode();
				if (status != HttpResponseMessage.STATUS_OK) {
					OAuthProblemException problem = result.toOAuthProblemException();
					if (problem.getProblem() != null) {
						throw problem;
					}
					Map<String, Object> dump = problem.getParameters();
					response.setContentType("text/plain");
					PrintWriter out = response.getWriter();
					out.println(dump.get(HttpMessage.REQUEST));
					out.println("----------------------");
					out.println(dump.get(HttpMessage.RESPONSE));
				} else {
					// Simply pass the data through to the browser:
					String json = twitterConsumerManager.copyResponse(result);
//				System.out.println("****************************************");
//				System.out.println(json);
//				System.out.println("****************************************");
					JSONObject jsonObject = new JSONObject(json);
					String id = jsonObject.getString("id");
					//String id_str = jsonObject.getString("id_str");				
					String name = jsonObject.getString("name");
					//log.info(" id " + jsonObject.getString("id"));
					//log.info(" id_str " + jsonObject.getString("id_str"));
//        log.info(" name " + jsonObject.getString("name"));
					ITwitterManager twitterManager = (ITwitterManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CLIENT_MANAGER, request);
					TwitterCredentials credentials = (TwitterCredentials) twitterManager.createSocialCredentials(accessor.accessToken, accessor.tokenSecret);
//					((HttpServletRequest) request).getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER, credentials);
					String currentAction = getAndRemoveNoLoginAction(request);
					URI uri = new URI(this.getRedirectUrl());
					List<NameValuePair> urlMap = URLEncodedUtils.parse(uri, "UTF-8");
					boolean login = true;
					for (int i = 0; i < urlMap.size(); i++) {
						NameValuePair nameValuePair = urlMap.get(i);
						String namePair = nameValuePair.getName();
						if(null != namePair && namePair.endsWith("login")){
							login = Boolean.parseBoolean(nameValuePair.getValue());
						}
					}
					if(!JpSocialSystemConstants.NO_LOGIN.equals(currentAction) && login){
						UserDetails userDetails = new TwitterUser(id + this.getDomain(), name);
						request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
					}
					JpSocialSystemUtils.saveTwitterCredentialsCookie(credentials, response, request);
					//log.info(" MineTwitterConsumer REDIRECT URL " + redirectUrl);
					if (null != this.getRedirectUrl() && this.getRedirectUrl().length() > 0) {
						//log.info(" MineTwitterConsumer REDIRECT");
						response.sendRedirect(this.getRedirectUrl());
						return;
					}
				}
				//log.info(" MineTwitterConsumer END ");
			} catch (Exception e) {
				twitterConsumerManager.handleException(e, request, response, consumer);
			} 
		}
		
	}

	protected String getDevLoginUrl() {
		StringBuilder devLogUrl = new StringBuilder(this.getRedirectUrl());
		if (this.getRedirectUrl().contains("?")) {
			devLogUrl.append("&loginrequest=");
			devLogUrl.append(this.getDomain());
		} else {
			devLogUrl.append("?loginrequest=");
			devLogUrl.append(this.getDomain());
		}
		return devLogUrl.toString();
	}

	protected UserDetails getFakeUser() {
		return new TwitterUser(322 + this.getDomain(), "mr nice");
	}

	protected String getDomain() {
		return "@twitter.com";
	}

	public String getRedirectUrl() {
		return _redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this._redirectUrl = redirectUrl;
	}
	private String _redirectUrl;
	private static final long serialVersionUID = 1L;
}
