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
package com.agiletec.plugins.jpforum.aps.system.services.markup.tag;

import java.util.Map;

import com.agiletec.plugins.jpforum.aps.system.services.markup.tag.xml.IForumXmlTag;

public abstract class AbstractMarkupTag implements IForumMarkupTag {
	
	@Override
	public IForumXmlTag getForumXmlTag() {
		return _forumXmlTag;
	}
	public void setForumXmlTag(IForumXmlTag forumXmlTag) {
		this._forumXmlTag = forumXmlTag;
	}
	
	@Override
	public String getRegExp() {
		return _regExp;
	}
	public void setRegExp(String regExp) {
		this._regExp = regExp;
	}
	
	@Override
	public String getRestoreExp() {
		return _restoreExp;
	}
	public void setRestoreExp(String restoreExp) {
		this._restoreExp = restoreExp;
	}
	
	@Override
	public Map getTagElements() {
		return _tagElements;
	}
	public void setTagElements(Map tagElements) {
		this._tagElements = tagElements;
	}
	
	private IForumXmlTag _forumXmlTag;
	private String _regExp;
	private String _restoreExp;
	private Map _tagElements;
	
}
