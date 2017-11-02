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
import java.util.Map;

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public interface ISeoMappingDAO {
	
	public Map<String, FriendlyCodeVO> loadMapping();
	
	public void updateMapping(FriendlyCodeVO vo);
	
	public void updateMapping(ContentFriendlyCode contentFriendlyCode);
	
	public void deleteMapping(String pageCode, String contentId);
	
	public List<String> searchFriendlyCode(FieldSearchFilter[] filters);
	
}