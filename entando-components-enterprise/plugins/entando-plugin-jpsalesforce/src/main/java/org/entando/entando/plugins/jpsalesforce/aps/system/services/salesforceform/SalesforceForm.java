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
*/package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;


/**
 * This class describes a configuration item as found in the table jpsalesforce_salesforceform
 * @author entando
 */
public class SalesforceForm {
	
	public SalesforceForm() {};
	
	public SalesforceForm(String pageCode, int frame, FormConfiguration cfg) {
		_frame = frame;
		_pagecode = pageCode;
		_config = cfg;
		_id = -1;
		_code = null;
	}
	
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	public String getPagecode() {
		return _pagecode;
	}
	public void setPagecode(String pagecode) {
		this._pagecode = pagecode;
	}

	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}
	
	public FormConfiguration getConfig() {
		return _config;
	}
	public void setConfig(FormConfiguration config) {
		this._config = config;
	}

	private int _id;
	private String _code;
	private String _pagecode;
	private int _frame;
	private FormConfiguration _config;

}
