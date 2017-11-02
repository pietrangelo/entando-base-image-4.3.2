package org.entando.entando.plugins.jppentaho5.aps.system.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author entando
 */
public class PentahoParameter {

	public PentahoParameter() {
		_attribute = new HashMap<String, PentahoAttribute>();
		_values = new HashMap<String, PentahoParameterValue>();
	}

	public boolean isIsList() {
		return _isList;
	}

	public void setIsList(boolean isList) {
		this._isList = isList;
	}

	public boolean isIsMandatory() {
		return _isMandatory;
	}

	public void setIsMandatory(boolean isMandatory) {
		this._isMandatory = isMandatory;
	}

	public boolean isIsMultiSelect() {
		return _isMultiSelect;
	}

	public void setIsMultiSelect(boolean isMultiSelect) {
		this._isMultiSelect = isMultiSelect;
	}

	public boolean isIsStrict() {
		return _isStrict;
	}

	public void setIsStrict(boolean isStrict) {
		this._isStrict = isStrict;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	void setTimezoneHint(String timezoneHint) {
		this._timezoneHint = timezoneHint;
	}

	public String getTimezoneHint() {
		return this._timezoneHint;
	}

	public Map<String, PentahoParameterValue> getValues() {
		return _values;
	}

	public void setValues(Map<String, PentahoParameterValue> values) {
		this._values = values;
	}

	public Map<String, PentahoAttribute> getAttribute() {
		return _attribute;
	}

	public void setAttribute(Map<String, PentahoAttribute> attribute) {
		this._attribute = attribute;
	}
	
	public String getFormName() {
		return _formName;
	}

	public void setFormName(String formName) {
		this._formName = formName;
	}


	private boolean _isList;
	private boolean _isMandatory;
	private boolean _isMultiSelect;
	private boolean _isStrict;
	private String _name;
	private String _type;
	private String _timezoneHint;
	// local to Entando, the varibale name as displayed in the form
	private String _formName;

	private Map<String, PentahoParameterValue> _values;
	private Map<String, PentahoAttribute> _attribute;

}
