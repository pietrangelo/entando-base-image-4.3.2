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
package org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol;

public class InputControlStateOption {

	public String getLabel() {
		return _label;
	}
	public void setLabel(String label) {
		this._label = label;
	}
	public boolean isSelected() {
		return _selected;
	}
	public void setSelected(boolean selected) {
		this._selected = selected;
	}
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
	private String _label;
	private boolean _selected;
	private String _value;
}
