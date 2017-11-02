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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of the couple access token and server instance to use for
 * every request to salesforce
 * @author entando
 */
public class SalesforceAccessDescriptor {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceAccessDescriptor.class);
	
	/**
	 * Parse JSON response in order to get connection data
	 * @param jaccessResponse
	 */
	public SalesforceAccessDescriptor(JSONObject jaccessResponse) {
		_accessToken = jaccessResponse.getString(ACCESS_TOKEN);
		_instanceUrl = jaccessResponse.getString(INSTANCE_URL);
		try {
			_id = jaccessResponse.getString(ID);
		} catch (Throwable t) {
			_logger.debug("salesforce access descriptor lacks of ID attribute");
		}
		try {
			_signature = jaccessResponse.getString(SIGNATURE);
		} catch (Throwable t) {
			_logger.debug("salesforce access descriptor lacks of SIGNATURE attribute");
		}
		try {
			_scope = jaccessResponse.getString(SCOPE);
		} catch (Throwable t) {
			_logger.debug("salesforce access descriptor lacks of SCOPE attribute");
		}
		try {
			_issued = jaccessResponse.getString(ISSUED_AT);
		} catch (Throwable t) {
			_logger.debug("salesforce access descriptor lacks of ISSUED AT attribute");
		}
	}
	
	@Deprecated
	public SalesforceAccessDescriptor() { }
	
	@Deprecated
	public SalesforceAccessDescriptor(String instanceUrl, String accessToken) {
		if (StringUtils.isNotBlank(accessToken)
				&& StringUtils.isNotBlank(instanceUrl)) {
			setAccessToken(accessToken);
			setInstaceUrl(instanceUrl);
		} else {
			_logger.debug("creating invalid salesforce login descriptor!");
		}
	}
	
	public boolean isValid() {
		return (StringUtils.isNotBlank(_accessToken)
				&& StringUtils.isNotBlank(_instanceUrl));
	}

	public String getInstaceUrl() {
		return _instanceUrl;
	}
	
	public void setInstaceUrl(String instaceUrl) {
		this._instanceUrl = instaceUrl;
	}
	
	public String getAccessToken() {
		return _accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this._accessToken = accessToken;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getIssued() {
		return _issued;
	}

	public void setIssued(String issued) {
		this._issued = issued;
	}

	public String getSignature() {
		return _signature;
	}

	public void setSignature(String signature) {
		this._signature = signature;
	}

	public String getScope() {
		return _scope;
	}

	public void setScope(String scope) {
		this._scope = scope;
	}

	private String _instanceUrl;
	private String _accessToken;
	private String _id;
	private String _issued;
	private String _signature;
	private String _scope;
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String INSTANCE_URL = "instance_url";
	public static final String ID = "id";
	public static final String ISSUED_AT = "issued_at";
	public static final String SCOPE = "scope";
	public static final String SIGNATURE = "signature";
}
