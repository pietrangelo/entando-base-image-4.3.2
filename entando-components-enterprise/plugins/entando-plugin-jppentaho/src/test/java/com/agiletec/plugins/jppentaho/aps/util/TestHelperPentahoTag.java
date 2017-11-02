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
package com.agiletec.plugins.jppentaho.aps.util;

import com.agiletec.plugins.jppentaho.aps.JppentahoBaseTestCase;

public class TestHelperPentahoTag extends JppentahoBaseTestCase {
	
	public void test() {
		PentahoHelper helperPentahoTag = new PentahoHelper();
		
		String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"" +
"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"it\">" +
"<head>" +
"<title>jAPSIntra| try</title>" +
"<link rel=\"stylesheet\" type=\"text/css\" href=\"/jAPSIntraPentaho/resources/static/css/media/stampa.css\" media=\"print\" />" +
"<script type=\"text/javascript\" src=\"/jAPSIntraPentaho/resources/administration/js/mootools-1.2-core.js\"></script>     " +  
"</head> " +
"<body>" +
"<div id=\"intro\" class=\"service\"></div></body></html>";
		
		html = helperPentahoTag.extractOnlyHtmlBody(html);
		
		assertNotNull(html);
	}
	
}