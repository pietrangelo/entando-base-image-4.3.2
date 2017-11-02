package org.entando.entando.plugins.jppentaho5.aps.system.services.model;

/**
 *
 * @author entando
 */
public class PentahoAttribute {

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getNamespace() {
		return _namespace;
	}

	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		this._value = value;
	}

	private String _name;
	private String _namespace; // not really used
	private String _value;

}
