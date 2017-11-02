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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Campaign extends AbstractSalesforceItem {

	private static final Logger _logger =  LoggerFactory.getLogger(Campaign.class);
	
	public Campaign(JSONObject json) throws Throwable {
		super(json);
		if (!getType().equals(CAMPAIGN)) {
			_logger.debug("Could not map '{}' to a Campaign", getType());
			throw new RuntimeException("Error creating new lead ");
		}
	}

	public final static String CAMPAIGN = "Campaign";
	
}
