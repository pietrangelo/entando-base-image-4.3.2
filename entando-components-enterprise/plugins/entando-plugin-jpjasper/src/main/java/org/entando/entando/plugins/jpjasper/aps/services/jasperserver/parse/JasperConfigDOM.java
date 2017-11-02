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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver.parse;

import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.JasperConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * <jpjasper><baseURL>http://localhost:7080/jasperserver-pro</baseURL></jpjasper>
 */

public class JasperConfigDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperConfigDOM.class);
	
	private static final String ELEMENT_ROOT = "jpjasper";
	private static final String ELEMENT_SERVER_URL = "baseURL";
	private static final String ELEMENT_PAGE_CODE = "reportViewerPageCode";

	/**
	 * Create an xml containing the configuration.
	 */
	public String createConfigXml(JasperConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}
	
	private Element createConfigElement(JasperConfig config) {
		Element configElem = new Element(ELEMENT_ROOT);
		if (!StringUtils.isBlank(config.getBaseURL())) {			
			Element element = new Element(ELEMENT_SERVER_URL);
			element.setText(config.getBaseURL());
			configElem.addContent(element);
		}
		if (!StringUtils.isBlank(config.getReportViewerPageCode())) {
			Element element = new Element(ELEMENT_PAGE_CODE);
			element.setText(config.getReportViewerPageCode());
			configElem.addContent(element);
		}
		return configElem;
	}
	
	public JasperConfig extractConfig(String xml) throws ApsSystemException {
		JasperConfig config = new JasperConfig();
		Element root = this.getRootElement(xml);
		this.extractUrl(root, config);
		this.extractPagerCode(root, config);
		return config;
	}
	
	private void extractUrl(Element root, JasperConfig config) {
		Element baseURLElement = root.getChild(ELEMENT_SERVER_URL);
		if (baseURLElement != null) {
			config.setBaseURL(baseURLElement.getValue());
		}
	}
	private void extractPagerCode(Element root, JasperConfig config) {
		Element pageElement = root.getChild(ELEMENT_PAGE_CODE);
		if (pageElement != null) {
			config.setReportViewerPageCode(pageElement.getValue());
		}
	}
	
	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Error parsing xml: {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
}
