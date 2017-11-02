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

package org.entando.entando.plugins.jpactiviti.aps.system;

import org.entando.entando.plugins.jpactiviti.aps.system.services.IActivitiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author S.Loru
 */
public class AbstractActivitiAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger =  LoggerFactory.getLogger(AbstractActivitiAction.class);
		
	public IActivitiManager getActivitiManager() {
		return _activitiManager;
	}

	public void setActivitiManager(IActivitiManager activitiManager) {
		this._activitiManager = activitiManager;
	}

	private IActivitiManager _activitiManager;

}
