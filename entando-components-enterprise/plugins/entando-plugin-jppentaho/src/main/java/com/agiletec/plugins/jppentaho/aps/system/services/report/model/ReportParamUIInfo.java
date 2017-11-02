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

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo for info on UI for report parameters
 * */
public class ReportParamUIInfo {
	
	public void setParam(List<ReportParameter> param) {
		this._param = param;
	}
	public List<ReportParameter> getParam() {
		return _param;
	}
	
	public void setOutputTypeLock(Boolean outputTypeLock) {
		this._outputTypeLock = outputTypeLock;
	}
	public Boolean getOutputTypeLock() {
		return _outputTypeLock;
	}

	public void setOutputType(String outputType) {
		this._outputType = outputType;
	}
	public String getOutputType() {
		return _outputType;
	}

	public void setReportTitle(String reportTitle) {
		this._reportTitle = reportTitle;
	}
	public String getReportTitle() {
		return _reportTitle;
	}


	private List<ReportParameter> _param = new ArrayList<ReportParameter>();
	private Boolean _outputTypeLock;
	private String _outputType;
	private String _reportTitle;

}
