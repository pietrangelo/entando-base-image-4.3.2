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

public interface ILoginDAO {

	/**
	 * Login to Salesforce using the configuration provided
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	public SalesforceAccessDescriptor login(SalesforceConfig config) throws Throwable;
	
	/**
	 * Get the list of available API - no authentication needed
	 * @return
	 * @throws Throwable
	 */
	public List<ApiVersionDescriptor> listAvailableApiVersion() throws Throwable;
	
	/**
	 * Get the rest resources available for the given API version 
	 * @param avd
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	public SalesforceRestResources getRestResource(ApiVersionDescriptor avd, SalesforceAccessDescriptor sad) throws Throwable;
	
}
