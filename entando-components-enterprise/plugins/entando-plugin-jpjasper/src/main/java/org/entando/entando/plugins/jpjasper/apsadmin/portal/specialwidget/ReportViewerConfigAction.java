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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;

public class ReportViewerConfigAction extends AbstractReportConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportViewerConfigAction.class);

	public Boolean getMainReport() {
		return true;
	}

	public String configReportUriString() {
		try {
			if (StringUtils.isBlank(this.getUriString())) {
				this.addActionError(this.getText("error.report.required"));
				return INPUT;
			}
			this.createValuedShowlet();
			this.getWidget().getConfig().setProperty(WIDGET_PARAM_URI_STRING, this.getUriString());
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
				this.addActionError(this.getText("error.report.required"));
				return INPUT;
			}
			this.prepareInputControlsMap();
		} catch (Throwable t) {
			_logger.error("error in entryReportConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	protected void createValuedShowlet() throws Exception {
		super.createValuedShowlet();
		List<String> downloadFormats = this.getDownloadFormats();
		StringBuffer sbBuffer = new StringBuffer();
		if (null != downloadFormats && !downloadFormats.isEmpty()) {
			for (int i = 0; i < this.getDownloadFormats().size(); i++) {
				if (i > 0) {
					sbBuffer.append(",");
				}
				sbBuffer.append(this.getDownloadFormats().get(i));
			}			
			this.getWidget().getConfig().setProperty(WIDGET_PARAM_DOWNLOAD_FORMATS, sbBuffer.toString());
		}
	}

	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		ApsProperties showletConfig = this.getWidget().getConfig();
		if (null != showletConfig) {
			String downloadFormats = showletConfig.getProperty(WIDGET_PARAM_DOWNLOAD_FORMATS);
			if (null != downloadFormats) {
				this.setDownloadFormats(Arrays.asList(downloadFormats.split(",")));
			}
		}
		return result;
	}

	@Override
	public String save() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();

			//////////////////////
			StringBuffer sbBuffer = new StringBuffer();
			List<String> downloadFormats = this.getDownloadFormats();
			if (null != downloadFormats && !downloadFormats.isEmpty()) {
				for (int i = 0; i < this.getDownloadFormats().size(); i++) {
					if (i > 0) {
						sbBuffer.append(",");
					}
					sbBuffer.append(this.getDownloadFormats().get(i));
				}			
				this.getWidget().getConfig().setProperty(WIDGET_PARAM_DOWNLOAD_FORMATS, sbBuffer.toString());
			}

			//UriString è null per salvataggio shwlet pubblicazione volante
			if (null != this.getUriString()) {
				//inzio validazioni
				Map<String, InputControl> inputControlMap = this.getReportInputControlsConfigMap();;

				boolean hasMandatoryFieldsNoOverride = this.checkMandatoryFieldsNoFrontEndOverride(inputControlMap, this.getFormInputControlConfigMap());
				boolean hasFormConfigErrors = this.checkFrontEndOverrides(inputControlMap, this.getFormInputControlConfigMap());
				boolean hasValidationErrors = this.checkInputControlsValidationErrors(inputControlMap);
				boolean hasTitleError = this.checkTitle();
				boolean errors = hasMandatoryFieldsNoOverride || hasFormConfigErrors || hasValidationErrors || hasTitleError;
				if (errors) {
					return INPUT;
				}
				//fine validazioni

				//costruzione parametri 

				Map<String, InputControlConfig> map = this.getFormInputControlConfigMap();
				//this.print("BEFORE_SAVE: ", map);

				String config = InputControlConfig.buildInputControlConfigFromMap(map);// this.buildInputControlConfigFromMap(this.getInputControlValues());
				if (!StringUtils.isBlank(config)) {
					this.getWidget().getConfig().setProperty(WIDGET_PARAM_INPUT_CONTROLS, config);
				}
			}

			//salvataggio

			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code = {}, pageCode = {}, frame = {}", this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}

	protected boolean checkTitle() {
		boolean error = false;
		String titleParamPrefix = WIDGET_PARAM_TITLE ;
		//if (this.isMultilanguageParamValued(titleParamPrefix)) {
		Lang defaultLang = this.getLangManager().getDefaultLang();
		String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
		String defaultTitle = this.getWidget().getConfig().getProperty(defaultTitleParam);
		if (defaultTitle == null || defaultTitle.trim().length() == 0) {
			String[] args = {defaultLang.getDescr()};
			this.addFieldError(defaultTitleParam, this.getText("error.report.defaultLangTitle.required", args));
			error = true;
		}
		//}
		return error;
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

	/**
	 * Metodo di utlità per la costruzione del form nella jsp
	 * @return
	 */
	public Map<String, InputControl> getReportInputControlsConfigMap() {
		Map<String, InputControl> mapV2 = new HashMap<String, InputControl>();
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String[][] arr =InputControlConfig.inputControlConfigToArray(this.getFormInputControlConfigMap());
			List<InputControl> list = this.getJasperServerManager().getInputControlsV2(cookieHeader, this.getUriString(), arr);
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


	//	/**
	//	 * Un input control è visibile se 
	//	 * 	- NON ha master dependencies
	//	 * 	- TUTTE le master dependencies sono settate nella showlet
	//	 * @param inputControl
	//	 * @return
	//	 */
	//	public boolean isControlVisible(InputControl inputControl) {
	//		boolean visible = false;
	//		try {
	//			if (!inputControl.isVisible()) {
	//				ApsSystemUtils.getLogger().info("The inputControl " + inputControl.getId() + " has visible=false");
	//				System.out.println("The inputControl " + inputControl.getId() + " has visible=false");
	//				return visible;
	//			}
	//			List<String> masterDependencies = inputControl.getMasterDependencies();
	//			if (null == masterDependencies || masterDependencies.isEmpty()) {
	//				visible = true;
	//			} else {
	//				visible = this.getFormInputControlConfigMap().keySet().containsAll(masterDependencies);
	//			}
	//		} catch (Throwable t) {
	//			ApsSystemUtils.logThrowable(t, this, "isControlVisible");
	//			throw new RuntimeException("error in check isControlVisible for input control " + inputControl.getId(), t);
	//		}
	//		return visible;
	//	}


	public String searchReport() {
		try {
			this.createValuedShowlet();
		} catch (Throwable t) {
			_logger.error("Error in searchReport", t);
			throw new RuntimeException("Error in searchReport", t);
		}
		return SUCCESS;
	}

	public String getUriString() {
		return _uriString;
	}
	public void setUriString(String uriString) {
		this._uriString = uriString;
	}

	public List<String> getDownloadFormats() {
		return _downloadFormats;
	}
	public void setDownloadFormats(List<String> downloadFormats) {
		this._downloadFormats = downloadFormats;
	}

	private String _uriString;
	private List<String> _downloadFormats;
}
