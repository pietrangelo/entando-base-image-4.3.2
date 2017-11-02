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

public class PentahoReportUserWidgetConfig {

	public String getPagecode() {
		return _pagecode;
	}
	public void setPagecode(String pagecode) {
		this._pagecode = pagecode;
	}
	
	public Integer getFramepos() {
		return _framepos;
	}
	public void setFramepos(Integer framepos) {
		this._framepos = framepos;
	}
	
	/**
	 * @deprecated Use {@link #getWidgetcode()} instead
	 */
	public String getShowletcode() {
		return getWidgetcode();
	}
	public String getWidgetcode() {
		return _widgetcode;
	}
	
	/**
	 * @deprecated Use {@link #setWidgetcode(String)} instead
	 */
	public void setShowletcode(String widgetcode) {
		setWidgetcode(widgetcode);
	}
	
	public void setWidgetcode(String widgetcode) {
		this._widgetcode = widgetcode;
	}
	
	public String getConfig() {
		return _config;
	}
	public void setConfig(String config) {
		this._config = config;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setName(String name) {
		this._name = name;
	}
	public String getName() {
		return _name;
	}

	private String _username;
	private String _pagecode;
	private Integer _framepos;
	private String _widgetcode;
	private String _config;
	private String _name;

}
