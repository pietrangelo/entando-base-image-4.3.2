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
package com.agiletec.plugins.jpforum.aps.system.services.markup.tag.xml;

public interface IForumXmlTag {
	
	/**
	 * Stringa utilizzata dal motore di conversione per effettuare 
	 * il passaggio da markup a xml; Tale stringa contiene uno o più
	 * elementi di sostrituzione $x in funzione della regExp del corrispondente 
	 * elemento markup;
	 * @return
	 */
	public String getXmlStringPattern();
}
