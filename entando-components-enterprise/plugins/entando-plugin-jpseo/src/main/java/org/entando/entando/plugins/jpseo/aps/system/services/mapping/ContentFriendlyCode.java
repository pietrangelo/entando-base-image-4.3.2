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

import java.util.HashMap;
import java.util.Map;

public class ContentFriendlyCode {
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public Map<String, String> getFriendlyCodes() {
		return _friendlyCodes;
	}
	public void addFriendlyCode(String langCode, String friendlyCode) {
		this._friendlyCodes.put(langCode, friendlyCode);
	}
	public String getFriendlyCode(String langCode) {
		return this._friendlyCodes.get(langCode);
	}
	
	private String _contentId;
	private Map<String, String> _friendlyCodes = new HashMap<String, String>();
	
}