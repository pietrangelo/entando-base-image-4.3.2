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
package org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.json.JSONObject;
import org.json.XML;

import com.agiletec.aps.util.ApsProperties;

/**
 * This class represents the binding between two fields of the profile and lead.
 * It also contains the localized labels as well as the mandatory clause.
 * @author entando
 */
public class FormBinding {
	
//	private static final Logger _logger =  LoggerFactory.getLogger(FormBinding.class);
	
	/**
	 * Construct the object given its JSON representation
	 * @param json
	 */
	public FormBinding(JSONObject json) {
		_labels = new ApsProperties();
		_id = json.getInt(ID);
		try {
			_profileField = json.getString(PROFILEFIELD);
		} catch (Throwable t) {
			_profileField = JpSalesforceConstants.NO_BINDING_KEY;
		}
		_leadField = json.getString(LEADFIELD);
		_mandatory = json.getBoolean(MANDATORY);
		JSONObject jobj = json.getJSONObject(LABELS);
		String[] names = JSONObject.getNames(jobj);
		for (String name: names) {
			_labels.put(name, jobj.get(name));
		}
	}
	
	public FormBinding(String leadField, boolean mandatory, ApsProperties labels, int id) {
		if (StringUtils.isNotBlank(leadField)) {
			_leadField = leadField;
		} else {
			throw new RuntimeException("creating invalid form field association");
		}
		_profileField = "";
		_id = id;
		_mandatory = mandatory;
		_labels = labels;
	}
	
	public FormBinding(String leadField, String profileField, boolean mandatory, ApsProperties labels, int id) {
		if (StringUtils.isNotBlank(leadField)) {
			_leadField = leadField;
		} else {
			_leadField = JpSalesforceConstants.NO_BINDING_KEY;
		}
		if (StringUtils.isNotBlank(profileField)) {
			_profileField = profileField;
		} else {
			_profileField = JpSalesforceConstants.NO_BINDING_KEY;
		}
		_id = id;
		_mandatory = mandatory;
		_labels = labels;
	}
	
	/**
	 * Map this object into JSON
	 * @return
	 * @throws Throwable
	 */
	public JSONObject toJSON() throws Throwable {
		JSONObject jobj = new JSONObject();
		JSONObject jlang = new JSONObject();
		
		jobj.put(ID, _id);
		jobj.put(MANDATORY, _mandatory);
		jobj.put(LEADFIELD, _leadField);
		jobj.put(PROFILEFIELD, _profileField);
		
		if (null != _labels
				&& !_labels.isEmpty()) {
			Iterator<Object> itr = _labels.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				String value = _labels.getProperty(key);
				
				jlang.put(key, value);
			}
		}
		jobj.put(LABELS, jlang);
		return jobj;
	}
	
	protected String toXML() throws Throwable {
		String xml = null;
		JSONObject json = toJSON();
		xml = XML.toString(json, BINDING);
		
		return xml;
	}
	
	public String getLeadField() {
		return _leadField;
	}
	public void setLeadField(String leadField) {
		this._leadField = leadField;
	}
	public String getProfileField() {
		return _profileField;
	}
	public void setProfileField(String profileField) {
		this._profileField = profileField;
	}
	public boolean isMandatory() {
		return _mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this._mandatory = mandatory;
	}
	public ApsProperties getLabels() {
		return _labels;
	}
	public void setLabels(ApsProperties labels) {
		this._labels = labels;
	}
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	private String _leadField;
	private String _profileField;
	private boolean _mandatory;
	private ApsProperties _labels;
	private int _id;
	
	public final static String LEADFIELD = "leadField";
	public final static String PROFILEFIELD = "profileField";
	public final static String MANDATORY = "isMandatory";
	public final static String LABELS = "labels";
	public final static String ID = "id";
	
	private static final String BINDING = "binding";
}
