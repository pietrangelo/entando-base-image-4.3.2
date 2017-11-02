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
package org.entando.entando.plugins.jpjasper.aps.services.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * jdom parser for resourceDescriptor elements
 *
 */
public class JasperResourceDescriptorDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperResourceDescriptorDOM.class);
	
	private static final String ATTR_NAME = "name";
	private static final String ATTR_WS_TYPE = "wsType";
	private static final String ATTR_URI_STRING = "uriString";
	private static final String ATTR_ISNEW = "isNew";
	private static final String ELEM_RESOURCE_DESCRIPTOR = "resourceDescriptor";
	private static final String ELEM_LABEL = "label";
	private static final String ELEM_CREATION_DATE = "creationDate";
	private static final String ELEM_DESCRIPTION = "description";
	private static final String ELEM_RESOURCE_PROPERTY = "resourceProperty";
	private static final String ATTR_RESOURCE_PROPERTY_NAME = "name";

	/**
	 * http://<host>:<port>/jasperserver[-pro]/rest/resource/path/to/resource/
	 * @param xml
	 * @return
	 * @throws ApsSystemException
	 */
	public JasperResourceDescriptor parseResource(String xml) throws ApsSystemException {
		JasperResourceDescriptor resourceDescriptor = new JasperResourceDescriptor();
		if (!xml.startsWith("<resourceDescriptor")) {
			_logger.error("Invalid xml. Expected <resourceDescriptor... but was {}", StringUtils.abbreviate(xml, 0, 10));
			return null;
		}
		
		Element root = this.getRootElement(xml);
		
		resourceDescriptor = this.parseDescriptor(root);
		return resourceDescriptor;
	}
	
	public List<JasperResourceDescriptor> parseXML(String xml) throws ApsSystemException {
		if (!xml.startsWith("<resourceDescriptors>")) {
			_logger.error("Invalid xml. Expected <resourceDescriptors>... but was {}", StringUtils.abbreviate(xml, 0, 10));
			return null;
		}
		List<JasperResourceDescriptor> list = new ArrayList<JasperResourceDescriptor>();
		Element root = this.getRootElement(xml);
		
		this.extractDescriptors(root, list);
		return list;
	}
	
	private void extractDescriptors(Element root, List<JasperResourceDescriptor> list) throws ApsSystemException {
		List<Element> resDescriptors = root.getChildren(ELEM_RESOURCE_DESCRIPTOR);
		if (null != resDescriptors && !resDescriptors.isEmpty()) {
			Iterator<Element> iterator = resDescriptors.iterator();
			while (iterator.hasNext()) {
				Element current = iterator.next();
				JasperResourceDescriptor resource = this.parseDescriptor(current);
				list.add(resource);
			}
		}
	}
	
/*
<resourceDescriptor name="themes" wsType="folder" uriString="/themes" isNew="false">
	<label><![CDATA[Themes]]></label>
	<creationDate>1346756014543</creationDate>
	<resourceProperty name="PROP_RESOURCE_TYPE">
		<value><![CDATA[com.jaspersoft.jasperserver.api.metadata.common.domain.Folder]]></value>
	</resourceProperty>
</resourceDescriptor>
 */
	
	private JasperResourceDescriptor parseDescriptor(Element current) throws ApsSystemException {
		JasperResourceDescriptor res = null;
		try {
			res = new JasperResourceDescriptor();
			res.setName(current.getAttributeValue(ATTR_NAME));
			res.setWsType(current.getAttributeValue(ATTR_WS_TYPE));
			res.setUriString(current.getAttributeValue(ATTR_URI_STRING));
			String newRes = current.getAttributeValue(ATTR_ISNEW);
			res.setNewResource(!StringUtils.isBlank(newRes) && newRes.equalsIgnoreCase("true"));

			res.setLabel(current.getChildText(ELEM_LABEL));
			String dateValue = current.getChildText(ELEM_CREATION_DATE);
			if (!StringUtils.isBlank(dateValue)) {
				Long longValue = new Long(dateValue);
				res.setCreationDate(new Date(longValue));
			}
			res.setDescription(current.getChildText(ELEM_DESCRIPTION));
			List<Element> properties = current.getChildren(ELEM_RESOURCE_PROPERTY);
			if (null != properties && !properties.isEmpty()) {
				Iterator<Element> iterator = properties.iterator();
				while (iterator.hasNext()) {
					Element propElement = iterator.next();
					String key = propElement.getAttributeValue(ATTR_RESOURCE_PROPERTY_NAME);
					JasperResourceDescriptorProperty property = new JasperResourceDescriptorProperty();
					this.buildProperty(property, propElement);
					
					//if (null != valueEl) {
						//value = valueEl.getText();
						res.getProperties().put(key, property);
					//}
				}
			}
			
			//http://<host>:<port>/jasperserver[-pro]/rest/resource/path/to/resource/
			List<Element> resDescriptors = current.getChildren(ELEM_RESOURCE_DESCRIPTOR);
			if (null != resDescriptors && !resDescriptors.isEmpty()) {
				Iterator<Element> iterator = resDescriptors.iterator();
				while (iterator.hasNext()) {
					Element resDescriptorElement = iterator.next();
					JasperResourceDescriptor resourceDescriptor = this.parseDescriptor(resDescriptorElement);
//					String key = resDescriptorElement.getAttributeValue(ATTR_RESOURCE_PROPERTY_NAME);
//					String value = null;
//					Element valueEl = resDescriptorElement.getChild("value");
//					if (null != valueEl) {
//						value = valueEl.getText();
//						res.getPropertyes().put(key, value);
//					}
					res.addResourceDescriptor(resourceDescriptor);
				}
			}
			
		} catch (Throwable t) {
			_logger.error("Error parsing Descriptor xml", t);
			throw new ApsSystemException("Error parsing Descriptor xml", t);
		}
		return res;
	}

	private void buildProperty(JasperResourceDescriptorProperty property, Element propElement) {
		Element valueEl = propElement.getChild("value");
		if (null != valueEl) property.setValue(valueEl.getText());
		List<Element> properties = propElement.getChildren(ELEM_RESOURCE_PROPERTY);
		if (null != properties && properties.size() > 0) {
			property.setProperty(new HashMap<String, JasperResourceDescriptorProperty>());
			Iterator<Element> iterator = properties.iterator();
			while (iterator.hasNext()) {
				Element propertyElement = iterator.next();
				String key = propertyElement.getAttributeValue(ATTR_RESOURCE_PROPERTY_NAME);
				JasperResourceDescriptorProperty property2 = new JasperResourceDescriptorProperty();
				this.buildProperty(property2, propertyElement);
				property.getProperty().put(key, property2);
			}
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
