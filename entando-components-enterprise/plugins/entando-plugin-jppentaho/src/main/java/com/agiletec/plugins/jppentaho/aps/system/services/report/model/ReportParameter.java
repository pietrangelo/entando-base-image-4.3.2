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
package com.agiletec.plugins.jppentaho.aps.system.services.report.model;

import java.util.List;
import java.util.Map;

/**
 * Pojo for report parameter definition
 * */
public class ReportParameter {

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public Map<String, String> getValues() {
		return _values;
	}
	public void setValues(Map<String, String> values) {
		this._values = values;
	}
	
	public void setType(String type) {
		this._type = type;
	}
	public String getType() {
		return _type;
	}
	
	public String getParameterRenderType() {
		return _parameterRenderType;
	}
	public void setParameterRenderType(String parameterRenderType) {
		this._parameterRenderType = parameterRenderType;
	}
	
	public boolean isMandatory() {
		return _mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this._mandatory = mandatory;
	}
	
	public String getLabel() {
		return _label;
	}
	public void setLabel(String label) {
		this._label = label;
	}

	public void setValuesNames(List<String> valuesNames) {
		this._valuesNames = valuesNames;
	}
	public List<String> getValuesNames() {
		return _valuesNames;
	}

	public void setMultiSelect(boolean isMultiSelect) {
		this._isMultiSelect = isMultiSelect;
	}
	public boolean isMultiSelect() {
		return _isMultiSelect;
	}

	public void setHidden(boolean hidden) {
		this._hidden = hidden;
	}
	public boolean isHidden() {
		return _hidden;
	}

	private String _name;
	private String _type;
	private String _parameterRenderType;
	private boolean _isMultiSelect;
	private boolean _mandatory;
	private boolean _hidden;
	private String _label;
	
	private Map<String,String> _values;
	private List<String> _valuesNames;
	
	public final static String PARAMETER_RENDER_TYPE_ATTR_NAME = "parameter-render-type";
	public final static String IS_MULTI_SELECT = "is-multi-select";
	public final static String IS_MANDATORY_ATTR_NAME = "is-mandatory";
	public final static String MANDATORY_ATTR_NAME = "mandatory";
	public final static String LABEL_ATTR_NAME = "label";
	public final static String HIDDEN_ATTR_NAME = "hidden";

}
