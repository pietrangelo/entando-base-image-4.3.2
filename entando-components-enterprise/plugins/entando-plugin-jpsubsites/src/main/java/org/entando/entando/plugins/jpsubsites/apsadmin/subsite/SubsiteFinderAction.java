/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.apsadmin.subsite;

import java.util.Collection;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.ISubsiteManager;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

public class SubsiteFinderAction extends BaseAction {
	
	public Collection<Subsite> getSubsites() {
		try {
			return this.getSubsiteManager().getSubsites();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSubsites");
			throw new RuntimeException("Error loading subsites", t);
		}
	}
	
	public ISubsiteManager getSubsiteManager() {
		return _subsiteManager;
	}
	public void setSubsiteManager(ISubsiteManager subsiteManager) {
		this._subsiteManager = subsiteManager;
	}
	
	private ISubsiteManager _subsiteManager;
	
}