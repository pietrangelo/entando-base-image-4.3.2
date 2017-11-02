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

import java.io.Serializable;

public class ReportDownload implements Serializable {
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Path: ");
		buffer.append(this.getReportPath());
		buffer.append("File: ");
		buffer.append(this.getFileName());
		buffer.append(" Validation Ok: ");
		buffer.append(isValidationKO());
		return buffer.toString();
	}
	
	public void setReportPath(String reportPath) {
		this._reportPath = reportPath;
	}
	public String getReportPath() {
		return _reportPath;
	}
	
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}
	public String getFileName() {
		return _fileName;
	}
	
	public void setValidationKO(boolean isValidationKO) {
		this._isValidationKO = isValidationKO;
	}
	public boolean isValidationKO() {
		return _isValidationKO;
	}
        
	public String getErrorCode() {
		return _errorCode;
	}
	public void setErrorCode(String errorCode) {
		this._errorCode = errorCode;
	}

	private String _reportPath;
	private String _fileName;
	
	private String _errorCode;
	private boolean _isValidationKO;
	
	
}
