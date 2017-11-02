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
package org.entando.entando.plugins.jpsugarcrm.aps.services.client;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public interface ISugarCRMClientManager {

	public String executeLogin(UserDetails user) throws ApsSystemException;

	public String executeLogin(UserDetails user, String redirectUrl) throws ApsSystemException;

	public List<CrmLeadInfo> getLeads(UserDetails user, int maxResults) throws ApsSystemException;

}
