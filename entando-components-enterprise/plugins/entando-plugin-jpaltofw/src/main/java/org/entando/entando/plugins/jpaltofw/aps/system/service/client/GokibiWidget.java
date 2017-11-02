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

/**
 * @author E.Santoboni
 */
public class GokibiWidget {
	
	public String getType() {
		return _type;
	}
	public void setType(String type) {
		this._type = type;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public String getPlaceholder() {
		return _placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this._placeholder = placeholder;
	}
	
	public String getCode() {
		return (this.getName() + "_" + this.getType());
	}
	
	/*
	"type": "select",
    "name": "s1",
    "description": "gui generated",
    "placeholder": "<div class='gokibiWidget' data='<afs>afs://widget/select/s1</afs>'></div>"
	*/
	
	private String _type;
	private String _name;
	private String _description;
	private String _placeholder;
	
}
