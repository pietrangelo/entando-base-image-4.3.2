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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputControl {
	
	public static InputControl getById(List<InputControl> list, String id) {
		InputControl value = null;
		if (null != list && !list.isEmpty()) {
			Iterator<InputControl> it = list.iterator();
			while (it.hasNext()) {
				InputControl ic = it.next();
				if (ic.getId().equals(id)) {
					value = ic;
					break;
				}
			}
		}
		return value;
	}
	
	public boolean isListValue() {
		return this.getType().equals("multiSelect") || this.getType().equalsIgnoreCase("multiSelectCheckbox");
	}

	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}

	public String getLabel() {
		return _label;
	}
	public void setLabel(String label) {
		this._label = label;
	}
	
	public boolean isMandatory() {
		return _mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this._mandatory = mandatory;
	}
	
	public List<String> getMasterDependencies() {
		return _masterDependencies;
	}
	public void setMasterDependencies(List<String> masterDependencies) {
		this._masterDependencies = masterDependencies;
	}
	
	public boolean isReadOnly() {
		return _readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this._readOnly = readOnly;
	}
	
	public List<String> getSlaveDependencies() {
		return _slaveDependencies;
	}
	public void setSlaveDependencies(List<String> slaveDependencies) {
		this._slaveDependencies = slaveDependencies;
	}
	
	public String getType() {
		return _type;
	}
	public void setType(String type) {
		this._type = type;
	}
	
	public boolean isVisible() {
		return _visible;
	}
	public void setVisible(boolean visible) {
		this._visible = visible;
	}
	
	public String getStateXML() {
		return _stateXML;
	}
	public void setStateXML(String stateXML) {
		this._stateXML = stateXML;
	}
	
	public InputControlState getState() {
		return _state;
	}
	public void setState(InputControlState state) {
		this._state = state;
	}

	public String getValidationRulesXML() {
		return _validationRulesXML;
	}
	public void setValidationRulesXML(String validationRulesXML) {
		this._validationRulesXML = validationRulesXML;
	}

	private String _id;
	private String _label;
	private boolean _mandatory;
	private List<String> _masterDependencies = new ArrayList<String>();
	private boolean _readOnly;
	private List<String> _slaveDependencies = new ArrayList<String>();
	
	private String _type;
	private boolean _visible;
	private String _stateXML;
	private InputControlState _state;
	private String _validationRulesXML;
	
}
