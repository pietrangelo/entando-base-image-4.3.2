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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface for the service that handles the post markup tags.
 * Posts are stored in xml format, displayed in html, and created by using bbcode tags.
 * This service provides the transormations between this formats
 */
public interface IMarkupParser {

	/**
	 * Converts a raw text on text with bbcode tags to XML
	 * Eg: form <code>[b]this is bold[/b]</code> to <code>&lt;b&gt;this is bold&lt;/b&gt;</code>
	 * @param markup the string to be converted in xml
	 * @return the xml representation og the provided string
	 * @throws ApsSystemException
	 */
	public String markupToXML(String markup) throws ApsSystemException;

	/**
	 * Converts an xml string to his markup representation. 
	 * Eg: form <code>&lt;b&gt;this is bold&lt;/b&gt;</code> to <code>[b]this is bold[/b]</code>
	 * @param xml the string to be converted
	 * @return the bbcode as the user inserted it
	 * @throws ApsSystemException
	 */
	public String XMLToMarkup(String xml) throws ApsSystemException;
	
	/**
	 * Converts an xml string to his html representation
	 * Eg: form <code>&lt;b&gt;this is bold&lt;/b&gt;</code> to <code>&lt;strong&gt;this is bold&lt;/strong&gt;</code>
	 * This conversion is done through an xsl transformation
	 * @param xml the string to be converted
	 * @return the html code for the provided xml
	 * @throws ApsSystemException
	 */
	public String XMLtoHTML(String xml) throws ApsSystemException;
	
//	/**
//	 * Checks if an xml is valid
//	 * @param xmlString
//	 * @return
//	 * @throws ApsSystemException
//	 */
//	public boolean isValid(String xmlString) throws ApsSystemException;
	
	/**
	 * Returns the class that holds list of the markup tags to be used
	 */
	public MarkupLanguage getMarkupLanguage();
	
	public static final String NEWLINECHAR = "#NEW_LINE#";
	
	public static final String CARRIAGERETURNCHAR = "#CARR_RET#";
}
