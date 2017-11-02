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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.DownloadModel;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControlState;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public abstract class AbstractReportConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractReportConfigAction.class);

	public abstract Map<String, InputControl> getReportInputControlsConfigMap();
	
	public abstract Boolean getMainReport();

	public Boolean isFrontendOverrideParamEnabled() {
		return true;
	}

	public Map<String, DownloadModel> getReportDownloadFormats() {
		return this.getJasperServerManager().getDownloadModelsMap();
	}

	public List<JasperResourceDescriptor> getReports() {
		List<JasperResourceDescriptor> resourceDescriptors = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			resourceDescriptors = this.getJasperServerManager().searchRestResources(cookieHeader, this.getDescr(), WsTypeParams.REPORT_UNIT, null, true);
		} catch (Throwable t) {
			_logger.error("error in getReports", t);
			throw new RuntimeException("error in getReports", t);
		}
		return resourceDescriptors;
	}

	public JasperResourceDescriptor getReport(String uriString) {
		JasperResourceDescriptor resourceDescriptor = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			resourceDescriptor = this.getJasperServerManager().getRestResource(cookieHeader, uriString);
		} catch (Throwable t) {
			_logger.error("error in getReport with uriString {}", uriString, t);
			throw new RuntimeException("error in getReport with uriString " + uriString, t);
		}
		return resourceDescriptor;
	}

	public void print(String title, Map<String, InputControlConfig> map) {
		Iterator<Map.Entry<String, InputControlConfig>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig icc = entry.getValue();
			StringBuffer sbBuffer = new StringBuffer();
			sbBuffer.append("id: ").append(icc.getId()).append(" ");
			sbBuffer.append("OVERRIDE: ").append(icc.getFrontEndOverride()).append(" ");
			sbBuffer.append("DT: ").append(icc.getDataType()).append(" ");
			sbBuffer.append("  value: ");
			for (int i = 0 ; i < icc.getValue().size(); i++) {
				if (i > 0) sbBuffer.append(",");
				sbBuffer.append(icc.getValue().get(i));

			}
			System.out.println(title + ": " + sbBuffer.toString());
		}
	}

	protected boolean checkInputControlsValidationErrors(Map<String, InputControl> inputControlMap) {
		boolean hasErrors = false;

		//Collection<InputControl> list = this.getReportInputControlsConfigMap().values();
		Collection<InputControl> list = inputControlMap.values();
		if (null != list) {
			Iterator<InputControl> it = list.iterator();
			while (it.hasNext()) {
				InputControl ic = it.next();
				InputControlState state = ic.getState();
				if (null != state && !StringUtils.isBlank(state.getError())) {
					hasErrors = true;
					List<String> args = new ArrayList<String>();
					args.add(ic.getId());
					args.add(state.getError());
					this.addActionError(this.getText("error.report.inputcontrol.errors.present", args));
				}
			}
		}
		return hasErrors;
	}

	/**
	 * Raise an error when a field is mandatory, it's blank and the end user can't edit it (isFrontEndOveddide==false)
	 * @param inputControlMap
	 * @return
	 */
	protected boolean checkMandatoryFieldsNoFrontEndOverride(Map<String, InputControl> inputControlMap,  Map<String, InputControlConfig> formInputControlConfigMap) {
		boolean hasErrors = false;
		Iterator<Map.Entry<String, InputControlConfig>> entries = formInputControlConfigMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig current = entry.getValue();
			boolean isFrontEndOveddide = current.isFrontEndOverride();
			if (!isFrontEndOveddide) {
				InputControl currentInputControl = inputControlMap.get(current.getId());
				if (currentInputControl.isMandatory()) {
					if (null == current.getValue() || current.getValue().isEmpty()) {
						hasErrors = true;
						List<String> args = new ArrayList<String>();
						args.add(current.getId());
						this.addActionError(this.getText("error.frontendOverride.currentFalse.mandatoryValueEmpty", args));
						break;
					}
				}
			}
		}
		return hasErrors;
	}

	/**
	 * Raise an error when a slave field can not be changed by endUsers, while his main field can
	 * @param inputControlMap
	 * @return
	 */
	protected boolean checkFrontEndOverrides(Map<String, InputControl> inputControlMap, Map<String, InputControlConfig> formInputControlConfigMap) {
		boolean hasErrors = false;
		Iterator<Map.Entry<String, InputControlConfig>> entries = formInputControlConfigMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig current = entry.getValue();
			boolean isFrontEndOveddide = current.isFrontEndOverride();
			if (!isFrontEndOveddide) {
				InputControl currentInputControl = inputControlMap.get(current.getId());
				if (null != currentInputControl.getMasterDependencies() && !currentInputControl.getMasterDependencies().isEmpty()) {
					//
					for (int i = 0; i < currentInputControl.getMasterDependencies().size(); i++) {
						String id = currentInputControl.getMasterDependencies().get(i);
						InputControlConfig masterInputControlConfig = formInputControlConfigMap.get(id);
						if (masterInputControlConfig.isFrontEndOverride()) {
							hasErrors = true;
							List<String> args = new ArrayList<String>();
							args.add(current.getId());
							args.add(masterInputControlConfig.getId());
							this.addActionError(this.getText("error.frontendOverride.currentFalse.masterTrue", args));
							break;
						}
					}
				}

			}
		}
		return hasErrors;
	}

	/**
	 * Se la showlet non ha settata la configurazione degli input controls, valorizza la mappa con i valori di default che arrivano dal server
	 * Se la showlet presenta invece una configurazione, questa vuene intrpretata per valorizzare la mappa degli inputControls
	 */
	protected void prepareInputControlsMap() {
		String configParam = null;
		Map<String, InputControlConfig> controlsMap = null;

		if (this.getMainReport().booleanValue()) {
			configParam = WIDGET_PARAM_INPUT_CONTROLS;
		} else {
			configParam = WIDGET_PARAM_DETAIL_INPUT_CONTROLS;
		}
		String inputControlsConfig = this.getWidget().getConfig().getProperty(configParam);

		if (StringUtils.isBlank(inputControlsConfig)) {
			Map<String, InputControl> inputControls = this.getReportInputControlsConfigMap();
			List<InputControl> listIc = new ArrayList<InputControl>();
			listIc.addAll(inputControls.values());
			controlsMap  = InputControlConfig.buildInputControlMapFromApi(listIc, this.getFormInputControlConfigMap());
		} else {
			controlsMap = InputControlConfig.buildInputControlMapFromConfig(inputControlsConfig);
		}

		this.setFormInputControlConfigMap(controlsMap);
	}
	
//	protected boolean isMultilanguageParamValued(String prefix) {
//		ApsProperties config = this.getShowlet().getConfig();
//		if (null == config) return false;
//		for (int i = 0; i < this.getLangs().size(); i++) {
//			Lang lang = this.getLangs().get(i);
//			String paramValue = config.getProperty(prefix+lang.getCode());
//			if (null != paramValue && paramValue.trim().length() > 0) return true;
//		}
//		return false;
//	}

	public Map<String, InputControlConfig> getFormInputControlConfigMap() {
		return _formInputControlConfigMap;
	}
	public void setFormInputControlConfigMap(Map<String, InputControlConfig> formInputControlConfigMap) {
		this._formInputControlConfigMap = formInputControlConfigMap;
	}

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	/**
	 * per la ricerca
	 * @return
	 */
	public String getDescr() {
		return _descr;
	}
	/**
	 * per la ricerca
	 * @return
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}

	private String _descr;
	private IJasperServerManager _jasperServerManager;
	private Map<String, InputControlConfig> _formInputControlConfigMap = new HashMap<String, InputControlConfig>();

	public static final String WIDGET_PARAM_URI_STRING = "uriString";
	public static final String WIDGET_PARAM_DOWNLOAD_FORMATS = "downloadFormats";
	public static final String WIDGET_PARAM_INPUT_CONTROLS = "inputControls";
	public static final String WIDGET_PARAM_TITLE = "title_";
	public static final String WIDGET_PARAM_DETAIL_URI_STRING = "uriStringDett";
	public static final String WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS = "downloadFormatsDett";
	public static final String WIDGET_PARAM_DETAIL_TITLE = "titleDett_";
	public static final String WIDGET_PARAM_DETAIL_INPUT_CONTROLS = "inputControlsDett";
		
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_URI_STRING} instead
	 */
	public static final String SHOWLET_PARAM_URI_STRING = WIDGET_PARAM_URI_STRING;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DOWNLOAD_FORMATS} instead
	 */
	public static final String SHOWLET_PARAM_DOWNLOAD_FORMATS = WIDGET_PARAM_DOWNLOAD_FORMATS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_INPUT_CONTROLS} instead
	 */
	public static final String SHOWLET_PARAM_INPUT_CONTROLS = WIDGET_PARAM_INPUT_CONTROLS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_TITLE} instead
	 */
	public static final String SHOWLET_PARAM_TITLE = WIDGET_PARAM_TITLE; 
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DETAIL_URI_STRING} instead
	 */
	public static final String SHOWLET_PARAM_DETAIL_URI_STRING = WIDGET_PARAM_DETAIL_URI_STRING;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS} instead
	 */
	public static final String SHOWLET_PARAM_DETAIL_DOWNLOAD_FORMATS = WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DETAIL_TITLE} instead
	 */
	public static final String SHOWLET_PARAM_DETAIL_TITLE = WIDGET_PARAM_DETAIL_TITLE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DETAIL_INPUT_CONTROLS} instead
	 */
	public static final String SHOWLET_PARAM_DETAIL_INPUT_CONTROLS = WIDGET_PARAM_DETAIL_INPUT_CONTROLS;

}
