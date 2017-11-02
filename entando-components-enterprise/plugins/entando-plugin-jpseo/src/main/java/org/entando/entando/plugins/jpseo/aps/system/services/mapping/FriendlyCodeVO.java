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

/**
 * @author E.Santoboni
 */
public class FriendlyCodeVO {
	
	public FriendlyCodeVO() {}
	
	public FriendlyCodeVO(String friendlyCode, String pageCode) {
		this.setFriendlyCode(friendlyCode);
		this.setPageCode(pageCode);
	}
	
	public FriendlyCodeVO(String friendlyCode, String contentId, String langCode) {
		this.setFriendlyCode(friendlyCode);
		this.setContentId(contentId);
		this.setLangCode(langCode);
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getFriendlyCode() {
		return _friendlyCode;
	}
	public void setFriendlyCode(String friendlyCode) {
		this._friendlyCode = friendlyCode;
	}
	
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	private String _friendlyCode;
	private String _pageCode;
	private String _contentId;
	private String _langCode;
	
}