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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IMultisiteManager {
	
	public List<Site> getSites();
	
	public Site getSite(String code);
	
	@Deprecated
	public Site getCurrentSite();
	
	public List<Site> getParents();
	
	public boolean isUseSan();
	
	public void setCurrentSiteCode(String currentSiteCode) throws ApsSystemException;
	
}
