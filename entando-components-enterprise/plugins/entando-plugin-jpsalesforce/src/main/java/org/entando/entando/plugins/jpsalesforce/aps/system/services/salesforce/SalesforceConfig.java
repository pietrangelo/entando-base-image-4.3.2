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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesforceConfig {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceConfig.class);
	
	public SalesforceConfig() {
		_preferredVersion = JpSalesforceConstants.CONFIG_DEFAULT_API_VERSION;
		_loginEndpoint = JpSalesforceConstants.CONFIG_DEFAULT_AUTHORIZATION_NEW;
		_tokenEndpoint = JpSalesforceConstants.CONFIG_DEFAULT_AUTHORIZATION_TOKEN;
	}
	
	public SalesforceConfig(String xml) {
		if (StringUtils.isNotBlank(xml)) {
			JSONObject json = XML.toJSONObject(xml);
			json = json.getJSONObject(CONFIG_ROOT); 
			parseJson(json);
		}
	}
	
	private void parseJson(JSONObject json) {
		String userId = null;
		String userPwd = null;
		String securityToken = null;
		String appId = null;
		String appSecret = null;
		String loginEndpoint = null;
		String tokenEndpoint = null;
		String preferredVersion = null;
		String oid = null;
		
		try {
			appId = json.getString(CONFIG_APP_ID);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			Object obj = json.get(CONFIG_APP_SECRET);
			appSecret = String.valueOf(obj);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			loginEndpoint = json.getString(CONFIG_URL_AUTHORIZATION_NEW);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			tokenEndpoint = json.getString(CONFIG_URL_AUTHORIZATION_TOKEN);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			userId = json.getString(CONFIG_ACCOUNT_USERNAME);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			userPwd = json.getString(CONFIG_ACCOUNT_PASSWORD);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			Object obj = json.get(CONFIG_OID);
			oid = String.valueOf(obj);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			securityToken = json.getString(CONFIG_SECURITY_TOKEN);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		try {
			Object obj = json.get(CONFIG_API_VERSION);
			preferredVersion = String.valueOf(obj);
		} catch (Throwable t) {
			_logger.error("Error loading configuration property: '{}' ", t.getLocalizedMessage());
		}
		setOid(oid);
		setLoginEndpoint(loginEndpoint);
		setTokenEndpoint(tokenEndpoint);
		setUserId(userId);
		setUserPwd(userPwd);
		setSecurityToken(securityToken);
		setPreferredVersion(preferredVersion);
		setAppId(appId);
		setAppSecret(appSecret);
	}
	
	public String toXML() {
		JSONObject json = new JSONObject();
		
		if (null != getAppId()) {
			json.put(CONFIG_APP_ID, getAppId());
		} else {
			json.put(CONFIG_APP_ID, JSONObject.NULL);
		}
		if (null != getAppSecret()) {
			json.put(CONFIG_APP_SECRET, getAppSecret());
		} else {
			json.put(CONFIG_APP_SECRET, JSONObject.NULL);
		}
		if (null != getLoginEndpoint()) {
			json.put(CONFIG_URL_AUTHORIZATION_NEW, getLoginEndpoint());
		} else {
			json.put(CONFIG_URL_AUTHORIZATION_NEW, JSONObject.NULL);
		}
		if (null != getTokenEndpoint()) {
			json.put(CONFIG_URL_AUTHORIZATION_TOKEN, getTokenEndpoint());
		} else {
			json.put(CONFIG_URL_AUTHORIZATION_TOKEN, JSONObject.NULL);
		}
		if (null != getUserId()) {
			json.put(CONFIG_ACCOUNT_USERNAME, getUserId());
		} else {
			json.put(CONFIG_ACCOUNT_USERNAME, JSONObject.NULL);
		}
		if (null != getUserPwd()) {
			json.put(CONFIG_ACCOUNT_PASSWORD, getUserPwd());
		} else {
			json.put(CONFIG_ACCOUNT_PASSWORD, JSONObject.NULL);
		}
		if (null != getOid()) {
			json.put(CONFIG_OID, getOid());
		} else {
			json.put(CONFIG_OID, JSONObject.NULL);
		}
		if (null != getSecurityToken()) {
			json.put(CONFIG_SECURITY_TOKEN, getSecurityToken());
		} else {
			json.put(CONFIG_SECURITY_TOKEN, JSONObject.NULL);
		}
		if (null != getPreferredVersion()) {
			json.put(CONFIG_API_VERSION, getPreferredVersion());
		} else {
			json.put(CONFIG_API_VERSION, JSONObject.NULL);
		}
		return XML.toString(json, CONFIG_ROOT);
	}
	
	/**
	 * Checks whether all parameters needed for the unmanned login are
	 * in place.
	 * @return
	 */
	public boolean isValidForUnmannedLogin() {
		return (isValidForBaseSettings()
				&& StringUtils.isNotBlank(_userId)
				&& StringUtils.isNotBlank(_userPwd)
				&& StringUtils.isNotBlank(_securityToken)
				&& StringUtils.isNotBlank(_tokenEndpoint));
	}
	
	/**
	 * Check whether all the parameters needed for the manned connections are
	 * in place
	 * @return
	 */
	public boolean isValidForMannedLogin() {
		return (isValidForBaseSettings()
				&& StringUtils.isNotBlank(_tokenEndpoint)
				&& StringUtils.isNotBlank(_loginEndpoint)
//				&& StringUtils.isNotBlank(_redirection)
				);
	}
	
	private boolean isValidForBaseSettings() {
		return (StringUtils.isNotBlank(_appId)
				&& StringUtils.isNotBlank(_appSecret));
	}
	
	public String getUserId() {
		return _userId;
	}
	public void setUserId(String userId) {
		this._userId = userId;
	}
	public String getUserPwd() {
		return _userPwd;
	}
	public void setUserPwd(String userPwd) {
		this._userPwd = userPwd;
	}
	public String getAppId() {
		return _appId;
	}
	public void setAppId(String appId) {
		this._appId = appId;
	}
	public String getAppSecret() {
		return _appSecret;
	}
	public void setAppSecret(String appSecret) {
		this._appSecret = appSecret;
	}
	public String getLoginEndpoint() {
		return _loginEndpoint;
	}
	public void setLoginEndpoint(String loginEndpoint) {
		this._loginEndpoint = loginEndpoint;
	}
	public String getTokenEndpoint() {
		return _tokenEndpoint;
	}
	public void setTokenEndpoint(String tokenEndpoint) {
		this._tokenEndpoint = tokenEndpoint;
	}
	public String getSecurityToken() {
		return _securityToken;
	}
	public void setSecurityToken(String securityToken) {
		this._securityToken = securityToken;
	}
	public String getPreferredVersion() {
		return _preferredVersion;
	}
	public void setPreferredVersion(String preferredVersion) {
		this._preferredVersion = preferredVersion;
	}
	public String getOid() {
		return _oid;
	}
	public void setOid(String oid) {
		this._oid = oid;
	}

	private String _userId;
	private String _userPwd;
	private String _securityToken;
	private String _appId;
	private String _appSecret;
	private String _loginEndpoint;
	private String _tokenEndpoint;
	private String _preferredVersion;
	private String _oid;
	
	public final static String CONFIG_ROOT = "config";
	public final static String CONFIG_APP_ID = "app_id";
	public final static String CONFIG_APP_SECRET = "app_secret";
	public final static String CONFIG_URL_AUTHORIZATION = "auth_url";
	public final static String CONFIG_URL_AUTHORIZATION_NEW = CONFIG_URL_AUTHORIZATION.concat("_new");
	public final static String CONFIG_URL_AUTHORIZATION_TOKEN = CONFIG_URL_AUTHORIZATION.concat("_token");
	public final static String CONFIG_ACCOUNT_USERNAME = "username";
	public final static String CONFIG_ACCOUNT_PASSWORD = "password";
	public final static String CONFIG_OID = "oid";
	public final static String CONFIG_API_VERSION = "api_version";
	public final static String CONFIG_SECURITY_TOKEN = "security_token";
}
