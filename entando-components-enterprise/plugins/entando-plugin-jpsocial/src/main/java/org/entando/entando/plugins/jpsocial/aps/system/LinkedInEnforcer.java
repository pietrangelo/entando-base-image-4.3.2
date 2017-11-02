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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.plugins.jpsocial.aps.system.services.client.ILinkedInManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.LinkedInManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.LinkedInCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

/**
 * @author S.Loru
 */
public class LinkedInEnforcer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(LinkedInEnforcer.class);
	
	private LinkedInCredentials createLinkedinCredentials(HttpServletRequest request, LinkedInAccessToken accessToken) {
		LinkedInCredentials linkedInCredentials = null;

		if (null != request && null != accessToken) {
			linkedInCredentials = new LinkedInCredentials(accessToken);
			request.getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_LINKEDIN, linkedInCredentials);
		} else {
			_logger.error("jpsocial: error creating the credentials for facebook");
		}
		return linkedInCredentials;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String currentAction = getAndRemoveNoLoginAction(request);
		IAuthorizationManager authManager =
				(IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, request);
		ConfigInterface configInterface =
				(ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request);
		ILinkedInManager linkedInManager = (LinkedInManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.LINKEDIN_MANAGER, request);
		String baseUrl = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
		String pathInfo = request.getPathInfo();
		String authRedirectionUrl = baseUrl.concat(JpSocialSystemConstants.SERVLET_LINKEDIN);
		String finalRedirectionUrl = "";
		// compose the final destination URL
		if (null != pathInfo && !"/".equals(pathInfo.trim()) && !"".contains(pathInfo.trim())) {
			finalRedirectionUrl = baseUrl.concat(pathInfo.substring(1));
			authRedirectionUrl = authRedirectionUrl.concat(pathInfo);
		}
		String devMode = configInterface.getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);
		if ("true".equalsIgnoreCase(devMode)) {
			_logger.debug(" DEV MODE :: {}", devMode);
			response.sendRedirect(authRedirectionUrl);
			String loginrequest = request.getParameter("loginrequest");
			if (this.getDomain().equals(loginrequest)) {
				UserDetails userDetails = this.getFakeUser();
				((HttpServletRequest) request).getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
			}
		} else {
			oauthService = linkedInManager.createLinkedInOAuthService();
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			// get the verification code
			String code = request.getParameter("oauth_verifier");
			// Path info, it really is the source of the invocation
			// redirection within the portal AFTER authentication
			LinkedInCredentials linkedInCredentials;
			if (code == null) {
				LinkedInRequestToken requestToken = oauthService.getOAuthRequestToken(authRedirectionUrl);
				request.getSession().setAttribute("requestToken", requestToken);
				response.sendRedirect(requestToken.getAuthorizationUrl());
			} else if (code != null && !"".equals(request.getSession().getAttribute("requestToken"))) {
				LinkedInRequestToken requestToken = (LinkedInRequestToken) request.getSession().getAttribute("requestToken");
				String oauthVerifier = request.getParameter("oauth_verifier");
				LinkedInAccessToken accessToken = oauthService.getOAuthAccessToken(requestToken, oauthVerifier);
				linkedInCredentials = createLinkedinCredentials(request, accessToken);
				if(linkedInCredentials != null){
					response.sendRedirect(finalRedirectionUrl);
				}
			}
			
			
			// URL (to this servlet) invoked to obtain the access token

			// get the confirmation code
//			if (null != code && !"".equals(code.trim())) {
//				ApsSystemUtils.getLogger().info("Ok LinkedIn verification code received");
//				// pass the verification code to facebook
//				LinkedInRequestToken accessTokenInfo = negotiateAccessToken(code, authRedirectionUrl);
//				// create the new authorization object for the current user
//				if (null != accessTokenInfo) {
//					// remove the facebook credentials
//					request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_LINKEDIN);
//					// check whether the current user has the social permission
//					 facebookCredentials = createFacebookCredentials(request, accessTokenInfo.getAccessToken());
//					if (facebookCredentials != null) {
//						User user = facebookCredentials.getClient().fetchObject("me", User.class, Parameter.with("fields", "id, name, birthday, website, email, quotes, bio"));
//						UserDetails userDetails = new FacebookUser(user.getId() + "@facebook.com", user.getName());
//						if (!JpSocialSystemConstants.NO_LOGIN.equals(currentAction)) {
//							request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
//						}
//					}
//					if (isSocialUser(request)) {
//						if (null != facebookCredentials) {
//							ApsSystemUtils.getLogger().info("--------------------------------------");
//							ApsSystemUtils.getLogger().info("user " + currentUser.getUsername() + " can now post contents on Facebook");
//							ApsSystemUtils.getLogger().info("--------------------------------------");
//						} else {
//							ApsSystemUtils.getLogger().error("**** FAILURE could not create Facebook credentials");
//						}
//						response.sendRedirect(finalRedirectionUrl);
//					} else if (isPortalUrl(pathInfo)) {
//
//						if (null == facebookCredentials) {
//							ApsSystemUtils.getLogger().error("**** FAILURE could not create Facebook credentials");
//						} else {
//							ApsSystemUtils.getLogger().info("--------------------------------------");
//							ApsSystemUtils.getLogger().info(" ANONYMOUS user can now post FREE TEXT on Facebook");
//							ApsSystemUtils.getLogger().info("--------------------------------------");
//						}
//						response.sendRedirect(finalRedirectionUrl);
//					} else {
//						// insufficient permissions
//						ApsSystemUtils.getLogger().info("--------------------------------------");
//						ApsSystemUtils.getLogger().info("user " + currentUser.getUsername() + " cannot post contents on Facebook due to insufficient permissions");
//						ApsSystemUtils.getLogger().info("--------------------------------------");
//						response.sendRedirect(finalRedirectionUrl);
//					}
//				}
//			}
			// TODO check if the current user has the social permission
		}
	}

	protected UserDetails getFakeUser() {
		return new com.agiletec.aps.system.services.user.User();
	}

	protected String getDomain() {
		return "@linkedin.com";
	}

	public ConfigInterface getConfigInterface() {
		return _configInterface;
	}

	public void setConfigInterface(ConfigInterface configInterface) {
		this._configInterface = configInterface;
	}
	
	private LinkedInApiClientFactory factory;
	private LinkedInApiClient client;
	private ConfigInterface _configInterface;
	private LinkedInOAuthService oauthService;
}
