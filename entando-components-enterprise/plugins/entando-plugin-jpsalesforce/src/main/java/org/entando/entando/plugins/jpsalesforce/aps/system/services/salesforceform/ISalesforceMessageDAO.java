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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import java.util.Map;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

public interface ISalesforceMessageDAO {

	/**
	 * Create a new form type given bindings
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	public String createMessageEntity(FormConfiguration config) throws Throwable;

	/**
	 * Create the map of the arguments of the web to lead form
	 * @param message
	 * @param _sad 
	 * @param formConfiguration 
	 */
	public Map<String, String> prepareWebToLeadForm(Message message, FormConfiguration config);

	/**
	 * Delete the Message entity prototype given the related form configuration
	 * @param cfg
	 * @throws Throwable
	 */
	public void deleteMessageEntity(String code) throws Throwable;

	/**
	 * Update the Message entity prototype given the related form configuration
	 * @param config
	 * @param typeCode TODO
	 * @throws Throwable
	 */
	public void updateMessageEntity(FormConfiguration config, String typeCode) throws Throwable;

}
