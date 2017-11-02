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
package org.entando.entando.plugins.jpseo.aps.system.services.url;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.url.IURLManager;

/**
 * @author E.Santoboni
 */
public class PageURL extends com.agiletec.aps.system.services.url.PageURL {
	
	public PageURL(IURLManager urlManager, RequestContext reqCtx) {
		super(urlManager, reqCtx);
	}
	
	public String getFriendlyCode() {
		return _friendlyCode;
	}
	public void setFriendlyCode(String friendlyCode) {
		this._friendlyCode = friendlyCode;
	}
	
	private String _friendlyCode;
	
}