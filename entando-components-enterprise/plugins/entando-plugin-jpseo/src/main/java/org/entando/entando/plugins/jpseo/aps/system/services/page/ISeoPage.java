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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;

/**
 * This class describes a pages of the portal.
 * @author E.Santoboni
 */
public interface ISeoPage extends IPage {
	
	public ApsProperties getDescriptions();
	
	public String getDescription(String langCode);
	
	public boolean isUseExtraDescriptions();
	
	public String getFriendlyCode();
	
	public String getXmlConfig();
	
	public Map<String, Object> getComplexParameters();
	
}