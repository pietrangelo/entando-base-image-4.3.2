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

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;

public interface ICampaignDAO {
	
	List<Campaign> getCampaigns(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable;
	
}
