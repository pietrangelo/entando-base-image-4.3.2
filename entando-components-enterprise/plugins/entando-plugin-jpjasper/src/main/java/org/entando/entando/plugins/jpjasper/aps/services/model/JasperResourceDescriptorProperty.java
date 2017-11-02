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

import java.util.Map;

public class JasperResourceDescriptorProperty {

	public Map<String, JasperResourceDescriptorProperty> getProperty() {
		return _property;
	}
	public void setProperty(Map<String, JasperResourceDescriptorProperty> property) {
		this._property = property;
	}
	
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}

	private String _value;
	private Map<String, JasperResourceDescriptorProperty> _property;
}
