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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageExtraConfigDOM;
import com.agiletec.aps.util.ApsProperties;

/**
 * Dom class for parse the xml of extra page config
 * @author E.Santoboni
 */
public class SeoPageExtraConfigDOM extends PageExtraConfigDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoPageExtraConfigDOM.class);

	@Override
	protected void addExtraConfig(Page page, Document doc) {
		super.addExtraConfig(page, doc);
		if (!(page instanceof SeoPage)) {
			return;
		}
		SeoPage seoPage = (SeoPage) page;
		Element useExtraDescriptionsElement = doc.getRootElement().getChild(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
		if (null != useExtraDescriptionsElement) {
			Boolean value = Boolean.valueOf(useExtraDescriptionsElement.getText());
			seoPage.setUseExtraDescriptions(value.booleanValue());
		}
		Element descriptionsElement = doc.getRootElement().getChild(DESCRIPTIONS_ELEMENT_NAME);
		if (null != descriptionsElement) {
			List<Element> descriptionElements = descriptionsElement.getChildren(DESCRIPTION_ELEMENT_NAME);
			for (int i=0; i<descriptionElements.size(); i++) {
				Element descriptionElement = descriptionElements.get(i);
				String langCode = descriptionElement.getAttributeValue(DESCRIPTION_LANG_ATTRIBUTE_NAME);
				String description = descriptionElement.getText();
				seoPage.getDescriptions().put(langCode, description);
			}
		}
		Element friendlyCodeElement = doc.getRootElement().getChild(FRIENDLY_CODE_ELEMENT_NAME);
		if (null != friendlyCodeElement) {
			seoPage.setFriendlyCode(friendlyCodeElement.getText());
		}
		Element xmlConfigElement = doc.getRootElement().getChild(XML_CONFIG_ELEMENT_NAME);
		if (null != xmlConfigElement) {
			String xml = xmlConfigElement.getText();
			seoPage.setXmlConfig(xml);
			seoPage.setComplexParameters(this.extractComplexParameters(xml));
		}
	}
	
	private Map<String, Object> extractComplexParameters(String xmlConfig) {
		Document doc = this.decodeComplexParameterDOM(xmlConfig);
		Map<String, Object> complexParameters = new HashMap<String, Object>();
		List<Element> elements = doc.getRootElement().getChildren("parameter");
		for (int i = 0; i < elements.size(); i++) {
			Element paramElement = elements.get(i);
			String paramKey = paramElement.getAttributeValue("key");
			List<Element> langElements = paramElement.getChildren("property");
			if (null != langElements && langElements.size() > 0) {
				ApsProperties properties = new ApsProperties();
				for (int j = 0; j < langElements.size(); j++) {
					Element langElement = langElements.get(j);
					String propertyKey = langElement.getAttributeValue("key");
					String propertyValue = langElement.getText();
					properties.setProperty(propertyKey, propertyValue);
				}
				complexParameters.put(paramKey, properties);
			} else {
				String paramValue = paramElement.getText();
				complexParameters.put(paramKey, paramValue);
			}
		}
		return complexParameters;
	}
	
	private Document decodeComplexParameterDOM(String xml) {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xml);
		try {
			doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error while parsing xml: {}",xml, t);
		}
		return doc;
	}
	
	@Override
	protected void fillDocument(Document doc, IPage page) {
		super.fillDocument(doc, page);
		if (!(page instanceof ISeoPage)) return;
		ISeoPage seoPage = (ISeoPage) page;
		Element useExtraDescriptionsElement = new Element(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
		useExtraDescriptionsElement.setText(String.valueOf(seoPage.isUseExtraDescriptions()));
		doc.getRootElement().addContent(useExtraDescriptionsElement);
		ApsProperties descriptions = seoPage.getDescriptions();
		if (null != descriptions && descriptions.size() > 0) {
			Element descriptionsElement = new Element(DESCRIPTIONS_ELEMENT_NAME);
			doc.getRootElement().addContent(descriptionsElement);
			Iterator<Object> iterator = descriptions.keySet().iterator();
			while (iterator.hasNext()) {
				String langCode = (String) iterator.next();
				Element extraDescriptionElement = new Element(DESCRIPTION_ELEMENT_NAME);
				extraDescriptionElement.setAttribute(DESCRIPTION_LANG_ATTRIBUTE_NAME, langCode);
				extraDescriptionElement.setText(descriptions.getProperty(langCode));
				descriptionsElement.addContent(extraDescriptionElement);
			}
		}
		if (null != seoPage.getFriendlyCode() && seoPage.getFriendlyCode().trim().length() > 0) {
			Element friendlyCodeElement = new Element(FRIENDLY_CODE_ELEMENT_NAME);
			friendlyCodeElement.setText(seoPage.getFriendlyCode().trim());
			doc.getRootElement().addContent(friendlyCodeElement);
		}
		if (null != seoPage.getXmlConfig() && seoPage.getXmlConfig().trim().length() > 0) {
			Element xmlConfigElement = new Element(XML_CONFIG_ELEMENT_NAME);
			xmlConfigElement.setText(seoPage.getXmlConfig().trim());
			doc.getRootElement().addContent(xmlConfigElement);
		}
	}
	
	private static final String USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME = "useextradescriptions";
	private static final String DESCRIPTIONS_ELEMENT_NAME = "descriptions";
	private static final String DESCRIPTION_ELEMENT_NAME = "property";
	private static final String DESCRIPTION_LANG_ATTRIBUTE_NAME = "key";
	
	private static final String FRIENDLY_CODE_ELEMENT_NAME = "friendlycode";
	
	private static final String XML_CONFIG_ELEMENT_NAME = "xmlConfig";
	
}