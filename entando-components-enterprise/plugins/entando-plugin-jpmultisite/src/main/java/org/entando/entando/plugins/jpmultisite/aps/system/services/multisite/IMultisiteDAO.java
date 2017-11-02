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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import java.util.Map;

public interface IMultisiteDAO {

	public List<String> searchMultisites(FieldSearchFilter[] filters);
	
	public Multisite loadMultisite(String code);

	public List<String> loadMultisitesCode();
	
	public List<Multisite> loadMultisites();
	
	public Map<String, Multisite> loadMultisitesMap();

	public void removeMultisite(String code);
	
	public void updateMultisite(Multisite multisite);

	public void insertMultisite(Multisite multisite);
	

}