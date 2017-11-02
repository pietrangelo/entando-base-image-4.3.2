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
*/package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISalesforceFormManager {

	/**
	 * Load a the binfing of the given Message item
	 * @param id
	 * @return
	 * @throws ApsSystemException
	 */
	public SalesforceForm getSalesforceForm(int id) throws ApsSystemException;

	/**
	 * Return the list of the Salesform binding configuration
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getSalesforceForms() throws ApsSystemException;

	/**
	 * Search among the Salesforce configuration using the given criteria
	 * @param filters
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> searchSalesforceForms(FieldSearchFilter filters[]) throws ApsSystemException;

	/**
	 * Add a Salesforce binding item
	 * @param salesforceForm
	 * @throws ApsSystemException
	 */
	public void addSalesforceForm(SalesforceForm salesforceForm) throws ApsSystemException;

	/**
	 * Update a Salesforce binding item
	 * @param salesforceForm
	 * @throws ApsSystemException
	 */
	public void updateSalesforceForm(SalesforceForm salesforceForm) throws ApsSystemException;

	/**
	 * Delete a Salesforce binding item
	 * @param id
	 * @throws ApsSystemException
	 */
	public void deleteSalesforceForm(int id) throws ApsSystemException;

	/**
	 * Add or update a form described by the configuration in the given position.
	 * If a form already exists in that position then it's updated
	 * @param config
	 * @return the newly generated entity code
	 * @throws Throwable
	 */
	public String addFrameForm(SalesforceForm config) throws Throwable;

	/**
	 * Add (or update if exists) the given form configuration element
	 * @param form
	 * @return true if the record was added
	 * @throws Throwable
	 */
	public boolean addUpdateSalesforceForm(SalesforceForm form) throws Throwable;

	/**
	 * Replace the the form in the given page and frame with a new entity
	 * described in the configuration. The existing form, if any, is NOT deleted but
	 * its frame position will be set to -1
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	public String replaceFrameForm(SalesforceForm config) throws Throwable;

}