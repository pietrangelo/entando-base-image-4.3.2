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
package com.agiletec.plugins.jppentaho.aps.system.services.config;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/*
 * 

<pentahoConfig>
	<debug>true</debug>
	<serverUsername>joe</serverUsername>
	<serverPassword>password</serverPassword>
	<serverLocalUrl>localhost</serverLocalUrl>
	<serverLocalPort>8180</serverLocalPort>
	<serverLocalProtocol>http</serverLocalProtocol>
	<serverUrl>localhost</serverUrl>
	<serverPort>8180</serverPort>
	<serverProtocol>http</serverProtocol>
	<reportDefBasePath>/tmp/pentahoTemp</reportDefBasePath>
	<serverVisibleFromClient>true</serverVisibleFromClient>
	<reportDetailPage>reportview</reportDetailPage>
	<dataIntegrationRepositoryName />
	<dataIntegrationUsername>admin</dataIntegrationUsername>
	<reportDefBasePath>adminadmin</reportDefBasePath>
</pentahoConfig>

 * */
public class PentahoConfigDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoConfigDOM.class);

	public PentahoConfig extractConfig(String xml) throws ApsSystemException {
		PentahoConfig config = new PentahoConfig();
		Element root = this.getRootElement(xml);
		this.extractConfig(root, config);
		return config;
	}

	/**
	 * Create an xml containing the jpmail configuration.
	 * @param config The jpmail configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(PentahoConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}

	/**
	 * Extract the senders from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the senders configuration.
	 * @param config The configuration.
	 */
	private void extractConfig(Element root, PentahoConfig config) {

		String debug = this.extractValue(DEBUG, root);
		if (null != debug) {
			boolean debugValue = (debug.equals("true"))? true:false;
			config.setDebug(debugValue);
		}			

		String serverUsername = this.extractValue(serverUsername_ELEM, root);
		if (null != serverUsername) config.setServerUsername(serverUsername);

		String serverPassword = this.extractValue(serverPassword_ELEM, root);
		if (null != serverPassword) config.setServerPassword(serverPassword);

		String serverLocalUrl = this.extractValue(serverLocalUrl_ELEM, root);
		if (null != serverLocalUrl) config.setServerLocalUrl(serverLocalUrl);

		String serverLocalPort = this.extractValue(serverLocalPort_ELEM, root);
		try {
			if (null != serverLocalPort) config.setServerLocalPort(Integer.parseInt(serverLocalPort));
		} catch (Throwable e) {
			//nothing to catch
		}

		String serverLocalProtocol = this.extractValue(serverLocalProtocol_ELEM, root);
		if (null != serverLocalProtocol) config.setServerLocalProtocol(serverLocalProtocol);

		String serverUrl = this.extractValue(serverUrl_ELEM, root);
		if (null != serverUrl) config.setServerUrl(serverUrl);

		String serverPort = this.extractValue(serverPort_ELEM, root);
		try {
			if (null != serverPort) config.setServerPort(Integer.parseInt(serverPort));
		} catch (Throwable e) {
			//nothing to catch
		}

		String serverProtocol = this.extractValue(serverProtocol_ELEM, root);
		if (null != serverProtocol) config.setServerProtocol(serverProtocol);

		String serverContextPath = this.extractValue(serverContextPath_ELEM, root);
		if (null != serverContextPath) config.setServerContextPath(serverContextPath);

		String reportDefBasePath = this.extractValue(reportDefBasePath_ELEM, root);
		if (null != reportDefBasePath) config.setReportDefBasePath(reportDefBasePath);

		String serverVisibleFromClient = this.extractValue(serverVisibleFromClient_ELEM, root);
		try {
			if (null != serverVisibleFromClient) config.setServerVisibleFromClient(Boolean.parseBoolean(serverVisibleFromClient));
		} catch (Throwable e) {
			//nothing to catch
		}

		String reportDetailPage = this.extractValue(reportDetailPage_ELEM, root);
		if (null != reportDetailPage) config.setReportDetailPage(reportDetailPage);

		String dataIntegrationRepositoryName = this.extractValue(dataIntegrationRepositoryName_ELEM, root);
		if (null != dataIntegrationRepositoryName) config.setDataIntegrationRepositoryName(dataIntegrationRepositoryName);

		String dataIntegrationUsername = this.extractValue(dataIntegrationUsername_ELEM, root);
		if (null != dataIntegrationUsername) config.setDataIntegrationUsername(dataIntegrationUsername);

		String dataIntegrationPassword = this.extractValue(dataIntegrationPassword_ELEM, root);
		if (null != dataIntegrationPassword) config.setDataIntegrationPassword(dataIntegrationPassword);
	}

	private String extractValue(String elementName, Element root) {
		Element elem = root.getChild(elementName);
		if (null != elem) return elem.getText();
		return null;
	}

	/**
	 * Extract the smtp configuration from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private Element createConfigElement(PentahoConfig config) {
		Element configElem = new Element(ROOT);
		this.addConfigElement(configElem, String.valueOf(config.isDebug()), DEBUG);
		this.addConfigElement(configElem, config.getServerUsername(), serverUsername_ELEM);
		this.addConfigElement(configElem, config.getServerPassword(), serverPassword_ELEM);
		this.addConfigElement(configElem, config.getServerLocalUrl(), serverLocalUrl_ELEM);
		this.addConfigElement(configElem, config.getServerLocalPort(), serverLocalPort_ELEM);
		this.addConfigElement(configElem, config.getServerLocalProtocol(), serverLocalProtocol_ELEM);
		this.addConfigElement(configElem, config.getServerUrl(), serverUrl_ELEM);
		this.addConfigElement(configElem, config.getServerPort(), serverPort_ELEM);
		this.addConfigElement(configElem, config.getServerProtocol(), serverProtocol_ELEM);
		this.addConfigElement(configElem, config.getServerContextPath(), serverContextPath_ELEM);
		this.addConfigElement(configElem, config.getReportDefBasePath(), reportDefBasePath_ELEM);
		this.addConfigElement(configElem, config.isServerVisibleFromClient(), serverVisibleFromClient_ELEM);
		this.addConfigElement(configElem, config.getReportDetailPage(), reportDetailPage_ELEM);
		this.addConfigElement(configElem, config.getDataIntegrationRepositoryName(), dataIntegrationRepositoryName_ELEM);
		this.addConfigElement(configElem, config.getDataIntegrationUsername(), dataIntegrationUsername_ELEM);
		this.addConfigElement(configElem, config.getDataIntegrationPassword(), dataIntegrationPassword_ELEM);
		return configElem;
	}

	private void addConfigElement(Element rootElem, Integer value, String elementName) {
		Element configElem = new Element(elementName);
		if (null != value) {
			configElem.setText(String.valueOf(value.intValue()));
		}
		rootElem.addContent(configElem);
	}
	private void addConfigElement(Element rootElem, String value, String elementName) {
		Element configElem = new Element(elementName);
		configElem.setText(value);
		rootElem.addContent(configElem);
	}
	private void addConfigElement(Element rootElem, boolean value, String elementName) {
		Element configElem = new Element(elementName);
		configElem.setText(String.valueOf(value));
		rootElem.addContent(configElem);
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
			_logger.error("Error parsing xml {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

	private final String ROOT = "pentahoConfig";

	private final String serverUsername_ELEM = "serverUsername";
	private final String serverPassword_ELEM = "serverPassword";

	private final String serverLocalUrl_ELEM = "serverLocalUrl";
	private final String serverLocalPort_ELEM = "serverLocalPort";
	private final String serverLocalProtocol_ELEM = "serverLocalProtocol";

	private final String serverUrl_ELEM = "serverUrl";
	private final String serverPort_ELEM = "serverPort";
	private final String serverProtocol_ELEM = "serverProtocol";

	private final String serverContextPath_ELEM = "serverContextPath";

	private final String reportDefBasePath_ELEM = "reportDefBasePath";
	private final String serverVisibleFromClient_ELEM = "serverVisibleFromClient";
	private final String reportDetailPage_ELEM = "reportDetailPage";

	private final String dataIntegrationRepositoryName_ELEM = "dataIntegrationRepositoryName";
	private final String dataIntegrationUsername_ELEM = "dataIntegrationUsername";
	private final String dataIntegrationPassword_ELEM = "reportDefBasePath";

	private final String DEBUG = "debug";

}