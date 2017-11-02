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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControlState;

public class InputControlConfig {

	public static final int DT_TYPE_TEXT = 1;
	public static final int DT_TYPE_NUMBER = 2;
	public static final int DT_TYPE_DATE = 3;
	public static final int DT_TYPE_DATE_TIME = 4;

	public static final String INPUT_CONTROL_EXPRESSION_SEPARATOR = ";";
	

	/**
	 * Costruisce i parametri di configurazione a partire da una lista di {@link InputControl}<br />
	 * La lista di {@link InputControl} è richiesta al server utilzzando una mappa Map<String, InputControlConfig>
	 * Il metodo preserva i valori "speciali" degli InputControlConfig (FrontEndOverride e dataType) 
	 * @param xxx
	 * @param startConfig
	 * @return
	 */
	public static Map<String, InputControlConfig> buildInputControlMapFromApi(List<InputControl> xxx, Map<String, InputControlConfig> startConfig) {
		Map<String, InputControlConfig> map = new HashMap<String, InputControlConfig>();
		if (null != xxx && !xxx.isEmpty()) {
			Iterator<InputControl> it = xxx.iterator();
			while (it.hasNext()) {
				InputControl ic = it.next();
				InputControlState state = ic.getState();
				String value = state.getValue();

				if (null == value && null != state.getOptions() && !state.getOptions().isEmpty()) {

					List<Integer> indexes =  state.getSelectedOptionIndex();
					if (null != indexes && !indexes.isEmpty()) {
						//value="";
						InputControlConfig icc = new InputControlConfig();
						icc.setId(state.getId());
						icc.setListValue(ic.isListValue() ? "true" : "false");
						for (int i = 0; i < indexes.size(); i++) {
							value = state.getOptions().get(indexes.get(i)).getValue();
							icc.getValue().add(value);
						}
						if (null != startConfig && startConfig.containsKey(state.getId())) {
							icc.setFrontEndOverride(startConfig.get(state.getId()).getFrontEndOverride());
							icc.setDataType(startConfig.get(state.getId()).getDataType());
						}
						
						map.put(state.getId(), icc);
					}
				} else {
					InputControlConfig icc = new InputControlConfig();
					icc.setId(state.getId());
					icc.setListValue(ic.isListValue() ? "true" : "false");
					icc.getValue().add(value);
					if (null != startConfig && startConfig.containsKey(state.getId())) {
						icc.setFrontEndOverride(startConfig.get(state.getId()).getFrontEndOverride());
						icc.setDataType(startConfig.get(state.getId()).getDataType());
					}
					map.put(state.getId(), icc);
				}
			}
		}
		return map;
	}
	
	public static Map<String, InputControlConfig> buildInputControlMapFromConfig(String config) {
		Map<String, InputControlConfig> map = new HashMap<String, InputControlConfig>();
		if (!StringUtils.isBlank(config)) {
			String[] params = config.split(INPUT_CONTROL_EXPRESSION_SEPARATOR);
			for (int i = 0; i < params.length; i++) {
				String expr = params[i];
				InputControlConfig inputControlConfig = new InputControlConfig(expr);
				map.put(inputControlConfig.getId(), inputControlConfig);
			}
		}
		return map;
	}
	
	public static String buildInputControlConfigFromMap(Map<String, InputControlConfig> inputControlsMap) {
		StringBuffer sbBuffer = new StringBuffer();
		if (null != inputControlsMap) {
			for (Map.Entry<String, InputControlConfig> entry : inputControlsMap.entrySet()) {
				InputControlConfig value = entry.getValue();
				if (sbBuffer.length() > 0) sbBuffer.append(INPUT_CONTROL_EXPRESSION_SEPARATOR);
				sbBuffer.append(value.asString());
			}
		}
		return sbBuffer.toString();
	}

	public static String[][] inputControlConfigToArray(Map<String, InputControlConfig> inputControlsMap) {
		int size = 0;
		for (Map.Entry<String, InputControlConfig> entry : inputControlsMap.entrySet()) {
			//String key = entry.getKey();
			InputControlConfig inputControlConfig = entry.getValue();
			size = size + inputControlConfig.getValue().size();
		}
		
	    String[][] arr = new String[size][2];
	    int i = 0;
		for (Map.Entry<String, InputControlConfig> entry : inputControlsMap.entrySet()) {
	        for (int k = 0; k < entry.getValue().getValue().size(); k++) {
	     
	        	arr[i][0] = entry.getKey().toString();
	        	arr[i][1] = entry.getValue().getValue().get(k);
	        	i++;
	        }
		}
		return arr;
	}
	

	public InputControlConfig() {}

	public boolean isFrontEndOverride() {
		return null != this.getFrontEndOverride() && this.getFrontEndOverride().equalsIgnoreCase("true");
	}
	
	public InputControlConfig(String expr) {
		//InputControlConfig config = new InputControlConfig();
		//String nameExpr = "'-name':'(.*)',\\s*?'-";
		String frontEndOverrideExpr = "'-FO':'(.*)',\\s*?'-DT";
		String dataTypeExpr = "'-DT':'(\\d)',\\s*?'-name";
		String nameExpr = "'-name':'(.*)',\\s*?'-is";
		String valueExpr = "'-value':\\[(.*)\\]";
		String listItemExpr = "'-isListItem':'(.*)',\\s*?'-";
		String frontEndOverride = this.extractValue(frontEndOverrideExpr, expr);
		if (null == frontEndOverride || !frontEndOverride.equalsIgnoreCase("true")) frontEndOverride = "false";
		
		String dataType = this.extractValue(dataTypeExpr, expr);
		String id = this.extractValue(nameExpr, expr);
		List<String> value = this.extractValueList(valueExpr, expr);
		String isList = this.extractValue(listItemExpr, expr);
		this.setId(id);
		this.setValue(value);
		this.setListValue(isList);
		this.setFrontEndOverride(frontEndOverride);
		if (!StringUtils.isBlank(dataType)) {
			this.setDataType(new Integer(dataType));
		}
	}
	
	private String extractValue(String regExp, String target) {
		String value = "";
		Pattern p = Pattern.compile(regExp);
		Matcher matcher = p.matcher(target);
		if(matcher.find()) {
			value = matcher.group(1);
		}
		return value;
	}

	private List<String> extractValueList(String regExpt, String target) {
		List<String> list = null;
		Pattern p = Pattern.compile(regExpt);
		Matcher matcher = p.matcher(target);
		if(matcher.find()) {
			String values = matcher.group(1);
			if (null != values) {
				list = this.extractList(values);
			}
		}
		return list;
	}
	
	private List<String> extractList(String values) {
		List<String> list = new ArrayList<String>();
		String regExp = "'(.+?)'";
		Pattern p = Pattern.compile(regExp);
		Matcher matcher = p.matcher(values);
		while(matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

	public String asString() {
		StringBuffer sbBuffer = new StringBuffer("{'parameter': {'-FO':'" + this.getFrontEndOverride() +  "','-DT':'" + this.getDataType() +  "','-name':'" + this.getId() +  "','-isListItem':'" + this.getListValue() + "','-value':[");
		for (int i = 0; i < this.getValue().size(); i++) {
			if (i > 0) sbBuffer.append(",");
			sbBuffer.append("'"+ this.getValue().get(i)+"'");
		}
		sbBuffer.append("]'}}");
		return sbBuffer.toString();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}

	public String getListValue() {
		return _listValue;
	}
	public void setListValue(String listValue) {
		this._listValue = listValue;
	}

	public int getDataType() {
		return _dataType;
	}
	public void setDataType(int dataType) {
		this._dataType = dataType;
	}

	public String getFrontEndOverride() {
		return _frontEndOverride;
	}
	public void setFrontEndOverride(String frontEndOverride) {
		this._frontEndOverride = frontEndOverride;
	}

	private String id;
	private List<String> value = new ArrayList<String>();
	private String _listValue;
	private int _dataType;
	private String _frontEndOverride;
}
