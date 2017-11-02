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
package org.entando.entando.plugins.jpsalesforce.aps.system.utils;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for manned logins to salesforce
 * @author entando
 */
public class SalesforceMannedLoginUtils {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceMannedLoginUtils.class);

	/**
	 * Get rid of useless //; optionally returns a NULL if the url is an empty string
	 * @param url
	 * @return
	 */
	public static String finalizeUrl(StringBuilder url) {
		String resUrl = null;

		if (StringUtils.isNotBlank(url.toString())) {
			resUrl = url.toString().replace("//", "/");
			resUrl = resUrl.replace(":/", "://");
		}
		return resUrl;
	}
	
	/**
	 * This also encodes the URL with the desired encoding
	 * @param url
	 * @param encoding
	 * @return
	 * @throws Throwable
	 */
	public static String finalizeUrl(StringBuilder url, String encoding) throws Throwable {
		String eurl = finalizeUrl(url);
		
		return URLEncoder.encode(eurl, encoding);
	}
	
	/**
	 * Return the url for the access token negotiation
	 * @return
	 * @throws Throwable
	 */
	public static String getTokenUrl(SalesforceConfig config) throws Throwable {
//		String baseUrl = this.getConfigManager().getParam(JpSalesForceConstants.ENDPOINT_TOKEN);
		StringBuilder tokUrl = new StringBuilder();
		
		if (null != config) {
			if (config.isValidForMannedLogin()) {
				tokUrl.append(config.getTokenEndpoint());
			} else {
				_logger.debug("warning: cannot generate the token URL because of invalid configuration!");
			}
		} else {
			_logger.debug("warning: cannot generate the token URL because of invalid invocation!");
		}
		
		return SalesforceMannedLoginUtils.finalizeUrl(tokUrl);
	}
	
	/**
	 * Generate the login URL
	 * 
	 * @param config
	 * @param callbackUrl
	 * @return
	 * @throws Throwable
	 */
	public static String getLoginUrl(SalesforceConfig config, String callbackUrl) throws Throwable {
		String url = null;

		if (null != config && null != callbackUrl
				&& StringUtils.isNotBlank(callbackUrl)) {
			StringBuilder authUrl = new StringBuilder();

			if (config.isValidForMannedLogin()) {
				authUrl.append(config.getLoginEndpoint());
				authUrl.append("?response_type=code&client_id=");
				authUrl.append(config.getAppId());
				authUrl.append("&redirect_uri=");
				authUrl.append(callbackUrl);
				url = finalizeUrl(authUrl);
			} else {
				_logger.debug("warning: cannot generate the authentication URL because of invalid configuration!");
			}
		} else {
			_logger.debug("warning: cannot generate the authentication URL because of invalid invocation!");
		}
		return url;
	}

	/**
	 * Generate the login URL adding a state that will be echoed when returning
	 * in the portal 
	 * @param config
	 * @param callbackUrl
	 * @param state
	 * @return
	 * @throws Throwable
	 */
	public static String getLoginUrl(SalesforceConfig config, String callbackUrl, String state) throws Throwable {
		String url = null;

		if (null != config && null != callbackUrl
				&& StringUtils.isNotBlank(callbackUrl)) {
			StringBuilder authUrl = new StringBuilder();

			if (config.isValidForMannedLogin()) {
				authUrl.append(config.getLoginEndpoint());
				authUrl.append("?response_type=code&client_id=");	// FIXME use constant as in the login action
				authUrl.append(config.getAppId());
				authUrl.append("&redirect_uri=");									// FIXME use constant as in the login action
				authUrl.append(callbackUrl);
				authUrl.append("&state=");												// FIXME use constant as in the login action
				state = finalizeUrl(new StringBuilder(state), "UTF-8");
				authUrl.append(state);
				url = finalizeUrl(authUrl);
			} else {
				_logger.debug("warning: cannot generate the authentication URL because of invalid configuration!");
			}
		} else {
			_logger.debug("warning: cannot generate the authentication URL because of invalid invocation!");
		}
		return url;
	}

}
