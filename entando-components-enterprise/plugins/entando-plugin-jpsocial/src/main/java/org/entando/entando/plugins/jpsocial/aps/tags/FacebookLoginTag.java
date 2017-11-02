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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.entando.entando.plugins.jpsocial.aps.system.AccessTokenInfo;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.FacebookUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

public class FacebookLoginTag extends AbstractSocialLoginTag {

	private static final Logger _logger =  LoggerFactory.getLogger(FacebookLoginTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ConfigInterface configInterface = this.getConfigInterface();
		try {
			String devMode = configInterface.getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);
			String client_id = configInterface.getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_KEY_PARAM_NAME);
			String client_secret = configInterface.getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_SECRET_PARAM_NAME);
			ServletRequest request = this.pageContext.getRequest();
			if ("true".equalsIgnoreCase(devMode)) {
				_logger.debug(" DEV MODE :: {}", devMode);
				this.pageContext.setAttribute("facebook_autentication_url", this.getDevLoginUrl());
				String loginrequest = request.getParameter("loginrequest");
				if (this.getDomain().equals(loginrequest)) {
					UserDetails userDetails = this.getFakeUser();
					((HttpServletRequest) request).getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
				}
			} else {
				this.pageContext.setAttribute("facebook_autentication_url", this.getAuthUrl(client_id));
				String code = request.getParameter("code");
				String error_reason = request.getParameter("error_reason");
				String error = request.getParameter("error");
				String error_description = request.getParameter("error_description");
				String access_token = request.getParameter("access_token");
				if (null != code && code.length() > 0) {
					_logger.debug(" Code {}", code);
					this.pageContext.setAttribute("code", code);
					AccessTokenInfo data;
					try {
						data = this.getRedirectForAccessToken(code, client_id, client_secret);
					} catch (UnsupportedEncodingException e) {
						_logger.error("error in doStartTag", e);
						throw new JspException(e);
					}
					((HttpServletRequest) request).getSession().setAttribute("access_code", data);
					FacebookClient facebookClient = new DefaultFacebookClient(data.getAccessToken());
					User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id, name, birthday, website, email, quotes, bio"));
					UserDetails userDetails = new FacebookUser(user.getId() + this.getDomain(), user.getName());
					((HttpServletRequest) request).getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
					_logger.debug(" facebook - id {}, name {}, email {}", user.getId(), user.getName(), user.getEmail());
					
					return super.EVAL_PAGE;
				}
				if (null != access_token && access_token.length() > 0) {
					_logger.debug(" access_token {}", access_token);
					this.pageContext.setAttribute("access_token", access_token);
					return super.EVAL_PAGE;
				}
				if (null != error) {
					_logger.debug(" Error:{} - error_description:{} - error_reason:{}",error , error_description, error_reason);
					this.pageContext.setAttribute("error_description", error_description);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected", t);
		}
		return super.EVAL_PAGE;
	}
	
	@Override
	protected UserDetails getFakeUser() {
		return new FacebookUser(322 + this.getDomain(), "mr nice");
	}
	
	private AccessTokenInfo getRedirectForAccessToken(String code, String client_id, String client_secret) throws UnsupportedEncodingException {
		StringBuffer url = new StringBuffer(accesstokenUrl);
		url.append("?");
		url.append("client_id=");
		url.append(client_id);
		url.append("&");
		url.append("redirect_uri=");
		System.out.println("redirect1: "+this.getCurrentUrl());
		url.append(this.getCurrentUrl());
		url.append("&");
		url.append("client_secret=");
		url.append(client_secret);
		url.append("&");
		url.append("code=");
		url.append(code);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url.toString());
		_logger.debug("Richiesto url - {}", url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		String data = null;
		try {
			int statusCode = client.executeMethod(method);
			_logger.debug("Eseguito connessione http");
			if (statusCode != HttpStatus.SC_OK) {
				_logger.error("Errore connessione http - {}", method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			
			data = new String(responseBody).trim();
			_logger.debug("Access code ricevuto:");
			_logger.debug(data);
			
		} catch (HttpException e) {
			_logger.error("Violazione protocollo http", e);
		} catch (IOException e) {
			_logger.error("Errore di trasferimento http ", e);
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
	
	private String getAuthUrl(String client_id) throws UnsupportedEncodingException {
		String baseUrl = this.getConfigInterface().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuilder url = new StringBuilder("https://www.facebook.com/dialog/oauth?client_id=");
		url.append(client_id);
		url.append("&");
		url.append("redirect_uri=");
		String redirectUri = URLEncoder.encode(baseUrl+"Facebook?"+"currentUrl="+this.getCurrentUrl() , "ISO-8859-1");
		System.out.println("redirect1: "+redirectUri);
		url.append(redirectUri);
		url.append("&");
		url.append("scope=email,read_stream");
		return url.toString();
	}
	
	@Override
	protected String getDomain() {
		return "@facebook.com";
	}
	
	private ConfigInterface getConfigInterface(){
		return (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, pageContext);
	}
	
	
	private String accesstokenUrl = "https://graph.facebook.com/oauth/access_token";
	
	
	
}
