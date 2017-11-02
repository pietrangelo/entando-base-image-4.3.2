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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;

public class ReportPreviewViewerConfigAction extends AbstractReportConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportPreviewViewerConfigAction.class);
	
	@Override
	public Boolean isFrontendOverrideParamEnabled() {
		if (this.getMainReport().booleanValue()) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	protected void createValuedShowlet() throws Exception {
		super.createValuedShowlet();
		List<String> downloadFormats = this.getDownloadFormatsDett();
		StringBuilder sbBuffer = new StringBuilder();
		if (null != downloadFormats && !downloadFormats.isEmpty()) {
			for (int i = 0; i < this.getDownloadFormatsDett().size(); i++) {
				if (i > 0) {
					sbBuffer.append(",");
				}
				sbBuffer.append(this.getDownloadFormatsDett().get(i));
			}			
			this.getWidget().getConfig().setProperty(WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS, sbBuffer.toString());
		}
	}
	
	@Override
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		ApsProperties showletConfig = this.getWidget().getConfig();
		if (null != showletConfig) {
			String downloadFormatsDett = showletConfig.getProperty(WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS);
			if (null != downloadFormatsDett) {
				this.setDownloadFormatsDett(Arrays.asList(downloadFormatsDett.split(",")));
			}
		}
		return result;
	}
	
	public String searchReport() {
		try {
			this.createValuedShowlet();
			if (!this.getMainReport().booleanValue() && StringUtils.isBlank(this.getUriString())) {
				this.addActionError(this.getText("error.mainreport.null"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in Error in searchReport", t);
			throw new RuntimeException("Error in searchReport", t);
		}
		return SUCCESS;
	}
	
	public String configReportUriString() {
		try {
			this.createValuedShowlet();
			if (StringUtils.isBlank(this.getUriString())) {
				this.addActionError(this.getText("error.report.required"));
				return INPUT;
			}
			if (!this.getMainReport().booleanValue() && StringUtils.isBlank(this.getUriStringDett())) {
				this.addActionError(this.getText("error.report.required"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in configReportUriString", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String entryReportConfig() {
		try {
			this.createValuedShowlet();
			if (StringUtils.isBlank(this.getUriString())) {
				this.addActionError(this.getText("error.main.report.required"));
				return INPUT;
			}
			if (!this.getMainReport() && StringUtils.isBlank(this.getUriStringDett())) {
				this.addActionError(this.getText("error.detail.report.required"));
				return INPUT;
			}
			this.prepareInputControlsMap();

			ApsProperties showletConfig = this.getWidget().getConfig();
			if (null != showletConfig) {
				String downloadFormatsDett = showletConfig.getProperty(WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS);
				if (null != downloadFormatsDett) {
					this.setDownloadFormatsDett(Arrays.asList(downloadFormatsDett.split(",")));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in entryReportConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String saveReportConfig() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			//inzio validazioni
			Map<String, InputControl> inputControlMap = this.getReportInputControlsConfigMap();

			boolean hasMandatoryFieldsNoOverride = this.checkMandatoryFieldsNoFrontEndOverride(inputControlMap, this.getFormInputControlConfigMap());
			boolean hasFormConfigErrors = this.checkFrontEndOverrides(inputControlMap, this.getFormInputControlConfigMap());
			boolean hasValidationErrors = this.checkInputControlsValidationErrors(inputControlMap);
			boolean hasTitleErrors = false;
			if (!this.getMainReport().booleanValue()) {
				hasTitleErrors = this.checkTitle();			
			}
			boolean errors = hasMandatoryFieldsNoOverride || hasFormConfigErrors || hasValidationErrors || hasTitleErrors;
			if (errors) {
				return INPUT;
			}
			//fine validazioni

			//costruzione parametri 

			Map<String, InputControlConfig> map = this.getFormInputControlConfigMap();
			//this.print("BEFORE_SAVE: ", map);

			String config = InputControlConfig.buildInputControlConfigFromMap(map);// this.buildInputControlConfigFromMap(this.getInputControlValues());
			if (!StringUtils.isBlank(config)) {
				if (this.getMainReport().booleanValue()) {					
					this.getWidget().getConfig().setProperty(WIDGET_PARAM_INPUT_CONTROLS, config);
				} else {
					this.getWidget().getConfig().setProperty(WIDGET_PARAM_DETAIL_INPUT_CONTROLS, config);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in saveReportConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String deleteDetailReportConfig() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			this.getWidget().getConfig().remove(WIDGET_PARAM_DETAIL_URI_STRING);
			this.getWidget().getConfig().remove(WIDGET_PARAM_DETAIL_INPUT_CONTROLS);
			this.getWidget().getConfig().remove(WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS);
			Iterator<Lang> it = this.getLangs().iterator();
			while (it.hasNext()) {
				Lang lang = it.next();
				this.getWidget().getConfig().remove(WIDGET_PARAM_DETAIL_TITLE + lang.getCode());	
			}
		} catch (Throwable t) {
			_logger.error("error in deleteDetailReportConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * @deprecated Use {@link #refreshWidget()} instead
	 */
	public String refreshShowlet() {
		return refreshWidget();
	}

	public String refreshWidget() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
		} catch (Throwable t) {
			_logger.error("error in refreshWidget", t);
			return FAILURE;
		}
		return SUCCESS;
	}


	@Override
	public String save() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();

			//////////////////////
			StringBuffer sbBuffer = new StringBuffer();
			List<String> downloadFormats = this.getDownloadFormatsDett();
			if (null != downloadFormats && !downloadFormats.isEmpty()) {
				for (int i = 0; i < this.getDownloadFormatsDett().size(); i++) {
					if (i > 0) {
						sbBuffer.append(",");
					}
					sbBuffer.append(this.getDownloadFormatsDett().get(i));
				}			
				this.getWidget().getConfig().setProperty(WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS, sbBuffer.toString());
			}

			//inizio validazioni
			boolean mainReportErrors = this.checkReportErrors(true);
			boolean detailReportErrors = false;
			if (!StringUtils.isBlank(this.getUriStringDett())) {
				detailReportErrors = this.checkReportErrors(false);
			}
			boolean errors = mainReportErrors || detailReportErrors;
			if (errors) {
				return INPUT;
			}
			//fine validazioni

			//salvataggio
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code = {}, pageCode ={}, frame = {}",this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}


	public boolean checkReportErrors(boolean mainReport) {
		this.setMainReport(mainReport);
		Map<String, InputControl> inputControlMap = this.getReportInputControlsConfigMap();
		boolean hasMandatoryFieldsNoOverride = this.checkMandatoryFieldsNoFrontEndOverride(inputControlMap, this.getFormInputControlConfigMap());
		boolean hasFormConfigErrors = this.checkFrontEndOverrides(inputControlMap, this.getFormInputControlConfigMap());
		boolean hasValidationErrors = this.checkInputControlsValidationErrors(inputControlMap);
		boolean hasTitleErrors = false;
		if (!this.getMainReport().booleanValue()) {
			hasTitleErrors = this.checkTitle();
		}
		boolean errors = hasMandatoryFieldsNoOverride || hasFormConfigErrors || hasValidationErrors || hasTitleErrors;
		return errors;
	}


	//ABSTRACT?
	public String refreshInputControls() {
		try {
			this.createValuedShowlet();
		} catch (Throwable t) {
			_logger.error("error in refreshInputControls", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/////////////////////

	/**
	 * Metodo di utlità per la costruzione del form nella jsp
	 * @return
	 */
	public Map<String, InputControl> getReportInputControlsConfigMap() {
		Map<String, InputControl> mapV2 = new HashMap<String, InputControl>();
		try {
			String uri = this.getUriString();
			if (null != this.getMainReport() && !this.getMainReport().booleanValue()) {
				uri = this.getUriStringDett();
			}
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String[][] arr = InputControlConfig.inputControlConfigToArray(this.getFormInputControlConfigMap());
			List<InputControl> list = this.getJasperServerManager().getInputControlsV2(cookieHeader, uri, arr);
			if (null != list) {
				for (int i = 0; i< list.size(); i++) {
					InputControl ic = list.get(i);
					mapV2.put(ic.getId(), ic);;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getReportInputControlsConfig for report uriString {}", this.getUriString(), t);
			throw new RuntimeException("error in getReportInputControlsConfig for report uriString " + this.getUriString(), t);
		}
		return mapV2;
	}

	protected boolean checkTitle() {
		boolean error = false;
		String titleParamPrefix = WIDGET_PARAM_DETAIL_TITLE ;
		//if (this.isMultilanguageParamValued(titleParamPrefix)) {
		Lang defaultLang = this.getLangManager().getDefaultLang();
		String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
		String defaultTitle = this.getWidget().getConfig().getProperty(defaultTitleParam);
		if (defaultTitle == null || defaultTitle.length() == 0) {
			String[] args = {defaultLang.getDescr()};
			this.addFieldError(defaultTitleParam, this.getText("error.detailReport.defaultLangTitle.required", args));
			error = true;
		}
		//}
		return error;
	}


	//	/**
	//	 * Un input control è visibile se 
	//	 * 	- NON ha master dependencies
	//	 * 	- TUTTE le master dependencies sono settate nella showlet
	//	 * @param inputControl
	//	 * @return
	//	 */
	//	public boolean isControlVisible(InputControl inputControl, String report) {
	//		boolean visible = false;
	//		try {
	//			
	//			Map<String, InputControlConfig> target = null;
	//			if (report.equalsIgnoreCase("main")) {
	//				target = this.getFormInputControlConfigMap();
	//			} else if (report.equalsIgnoreCase("dett")) {
	//				target = this.getDettFormInputControlConfigMap();
	//			}
	//			
	//			if (!inputControl.isVisible()) {
	//				ApsSystemUtils.getLogger().info("The inputControl " + inputControl.getId() + " has visible=false");
	//				System.out.println("The inputControl " + inputControl.getId() + " has visible=false");
	//				return visible;
	//			}
	//			List<String> masterDependencies = inputControl.getMasterDependencies();
	//			if (null == masterDependencies || masterDependencies.isEmpty()) {
	//				visible = true;
	//			} else {
	//				visible = target.keySet().containsAll(masterDependencies);
	//			}
	//		} catch (Throwable t) {
	//			ApsSystemUtils.logThrowable(t, this, "isControlVisible");
	//			throw new RuntimeException("error in check isControlVisible for input control " + inputControl.getId(), t);
	//		}
	//		return visible;
	//	}


	//	public Map<String, InputControlConfig> getFormInputControlConfigMap() {
	//		return _formInputControlConfigMap;
	//	}
	//	public void setFormInputControlConfigMap(Map<String, InputControlConfig> formInputControlConfigMap) {
	//		this._formInputControlConfigMap = formInputControlConfigMap;
	//	}

	public String getUriString() {
		return _uriString;
	}
	public void setUriString(String uriString) {
		this._uriString = uriString;
	}

	public Boolean getMainReport() {
		return _mainReport;
	}
	public void setMainReport(Boolean mainReport) {
		this._mainReport = mainReport;
	}

	public String getUriStringDett() {
		return _uriStringDett;
	}
	public void setUriStringDett(String uriStringDett) {
		this._uriStringDett = uriStringDett;
	}

	public String getInputControls() {
		return _inputControls;
	}
	public void setInputControls(String inputControls) {
		this._inputControls = inputControls;
	}

	public String getInputControlsDett() {
		return _inputControlsDett;
	}
	public void setInputControlsDett(String inputControlsDett) {
		this._inputControlsDett = inputControlsDett;
	}

	public List<String> getDownloadFormatsDett() {
		return _downloadFormatsDett;
	}
	public void setDownloadFormatsDett(List<String> downloadFormatsDett) {
		this._downloadFormatsDett = downloadFormatsDett;
	}

	private Boolean _mainReport;
	private String _uriString;
	private String _uriStringDett;
	private String _inputControls;
	private String _inputControlsDett;

	private List<String> _downloadFormatsDett;
	//private Map<String, InputControlConfig> _formInputControlConfigMap = new HashMap<String, InputControlConfig>();
}
