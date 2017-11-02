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
package org.entando.entando.plugins.jpjasper.aps.services.report.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptorProperty;

@XmlRootElement(name = "resource")
@XmlType(propOrder = {"properties","resourceDescriptorsList"})
@XmlSeeAlso({ArrayList.class, HashMap.class})
public class JAXBResourceDescriptor extends JAXBBaseResourceDescriptor {

	public JAXBResourceDescriptor() {}

	public JAXBResourceDescriptor(JasperResourceDescriptor resourceDescriptor) {	
		super(resourceDescriptor);
		//properties
		Iterator<Map.Entry<String, JasperResourceDescriptorProperty>> propertyEntries = resourceDescriptor.getProperties().entrySet().iterator();
		while (propertyEntries.hasNext()) {
			Map.Entry<String, JasperResourceDescriptorProperty> entry = propertyEntries.next();
			this.getProperties().put(entry.getKey(), entry.getValue());
		}
		//nested resource descriptors
		Iterator<Map.Entry<String, List<JasperResourceDescriptor>>> entries = resourceDescriptor.getResourceDescriptors().entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, List<JasperResourceDescriptor>> entry = entries.next();
			List<JasperResourceDescriptor> list = entry.getValue();
			for (int i = 0; i < list.size(); i++) {
				JasperResourceDescriptor currentDescriptor = list.get(i);
				JAXBResourceDescriptor current = new JAXBResourceDescriptor(currentDescriptor);
				this.addResourceDescriptor(current);
			}
		}
	}

	public void addResourceDescriptor(JAXBResourceDescriptor descriptor) {
		this.getResourceDescriptorsList().add(descriptor);
	}



	public Map<String, JasperResourceDescriptorProperty> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, JasperResourceDescriptorProperty> properties) {
		this.properties = properties;
	}

	public List <JAXBResourceDescriptor> getResourceDescriptorsList() {
		return resourceDescriptorsList;
	}
	public void setResourceDescriptorsList(List <JAXBResourceDescriptor> resourceDescriptorsList) {
		this.resourceDescriptorsList = resourceDescriptorsList;
	}

	private Map<String, JasperResourceDescriptorProperty> properties = new HashMap<String, JasperResourceDescriptorProperty>();
	private List <JAXBResourceDescriptor> resourceDescriptorsList = new ArrayList<JAXBResourceDescriptor>();

}
