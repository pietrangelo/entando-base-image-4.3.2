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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model;

import org.entando.entando.plugins.jpsalesforce.aps.system.utils.SalesforceFieldUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lead extends AbstractSalesforceItem {

	private static final Logger _logger =  LoggerFactory.getLogger(Lead.class);
	
	public Lead() throws Throwable {
		super();
	}
	
	/**
	 * Create a object given its JSON representation
	 * @param json the json
	 */
	public Lead(JSONObject json) throws Throwable {
		super(json);
		if (!getType().equals(LEAD)) {
			_logger.debug("Could not map '{}' to a Lead", getType());
			throw new RuntimeException("Could not initialize the Lead with the JSON provided");
		}
	}
	
	/**
	 * This method checks if the lead contains the MINIMUM fields so that
	 * it can be uploaded or create into Salesforce.
	 * NOTE: we check the JSON and not the list of attributes
	 * @return
	 */
	public boolean isUploadable() {
		boolean res = true;
		if (null != getJSON() 
				&& getJSON().length() > 0) {
			for (String cur: SalesforceFieldUtils.minimumLeadFields) {
				if (!getJSON().has(cur)
						|| getJSON().isNull(cur)) {
					res = false;
					break;
				}
			}
		} else {
			res = false;
		}
		return res;
	}
	
	// Type reported 
	public final static String LEAD = "Lead";
	
	public final static String FIELD_LEAD_FIRSTNAME = "FirstName";
	public final static String FIELD_LEAD_COMPANY = "Company";
	public final static String FIELD_LEAD_LASTNAME = "LastName";
	public final static String FIELD_LEAD_EMAIL = "Email";
	
}