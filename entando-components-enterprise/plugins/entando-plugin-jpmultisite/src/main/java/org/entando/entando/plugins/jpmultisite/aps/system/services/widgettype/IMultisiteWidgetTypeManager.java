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


package org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

/**
 *
 * @author work
 */
public interface IMultisiteWidgetTypeManager extends IWidgetTypeManager {
	
	
	/**
	 * This method allow you to delete widget type locked also. It's built only for multisite deletion purpose.
	 * @param widgetTypeCode
	 * @throws ApsSystemException 
	 */
	public void deleteWidgetTypeLocked(String widgetTypeCode) throws ApsSystemException;
	
	/**
	 * Extract list of widgetType by multisite code
	 * @param multisiteCode
	 * @return 
	 */
	public List<WidgetType> getWidgetTypesByMultisiteCode(final String multisiteCode);

}
