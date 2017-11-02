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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;


/**
 * This utility class is needed to create a list of the fields that can be
 * used when creating a new lead from the user interfaces.
 * The problem is that the description of a lead as returned by Salesforce
 * contains a great number of fields, and some of them cannot be valued or
 * updated: with the method above we check whether the given field can be
 * used for standard CRUD operations.
 * @author entando
 * TODO check whether lead fields are subjected to change across versions
 */
public class SalesforceFieldUtils {
	
	/**
	 * Return true if the passed field is among those that can be freely edited
	 * @param field
	 * @return
	 */
	public static boolean isLeadFieldCustomizable(String field) {
		return StringUtils.isNotBlank(field) ? allowedLeadFields.contains(field) : false;
	}
	
	/**
	 * Those are the fields name as returned by Salesforce
	 */
	public static final List<String> allowedLeadFields = Arrays.asList(
			Lead.FIELD_LEAD_LASTNAME
		,"FirstName"
		,"Salutation"
		,"Title"
		,Lead.FIELD_LEAD_COMPANY
		,"Street"
		,"City"
		,"State"
		,"PostalCode"
		,"Country"
		,"Phone"
		,"MobilePhone"
		,"Fax"
		,Lead.FIELD_LEAD_EMAIL
		,"Website"
		,"Description"
		,"Industry"
		,"AnnualRevenue"
		,"NumberOfEmployees"
		);
	
	/**
	 * Those are the minimum fields needed to create a Lead in the lead
	 * management widget
	 */
	public static final List<String> minimumLeadFields = Arrays.asList(
			Lead.FIELD_LEAD_LASTNAME,
			Lead.FIELD_LEAD_COMPANY);
	
}
