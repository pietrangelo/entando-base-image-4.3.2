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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Generic description of a user (Lead, Account)
 * @author entando
 */
public class AbstractSalesforceItem {

	public AbstractSalesforceItem() { }
	
	/**
	 * Currently only Id and the attributes must be always present 
	 * @param json
	 * @throws Throwable 
	 */
	public AbstractSalesforceItem(JSONObject json) throws Throwable {
		setJson(json);
		// check whether to parse a description or an existing lead
		if (json.has(FIELDS)) {
			// parse a description
			JSONArray fields = json.getJSONArray(FIELDS);
			parseDescription(json);
		} else {
			// parse actual object as returned from API service
			parse(json);
		}

	}
	
	/**
	 * Parse an actual Lead object
	 * @param json
	 * @throws Throwable
	 */
	public void parse(JSONObject json) throws Throwable {
		if (json.has(NAME)) {
			_name = json.getString(NAME);
		}
		_id = json.getString(ID);
		JSONObject jattr = json.getJSONObject(ATTRIBUTES);
		_type = jattr.getString(TYPE);
		_url = jattr.getString(URL);
		Iterator itr = json.keys();
		// create a list of the attributes
		while (itr.hasNext()) {
			String key = (String)itr.next();

			_attributes.add(key);
		}
	
	}
	
	/**
	 * Parse the description. We only update the attributes list and the type
	 * @param json
	 * @throws Throwable
	 */
	protected void parseDescription(JSONObject json) throws Throwable {
		String name = json.getString(NAME_FIELD);
		_type = name;
		
		JSONArray jar = json.getJSONArray(FIELDS);
		for (int i = 0; i < jar.length(); i++) {
			JSONObject jobj = (JSONObject) jar.get(i);
			String key = jobj.getString(NAME_FIELD);
			Boolean updateable = jobj.getBoolean(UPDEATABLE);
			// check whether the attribute 
			if (updateable) {
				_attributes.add(key);
//				System.out.println("Including updeatable field " + key);
			} else {
//				System.out.println("Discarding not updeatable field " + key + "  " + json.get(UPDEATABLE));
			}
		}

	}
	
	/**
	 * Add an attribute to the JSON
	 * @param key
	 * @param value
	 * @throws Throwable
	 */
	public void addAttribute(String key, Object value) {
		if (null == this._json) {
			this._json = new JSONObject();
		}
		this._json.put(key, value);
//		if (!_attributes.contains(key)) {
		this._attributes.add(key);
//		}
	}
	
	/**
	 * Remove the given attribute
	 * @param key
	 */
	public void removeAttribute(String key) {

		if (StringUtils.isNotBlank(key)) {
			// remove from JSON
			if (null != _json
					&& _json.has(key)) {
				_json.put(key, JSONObject.NULL);
			}
			// remove from attributes
			if (null != _attributes
					&& _attributes.contains(key)) {
				_attributes.remove(key);
			}
		}
	}
	
	/**
	 * Get an attribute from the JSON representation
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return _json.get(key);
	}
	
	/**
	 * Check whether this object is valid
	 * @return
	 */
	public boolean isValid() {
		return (StringUtils.isNotBlank(_id)
				&& StringUtils.isNotBlank(_name)
				&& StringUtils.isNotBlank(_type)
				&& StringUtils.isNotBlank(_url));
	}

	public String getType() {
		return _type;
	}

	protected void setType(String type) {
		_type = type;
	}

	public String getName() {
		return _name;
	}

	protected void setName(String name) {
		this._name = name;
	}

	public String getId() {
		return _id;
	}

	protected void setId(String id) {
		this._id = id;
	}

	public String getUrl() {
		return _url;
	}

	protected void setUrl(String url) {
		this._url = url;
	}
	
	public JSONObject getJSON() {
		return _json;
	}

	protected void setJson(JSONObject json) {
		this._json = json;
	}
	
	public List<String> getAttributes() {
		return _attributes;
	}

	protected void setAttributes(List<String> attributes) {
		this._attributes = attributes;
	}

	private String _name;
	private String _id;
	private String _url;
	private String _type;
	private JSONObject _json;
	private List<String> _attributes = new ArrayList<String>();
	
	public static final String ATTRIBUTES = "attributes";
	public static final String NAME = "Name";
	public static final String ID = "Id";
	public static final String TYPE = "type";
	public static final String URL = "url";
	public static final String FIELDS = "fields";
	public static final String NAME_FIELD = "name";
	
	public static final String UPDEATABLE = "updateable";
}
