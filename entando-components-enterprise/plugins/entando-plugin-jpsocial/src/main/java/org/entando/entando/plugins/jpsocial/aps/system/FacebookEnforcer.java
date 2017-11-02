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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.FacebookUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.restfb.util.StringUtils;

/**
 *
 * @author entando
 */
public class FacebookEnforcer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(FacebookEnforcer.class);

	/**
	 * Create the Facebook credentials and put them in the session
	 *
	 * @param request
	 * @param accessToken
	 * @return
	 */
	private FacebookCredentials createFacebookCredentials(HttpServletRequest request, HttpServletResponse response, String accessToken) throws ApsSystemException {
		FacebookCredentials facebookCredentials = null;

		if (null != request && null != accessToken && !"".equals(accessToken.trim())) {
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
			facebookCredentials = new FacebookCredentials(facebookClient);

			facebookCredentials.setAccessToken(accessToken);
		} else {
			_logger.error("jpsocial: error creating the credentials for facebook");
		}
		return facebookCredentials;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String currentAction = getAndRemoveNoLoginAction(request);
		IAuthorizationManager authManager =
				(IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, request);
		ConfigInterface configInterface =
				(ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request);
		String baseUrl = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
		String pathInfo = request.getPathInfo();
		String authRedirectionUrl = baseUrl.concat(JpSocialSystemConstants.SERVLET_FACEBOOK);
		String finalRedirectionUrl = "";
		String loginParam = request.getParameter("login");


		// compose the final destination URL
		if (null != pathInfo && !"/".equals(pathInfo.trim()) && !"".contains(pathInfo.trim())) {
			finalRedirectionUrl = baseUrl.concat(pathInfo.substring(1));
			authRedirectionUrl = authRedirectionUrl.concat(pathInfo);
			if(finalRedirectionUrl.contains("/contentOnSessionMarker")){
				finalRedirectionUrl = finalRedirectionUrl.replace("/contentOnSessionMarker", "?contentOnSessionMarker");
			}
		}
		authRedirectionUrl = authRedirectionUrl.concat("?"+request.getQueryString());
		String devMode = configInterface.getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);
		/*
		if ("true".equalsIgnoreCase(devMode)) {
			_logger.debug(" DEV MODE :: " + devMode);
			response.sendRedirect(authRedirectionUrl);
			String loginrequest = request.getParameter("loginrequest");
			if (this.getDomain().equals(loginrequest)) {
				UserDetails userDetails = this.getFakeUser();
				((HttpServletRequest) request).getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
			}
		} else {
		*/	
			String clientId = configInterface.getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_KEY_PARAM_NAME);
			String appId = configInterface.getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_SECRET_PARAM_NAME);
			UserDetails currentUser = getCurrentUser(request);
			// get the verification code
			String code = request.getParameter("code");
			// Path info, it really is the source of the invocation
			// redirection within the portal AFTER authentication

			// URL (to this servlet) invoked to obtain the access token

			// get the confirmation code
			if (null != code && !"".equals(code.trim())) {
				_logger.debug("Ok Facebook verification code received");
				// pass the verification code to facebook
				AccessTokenInfo accessTokenInfo = negotiateAccessToken(code, clientId, appId, authRedirectionUrl);
				// create the new authorization object for the current user
				if (null != accessTokenInfo) {
					// remove the facebook credentials
					request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_FACEBOOK);
					// check whether the current user has the social permission
					FacebookCredentials facebookCredentials = null;
					try {
						facebookCredentials = createFacebookCredentials(request, response, accessTokenInfo.getAccessToken());
					} catch (ApsSystemException ex) {
						_logger.error("error in createFacebookCredentials", ex);
						throw new ServletException("error getting facebook credentials");
					}
					if (facebookCredentials != null) {
						User user = facebookCredentials.getClient().fetchObject("me", User.class, Parameter.with("fields", "id, name, birthday, website, email, quotes, bio"));
						UserDetails userDetails = new FacebookUser(user.getId() + "@facebook.com", user.getName());
						if (!JpSocialSystemConstants.NO_LOGIN.equals(currentAction) && StringUtils.isBlank(loginParam)) {
							request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
						}
						try {
							JpSocialSystemUtils.saveFacebookCredentialsCookie(facebookCredentials, response, request);
						} catch (ApsSystemException ex) {
							_logger.error("error in saveFacebookCredentialsCookie", ex);
							throw new ServletException("error saving facebook credentials");
						}
					}
					if (isSocialUser(request)) {
						if (null != facebookCredentials) {
							_logger.debug("user {} can now post contents on Facebook", currentUser.getUsername());
						} else {
							_logger.error("**** FAILURE could not create Facebook credentials");
						}
						response.sendRedirect(finalRedirectionUrl);
					} else if (isPortalUrl(pathInfo)) {

						if (null == facebookCredentials) {
							_logger.error("**** FAILURE could not create Facebook credentials");
						} else {
							_logger.debug(" ANONYMOUS user can now post FREE TEXT on Facebook");
						}
						response.sendRedirect(finalRedirectionUrl);
					} else {
						// insufficient permissions
						_logger.debug("user {} cannot post contents on Facebook due to insufficient permissions", currentUser.getUsername());
						response.sendRedirect(finalRedirectionUrl);
					}
				}
			//}
			// TODO check if the current user has the social permission
		}
	}

	protected String getDomain() {
		return "@facebook.com";
	}

	protected String getDevLoginUrl(String redirectUrl) {
		StringBuilder devLogUrl = new StringBuilder(redirectUrl);
		if (redirectUrl.contains("?")) {
			devLogUrl.append("&loginrequest=");
			devLogUrl.append(this.getDomain());
		} else {
			devLogUrl.append("?loginrequest=");
			devLogUrl.append(this.getDomain());
		}
		return devLogUrl.toString();
	}

	/**
	 * Obtain access token from the provider
	 *
	 * @param code
	 * @param app_id
	 * @param app_secret
	 * @param redirUrl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private AccessTokenInfo negotiateAccessToken(String code, String clientId, String app_secret, String redirUrl) throws UnsupportedEncodingException {
		HttpClient client = new HttpClient();
		StringBuilder url = new StringBuilder("https://graph.facebook.com/oauth/access_token");
		redirUrl = URLEncoder.encode(redirUrl, "UTF-8");
		url.append("?");
		url.append("client_id=");
		url.append(clientId);
		url.append("&");
		url.append("redirect_uri=");
		System.out.println("redirect2: " + redirUrl);
		url.append(redirUrl);
		url.append("&");
		url.append("client_secret=");
		url.append(app_secret);
		url.append("&");
		url.append("code=");
		url.append(code);
		GetMethod method = new GetMethod(url.toString());
		//    ApsSystemUtils.getLogger().info("URL requested - " + url);

		System.out.println("URL requested - " + url.toString());

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		String data = null;
		try {
			int statusCode = client.executeMethod(method);
			_logger.debug("Established HTTP connection with Facebook to get the access token");
			if (statusCode != HttpStatus.SC_OK) {
				_logger.error("HTTP connection error - {}", method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();

			data = new String(responseBody).trim();

		} catch (HttpException e) {
			_logger.error("HTTP exception detected ", e);
		} catch (IOException e) {
			_logger.error("HTTP transfer error", e);
		} finally {
			method.releaseConnection();
			client = null;
			method = null;
		}
		AccessTokenInfo tokenInfo = new AccessTokenInfo();
		if (null != data && data.length() > 0) {
			String[] params = data.split("&");

			for (String par : params) {
				String[] keyValue = par.split("=");

				if (keyValue[0].equals("access_token")) {
					tokenInfo.setAccessToken(keyValue[1]);
				}
				if (keyValue[0].equals("expires")) {
					tokenInfo.setExpires(Integer.valueOf(keyValue[1]));
				}
			}
		}
		tokenInfo.setStart(new Date().getTime());
		return tokenInfo;
	}

	protected UserDetails getFakeUser() {
		return new FacebookUser(322 + this.getDomain(), "mr nice");
	}
}
