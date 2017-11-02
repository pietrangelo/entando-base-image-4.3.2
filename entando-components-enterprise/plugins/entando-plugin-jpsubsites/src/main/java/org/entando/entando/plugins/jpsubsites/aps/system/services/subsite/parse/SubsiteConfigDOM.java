/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.parse;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.SubsiteConfig;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that provides conversion from and to XML for the Subsite configuration.
 */
public class SubsiteConfigDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(SubsiteConfigDOM.class);
	
	/**
	 * Extract the subsites configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The subsites configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public SubsiteConfig extractConfig(String xml) throws ApsSystemException {
		SubsiteConfig config = new SubsiteConfig();
		Element root = this.getRootElement(xml);
		String rootPageCode = root.getAttributeValue(PAGECODE_ATTR);
		config.setRootPageCode(rootPageCode);
		String pageModel = root.getAttributeValue(PAGEMODEL_ATTR);
		config.setPageModel(pageModel);
		String categoryRoot = root.getAttributeValue(CATEGORY_ATTR);
		config.setCategoriesRoot(categoryRoot);
		List contentTypes = root.getChildren(CONTENTTYPE_CHILD);
		if (contentTypes!=null && contentTypes.size()>0) {
			Iterator contentTypesIter = contentTypes.iterator();
			while (contentTypesIter.hasNext()) {
				Element child = (Element) contentTypesIter.next();
				String typeCode = child.getAttributeValue(CONTENTTYPE_CODE_ATTR);
				String modelId = child.getAttributeValue(CONTENTTYPE_MODELID_ATTR);
				config.addContentModel(typeCode, Long.valueOf(modelId));
			}
		}
		return config;
	}
	
	/**
	 * Create an xml containing the subsites configuration.
	 * @param config The subsites configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(SubsiteConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	/**
	 * Create an xml element containing the subsites configuration.
	 * @param config The configuration.
	 * @return The xml element containing the subsites configuration.
	 */
	private Element createConfigElement(SubsiteConfig config) {
		Element configElem = new Element(ROOT);
		configElem.setAttribute(PAGECODE_ATTR, config.getRootPageCode());
		configElem.setAttribute(PAGEMODEL_ATTR, config.getPageModel());
		configElem.setAttribute(CATEGORY_ATTR, config.getCategoriesRoot());
		Map<String, Long> contentModels = config.getContentModels();
		if (contentModels!=null && contentModels.size()>0) {
			for (Entry<String, Long> contentModel : contentModels.entrySet()) {
				Element contentTypeElem = new Element(CONTENTTYPE_CHILD);
				contentTypeElem.setAttribute(CONTENTTYPE_CODE_ATTR, contentModel.getKey());
				contentTypeElem.setAttribute(CONTENTTYPE_MODELID_ATTR, contentModel.getValue().toString());
				configElem.addContent(contentTypeElem);
			}
		}
		return configElem;
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
			_logger.error("Error parsing xml: " + t.getMessage(), t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private final String ROOT = "subsitesConfig";
	private final String PAGECODE_ATTR = "rootPageCode";
	private final String PAGEMODEL_ATTR = "pageModel";
	private final String CATEGORY_ATTR = "categoryRoot";
	private final String CONTENTTYPE_CHILD = "contentType";
	private final String CONTENTTYPE_MODELID_ATTR = "modelId";
	private final String CONTENTTYPE_CODE_ATTR = "code";
	
}