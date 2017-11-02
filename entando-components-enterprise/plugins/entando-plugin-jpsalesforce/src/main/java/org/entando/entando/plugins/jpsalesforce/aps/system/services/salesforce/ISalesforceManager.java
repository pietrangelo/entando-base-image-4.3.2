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

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.json.JSONObject;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISalesforceManager {
	
	/**
	 * Log the user in salesforce using configuration variables
	 * @return the session ID, null otherwise 
	 * @throws Throwable
	 */
	public SalesforceAccessDescriptor login() throws Throwable;
	
	/**
	 * Load the user in salesforce with the given credentials
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	public SalesforceAccessDescriptor login(SalesforceConfig config) throws Throwable;
	
	/**
	 * Return a copy of the configuration parameters defined in the system
	 */
	public SalesforceConfig getConfig();
	
	/**
	 * Get the list of leads. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<Lead> getLeads(SalesforceAccessDescriptor sad) throws Throwable ;

	/**
	 * Add a new lead given its JSON representation
	 * @param obj representation of the user
	 * @param sad access descriptor s returned by the login process
	 * @throws Throwable
	 */
	public String addLead(JSONObject obj, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Add a new lead
	 * @param lead
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public String addLead(Lead lead, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Delete a lead. WARNING! An exception will be thrown if the lead to delete
	 * does not exist
	 * @param id
	 * @param sad
	 * @throws Throwable in case of error or of the lead does not exist
	 */
	public void deleteLead(String id, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Lead the given lead
	 * @param id
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public Lead getLead(String id, SalesforceAccessDescriptor sad) throws Throwable;
	
	
	/**
	 * Update existing user
	 * @param user
	 * @param sad
	 * @throws Throwable
	 */
	public void updateLead(Lead lead, SalesforceAccessDescriptor sad) throws Throwable;
	
	/**
	 * Search for a text in all the fields of leads
	 * @param text
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<String> searchLeadAllFields(String text, SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Get the list of Campaigns. The list returned is empty if no campaigns are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public List<Campaign> getCampaigns(SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Get the prototype of a lead with all the empty fields
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public Lead getLeadPrototype(SalesforceAccessDescriptor sad) throws Throwable;

	/**
	 * Submit a Web to Lead form to Salesforce with the given parameters map
	 * @param campaignId
	 * @param map
	 * @param debug
	 * @return
	 * @throws Throwable
	 */
	public boolean submitWeb2LeadForm(String campaignId, Map<String, String> map) throws Throwable;

	/**
	 * Reset endpoints configuration 
	 * @param config
	 * @return
	 * @throws ApsSystemException
	 */
	public SalesforceConfig resetEndpoints(SalesforceConfig config) throws ApsSystemException;

	/**
	 * Update plugin configuration
	 * @param config
	 * @throws ApsSystemException
	 */
	public void updateConfiguration(SalesforceConfig config) throws ApsSystemException;
	
}
