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
package com.agiletec.plugins.jppentaho.apsadmin.config;

public interface IPentahoConfigAction {
	
	/**
	 * Execute the action of editing of the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String edit();
	
	/**
	 * Execute the action of saving of the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String save();
	
}