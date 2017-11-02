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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginDAO implements ILoginDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(LoginDAO.class);
	
	/**
	 * Login to salesforce using the configuration provided
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	@Override
	public SalesforceAccessDescriptor login(SalesforceConfig config) throws Throwable {
		SalesforceAccessDescriptor accessDescriptor = null;
		HttpResponse response = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = null;

		try {
			String loginURL = getLoginUrl(config);
			post = new HttpPost(loginURL);
			
			try {
				response = httpclient.execute(post);
			} catch (ClientProtocolException cpException) {
				_logger.error("could not log in salesforce", cpException);
			} catch (IOException ioException) {
				_logger.error("could not log in salesforce", ioException);
			}
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				accessDescriptor = parseLoginResponse(response);
			} else {
				_logger.error("Could not log in salesforce, status '{}' received", statusCode);
				throw new RuntimeException("HTTP error detected while logging in");
			}
		} catch (Throwable t) {
			_logger.error("Login error", t);
			throw new RuntimeException("Login error", t);
		} finally {
			if (null != post) {
				post.releaseConnection();
			}
		}
		return accessDescriptor;
	}

	/**
	 * Parse the HTTP response in order to get the access token and the URL instance
	 * @param response
	 * @return
	 * @throws Throwable
	 */
	private SalesforceAccessDescriptor parseLoginResponse(HttpResponse response) throws Throwable {
		SalesforceAccessDescriptor desc = null;
		JSONObject jobj = null;

		if (null != response) {
			String result =  EntityUtils.toString(response.getEntity());
			jobj = (JSONObject) new JSONTokener(result).nextValue();
			desc = new SalesforceAccessDescriptor(jobj);
		}
		return desc;
	}
	
	/**
	 * Return the URL needed in order to login a user with the given configuration
	 * @param config
	 * @return null if the configuration is insufficient, a url otherwise
	 * @throws Throwable
	 */
	private final String getLoginUrl(SalesforceConfig config) throws Throwable {
		String url = null;
		StringBuilder logUrl = new StringBuilder();

		if (null != config
				&& config.isValidForUnmannedLogin()) {
			logUrl.append(config.getTokenEndpoint());
			logUrl.append("?grant_type=password");
			logUrl.append("&client_id=");
			logUrl.append(config.getAppId());
			logUrl.append("&client_secret=");
			logUrl.append(config.getAppSecret());
			logUrl.append("&username=");
			logUrl.append(config.getUserId());
			logUrl.append("&password=");
			logUrl.append(config.getUserPwd());
			logUrl.append(config.getSecurityToken());
			url = logUrl.toString();
		} else {
			_logger.debug("warning: could not generate the login url for unmanned operation");
		}
		return url;
	}
	
	/**
	 * Get the rest resources available for the given API version 
	 * @param avd
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	@Override
	public SalesforceRestResources getRestResource(ApiVersionDescriptor avd, SalesforceAccessDescriptor sad) throws Throwable {
		SalesforceRestResources ars = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(avd.getUrl());
		String queryUrl = sad.getInstaceUrl().concat(avd.getUrl());
		URIBuilder builder = new URIBuilder(queryUrl);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		
		try {
			_logger.debug("Qurying for the available REST resource '{}'", queryUrl);
			HttpResponse response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				_logger.debug("HTTP response ok, parsing results");
				InputStream ris = response.getEntity().getContent();
				JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
				JSONObject jobj = new JSONObject(jtok);
//        System.out.println("RESPONSE:\n" + jobj.toString());
				ars = new SalesforceRestResources(jobj);
			} else {
				_logger.debug("HTTP status received: {}", response.getStatusLine().getStatusCode());
				throw new RuntimeException("HTTP error getting the list of the available REST resources");
			}
		} catch (Throwable t) {
			_logger.error("Error detected getting REST resource", t);
			throw new RuntimeException("Error getting the list of the available REST resources");
		} finally {
			httpGet.releaseConnection();
		}
		return ars;
	}
	
	/**
	 * Get the list of available API - no authentication needed
	 * @return
	 * @throws Throwable
	 */
	public List<ApiVersionDescriptor> listAvailableApiVersion() throws Throwable {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder(ENDPOINT_VERSION_URL);
		HttpGet httpGet = new HttpGet(builder.build());
		List <ApiVersionDescriptor> list = new ArrayList<ApiVersionDescriptor>();

		try {
			_logger.debug("Getting api versions '{}'", httpGet.getURI().toString());
			HttpResponse response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				_logger.debug("HTTP response ok, parsing results");

				InputStream ris = response.getEntity().getContent();
				JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
				JSONArray jver = new JSONArray(jtok);
				for (int i = 0; i < jver.length(); i++) {
					JSONObject jcur = jver.getJSONObject(i);
					ApiVersionDescriptor avd = new ApiVersionDescriptor(jcur);
					_logger.info("Added version {}:'{}' to the dictionary", avd.getVersion(), avd.getLabel());
					list.add(avd);
				}
//				_logger.debug("response received '{}'", jver.toString());
			} else {
				_logger.debug("HTTP status received {}", response.getStatusLine().getStatusCode());
				throw new RuntimeException("HTTP error getting the list of available API");
			}
		} catch (Throwable t) {
			_logger.error("Error detecting the list of API", t);
			throw new RuntimeException("Error detected getting the list of available API");
		} finally {
			httpGet.releaseConnection();
		}
		return list;
	}
	
	private static final String ENDPOINT_VERSION_URL = "http://na1.salesforce.com/services/data/";
}
