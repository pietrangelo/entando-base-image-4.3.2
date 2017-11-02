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
package org.entando.entando.plugins.jpsalesforce.aps.internalservlet.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpsalesforce.aps.internalservlet.AbstractSalesforceFrontAction;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceConfig;
import org.entando.entando.plugins.jpsalesforce.aps.system.utils.SalesforceMannedLoginUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.opensymphony.xwork2.ActionSupport;

public class SalesforceLoginAction extends AbstractSalesforceFrontAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceLoginAction.class);
	
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	
	/**
	 * Generate the callback URL needed by OAuth2
	 * @return the callbackURL to the current action
	 * @throws Throwable
	 */
	private final String getCallbackUrl() throws Throwable {
		StringBuilder redirUrl = new StringBuilder();
		String baseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		
		if (null != baseUrl) {
			String namespace = ServletActionContext.getActionMapping().getNamespace();
			redirUrl.append(baseUrl);
			redirUrl.append(namespace);
			redirUrl.append("/");
			redirUrl.append(LOGIN_ACTION_NAME);
		} else {
			_logger.error("Cannot generate the redirection URL");
		}
		String encUrl = SalesforceMannedLoginUtils.finalizeUrl(redirUrl);
		return encUrl;
	}
	
	
	/**
	 * Get the login URL for salesforce users
	 * @return
	 * @throws Throwable
	 */
	private String getLoginUrl(String state) throws Throwable {
		SalesforceConfig config = this.getSalesforceManager().getConfig();
		String callbackUrl = this.getCallbackUrl();
		return SalesforceMannedLoginUtils.getLoginUrl(config, callbackUrl, state);
	}
	
	/**
	 * Initiate the login process with salesforce, must be invoked without 
	 * parameters! This gets also invoked when returning from the login page
	 * with all the arguments needed to complete the authentication process
	 * @return
	 */
	public String access() {
		UserDetails user = getCurrentUser();
		SalesforceConfig config = this.getSalesforceManager().getConfig();
		
		try {
			Map pmap = this.getRequest().getParameterMap();

			if (null == pmap 
					|| pmap.isEmpty() 
					|| !pmap.containsKey(REQUEST_PARAM_CODE)) {
				
				String redirUrl = getLoginUrl(this.getRedirectPage());

				_logger.debug("Sending user to authentication page '{}'", redirUrl);
				ServletActionContext.getResponse().sendRedirect(redirUrl);
				return ActionSupport.NONE;
			} else {
				
				_logger.debug("Retrieving login authentication '{}'", "tokenUrl");
				String code = this.getRequest().getParameter(REQUEST_PARAM_CODE);
				String state = this.getRequest().getParameter(REQUEST_PARAM_STATE);
				String portalRedirectUrl = getPageUrl(state);
				_logger.debug("Will redirect to '{}' after login", portalRedirectUrl);
				
				setRedirectPage(portalRedirectUrl);

				if (StringUtils.isNotBlank(code)) {
					DefaultHttpClient client = new DefaultHttpClient();
					String redirUrl = this.getCallbackUrl();
					String tokenUrl = SalesforceMannedLoginUtils.getTokenUrl(config);

					HttpPost post = new HttpPost(tokenUrl);
					List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
					urlParameters.add(new BasicNameValuePair("code", code));
					urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
					urlParameters.add(new BasicNameValuePair("client_id", config.getAppId()));
					urlParameters.add(new BasicNameValuePair("client_secret", config.getAppSecret()));
					urlParameters.add(new BasicNameValuePair("redirect_uri", redirUrl));
					post.setEntity(new UrlEncodedFormEntity(urlParameters));

					try {
						HttpResponse response = client.execute(post);
						int status = response.getStatusLine().getStatusCode();

						if ( status == HttpStatus.SC_OK) {
							_logger.debug("ok, parsing response to get access token");
							BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							JSONTokener authresponse = new JSONTokener(rd);

							try {
								JSONObject jauth = new JSONObject(authresponse);
								SalesforceAccessDescriptor access = new SalesforceAccessDescriptor(jauth);
								
								this.getRequest().getSession().removeAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD);
								this.getRequest().getSession().setAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD, access);
							} catch (Throwable t) {
								_logger.debug("Error detected while negotiating access token", t);
							}
						} else {
							_logger.debug("Invalid status on response '{}' ", status);
							return "error";
						}
					} finally {
						post.releaseConnection();
					}

				} else {
					_logger.error("'{}' parameter not found in the request, cannot proceed", REQUEST_PARAM_CODE);
					return "error";
				}
			}
		} catch (Throwable t) {
			_logger.error("login to salesforce failed!", t);
			return "error";
		}
		return "redirect";
	}

	public String getRedirectPage() {
		return _redirectPage;
	}

	public void setRedirectPage(String redirectPage) {
		this._redirectPage = redirectPage;
	}
	
	public String _redirectPage;
	
	private final static String LOGIN_ACTION_NAME = "access";
	
	public static final String REQUEST_PARAM_CODE = "code";
	public static final String REQUEST_PARAM_STATE = "state";
	public static final String REQUEST_PARAM_REDIRECT_PAGE = "redirectPage";
}
