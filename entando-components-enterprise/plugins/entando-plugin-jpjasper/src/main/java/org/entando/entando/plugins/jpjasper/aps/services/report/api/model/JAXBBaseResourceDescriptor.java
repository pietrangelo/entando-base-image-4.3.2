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

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;

import com.agiletec.aps.system.common.entity.model.JAXBEntity;

@XmlRootElement(name = "resource")
@XmlType(propOrder = {"name","wsType","uriString","newResource","label","description","creationDate"})

public class JAXBBaseResourceDescriptor extends JAXBEntity {

	public JAXBBaseResourceDescriptor() {}

	public JAXBBaseResourceDescriptor(JasperResourceDescriptor resourceDescriptor) {
		this.setName(resourceDescriptor.getName());
		this.setWsType(resourceDescriptor.getWsType());
		this.setUriString(resourceDescriptor.getUriString());
		this.setNewResource(resourceDescriptor.isNewResource());
		this.setLabel(resourceDescriptor.getLabel());
		this.setDescription(resourceDescriptor.getDescription());
		this.setCreationDate(resourceDescriptor.getCreationDate());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWsType() {
		return wsType;
	}
	public void setWsType(String wsType) {
		this.wsType = wsType;
	}
	public String getUriString() {
		return uriString;
	}
	public void setUriString(String uriString) {
		this.uriString = uriString;
	}
	public boolean isNewResource() {
		return newResource;
	}
	public void setNewResource(boolean newResource) {
		this.newResource = newResource;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	private String name;
	private String wsType;
	private String uriString;
	private boolean newResource;

	private String label;
	private String description;
	private Date creationDate;
}
