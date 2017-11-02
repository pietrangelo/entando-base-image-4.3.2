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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;

public class SimpleMarkupTag extends AbstractMarkupTag {

	public String replaceWithXml(String markupText) throws ApsSystemException {
		String result = markupText;
		String strToReplace = this.getRegExp();
		Pattern pattern = Pattern.compile(strToReplace, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("");
		matcher.reset(result);
		while (matcher.find()) {
			result = matcher.replaceFirst(getForumXmlTag().getXmlStringPattern());
			matcher.reset(result);
		}
		return result;
	}
	
	public String getMarkup(Element element, IMarkupParser markupParser) throws ApsSystemException {
		String theString = getRestoreExp();
		List childs = element.getContent();
		Iterator chIt = childs.iterator();
		StringBuffer sBuffer = new StringBuffer();
		while (chIt.hasNext()) {
			Object current = chIt.next();
			if (current instanceof Text) {
				Text t = (Text) current;
				sBuffer.append(t.getText());
			} else {
				Element e = (Element) current;
				IForumMarkupTag tag = (IForumMarkupTag) markupParser.getMarkupLanguage().getTagMapping().get(e.getName());
				sBuffer.append(tag.getMarkup(e, markupParser));
			}
		}
		theString = theString.replace("getText", sBuffer.toString());
		
		Iterator it = element.getAttributes().iterator();
		while (it.hasNext()) {
			Attribute attr = (Attribute) it.next();
			StringBuffer target = new StringBuffer();
			target.append("getAttribute(").append(attr.getName()).append(")");
			theString = theString.replace(target, attr.getValue());
		}
		return theString.replace(IForumMarkupTag.NESTED_ELEMENTS_PLACEHOLDER, "");
	}
}
