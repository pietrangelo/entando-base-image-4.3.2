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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public interface ISeoMappingManager {
	
	public FriendlyCodeVO getReference(String friendlyCode);
	
	public String getPageReference(String pageCode);
	
	public String getContentReference(String contentId, String langCode);
	
	public List<String> searchFriendlyCode(FieldSearchFilter[] filters) throws ApsSystemException;
	
}