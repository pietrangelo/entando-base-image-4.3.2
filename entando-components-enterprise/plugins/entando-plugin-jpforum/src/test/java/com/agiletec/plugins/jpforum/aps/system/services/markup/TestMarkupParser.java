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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;

public class TestMarkupParser extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testMarkup1() throws Throwable {
		//code - b - i - u - a
		assertNotNull(_markupParser);
		String markup = this.convertStreamToString("testpost1.txt");
		String xml = _markupParser.markupToXML(markup);
		
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		assertEquals("[code]code[code] - <strong>bold</strong> - <em>important</em> - <span style=\"text-decoration: underline;\">underline</span> - <a href=\"http://www.google.com\">google</a>\n", html);
	}

	public void testMarkup2() throws Throwable {
		//img - quote - size - color
		assertNotNull(_markupParser);
		String markup = this.convertStreamToString("testpost2.txt");
		String xml = _markupParser.markupToXML(markup);
		
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		String expectedHtml = "<img src=\"http://www.japsportal.org/jAPSPortal/resources/static/img/headerLogo_background.jpg\" alt=\"immagine\"/>	- <blockquote><div class=\"citazione\">lorem ipsum</div></blockquote> - <span style=\"font-size:12px\">help <span style=\"color:red\">me</span></span>\n";
		assertEquals(expectedHtml, html);
	}

	public void testMarkup3() throws Throwable {
		//quote  - color - size
		assertNotNull(_markupParser);
		String markup = this.convertStreamToString("testpost3.txt");
		String xml = _markupParser.markupToXML(markup);
		//System.out.println(xml);
		
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);

		assertEquals("<blockquote><div class=\"citazione\">testo</div></blockquote> <span style=\"color:red\">ciao <span style=\"font-size:10px\">mondo</span></span>\n", html);
	}
	
	public void testMarkup4_titles() throws Throwable {
		//title1-2-3
		assertNotNull(_markupParser);
		String markup = "* uno\n\r";//
		
		String xml = _markupParser.markupToXML(markup);

		
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		assertEquals("<span style=\"font-size: 2.5em; display: block; margin: 1em 1em 1em 0; font-weight: bold;\">uno\n</span>\r", html);
		
	}

	public void testMarkup5_lists() throws Throwable {
		//ul ol
		assertNotNull(_markupParser);
		String markup = "+uno\r\n+due\r\n+tre\r\n+quattro\r\n";
		String xml = _markupParser.markupToXML(markup);
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		assertEquals("<ol><li>uno</li>\n<li>due</li>\n<li>tre</li>\n<li>quattro</li></ol>\n", html);

		markup = "-uno\r\n-due\r\n-tre\r\n-quattro\r\n";
		xml = _markupParser.markupToXML(markup);
		reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		html = _markupParser.XMLtoHTML(xml);
		assertEquals("<ul><li>uno</li>\n<li>due</li>\n<li>tre</li>\n<li>quattro</li></ul>\n", html);
	}
	
	public void testMarkup5_br() throws Throwable {
		//$
		assertNotNull(_markupParser);
		String markup = "uno$$due$$tre$$quattro";
		String xml = _markupParser.markupToXML(markup);
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		assertEquals("uno$$due$$tre$$quattro", html);
	}

	public void testMarkupCitato() throws Throwable {
		//citato
		assertNotNull(_markupParser);
		String markup = this.convertStreamToString("citato.txt");
		String xml = _markupParser.markupToXML(markup);
		
		String reMarkup = _markupParser.XMLToMarkup(xml);
		assertEquals(markup, reMarkup);
		String html = _markupParser.XMLtoHTML(xml);
		assertEquals("Dante:<div class=\"citazione\">Uomini siate, e non pecore matte.<br class=\"TODO\"/></div>\n", html);
	}
	
	
	public String convertStreamToString(String testpost) throws IOException {
		File file = new File("target/test/" + testpost);
		InputStream is = new FileInputStream(file);
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {        
			return "";
		}
	}


	private void init() {
		_markupParser = (IMarkupParser) this.getService(JpforumSystemConstants.MARKUP_PARSER);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private IMarkupParser _markupParser;
}
