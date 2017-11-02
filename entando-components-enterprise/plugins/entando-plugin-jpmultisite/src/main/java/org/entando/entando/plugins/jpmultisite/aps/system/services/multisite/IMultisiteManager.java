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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.user.UserDetails;

public interface IMultisiteManager {

	public Multisite loadMultisite(String code) throws ApsSystemException;

	public List<String> loadMultisites() throws ApsSystemException;

	public List<String> searchMultisites(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addMultisite(Multisite multisite, UserDetails user) throws ApsSystemException;

	public void updateMultisite(Multisite multisite, UserDetails user) throws ApsSystemException;

	public void deleteMultisite(String code) throws ApsSystemException;
	
	public String getRootCategoryCode();
	
	public ITreeNode getRootCategory();
	
	public String loadMultisiteByUrl(String url) throws ApsSystemException;
	
	public void cloneMultisite(Multisite source, Multisite clone, UserDetails user) throws ApsSystemException;
	
}