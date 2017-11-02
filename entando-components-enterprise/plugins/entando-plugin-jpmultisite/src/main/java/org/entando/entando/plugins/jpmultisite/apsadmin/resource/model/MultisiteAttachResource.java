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

import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;

/**
 * @author S.Loru
 */
public class MultisiteAttachResource extends AttachResource implements MultisiteResourceInterface {

	/**
	 * Repurn the url path of the given istance.
	 * @param instance the resource instance
	 * @return Il path del file relativo all'istanza.
	 */
	protected String getUrlPath(ResourceInstance instance) {
		if (null == instance) return null;
		StringBuilder urlPath = new StringBuilder();
		if (this.isProtectedResource()) {
			//PATH di richiamo della servlet di autorizzazione
			//Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/
			String DEF = "def";
			urlPath.append(this.getProtectedBaseURL());
			if (!urlPath.toString().endsWith("/")) urlPath.append("/");
			urlPath.append(this.getId()).append("/");
			if (instance.getSize() < 0) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getSize());
			}
			urlPath.append("/");
			if (instance.getLangCode() == null) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getLangCode());
			}
			urlPath.append("/");
    	} else {
			StringBuilder subFolder = new StringBuilder(this.getFolder());
    		if (!subFolder.toString().endsWith("/")) {
				subFolder.append("/");
			}
			//This add the multisite code to the path
			String multisiteCodeByString = MultisiteUtils.getMultisiteCodeByString(this.getId());
			if(StringUtils.isNotBlank(multisiteCodeByString)){
				subFolder.append(multisiteCodeByString).append("/");
			}
    		subFolder.append(instance.getFileName());
			String path = this.getStorageManager().getResourceUrl(subFolder.toString(), false);
			urlPath.append(path);
    	}
		return urlPath.toString();
	}
	
	@Override
	protected String getDiskSubFolder() {
		StringBuilder diskFolder = new StringBuilder(super.getDiskSubFolder());
		if(null != this.getMultisiteCode() && !diskFolder.toString().contains(this.getMultisiteCode())){
			diskFolder = diskFolder.append(diskFolder).append(File.separator);
		}
		return diskFolder.toString();
	}

	public String getMultisiteCode() {
		return _multisiteCode;
	}

	public void setMultisiteCode(String multisiteCode) {
		this._multisiteCode = multisiteCode;
	}

	private String _multisiteCode;

	
	
}
