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

package org.entando.entando.plugins.jpmultisite.apsadmin.resource.model;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import java.io.File;

/**
 * @author S.Loru
 */
public abstract class MultisiteAbstractResource extends AbstractMultiInstanceResource {

	@Override
	protected String getDiskSubFolder() {
		StringBuilder diskFolder = new StringBuilder(super.getDiskSubFolder());
		if(null != this.getMultisiteSuffix() && !diskFolder.toString().contains(this.getMultisiteSuffix())){
			diskFolder = diskFolder.append(diskFolder).append(File.separator);
		}
		return diskFolder.toString();
	}

	public String getMultisiteSuffix() {
		return _multisiteSuffix;
	}

	public void setMultisiteSuffix(String multisiteSuffix) {
		this._multisiteSuffix = multisiteSuffix;
	}
	
	private String _multisiteSuffix;

}
