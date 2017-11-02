package org.entando.entando.plugins.jppentaho5.aps.system.services.model;

/**
 *
 * @author entando
 */
public class PentahoParameterValue {

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

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		this._value = value;
	}

	public boolean isIsNull() {
		return _isNull;
	}

	public void setIsNull(boolean isNull) {
		this._isNull = isNull;
	}

	private String _label;
	private boolean _selected;
	private String _type;
	private String _value;
	private boolean _isNull;
}
