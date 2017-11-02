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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.entando.entando.plugins.jpsalesforce.aps.system.utils.SalesforceFieldUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeadDAO implements ILeadDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(LeadDAO.class);

	/**
	 * Submit a web to lead form.
	 * @param map
	 * 
	 * FIXME / TODO the mapping must have a configuration menu indexed by API version
	 * 
	 * @return true if the form was successfully submitted (this does NOT imply the lead was created!!!)
	 */ 
	@Override
	public boolean submitWeb2LeadForm(String instance, String oid, String campaignId, Map<String, String> map, String email) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		HttpResponse response = null;
		DefaultHttpClient client = new DefaultHttpClient();
		StringBuilder url = new StringBuilder(instance);
		url.append(WEB2LEAD_URL);
		HttpPost post = new HttpPost(url.toString());
		boolean submitted = false;
		
		try {
			_logger.debug("Submitting web to lead form to '{}'", url.toString());
			Iterator<String> itr = map.keySet().iterator();
			
			while (itr.hasNext()) {
				String key = itr.next();
				String value = map.get(key);
				
				if (key.equalsIgnoreCase("FirstName")) {
					key = "first_name";
				} else if (key.equalsIgnoreCase("LastName")) {
					key = "last_name";
				} else if (key.equalsIgnoreCase("Email")) {
					key = "email";
				} else if (key.equalsIgnoreCase("Country")) {
					key = "country";
				} else if (key.equalsIgnoreCase("Company")) {
					key = "company";
				} else if (key.equalsIgnoreCase("City")) {
					key = "city";
				} else if (key.equalsIgnoreCase("State")) {
					key = "state";
				} else if (key.equalsIgnoreCase("Title")) {
					key = "title";
				} else if (key.equalsIgnoreCase("Website")) {
					key = "URL";
				} else if (key.equalsIgnoreCase("Phone")) {
					key = "phone";
				} else if (key.equalsIgnoreCase("MobilePhone")) {
					key = "mobile";
				} else if (key.equalsIgnoreCase("Fax")) {
					key = "fax";
				} else if (key.equalsIgnoreCase("Street")) {
					key = "street";
				} else if (key.equalsIgnoreCase("PostalCode")) {
					key = "zip";
				} else if (key.equalsIgnoreCase("Description")) {
					key = "description";
				} else if (key.equalsIgnoreCase("LeadSource")) {
					key = "lead_source";
				} else if (key.equalsIgnoreCase("Industry")) {
					key = "industry";
				} else if (key.equalsIgnoreCase("Rating")) {
					key = "rating";
				} else if (key.equalsIgnoreCase("AnnualRevenue")) {
					key = "revenue";
				} else if (key.equalsIgnoreCase("NumberOfEmployees")) {
					key = "employees";
				} else {
					_logger.info("Unknwon mapping for lead attribute '{}'", key);
				}
				_logger.debug("Adding the following parameter to the web to lead form: '{}':'{}'", key, value);
				urlParameters.add(new BasicNameValuePair(key, value));
			}
			urlParameters.add(new BasicNameValuePair(JpSalesforceConstants.FIELD_ENCODING, "UTF-8"));
			// Campaign_ID
			urlParameters.add(new BasicNameValuePair(JpSalesforceConstants.FIELD_CAMPAIGN_ID, campaignId));
			// OID
			urlParameters.add(new BasicNameValuePair(JpSalesforceConstants.FIELD_OID, oid));
			if (StringUtils.isNotBlank(email)) {
				_logger.info("Web2Lead debug enbled");
				urlParameters.add(new BasicNameValuePair(JpSalesforceConstants.FIELD_DEBUG, "1"));
				urlParameters.add(new BasicNameValuePair(JpSalesforceConstants.FIELD_DEBUG_EMAIL, email));
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			
			try {
				response = client.execute(post);
				int status = response.getStatusLine().getStatusCode();
				
				if ( status == HttpStatus.SC_OK) {
					_logger.debug("Status OK");
					submitted = true;
				} else {
					_logger.debug("Invalid status on response '{}' ", status);
				}
				
			} catch (Throwable t) {
				_logger.error("Error submitting web2lead form", t);
			}
		} catch (Throwable t) {
			
		}
		return submitted;
	}
	
	/**
	 * Get the list of the leads. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	@Override
	public List<Lead> getLeads(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		List<Lead> list = new ArrayList<Lead>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String queryUrl = sad.getInstaceUrl().concat(sarr.getQuery());
		URIBuilder builder = new URIBuilder(queryUrl);
		builder.addParameter("q", GET_LEADS);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());

		try {
			_logger.debug("Querying '{}' for leads", httpGet.getRequestLine());
			HttpResponse response = httpclient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				_logger.debug("status OK");
				InputStream ris = response.getEntity().getContent();
				JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
				JSONObject jobj = new JSONObject(jtok);
				JSONArray jrec = jobj.getJSONArray("records");

				for (int i = 0; i < jrec.length(); i++) {
					JSONObject jcur = jrec.getJSONObject(i);
					Lead user = new Lead(jcur);
					list.add(user);
				}
			} else {
				_logger.info("server reported error status '{}'", response.getStatusLine().getStatusCode());
				throw new RuntimeException("HTTP status error while querying leads");
			}
		} catch (Throwable t) {
			_logger.error("Error querying leads", t);
			throw new RuntimeException("Error querying leads");
		} finally {
			httpGet.releaseConnection();
		}
		return list;
	}

	/**
	 * Get the list of the Accounts. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	@Override
	public List<Lead> getAccounts(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		List<Lead> list = new ArrayList<Lead>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String queryUrl = sad.getInstaceUrl().concat(sarr.getQuery());
		URIBuilder builder = new URIBuilder(queryUrl);
		builder.addParameter("q", GET_LEADS);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());

		try {
			_logger.debug("Querying '{}' for accounts", httpGet.getRequestLine());
			HttpResponse response = httpclient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				_logger.info("status OK");

				InputStream ris = response.getEntity().getContent();
				JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
				JSONObject jobj = new JSONObject(jtok);
				JSONArray jrec = jobj.getJSONArray("records");

				for (int i = 0; i < jrec.length(); i++) {
					JSONObject jcur = jrec.getJSONObject(i);
					Lead user = new Lead(jcur);
					list.add(user);
				}
			} else {
				_logger.info("server reported error status '{}'", response.getStatusLine().getStatusCode());
				throw new RuntimeException("HTTP status error while querying accounts");
			}
		} catch (Throwable t) {
			_logger.error("Error querying accounts", t);
			throw new RuntimeException("Error querying accounts");
		} finally {
			httpGet.releaseConnection();
		}
		return list;
	}
	
	/**
	 * Create a new lead given its JSON representation
	 * @param jobj
	 * @param sarr
	 * @param sad
	 * @throws Throwable
	 */
	@Override
	public String addLead(JSONObject jobj, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(sarr.getSobjects());
		qUrl.append("/Lead");
		String id = null;
		
		URIBuilder builder = new URIBuilder(qUrl.toString());
		HttpPost httpPost = new HttpPost(builder.build());
		httpPost.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		StringEntity jsonEntity = new StringEntity(jobj.toString());
		jsonEntity.setContentType("application/json");
		httpPost.setEntity(jsonEntity);
		_logger.debug("Querying '{}' to create a new lead", httpPost.getRequestLine());

		try {
				HttpResponse response = httpclient.execute(httpPost);
				id = parseCreationResponse(response);
		} catch (Throwable t) {
			_logger.error("error creating new lead", t);
			throw new RuntimeException("Error creating new lead");
		} finally {
			httpPost.releaseConnection();
		}
		return id;
	}
	
	/**
	 * Load the given lead
	 */
	@Override
	public JSONObject getLead(String id, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		JSONObject lead = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(sarr.getSobjects());
		qUrl.append("/Lead/");
		qUrl.append(id);
		URIBuilder uri = new URIBuilder(qUrl.toString());
		HttpGet httpGet = new HttpGet(uri.build());

		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		_logger.debug("Querying '{}' to load a lead", httpGet.getRequestLine());
		try {
			HttpResponse response = httpclient.execute(httpGet);
			
			lead = parseLead(response);
		} catch (Throwable t) {
			_logger.error("Error caught while loading a lead", t);
			throw new RuntimeException("Error caught while loading a lead");
		} finally {
			httpGet.releaseConnection();
		}
		return lead;
	}
	
	/**
	 * Parse the response representing the lead
	 * @param response
	 * @return
	 * @throws Throwable
	 */
	protected JSONObject parseLead(HttpResponse response) throws Throwable {
		JSONObject uObj = null;
		int httpStatus = response.getStatusLine().getStatusCode();
		
		String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		_logger.debug("Parsing (load) response: '{}'", body);
		if (httpStatus == HttpStatus.SC_OK) {
			uObj = new JSONObject(body);
		} else {
			_logger.info("Status: '{}'", response.getStatusLine().getStatusCode());
//			throw new RuntimeException("Unexpected HTTP status parsing a lead");
		}
		
		return uObj;
	}
	
	/**
	 * Get the complete description of the Lead. Please note that we parse only fields name 
	 */
	@Override
	public JSONObject describe(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		JSONObject jlead = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(sarr.getSobjects());
		qUrl.append("/Lead");
		qUrl.append("/describe/");
		URIBuilder uri = new URIBuilder(qUrl.toString());
		HttpGet httpGet = new HttpGet(uri.build());

		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		_logger.debug("Querying '{}' to describe a lead", httpGet.getRequestLine());
		try {
			HttpResponse response = httpclient.execute(httpGet);
			
			jlead = parseLead(response);
		} catch (Throwable t) {
			_logger.error("Error caught while getting the lead description", t);
			throw new RuntimeException("Error caught while getting the lead description");
		} finally {
			httpGet.releaseConnection();
		}
		return jlead;
	}
	
	/**
	 * Delete the desired lead
	 * @param id
	 * @param sarr
	 * @param sad
	 * @throws Throwable
	 */
	@Override
	public void deleteLead(String id, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(sarr.getSobjects());
		qUrl.append("/Lead/");
		qUrl.append(id);
		URIBuilder uri = new URIBuilder(qUrl.toString());
		HttpDelete httpDelete = new HttpDelete(uri.build());
		
		httpDelete.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		_logger.debug("Querying '{}' to delete a lead", httpDelete.getRequestLine());
		try {
			HttpResponse response = httpclient.execute(httpDelete);
			
			int httpStatus = response.getStatusLine().getStatusCode();
			
//			String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//			_logger.debug(body);
			if (httpStatus == HttpStatus.SC_NO_CONTENT) {
				// DO NOTHING
//				_logger.debug("Parsing deletion response: '{}'", body);
			} else {
				_logger.error("Status: '{}'", response.getStatusLine().getStatusCode());
				throw new RuntimeException("Unexpected HTTP status after lead deletion");
			}			
		} catch (Throwable t) {
			_logger.error("Error caught while deleting a lead", t);
			throw new RuntimeException("Error caught while deleting a lead");
		} finally {
			httpDelete.releaseConnection();
		}
	}
	
	/**
	 * Parse the response after the creation of an element and get the ID
	 * @param response
	 * @return
	 * @throws Throwable
	 */
	protected String parseCreationResponse(HttpResponse response) throws Throwable {
		int httpStatus = response.getStatusLine().getStatusCode();
		String id = null;
		
		String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		_logger.debug("Parsing (New) response: '{}'", body);
		if (httpStatus == HttpStatus.SC_CREATED) {
//			InputStream ris = response.getEntity().getContent();
//			JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
			JSONObject jobj = new JSONObject(body);
			boolean success = jobj.getBoolean("success");
			if (success) {
				id = jobj.getString("id");
			}
		} else {
			_logger.debug("Status: '{}'", response.getStatusLine().getStatusCode());
			throw new RuntimeException("Invalid HTTP status detected parsing result");
		}
		return id;
	}
	
	/**
	 * Update existing Lead.
	 * @param jobj
	 * @param sarr
	 * @param sad
	 * @throws Throwable
	 */
	@Override
	public void updateLead(JSONObject jobj, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		JSONObject ujobj = new JSONObject(jobj.toString());
		
		// remove unneeded fields
		Iterator itr = jobj.keys();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			
			if (!SalesforceFieldUtils.isLeadFieldCustomizable(key)) {
				_logger.debug("Removing '{}' for the update", key);
				ujobj.remove(key);
			}
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer qUrl = new StringBuffer(sad.getInstaceUrl());
		qUrl.append(sarr.getSobjects());
		qUrl.append("/Lead/");
		qUrl.append(jobj.getString("Id"));
		URIBuilder uri = new URIBuilder(qUrl.toString());
		HttpPost httpPost = new HttpPost(uri.build()) {
			@Override
			public String getMethod() {
				return "PATCH";
			}
		};
		httpPost.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		StringEntity jsonEntity = new StringEntity(ujobj.toString());
		jsonEntity.setContentType("application/json");
		httpPost.setEntity(jsonEntity);
		_logger.debug("Querying '{}' to update a lead", httpPost.getRequestLine());
		
		try {
			HttpResponse response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_NO_CONTENT) {
				
				_logger.error("Status: '{}'", response.getStatusLine().getStatusCode());
				throw new RuntimeException("Unexpected HTTP status after lead update");
			}
		} catch (Throwable t) {
			_logger.error("Error caught while updating a lead", t);
			throw new RuntimeException("Error caught while updating a lead");
		} finally {
			httpPost.releaseConnection();
		}
	}

	/**
	 * Search the given text in all the fields of the lead
	 * @param text
	 * @param sarr
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	@Override
	public List<String> searchAllFields(String text, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		List<String> list = new ArrayList<String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		StringBuffer searchUrl = new StringBuffer(sad.getInstaceUrl());
		searchUrl.append(sarr.getSearch());
		URIBuilder builder = new URIBuilder(searchUrl.toString());
		String queryString = doSearchAllFieldsString(text);
		
		builder.addParameter("q", queryString);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());
		
		try {
			_logger.debug("Querying '{}' to search for leads", httpGet.getRequestLine());
			_logger.debug("Query: '{}'", queryString);
			HttpResponse response = httpclient.execute(httpGet);

			list = parseSearchResponse(response);
		} catch (Throwable t) {
			_logger.error("Error querying leads", t);
			throw new RuntimeException("Error querying leads");
		} finally {
			httpGet.releaseConnection();
		}
		return list;
	}
	
	/**
	 * Create the search string considering all the fields
	 * @param text
	 * @return
	 * @throws Throwable
	 */
	private String doSearchAllFieldsString(String text) throws Throwable {
		//FIND {Joe Smith} IN Name Fields RETURNING lead
		StringBuffer sosl = new StringBuffer("FIND {");
		sosl.append(text);
		sosl.append("*} IN ALL FIELDS RETURNING Lead");
		return sosl.toString();
	}
	
	/**
	 * Parse the result of the search
	 * @param response
	 * @return
	 * @throws Throwable
	 */
	private List<String> parseSearchResponse(HttpResponse response) throws Throwable {
		List<String> list = new ArrayList<String>();
		
		String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		_logger.debug("Parsing (Search) response: '{}'", body);
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			_logger.debug("status OK");
			JSONArray jrec = new JSONArray(body);
			for (int i = 0; i < jrec.length(); i++) {
				JSONObject jcur = jrec.getJSONObject(i);
				Lead user = new Lead(jcur);
				list.add(user.getId());
			}
		} else {
			_logger.info("server reported error status '{}'", response.getStatusLine().getStatusCode());
			throw new RuntimeException("HTTP status error while querying leads");
		}
		
		return list;
	}
	
	private static final String GET_LEADS = "SELECT Name, Id from Lead";
	private static final String WEB2LEAD_URL = "/servlet/servlet.WebToLead";
}
