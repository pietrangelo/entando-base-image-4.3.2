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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>Form JasperReports-Server-Web-Services-Guide.pdf</b><br />
 * Resources (such as reports, images, queries, and content resources) are stored 
 * in a repository, which is organized like a file system, 
 * with a root and a hierarchical set of folders. 
 * Each object in the repository is considered a resource: a folder is a resource of 
 * type folder, a JRXML resource is a resource of type file, 
 * just as images and JAR files are of type file. 
 * Some resources are more abstract, such as connection definitions and an input controls. 
 * The repository web services operates on all resources
 *
 */
public class JasperResourceDescriptor {
	
	public void addResourceDescriptor(JasperResourceDescriptor descriptor) {
		String type = descriptor.getWsType();
		if (!this.getResourceDescriptors().containsKey(type)) {
			this.getResourceDescriptors().put(type, new ArrayList<JasperResourceDescriptor>());
		}
		this.getResourceDescriptors().get(type).add(descriptor);
	}
	
	public List<JasperResourceDescriptor> getInputControls() {
		String key = "inputControl";
		List<JasperResourceDescriptor> list = null;
		 if (this.getResourceDescriptors().containsKey(key)) {
			 List<JasperResourceDescriptor> ic = this.getResourceDescriptors().get(key);
			 if (null != ic && !ic.isEmpty()) list = ic;
		 }
		 return list;
	}

	public JasperResourceDescriptorProperty getProperty(String key) {
		JasperResourceDescriptorProperty value = null;
		if (this.getProperties().containsKey(key)) {
			value = this.getProperties().get(key);
		}
		return value;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getWsType() {
		return _wsType;
	}
	public void setWsType(String wsType) {
		this._wsType = wsType;
	}
	public String getUriString() {
		return _uriString;
	}
	public void setUriString(String uriString) {
		this._uriString = uriString;
	}
	public boolean isNewResource() {
		return _newResource;
	}
	public void setNewResource(boolean newResource) {
		this._newResource = newResource;
	}
	public String getLabel() {
		return _label;
	}
	public void setLabel(String label) {
		this._label = label;
	}
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	public Map<String, JasperResourceDescriptorProperty> getProperties() {
		return _properties;
	}
	public void setProperties(Map<String, JasperResourceDescriptorProperty> propertyes) {
		this._properties = propertyes;
	}
	
	/**
	 * Come key c'è il tipo (es: img) e come value la lista...<br />
	 * Quando viene invocata una risorsa tramite http://<host>:<port>/jasperserver[-pro]/rest/resource/path/to/resource/
	 * il risultato può contenere anche i componenti della risorsa stessa sotto forma di resource descriptor
	 * @return
	 */
	public Map<String, List<JasperResourceDescriptor>> getResourceDescriptors() {
		return _resourceDescriptors;
	}
	public void setResourceDescriptors(Map<String, List<JasperResourceDescriptor>> resourceDescriptors) {
		this._resourceDescriptors = resourceDescriptors;
	}

	private String _name;
	private String _wsType;
	private String _uriString;
	private boolean _newResource;
	
	private String _label;
	private String _description;
	private Date _creationDate;
	private Map<String, JasperResourceDescriptorProperty> _properties = new HashMap<String, JasperResourceDescriptorProperty>();
	private Map<String, List<JasperResourceDescriptor>> _resourceDescriptors = new HashMap<String, List<JasperResourceDescriptor>>();
	
}
