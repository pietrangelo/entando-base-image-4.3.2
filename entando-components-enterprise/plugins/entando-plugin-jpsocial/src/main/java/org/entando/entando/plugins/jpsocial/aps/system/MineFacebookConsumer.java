/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * Entando is a free software;
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 *
 * See the file License for the specific language governing permissions
 * and limitations under the License
 *
 *
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpsocial.aps.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
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

/**
 *
 * @author S.Loru
 */
public class MineFacebookConsumer extends HttpServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(MineFacebookConsumer.class);
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.setConfigInterface((ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request));
		String code = request.getParameter("code");
		this.setCurrentUrl(request.getParameter("currentUrl"));
		String client_secret = this.getConfigInterface().getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_SECRET_PARAM_NAME);
		String client_id = this.getConfigInterface().getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_KEY_PARAM_NAME);
		AccessTokenInfo data;
		String token = null;
		if (code == null || code.equals("")) {
			// an error occurred, handle this
		}
		data = this.getRedirectForAccessToken(code, client_id, client_secret);
		FacebookClient facebookClient = new DefaultFacebookClient(data.getAccessToken());
		request.getSession().setAttribute("access_code", data);
		User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id, name, birthday, website, email, quotes, bio"));
		UserDetails userDetails = new FacebookUser(user.getId() + this.getDomain(), user.getName());
		((HttpServletRequest) request).getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
		_logger.info(" facebook - id {}, name {}, email {}", user.getId(), user.getName(),user.getEmail());
	}

	private AccessTokenInfo getRedirectForAccessToken(String code, String client_id, String client_secret) throws UnsupportedEncodingException {
		StringBuffer url = new StringBuffer(accesstokenUrl);
		String baseUrl = this.getConfigInterface().getParam(SystemConstants.PAR_APPL_BASE_URL);
		url.append("?");
		url.append("client_id=");
		url.append(client_id);
		url.append("&");
		url.append("redirect_uri=");
		String redirectUri = URLEncoder.encode(baseUrl+"Facebook?"+"currentUrl="+this.getCurrentUrl() , "ISO-8859-1");
		System.out.println("redirect2: "+redirectUri);
		url.append(redirectUri);
		url.append("&");
		url.append("client_secret=");
		url.append(client_secret);
		url.append("&");
		url.append("code=");
		url.append(code);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url.toString());
		_logger.debug("Richiesto url - {}", url);
		System.out.println("Richiesto url - " + url);
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
			_logger.info("Access code ricevuto:{}", data);

		} catch (HttpException e) {
			_logger.error("Violazione protocollo http", e);
		} catch (IOException e) {
			_logger.error("Errore di trasferimento http", e);
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
	private String accesstokenUrl = "https://graph.facebook.com/oauth/access_token";

	public String getCurrentUrl() {
		return _currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this._currentUrl = currentUrl;
	}
	
	private String getDomain() {
		return "@facebook.com";
	}
	
	private void setConfigInterface(ConfigInterface configInterface){
		this._configInterface = configInterface;
	}
	
	private ConfigInterface getConfigInterface(){
		return this._configInterface;
	}
	
	
	private ConfigInterface _configInterface;
	
	private String _currentUrl;
}
