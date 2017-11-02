/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.aps.system.services;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.actionlog.ActionLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteActivityStreamManager extends ActionLogManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteActivityStreamManager.class);

	
	public List<Integer> getActionRecords(FieldSearchFilter[] filters) throws ApsSystemException {
		List<Integer> actionRecords = new ArrayList<Integer>();
		try{
			actionRecords = this.getActionLogDAO().getActionRecords(filters);
		} catch (Throwable t) {
			_logger.error("Error extracting activity stream records");
			throw new ApsSystemException("Error extracting activity stream records", t);
		}
		return actionRecords;
	}
	
}
