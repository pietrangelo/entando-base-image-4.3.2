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
package org.entando.entando.plugins.jpjasper.aps.internalservlet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;

public class JasperDownloadAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperDownloadAction.class);
	
	public List<String> getFormats() {
		List<String> formats = new ArrayList<String>();
		if (!StringUtils.isBlank(this.getShowletFormats())) {
			String[] arrayFormats = this.getShowletFormats().split(",");
			formats = Arrays.asList(arrayFormats);
		}
		return formats;
	}

	public String downloadReport() {
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			JasperResourceDescriptor res = this.getJasperServerManager().getRestResource(cookieHeader, this.getUriString());
			this.setFileName(res.getName() + "." + this.getFormat());
			this.setContentType(this.getJasperServerManager().getDownloadModel(this.getFormat()).getMimeType());

			InputStream is = this.getJasperServerManager().runReportRestV2(cookieHeader, this.getUriString(), this.getFormat(), this.getShowletInputControlValues());
			this.setInputStream(is);
		} catch (Throwable t) {
			_logger.error("error in download report. Uri String: {}, format: {}", this.getUriString(), this.getFormat(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getHtml() {
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String format = "html";
			String imageUri = JpJasperApsadminUtils.getReportImageUriV2(this.getConfigService());
			//get report html and parse image src to apply entando image uri
			InputStream is = this.getJasperServerManager().runReportRestV2(cookieHeader, this.getUriString(), format, this.getShowletInputControlValues());
			String html = IOUtils.toString(is, "UTF-8");
			
			String baseUrl = this.getJasperServerManager().getJasperBaseUrl();
			html = JpJasperApsadminUtils.updateJSUri(html, baseUrl);
			html = JpJasperApsadminUtils.updateImageUri(html, imageUri);
			
			is = IOUtils.toInputStream(html);
			//
			String contentType = "text/html";
			if (!StringUtils.isBlank(contentType)) {
				this.setContentType(contentType);
			}
			this.setInputStream(is);
		} catch (Throwable t) {
			_logger.error("Error in download report. uriString: {}, format: {}", this.getUriString(), this.getFormat(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getImageV2() {
		String path = this.getRequest().getParameter("path");
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			InputStream i  = this.getJasperServerManager().getReportImageV2(cookieHeader, path);
			this.setInputStream2(i);
		} catch (Throwable t) {
			_logger.error("errog loading image with path {}", path, t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/*
	public String getHtml() {
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String format = "html";
			String reportFile = "report";

			String imageUri = null;


			//ESEGUO IL REPORT IN PUT PER AVERE L'UUID
			Integer page = null;
//			JasperRunReportResponse reportResponse = this.getJasperServerManager().runReportRest(cookieHeader, this.getUriString(), format, imageUri, page, null, this.getShowletInputControlValues());
//			
//
//			String applicationURL = this.getConfigService().getParam(SystemConstants.PAR_APPL_BASE_URL);
//			if (!applicationURL.endsWith("/"))applicationURL = applicationURL + "/";
//			
//			//UTILIZZO UUID PER IL PARAMETRO IMAGE_URI
//			imageUri = applicationURL + "do/jpjasper/FrontEnd/Report/Download/getReportImage?uuid=" + reportResponse.getUuid() + "::reportFile=";
//			
//			
//			//RIGENERO IL FILE
//			reportResponse = ((JasperServerManager)this.getJasperServerManager()).regenerateReportByUUIDAsStream(cookieHeader, reportResponse.getUuid(), reportFile, imageUri, format, this.getShowletInputControlValues() );
//			
			System.out.println("ACTION: " + this.getUriString());
			InputStream is = ((JasperServerManager) this.getJasperServerManager()).getHtml(reportFile, cookieHeader, this.getUriString(), format, imageUri, page, this.getShowletInputControlValues());

//			String contentType = reportResponse.getFileContentType(reportFile);
//			if (!StringUtils.isBlank(contentType)) {
//				this.setContentType(contentType);
//			}
			this.setInputStream(is);


		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "downloadReport", "Errore download report. uriString: " + this.getUriString() + " format: " + this.getFormat());
			return FAILURE;
		}
		return SUCCESS;
	}*/
	
	/*
	public String getImage() {
		try {
			//String format = "html";
			String uuid = this.getUuid().split("::reportFile")[0];
			String reportFile = this.getUuid().split("::reportFile=")[1];
			this.setInputStream2(this.loadImage(this.getUuid()));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "downloadReport", "Errore download report. uriString: " + this.getUriString() + " format: " + this.getFormat());
			return FAILURE;
		}
		return SUCCESS;
	}

	public InputStream loadImage(String xxxxxxx) {
		InputStream i = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String uuid = xxxxxxx.split("::reportFile")[0];
			String reportFile = xxxxxxx.split("::reportFile=")[1];

			i  = this.getJasperServerManager().getReportFileAsStream(cookieHeader, uuid, reportFile);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "downloadReport", "Errore download report. uriString: " + this.getUriString() + " format: " + this.getFormat());
			//return FAILURE;
		}
		return i;
	}


*/


	public String getUriString() {
		return _uriString;
	}

	public void setUriString(String uriString) {
		this._uriString = uriString;
	}

	public String getFormat() {
		return _format;
	}
	public void setFormat(String format) {
		this._format = format;
	}

	public InputStream getInputStream() {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}

	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	public String getShowletFormats() {
		return _showletFormats;
	}
	public void setShowletFormats(String showletFormats) {
		this._showletFormats = showletFormats;
	}

	public String getContentType() {
		return _contentType;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
	}

	public String getUuid() {
		return _uuid;
	}
	public void setUuid(String uuid) {
		this._uuid = uuid;
	}

	public InputStream getInputStream2() {
		return _inputStream2;
	}

	public void setInputStream2(InputStream inputStream2) {
		this._inputStream2 = inputStream2;
	}

	protected ConfigInterface getConfigService() {
		return _configService;
	}
	public void setConfigService(ConfigInterface configService) {
		this._configService = configService;
	}

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	public String getShowletInputControlValues() {
		return _showletInputControlValues;
	}

	public void setShowletInputControlValues(String showletInputControlValues) {
		this._showletInputControlValues = showletInputControlValues;
	}

	private String _uriString;
	private String _showletFormats;
	private String _showletInputControlValues;


	private String _format;
	private InputStream _inputStream;
	private InputStream _inputStream2;
	private String _fileName;
	private String _contentType;
	private String _uuid;

	private IJasperServerManager _jasperServerManager;
	private ConfigInterface _configService;// = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.pageContext);
}
