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

import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.json.JSONObject;

public interface ILeadDAO {
	
	/**
	 * Get the list of the leads. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<Lead> getLeads(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Get the list of the Accounts. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<Lead> getAccounts(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Create a new lead given its JSON representation
	 * @param obj
	 * @param sarr
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public String addLead(JSONObject obj, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Delete the desired lead
	 * @param id
	 * @param sarr
	 * @param sad
	 * @throws Throwable
	 */
	public void deleteLead(String id, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Lead the desired lead. Return null if nothing is found
	 * @param id
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public JSONObject getLead(String id, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Update existing Lead
	 * @param jobj
	 * @param sarr
	 * @param sad
	 * @throws Throwable
	 */
	public void updateLead(JSONObject jobj, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Search the given text in all the fields of the lead
	 * @param text
	 * @param sarr
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<String> searchAllFields(String text, SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Get the description of the Lead
	 * @param sarr
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public JSONObject describe(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;
	/**
	 * Submit a web2lead form with the given parameters
	 * @param oid
	 * @param campaignId
	 * @param map
	 * @param email notification address
	 * @return
	 */
	public boolean submitWeb2LeadForm(String instance, String oid, String campaignId, Map<String, String> map, String email);

}
