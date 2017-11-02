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
package org.entando.entando.plugins.jpjasper.aps.services.model;

import java.util.Date;
import java.util.List;

/**
 * @author rinaldo
 */
public class JasperReportModel implements java.io.Serializable {

	private String name;
	private String wsType;
	private String uriString;

	//private boolean new;
	
	private String label;
	private String description;
	private Date creationDate;

	
	
	
	//private String resourcetype;
	
	
	private String parentFolder;
	
	//private String version;
	//private String securityPermissionmash;
	//private String hasdata;
	
	
	private String reportLocale;
	private List<JasperReportParameterModel> reportParameters;
	private List<String> reportLocales;

	public JasperReportModel() {
	}

	public JasperReportModel(
			String wsType,
			String uriString,
			String name,
			String label,
			String description,
			String parentFolder,
			String reportLocale,
			java.util.List<JasperReportParameterModel> reportParameters,
			java.util.List<String> reportLocales,
			java.util.Date creationDate) {
		this.wsType = wsType;
		this.uriString = uriString;
		this.name = name;
		this.label = label;
		this.description = description;
		this.parentFolder = parentFolder;
		this.reportLocale = reportLocale;
		this.reportParameters = reportParameters;
		this.reportLocales = reportLocales;
		this.creationDate = creationDate;
	}

	public java.util.List<JasperReportParameterModel> getReportParameters() {
		return this.reportParameters;
	}

	public void setReportParameters(java.util.List<JasperReportParameterModel> reportParameters) {
		this.reportParameters = reportParameters;
	}

	public java.util.List<String> getReportLocales() {
		return this.reportLocales;
	}

	public void setReportLocales(java.util.List<String> reportLocales) {
		this.reportLocales = reportLocales;
	}

	public String getUriString() {
		return this.uriString;
	}

	public void setUriString(String uriString) {
		this.uriString = uriString;
	}

	public String getWsType() {
		return this.wsType;
	}

	public void setWsType(String wsType) {
		this.wsType = wsType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentFolder() {
		return this.parentFolder;
	}

	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}

	public java.util.Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getReportLocale() {
		return this.reportLocale;
	}

	public void setReportLocale(String reportLocale) {
		this.reportLocale = reportLocale;
	}
}
