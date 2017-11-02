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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 *
 * @author S.Loru
 */
public interface IMultisiteCloneManager {
	
	public void cloneGroups(Multisite source, Multisite clone) throws ApsSystemException;
	
	public void cloneCategories(Multisite source, Multisite clone) throws ApsSystemException;
	
	public void cloneWidgets(Multisite source, Multisite clone) throws ApsSystemException;
	
	public void clonePages(Multisite source, Multisite clone) throws ApsSystemException;
	
	public void cloneResources(Multisite source, Multisite clone) throws ApsSystemException; 
	
	public void cloneContents(Multisite source, Multisite clone) throws ApsSystemException;


}
