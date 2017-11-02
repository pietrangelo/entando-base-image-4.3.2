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

import org.jdom.Element;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.markup.tag.xml.IForumXmlTag;

/**
 * Interface to bo implemented by the markupTag objects
 *
 */

public interface IForumMarkupTag {

	/**
	 * Converts the given string from markup to xml
	 * @param markupText the string to be converted
	 * @return the xml representation of the previded markup text 
	 * @throws ApsSystemException
	 */
	public String replaceWithXml(String markupText) throws ApsSystemException;
	

	/**
	 * Converts the given string from xml to markup
	 * @param element the jdom element to be converted
	 * @param markupParser the markup service
	 * @return the markup representation of the previded xml text 
	 * @throws ApsSystemException
	 */
	public String getMarkup(Element element, IMarkupParser markupParser) throws ApsSystemException;
	
	
//	/**
//	 * restituisce l'elemento {@link IForumMarkupTag} necessario per la trasformazione
//	 */

	/**
	 * 
	 */
	public IForumXmlTag getForumXmlTag();

	/**
	 * RegExp che utlizzata per catturare l'intero elemento di markup;
	 * i gruppi di questa esperssione sono utilizzati dal corrispondente
	 * {@link IForumXmlTag} per effettuare la confersione da markup a xml
	 */
	public String getRegExp();
	
	/**
	 * Stringa utilizzata dal motore per effettuare la conversione da markup a xml
	 */
	public String getRestoreExp();
	
	
	/**
	 * Se necessario raggruppa i sotto-elementi di tipo {@link IForumMarkupTag}
	 */
	public Map getTagElements();
	
	
	
	public static final String CODE_PLACEHOLDER = "#FORUM_CODE_PLACEHOLDER#";
	public static final String NESTED_ELEMENTS_PLACEHOLDER = "#NESTED_ELEMENTS_PLACEHOLDER#";
	public static final String LI_PLACEHOLDER = "#LI_PLACEHOLDER#";
}
