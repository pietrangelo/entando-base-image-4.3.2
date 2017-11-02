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

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.services.markup.tag.IForumMarkupTag;

public class MarkupParser extends AbstractService implements IMarkupParser, ServletContextAware {

	private static final Logger _logger =  LoggerFactory.getLogger(MarkupParser.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public String markupToXML(String markup) throws ApsSystemException {
		String converted = this.getSinglelineText(markup);
		try {
			if (this.getMarkupLanguage().getTagMapping().containsKey("code")) {
				converted = this.replaceCodeWithXml(converted);
				Object[] obj = this.replaceCodeBlocks(converted);
				List parts = (List) obj[0];
				converted = (String) obj[1];
				converted = this.replaceTagsWithXml(converted);
				converted = restoreCodeBlocks(converted, parts);
			} else {
				converted = this.replaceTagsWithXml(converted);
			}
		} catch (Throwable t) {
			_logger.error("Errore in getXml", t);
			throw new ApsSystemException ("Errore in getXml", t);
		}
		StringBuffer sBuffer = new StringBuffer();
		String prolog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String openRoot = "<post>";
		String closeRoot = "</post>";
		sBuffer.append(prolog).append(openRoot).append(converted).append(closeRoot);
		return sBuffer.toString();
	}

	@Override
	public String XMLToMarkup(String xml) throws ApsSystemException {
		String markUp = null;
		try {
			Document doc = this.getDocument(xml);
			markUp = this.xmlToMarkUp(doc.getRootElement());
			markUp = this.getMultilineText(markUp);
		} catch (Throwable t) {
			_logger.error("error in XMLToMarkup", t);
			throw new ApsSystemException ("error in XMLToMarkup", t);
		}
		return markUp;
	}
	
	@Override
	public String XMLtoHTML(String xml) throws ApsSystemException {
		String string = null;
		try {
			String stylesheetLocationPattern = "classpath:xsl/stylesheet.xsl";
                        if (null == _cachedXsltSource) {
				Resource[] resources = ApsWebApplicationUtils.getResources(stylesheetLocationPattern, this.getServletContext());
                                InputStream is = resources[0].getInputStream();
				Source xsltSource = new StreamSource(is);
				TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
				transformerFactory.setURIResolver(null);
				_cachedXsltSource = transformerFactory.newTemplates(xsltSource);
			}
			Transformer transformer = _cachedXsltSource.newTransformer();
			string = transform(xml, transformer);
			string = string.replaceAll("#CARR_RET#", "\r");
			string = string.replaceAll("#NEW_LINE#", "\n");
		} catch (Throwable t) {
			_logger.error("Errore in getHtml", t);
			throw new ApsSystemException ("Errore in getHtml", t);
		}
		return string;
	}
	
	private String xmlToMarkUp(Element root) throws ApsSystemException {
		Iterator it = root.getContent().iterator();
		StringBuffer sBuffer = new StringBuffer();
		try {
			while (it.hasNext()) {
				Object current = it.next();
				if (current instanceof Text) {
					Text t = (Text) current;
					sBuffer.append(t.getText());
				} else {
					Element e = (Element) current;
					//System.out.println(">  Parsing:\t" + e.getName());
					String name = e.getName();
					if (name != "li") {
						IForumMarkupTag markupTag = (IForumMarkupTag) _markupLanguage.getTagMapping().get(e.getName());
						sBuffer.append(markupTag.getMarkup(e,this));
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in xmlToMarkUp", t);
			throw new ApsSystemException ("Errore in xmlToMarkUp", t);
		}
		return sBuffer.toString();
	}	

	public String getMultilineText(String text) throws ApsSystemException {
		StringBuffer sBuffer = new StringBuffer();
		try {
			sBuffer = this.restoreCarrigeReturn(this.restoreNewLine(new StringBuffer(text)));
		} catch (Throwable t) {
			_logger.error("Errore in getSingleLineText", t);
			throw new ApsSystemException ("Errore in getSingleLineText", t);
		}
		return sBuffer.toString();
	}
	
	
	private Object[] replaceCodeBlocks(String markupText) throws ApsSystemException {
		List codeParts = new ArrayList();
		try {
			Pattern codePattern = Pattern.compile("(<code>.*?</code>)");
			Matcher codeMatcher = codePattern.matcher("");
			codeMatcher.reset(markupText);
			while (codeMatcher.find()) {
				codeParts.add(codeMatcher.group(1).replaceAll("\\$", "#DOLLAR#"));
				markupText = codeMatcher.replaceFirst(IForumMarkupTag.CODE_PLACEHOLDER);
				codeMatcher.reset(markupText);
			}
		} catch (Throwable t) {
			_logger.error("Errore in replaceCodeWithXml", t);
			throw new ApsSystemException ("Errore in replaceCodeWithXml", t);
		}
		return new Object[]{codeParts, markupText};
	}
	
	private String restoreCodeBlocks(String markupText, List parts) throws ApsSystemException {
		String converted = markupText;
		try {
			Iterator bodeBlockIterator = parts.iterator();
			while (bodeBlockIterator.hasNext()) {
				String part = (String)bodeBlockIterator.next();
				converted = converted.replaceFirst(IForumMarkupTag.CODE_PLACEHOLDER, part).replaceAll("#DOLLAR#", "\\$");
			}
		} catch (Throwable t) {
			_logger.error("Errore in restoreCodeBlocks", t);
			throw new ApsSystemException ("Errore in restoreCodeBlocks", t);
		}
		return converted;
	}	
	
	private String replaceTagsWithXml(String markupText) throws ApsSystemException {
		Iterator it = this.getMarkupLanguage().getTagMapping().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			IForumMarkupTag currentTag = (IForumMarkupTag) pairs.getValue();
			markupText = currentTag.replaceWithXml(markupText);
		}
		return markupText;
	}
	
	private StringBuffer restoreNewLine(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = IMarkupParser.NEWLINECHAR;
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll("\n"));
		return sBuffer;
	}

	private StringBuffer restoreCarrigeReturn(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = IMarkupParser.CARRIAGERETURNCHAR;
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll("\r"));
		return sBuffer;
	}

	private String transform(String input, Transformer transformer) throws ApsSystemException {
		/**
		Source xsltSource = new StreamSource(xslstyle);
		TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
		transformerFactory.setURIResolver(null);
		 **/
		StringWriter sw = new StringWriter();
		Result res = new StreamResult(sw);
		SAXSource src = null;
		try {
			Reader reader = new StringReader(input);
			src = new SAXSource(new InputSource(reader));
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setEntityResolver(null);
			src.setXMLReader(xmlReader);
			/*
			Templates cachedXSLT = transformerFactory.newTemplates(xsltSource);
			Transformer transformer = cachedXSLT.newTransformer();
			 */
			transformer.getOutputProperties();
			transformer.transform(src, res);
		} catch (Throwable t) {
			_logger.error("Errore in transform", t);
			throw new ApsSystemException ("Errore in transform", t);
		}
		return sw.toString();
	}
	
	private Document getDocument(String xmlText) throws ApsSystemException {
		Document doc = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder(false);
			Reader stringReader = new StringReader(xmlText);
			doc = saxBuilder.build(stringReader);
		} catch (Throwable t) {
			_logger.error("Errore in getDocument", t);
			throw new ApsSystemException ("Errore in getDocument", t);
		}
		return doc;
	}
	
	/**
	 * Tratta una stringa 'grezza' sostituendo gli acapo ecc... con il valore
	 * di NEWLINECHAR fino ad ottenere il testo in una singola linea
	 * @param text il testo in ingresso
	 * @return una stringa su una sola linea
	 */
	private String getSinglelineText(String text) throws ApsSystemException {
		StringBuffer sBuffer = new StringBuffer();
		try {
			StringBuffer string = new StringBuffer(text);
			sBuffer = carriageReturnToSpecialChar(this.newLineToSpecialChar(this.replaceGTAngles(this.replaceLTAngles(this.replaceAmp(string)))));
		} catch (Throwable t) {
			_logger.error("Errore in getSingleLineText", t);
			throw new ApsSystemException ("Errore in getSingleLineText", t);
		}
		return sBuffer.toString();
	}

	private StringBuffer carriageReturnToSpecialChar(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = "\\r";
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll(IMarkupParser.CARRIAGERETURNCHAR));
		return sBuffer;
	}

	private StringBuffer newLineToSpecialChar(StringBuffer  input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = "\\n";
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll(IMarkupParser.NEWLINECHAR));
		return sBuffer;
	}

	private StringBuffer replaceLTAngles(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = "<";
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll("&lt;"));
		return sBuffer;
	}
	
	private StringBuffer replaceAmp(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = "&";
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll("&amp;"));
		return sBuffer;
	}

	private StringBuffer replaceGTAngles(StringBuffer input) {
		StringBuffer sBuffer = new StringBuffer();
		String codeRegexp = ">";
		Pattern codePattern = Pattern.compile(codeRegexp);
		Matcher codeMatcher = codePattern.matcher("");
		codeMatcher.reset(input);
		sBuffer.append(codeMatcher.replaceAll("&gt;"));
		return sBuffer;
	}
	
	private String replaceCodeWithXml(String markupText) throws ApsSystemException {
		try {
			IForumMarkupTag currentTag = (IForumMarkupTag) this.getMarkupLanguage().getTagMapping().get("code");
			markupText = currentTag.replaceWithXml(markupText);
		} catch (Throwable t) {
			_logger.error("Errore in replaceCodeWithXml", t);
			throw new ApsSystemException ("Errore in replaceCodeWithXml", t);
		}
		return markupText;
	}

	public void setCachedXsltSource(Templates cachedXsltSource) {
		this._cachedXsltSource = cachedXsltSource;
	}
	public Templates getCachedXsltSource() {
		return _cachedXsltSource;
	}

	public void setMarkupLanguage(MarkupLanguage markupLanguage) {
		this._markupLanguage = markupLanguage;
	}
	@Override
	public MarkupLanguage getMarkupLanguage() {
		return _markupLanguage;
	}
        
	protected ServletContext getServletContext() {
		return this._servletContext;
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}
	
	private MarkupLanguage _markupLanguage;
	private Templates _cachedXsltSource;
        
        private ServletContext _servletContext;
	
}