/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpaltofw.aps.system.service.client;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author eu
 */
public class GokibiInstancesDOM {
	
	public List<GokibiInstance> extractConfig(String xml) throws ApsSystemException {
		Element root = this.getRootElement(xml);
		return this.extractInstances(root);
	}
	
	private List<GokibiInstance> extractInstances(Element root) {
		List<GokibiInstance> instances = new ArrayList<GokibiInstance>();
		List<Element> instanceElements = root.getChildren(INSTANCE_ELEM);
		Iterator iter = instanceElements.iterator();
		while (iter.hasNext()) {
			Element instanceElem = (Element) iter.next();
			GokibiInstance instance = new GokibiInstance();
			instance.setId(instanceElem.getChildText(ID_ELEM));
			instance.setBaseUrl(instanceElem.getChildText(BASE_URL_ELEM));
			instance.setPid(instanceElem.getChildText(PID_ELEM));
			instances.add(instance);
		}
		return instances;
	}
	
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private static final String INSTANCE_ELEM = "instance";
	
	private static final String ID_ELEM = "id";
	private static final String BASE_URL_ELEM = "baseUrl";
	private static final String PID_ELEM = "pid";
	
}