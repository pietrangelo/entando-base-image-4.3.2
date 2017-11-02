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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;

public class JasperConfig {
	
	public String getBaseURL() {
		return _baseURL;
	}
	public void setBaseURL(String baseURL) {
		this._baseURL = baseURL;
	}

	public String getReportViewerPageCode() {
		return _reportViewerPageCode;
	}
	public void setReportViewerPageCode(String reportViewerPageCode) {
		this._reportViewerPageCode = reportViewerPageCode;
	}

	private String _baseURL;
	private String _reportViewerPageCode;
}
