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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;


/**
 * Oggetto in risposta ad una chiamata di esecuzione report 
 * http://<host>:<port>/jasperserver[-pro]/rest/report/path/to/report
 * @author spuddu
 *
 */
public class JasperRunReportResponse {

	/*
<report>
<uuid>d7bf6c9-9077-41f7-a2d4-8682e74b637e</uuid>
<originalUri>/reports/samples/AllAccounts</originalUri>
<totalPages>43</totalPages>
<startPage>1</startPage>
<endPage>43</endPage>
<file type="image/png">img_0_0_0</file>
<file type="image/gif">px</file>
<file type="text/html">report</file>
<file type="image/jpeg">img_0_42_27</file>
<file type="image/png">img_0_42_26</file>
</report>
	 * */
	
	
	

	public String getReport() {
		return _report;
	}
	public void setReport(String report) {
		this._report = report;
	}
	public String getUuid() {
		return _uuid;
	}
	public void setUuid(String uuid) {
		this._uuid = uuid;
	}
	public String getOriginalUri() {
		return _originalUri;
	}
	public void setOriginalUri(String originalUri) {
		this._originalUri = originalUri;
	}
	public String getTotalPages() {
		return _totalPages;
	}
	public void setTotalPages(String totalPages) {
		this._totalPages = totalPages;
	}
	public String getStartPage() {
		return _startPage;
	}
	public void setStartPage(String startPage) {
		this._startPage = startPage;
	}
	public String getEndPage() {
		return _endPage;
	}
	public void setEndPage(String endPage) {
		this._endPage = endPage;
	}

	
	public List<JasperRunReportFileElement> getFiles() {
		return _files;
	}
	public void setFiles(List<JasperRunReportFileElement> files) {
		this._files = files;
	}
	
	public String getFileContentType(String file) {
		String contentType = null;
		if (null != this.getFiles()) {
			for (int i = 0; i< this.getFiles().size(); i++) {
				JasperRunReportFileElement f = this.getFiles().get(i);
				if (f.getName().equals(file)) {
					contentType = f.getType();
					break;
				}
			}
		}
		return contentType;
	}


//	/**
//	 * Non fa parte della response xml. 
//	 * 
//	 * @return
//	 */
//	public HttpClient getClient() {
//		return _client;
//	}
//	public void setClient(HttpClient client) {
//		this._client = client;
//	}


	/**
	 * Determina la dir di salvataggio delle immagini
	 * Non fa parte della response xml. 
	 * @return
	 */
	public String getImagesUri() {
		return _imagesUri;
	}
	public void setImagesUri(String imagesUri) {
		this._imagesUri = imagesUri;
	}


	/**
	 * determina l'estensione
	 * Non fa parte della response xml. 
	 * @return
	 */
	public String getReportFormat() {
		return _reportFormat;
	}
	public void setReportFormat(String reportFormat) {
		this._reportFormat = reportFormat;
	}


	private String _reportFormat;
	private String _imagesUri;
	//private HttpClient _client;
	private String _report;
	private String _uuid;
	private String _originalUri;
	private String _totalPages;
	private String _startPage;
	private String _endPage;
	private List<JasperRunReportFileElement> _files = new ArrayList<JasperRunReportFileElement>();
	
}
