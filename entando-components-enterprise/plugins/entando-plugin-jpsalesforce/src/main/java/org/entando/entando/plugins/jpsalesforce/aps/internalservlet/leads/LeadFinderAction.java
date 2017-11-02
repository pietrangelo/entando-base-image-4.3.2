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
import java.util.List;

import org.entando.entando.plugins.jpsalesforce.aps.internalservlet.AbstractSalesforceFrontAction;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeadFinderAction extends AbstractSalesforceFrontAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(LeadFinderAction.class);
	
	public List<String> getLeads() {
		List<String> list = new ArrayList<String>();
		SalesforceAccessDescriptor sad = null;
		try {
			sad = getSessionAccessor();
			list = getSalesforceManager().searchLeadAllFields(getSearchText(), sad);
		} catch (Throwable t) {
			_logger.error("Error looking for leads", t);
		}
		return list;
	}
	
	public String getSearchText() {
		return _searchText;
	}

	public void setSearchText(String searchText) {
		this._searchText = searchText;
	}

	private String _searchText;
}
