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

import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

public interface ISalesforceMessageDAO {

	/**
	 * Create a new form type given bindings
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	String createMessageEntity(FormConfiguration config) throws Throwable;

}
