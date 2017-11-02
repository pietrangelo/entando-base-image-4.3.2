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
package org.entando.entando.plugins.jpsalesforce.aps.internalservlet.leads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.internalservlet.AbstractSalesforceFrontAction;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.entando.entando.plugins.jpsalesforce.aps.system.utils.SalesforceFieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

public class LeadAction extends AbstractSalesforceFrontAction implements ILeadAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(LeadAction.class);
	
	
	/**
	 * Return the map with the fields of the form, indexed by key and value 
	 * @return
	 */
	private void fetchFields() {
		if (null == _fieldMap) {
			_fieldMap = new HashMap<String, String>();
		} else {
			_fieldMap.clear();
		}
		for (String field: SalesforceFieldUtils.allowedLeadFields) {
			String value = (String) this.getRequest().getParameter(field);
				_fieldMap.put(field, value);
		}
	}
	
	@Override
	public String newLead() {
		SalesforceAccessDescriptor sad = getSessionAccessor();
		try {
			if (null != sad) {
				_logger.debug("Access descriptor found");
				setStrutsAction(ApsAdminSystemConstants.ADD);
				getSalesforceManager().getLeadPrototype(sad);
			} else {
				_logger.debug("Access descriptor not found, not logged?");
				return "signIn";
			}
		} catch (Throwable t) {
			_logger.error("Error executing newLead", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String editLead() {
		setStrutsAction(ApsAdminSystemConstants.EDIT);
		return SUCCESS;
	}
	
	@Override
	public String viewLead() {
		setStrutsAction(ApsAdminSystemConstants.PASTE);
		return SUCCESS;
	}

	@Override
	public String trashLead() {
		return SUCCESS;
	}

	@Override
	public String deleteLead() {
		SalesforceAccessDescriptor sad = getSessionAccessor();
		try {
			getSalesforceManager().deleteLead(getSobj(), sad);
			_logger.debug("Deleted lead with id '{}'", getSobj());
		} catch (Throwable t) {
			_logger.error("Error while deleting the lead", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveLead() {
		Lead lead = null;
		SalesforceAccessDescriptor sad = getSessionAccessor();
		
		try {
			fetchFields();
			if (getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				if (StringUtils.isNotBlank(getSobj())) {
				_logger.debug("updating lead '{}'", getSobj());
					lead = getSalesforceManager().getLead(getSobj(), sad);
					syncFieldsToLead(lead, getFieldMap());
					// check if the lead has enough fields valued
					if (!checkMinimumFields(lead)) {
						_logger.error("not enough fields where submitted");
						return INPUT;
					}
					getSalesforceManager().updateLead(lead, sad);
				} else {
					addActionError(getText("jpsalesforce.lead.missing.sobj"));
					_logger.error("Missing sobject id");
					return INPUT;
				}
			} else {
				lead = new Lead();
				syncFieldsToLead(lead, getFieldMap());
				// check if the lead has enough fields valued
				if (!checkMinimumFields(lead)) {
					_logger.error("not enough fields where submitted");
					return INPUT;
				} else {
					// create the lead
					String id = this.getSalesforceManager().addLead(lead, sad);
					_logger.info("New lead with id '{}' created", id);
				}
				// Handle redirection; check for the target page code
				if (StringUtils.isNotBlank(getPageCode())) {
					// evaluate the return page
					String url = getPageUrl(getPageCode());
					setRedirectionUrl(url);
					_logger.info("redirecting user to page '{}'", url);
				} else {
					// get the default return page
					String url = getSearchLeadWidgetPageUrl();
					setRedirectionUrl(url);
					_logger.info("redirecting user to default page '{}'", url);
				}
				return "redirect";
			}
		} catch (Throwable t) {
			_logger.error("Error saving or updating a new lead", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Invoked to create a new Lead. NOTE: we remove unneeded fields from the
	 * prototype
	 * @return
	 */
	public List<String> getLeadFields() {
		SalesforceAccessDescriptor sad = getSessionAccessor();
		List<String> allowable = new ArrayList<String>();
		
		try {
		Lead proto = getSalesforceManager().getLeadPrototype(sad);
		for (String field: SalesforceFieldUtils.allowedLeadFields) {
			if (proto.getAttributes().contains(field)) {
				allowable.add(field);
			} else {
				_logger.debug("Removing '{}' not present in the current API from the updateable fields", field);
			}
		}
		} catch (Throwable t) {
			_logger.debug("Error generating the list of allowable fields",t);
		}
		return allowable;
	}
	
	/**
	 * Sync the parameters in the map with the given Lead
	 * TODO move in the Lead object?
	 * @return
	 * @throws Throwable
	 */
	private void syncFieldsToLead(Lead lead, Map<String, String> params) throws Throwable {
		Iterator<String> itr = params.keySet().iterator();
		
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String value = params.get(key);
			
			if (StringUtils.isNotBlank(value)) {
				lead.addAttribute(key, value);
			} else {
				lead.removeAttribute(key);
			}
		}
	}
	
	/**
	 * Check whether the lead has the minimum number of fields to be saved.
	 * If the case, we create the error message
	 * @param lead
	 */
	private boolean checkMinimumFields(Lead lead) {
		boolean res = lead.isUploadable();
		
		if (!res) {
			for(String field: SalesforceFieldUtils.minimumLeadFields) {
				
				if (null == lead.getJSON() 
						|| !lead.getJSON().has(field)
						|| lead.getJSON().isNull(field)
						|| !StringUtils.isNotBlank(lead.getJSON().getString(field))) {
					addActionError(getText("salesforce.label.field.required", new String[] {field}));
				}
			}
		}
		return res;
	}
	
	public String getRedirectionUrl() {
		return _redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this._redirectionUrl = redirectionUrl;
	}
	
	public Map<String, String> getFieldMap() {
		return _fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this._fieldMap = fieldMap;
	}

	private String _redirectionUrl;
	// used to populate the New lead form when returning from an error
	private Map<String, String> _fieldMap = new HashMap<String, String>();
}
