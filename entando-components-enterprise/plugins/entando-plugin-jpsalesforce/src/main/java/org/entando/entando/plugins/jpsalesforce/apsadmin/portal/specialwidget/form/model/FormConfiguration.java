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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * This class describes the form configuration
 * @author entando
 *
 */
public class FormConfiguration {
	
	private static final Logger _logger =  LoggerFactory.getLogger(FormConfiguration.class);
	
	public FormConfiguration(JSONObject json) {
		JSONArray jarr = new JSONArray();
		
		try {
			// this is needed when the JSON was obtained from a XML conversion
			JSONObject tmp = json.getJSONObject(CONFIGURATION);
			json = tmp;
		} catch (Throwable t) {
			// do nothing
		}
		_campaignId = json.getString(CAMPAIGN);
		_profileId = json.getString(PROFILE);
		try {
			jarr = json.getJSONArray(BINDINGS);
			for (int idx = 0; idx < jarr.length(); idx++) {
				JSONObject jbind = (JSONObject) jarr.get(idx);
				FormBinding bind = new FormBinding(jbind);
				_bindings.add(bind);
			}
		} catch (Throwable t) {
			// load a single binding
			JSONObject jsb = json.getJSONObject(BINDINGS);
			FormBinding bind = new FormBinding(jsb);
			_bindings.add(bind);
		}
	}
	
	public FormConfiguration(String campaignId, String profileId) {
		if (StringUtils.isNotBlank(campaignId)) {
			_campaignId = campaignId;
		} else {
			throw new RuntimeException("creating invalid form configuration");
		}
		if (StringUtils.isNotBlank(profileId)) {
			_profileId = profileId;
		} else {
			throw new RuntimeException("creating invalid form configuration");
		}
	}
	
	public FormConfiguration clone() {
		FormConfiguration form = new FormConfiguration(_campaignId, _profileId);
	
		form.setBindings(getBindings());
		return form;
	}

	/**
	 * Add a new field binding
	 * @param bind
	 * @return
	 */
	public int addFieldAssociation(FormBinding bind) throws Throwable {
		boolean lead = true;
		boolean prof = true;
		
		if (!bindExists(bind)) {
			_bindings.add(bind);
			if (!bind.getLeadField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
				lead = _remainingLeadFields.remove(bind.getLeadField());
			}
			if (null != bind.getProfileField()
					&& !bind.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
				prof = _remainingProfileFields.remove(bind.getProfileField());
			}
			if (!lead || !prof) {
				_logger.debug("Error updating the lists of available fields (page refreshed?)");
				throw new ApsSystemException("Error updating the lists of available fields");
			}
		} else {
			_logger.debug("Cannot add duplicate binding");
		}
		return _bindings.size();
	}
	
	/**
	 * Check whether a given binding exists
	 * @param leadField
	 * @param profileField
	 * @return
	 */
	public boolean bindExists(String leadField, String profileField) {
		boolean res = false;
		
		for (FormBinding bind: _bindings) {
			
			if (bind.getLeadField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
				if (StringUtils.isNotBlank(profileField)
						&& bind.getProfileField().equals(profileField)) {
					res = true;
				}
			} else if (bind.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
				if (StringUtils.isNotBlank(leadField) 
						&& bind.getLeadField().equals(leadField)) {
					res = true;
				}
			} else {
				if (StringUtils.isNotBlank(leadField)
						&& StringUtils.isNotBlank(profileField)
						&& bind.getLeadField().equals(leadField) 
						&& bind.getProfileField().equals(profileField)) {
					res = true;
				}
			}
		}
		return res;
	}
	
	/**
	 * Check whether a given binding exists
	 * @param bind
	 * @return
	 */
	public boolean bindExists(FormBinding bind) {
		boolean res = false;
		
		if (null != bind) {
			res = bindExists(bind.getLeadField(), bind.getProfileField());
		}
		return res;
	}
	
	/**
	 * Flush the configuration
	 */
	public void flush() {
		_bindings.clear();
		_remainingLeadFields.clear();
		_remainingProfileFields.clear();
		_remainingLeadFields.addAll(_leadFields);
		_remainingProfileFields.addAll(_profileFields);
	}
	
	/**
	 * Delete a the given field association
	 * @param idx
	 */
	public void deleteFieldAssociation(int idx) {
		FormBinding rem = _bindings.get(idx);
		
		if (!rem.getLeadField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
			_remainingLeadFields.add(rem.getLeadField());
		}
		if (!rem.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
			_remainingProfileFields.add(rem.getProfileField());
		}
		_bindings.remove(idx);
	}
	
	/**
	 * Swap two elements
	 * @param first
	 * @param second
	 */
	public void swap(int smaller, int greater) {
		
		if (smaller >= 0 &&
				greater < _bindings.size()) {
			Collections.swap(_bindings, smaller, greater);
		}
	}
	
	/**
	 * Return the JSON representation of this object
	 * @return
	 * @throws Throwable
	 */
	public JSONObject toJSON() throws Throwable {
		JSONObject jobj = new JSONObject();
		JSONArray jarr = new JSONArray();
		
		jobj.put(CAMPAIGN, _campaignId);
		jobj.put(PROFILE, _profileId);
		for (FormBinding bind: _bindings) {
			JSONObject jbind = bind.toJSON(); 
			jarr.put(jbind);
		}
		jobj.put(BINDINGS, jarr);
		return jobj;
	}
	
	/**
	 * Return the XML representation of this object
	 * @return
	 * @throws Throwable
	 */
	public String toXML() throws Throwable {
		String xml = null;
		JSONObject obj = toJSON();
		xml = XML.toString(obj, CONFIGURATION);
		return xml;
	}
	
	public String getCampaignId() {
		return _campaignId;
	}
	public void setCampaignId(String campaignId) {
		_campaignId = campaignId;
	}
	public String getProfileId() {
		return _profileId;
	}
	public void setLeadFields(List<String> leadFields) {
		this._leadFields = leadFields;
		this._remainingLeadFields.addAll(_leadFields);
	}
	public List<String> getLeadFields() {
		return _leadFields;
	}
	public List<String> getProfileFields() {
		return _profileFields;
	}
	public void setProfileFields(List<String> profileFields) {
		this._profileFields = profileFields;
		this._remainingProfileFields.addAll(_profileFields);
	}
	public List<FormBinding> getBindings() {
		return _bindings;
	}
	protected void setBindings(List<FormBinding> bindings) {
		this._bindings = bindings;
	}
	public List<String> getRemainingLeadFields() {
		return _remainingLeadFields;
	}
	public List<String> getRemainingProfileFields() {
		return _remainingProfileFields;
	}
	public List<String> getRequiredProfileFields() {
		return _requiredProfileFields;
	}
	public void setRequiredProfileFields(List<String> requiredProfileFields) {
		this._requiredProfileFields = requiredProfileFields;
	}
	public String getProtectionType() {
		return _protectionType;
	}
	public void setProtectionType(String protectionType) {
		this._protectionType = protectionType;
	}
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}

	// data 
	private String _protectionType;
	private String _description;
	// remaining fields available for use
	private List<String> _remainingLeadFields = new ArrayList<String>();
	private List<String> _remainingProfileFields = new ArrayList<String>();
	// required fields (won't be saved)
	private List<String> _requiredProfileFields = new ArrayList<String>();
//	private List<String> requiredLeadsFields = new ArrayList<String>();
	
	// Backup of attribute lists
	private List<String> _leadFields = new ArrayList<String>();
	private List<String> _profileFields = new ArrayList<String>();
	
	// Field associations - those are persistent
	private List<FormBinding> _bindings = new ArrayList<FormBinding>();
	
	private String _campaignId;
	private String _profileId;
	
	public final static String CAMPAIGN = "campaignId";
	public final static String PROFILE = "profileId";
	public final static String BINDINGS = "bindings";
	public static final String CONFIGURATION = "form";
}
