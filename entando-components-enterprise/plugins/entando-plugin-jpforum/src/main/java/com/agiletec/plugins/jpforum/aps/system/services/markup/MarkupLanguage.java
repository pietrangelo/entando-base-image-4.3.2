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
package com.agiletec.plugins.jpforum.aps.system.services.markup;

import java.util.Map;

import com.agiletec.plugins.jpforum.aps.system.services.markup.tag.IForumMarkupTag;
/**
 * This class contains the markup tags to be used by the markupParser service
 * Each tag implements his own way to convert the string to xml and vice versa
 *
 */
public class MarkupLanguage {
	
	public Map getTagMapping() {
		return _tagMapping;
	}
	public void setTagMapping(Map tagMapping) {
		this._tagMapping = tagMapping;
	}
	
	public void setDefaultQuoteTag(IForumMarkupTag defaultQuoteTag) {
		this._defaultQuoteTag = defaultQuoteTag;
	}
	public IForumMarkupTag getDefaultQuoteTag() {
		return _defaultQuoteTag;
	}

	private IForumMarkupTag _defaultQuoteTag;
	private Map _tagMapping;
}
