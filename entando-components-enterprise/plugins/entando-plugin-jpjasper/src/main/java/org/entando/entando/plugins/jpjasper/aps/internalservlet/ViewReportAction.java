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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControlState;
import org.entando.entando.plugins.jpjasper.aps.services.util.QueryStringBuilder;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.InputControlConfig;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.ReportViewerConfigAction;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;

public class ViewReportAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ViewReportAction.class);
	
	/**
	 * Costruisce l'url dell'iframe
	 * @return
	 * @throws Throwable
	 */
	public String getIframeSrcUrl() throws Throwable {
		StringBuffer stringBuffer = new StringBuffer();

		String applicationURL = this.getConfigService().getParam(SystemConstants.PAR_APPL_BASE_URL);
		if (!applicationURL.endsWith("/"))applicationURL = applicationURL + "/";

		stringBuffer.append(applicationURL);
		stringBuffer.append("do/jpjasper/FrontEnd/Report/Download/getHtmlReport.action");

		Properties parameters = new Properties();
		parameters.put(ReportViewerConfigAction.WIDGET_PARAM_URI_STRING, this.getActionUriString());

		String queryString = QueryStringBuilder.buildQueryString(parameters);
		if (!StringUtils.isBlank(queryString)) {
			queryString = "?" + queryString;
			stringBuffer.append(queryString);
			String formICParams = InputControlConfig.buildInputControlConfigFromMap(this.getFormInputControlConfigMap());
			if (!StringUtils.isBlank(formICParams)) {
				stringBuffer.append("&showletInputControlValues=").append(URIUtil.encodeAll(formICParams));
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * metodo di utilità per la costruzione dell'url di download
	 * @return
	 */
	public String getFormInputControlParams() {
		return InputControlConfig.buildInputControlConfigFromMap(this.getFormInputControlConfigMap());
	}


	/**
	 * SUCCESS (renderizza il report)
	 * NO_REPORT(COOKIE INVALIDO OPPURE REPORT NON TROVATO.... BISONGA DISTINGUERE I 2 CASI!!!!!)
	 * INPUT_CONTROLS_FORM(COOKIE INVALIDO OPPURE REPORT NON TROVATO.... BISONGA DISTINGUERE I 2 CASI!!!!!)
	 * 
	 * @return
	 */
	public String renderReport() {
		//System.out.println("RENDER");
		try {

			JasperResourceDescriptor report = this.getReport();
			if (null == report) {
				return "no_report";
			}
			_logger.debug("Render report {}", report.getUriString());
			this.setCurrentReport(report);

			Map<String, InputControlConfig> mapFromShowConfig = InputControlConfig.buildInputControlMapFromConfig(this.getShowletInputControlValues());
			this.setShowletInputControlConfigMap(mapFromShowConfig);
			//this.print("SOLO CONF ", this.getShowletInputControlConfigMap());

			Map<String, InputControlConfig> formMap  = this.getFormParamsMap();
			//this.print("SOLO FORM", formMap);


			Map<String, InputControlConfig> fullMap = new HashMap<String, InputControlConfig>();
			fullMap.putAll(mapFromShowConfig);
			fullMap.putAll(formMap);
			//this.print("FULLMAP", fullMap);


			boolean hasIC = false;
			List<InputControl> reportInputControls = this.getReportInputControlsConfig(fullMap);
			if (null != reportInputControls && !reportInputControls.isEmpty()) {
				hasIC = true;
			}

			if (hasIC) {
				//se è la prima volta (arrivo dal tag) deve generare i controlli secondo la conf della showlet
				if (StringUtils.isBlank(this.getShowForm())) {
					//se la showlet non ha conf... e il form non è ancora stato rederizzato
					if (fullMap.isEmpty()) {
						fullMap = InputControlConfig.buildInputControlMapFromApi(reportInputControls, fullMap);
						//this.print("DA_API ", fullMap);
					}
					this.setFormInputControlConfigMap(fullMap);

					//this.print("DA_SHOWLET O DA LISTA... ", fullMap);
				} else {
					boolean hasMandatoryFieldErrors =  this.checkMandatoryFields(reportInputControls, this.getFormInputControlConfigMap());
					if (hasMandatoryFieldErrors) {
						return "input_control_form";						
					}
					this.setFormInputControlConfigMap(fullMap);
				}
				
				
				String formICParams = InputControlConfig.buildInputControlConfigFromMap(this.getFormInputControlConfigMap());
				//TODO qui forse... è possibile innestare un COSO che immette dinamicamente parametri.. es: un attributo del profilo

				//this.print("FULLMAP FINALE", fullMap);
				
				if (StringUtils.isBlank(formICParams)) {
					return "input_control_form";
				} else {
					//verifica anche errori di validazione
					boolean showInputControlsForm = this.checkShowInputControlsForm(this.getFormInputControlConfigMap(), reportInputControls);
					if (showInputControlsForm) {
						return "input_control_form";						
					}
				}
			}

		} catch (Throwable t) {
			_logger.error("error in renderReport", t);
			return FAILURE;
		}

		if (!StringUtils.isBlank(this.getRefreshedControlId())) {
			return "input_control_form";	
		} else {
			if (!StringUtils.isBlank(this.getReportKey())) {
				this.getRequest().setAttribute(this.getReportKey(), SUCCESS);
			}
			return SUCCESS;
		}
	}




	/**
	 * il valore è nullo (all'inteno della configurazione della showlet) (altrimenti deve essere sempre visibile)
	 * oppure
	 * è dichiarato frontend override
	 * @param id
	 * @return
	 */
	public boolean isModuleVisible(InputControlConfig inputControlConfig) {
		boolean visible = false;
		if (!this.getShowletInputControlConfigMap().containsKey(inputControlConfig.getId())) {
			visible = true;
		} else {
			visible = null != inputControlConfig.getFrontEndOverride() && inputControlConfig.getFrontEndOverride().equalsIgnoreCase("true"); 
		}
		return visible;
	}

	public InputControlConfig getInputControlConfig(String id) {
		InputControlConfig config = null;
		InputControlConfig target = this.getFormInputControlConfigMap().get(id);
		if (null != target && this.isModuleVisible(target)) {
			config = target;
		}
		return config;
	}


	/**
	 * Verifica se il form deve essere mostrato oppure no
	 * - 2 hasIC && showletReportParams != null e NON tutti i parametri sono cofigurati o uno di questi è dichiarato editabile
	 * @param inputControlConfigMap
	 * @return
	 */
	private boolean checkShowInputControlsForm(Map<String, InputControlConfig> inputControlConfigMap, List<InputControl> reportInputControls) {
		boolean showFrom = false;

		if (this.checkInputControlsValidationErrors(reportInputControls)) {
			return true;
		}

		//1 Estaggo la lista degli id degli IC del report
		List<String> reportInputControlIdList = new ArrayList<String>();
		for (int i = 0; i < reportInputControls.size(); i++) {
			InputControl ic = reportInputControls.get(i);
			reportInputControlIdList.add(ic.getId());
		}

		if (!inputControlConfigMap.keySet().containsAll(reportInputControlIdList)) {
			//NON tutti i parametri previsti sono presenti nella configurazione della showlet
			return true;
		}

		//TUTTI i parametri previsti sono presenti nella configurazione della showlet
		Iterator<Map.Entry<String, InputControlConfig>> entries = inputControlConfigMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig current = entry.getValue();

			if (null == current.getValue() || current.getValue().isEmpty()) {
				//System.out.println("CONTROLLO IL VALORE");
				showFrom = true;
				break;				
			}

			if (null == this.getShowForm()) {
				//se la chiamata arriva dal tag... showform == null.. allora controlla  formOverride
				String overrideValue = current.getFrontEndOverride();
				if (null == overrideValue || overrideValue.equalsIgnoreCase("true")) {
					//...ma almeno uno è dichiarato sosvrascrivibile...
					showFrom = true;
					break;
				}
			}
		}
		return showFrom;
	}

	protected boolean checkInputControlsValidationErrors(List<InputControl> inputControls) {
		boolean hasErrors = false;
		if (null != inputControls) {
			Iterator<InputControl> it = inputControls.iterator();
			while (it.hasNext()) {
				InputControl ic = it.next();
				InputControlState state = ic.getState();
				if (null != state && !StringUtils.isBlank(state.getError())) {
					hasErrors = true;
					List<String> args = new ArrayList<String>();
					args.add(ic.getLabel());
					args.add(ic.getId());
					args.add(state.getError());
					this.addActionError(this.getText("error.report.inputcontrol.errors.present", args));
				}
			}
		}
		return hasErrors;
	}

	protected boolean checkMandatoryFields(List<InputControl> inputControls, Map<String, InputControlConfig> fullMap) {
		boolean hasErrors = false;
		Iterator<Map.Entry<String, InputControlConfig>> entries = fullMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig current = entry.getValue();

			//in caso più di un report pubblicato nella stessa pagina, la mappa fullMap (popolata anche in funzione del form)
			//può contenere delle referenze ad inputControl di altri report, quindi currentInputControl può essere null. 
			InputControl currentInputControl = InputControl.getById(inputControls, current.getId());
			if (null != currentInputControl && currentInputControl.isMandatory()) {
				if (null == current.getValue() || current.getValue().isEmpty()) {
					hasErrors = true;
					List<String> args = new ArrayList<String>();
					args.add(currentInputControl.getLabel());
					args.add(currentInputControl.getId());
					//args.add(currentInputControl.getValidationRulesXML());
					this.addActionError(this.getText("error.report.inputcontrol.mandatoryField.Empty", args));
				}
			}
		}
		return hasErrors;
	}

	public JasperResourceDescriptor getReport() {
		JasperResourceDescriptor resourceDescriptor = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			if (!StringUtils.isBlank(cookieHeader)) {				
				resourceDescriptor = this.getJasperServerManager().getRestResource(cookieHeader, this.getActionUriString());
			}
		} catch (Throwable t) {
			_logger.error("error in getReport with uriString {}", this.getActionUriString(), t);
			throw new RuntimeException("error in getReport with uriString " + this.getActionUriString(), t);
		}
		return resourceDescriptor;
	}

	/**
	 * Chiede gli inputControls a partire da una configurazione
	 * @param map
	 * @return
	 */
	public List<InputControl> getReportInputControlsConfig(Map<String, InputControlConfig> map) {
		List<InputControl> list = null;
		try {
			Map <String, InputControlConfig> restQueryParameter = new HashMap<String, InputControlConfig>();
			restQueryParameter.putAll(map);
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String[][] arr = null; 
			if (null != map) {
				arr =InputControlConfig.inputControlConfigToArray(map);
			}

			list = this.getJasperServerManager().getInputControlsV2(cookieHeader, this.getActionUriString(), arr);
		} catch (Throwable t) {
			_logger.error("error in getReportInputControlsConfig for report uriString {}", this.getActionUriString(), t);
			throw new RuntimeException("error in getReportInputControlsConfig for report uriString " + this.getActionUriString(), t);
		}
		return list;
	}

	/**
	 * Metodo di utlità per la costruzione del form nella jsp
	 * @return
	 */
	public Map<String, InputControl> getReportInputControlsConfigMap() {
		Map<String, InputControl> mapV2 = new HashMap<String, InputControl>();
		try {
			Map<String, InputControlConfig> fullMap = new HashMap<String, InputControlConfig>();
			fullMap.putAll(this.getShowletInputControlConfigMap());
			fullMap.putAll(this.getFormParamsMap());
			//this.print("getReportInputControlsConfigMap JSP ", fullMap);


			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			String[][] arr = null; 
			if (null != fullMap) {
				arr =InputControlConfig.inputControlConfigToArray(this.getFormInputControlConfigMap());
			}

			List<InputControl> list = this.getJasperServerManager().getInputControlsV2(cookieHeader, this.getActionUriString(), arr);
			if (null != list) {
				for (int i = 0; i< list.size(); i++) {
					InputControl ic = list.get(i);
					mapV2.put(ic.getId(), ic);;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getReportInputControlsConfig for report uriString {}", this.getActionUriString(), t);
			throw new RuntimeException("error in getReportInputControlsConfig for report uriString " + this.getActionUriString(), t);
		}
		return mapV2;
	}

	public Map<String, InputControlConfig> getFormParamsMap() {
		Map<String, InputControlConfig> formConfiguration = this.getFormInputControlConfigMap();

		//this.print("START ", formConfiguration);

		Map<String, InputControlConfig> showletConfiguration = new HashMap<String, InputControlConfig>();
		String showletConfigParams = this.getShowletInputControlValues();
		if (!StringUtils.isBlank(showletConfigParams)) {
			showletConfiguration = InputControlConfig.buildInputControlMapFromConfig(showletConfigParams);
		}

		Map <String, InputControlConfig> restQueryParameter = new HashMap<String, InputControlConfig>();
		restQueryParameter.putAll(formConfiguration);

		//I parametri che in backend non sono stati specificati come sovrascivibili prevalgono su quelli del form

		Iterator<Map.Entry<String, InputControlConfig>> entries = showletConfiguration.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, InputControlConfig> entry = entries.next();
			InputControlConfig icc = entry.getValue();
			if (formConfiguration.containsKey(entry.getKey())) {
				if (null == icc.getFrontEndOverride() || !icc.getFrontEndOverride().equalsIgnoreCase("true")) {
					restQueryParameter.put(entry.getKey(), icc);
				}
			}
		}

		return restQueryParameter;
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




	public String getActionUriString() {
		return _actionUriString;
	}
	public void setActionUriString(String actionUriString) {
		this._actionUriString = actionUriString;
	}

	public String getShowletInputControlValues() {
		return _showletInputControlValues;
	}
	public void setShowletInputControlValues(String showletInputControlValues) {
		this._showletInputControlValues = showletInputControlValues;
	}

	public List<String> getShowletDownloadFormats() {
		return _showletDownloadFormats;
	}
	public void setShowletDownloadFormats(List<String> showletDownloadFormats) {
		this._showletDownloadFormats = showletDownloadFormats;
	}

	public JasperResourceDescriptor getCurrentReport() {
		return _currentReport;
	}
	public void setCurrentReport(JasperResourceDescriptor currentReport) {
		this._currentReport = currentReport;
	}

	public Map<String, InputControlConfig> getShowletInputControlConfigMap() {
		return _showletInputControlConfigMap;
	}
	public void setShowletInputControlConfigMap(Map<String, InputControlConfig> showletInputControlConfigMap) {
		this._showletInputControlConfigMap = showletInputControlConfigMap;
	}

	/**
	 * la configurazione finale dei parametri del report. Comprensiva dei parametri della showlet e di quelli del form. 
	 * Costruisce i parametri inseriti nell'url dell'iframe
	 * @return
	 */
	public Map<String, InputControlConfig> getFormInputControlConfigMap() {
		return _formInputControlConfigMap;
	}

	/**
	 * la configurazione finale dei parametri del report. Comprensiva dei parametri della showlet e di quelli del form. 
	 * Costruisce i parametri inseriti nell'url dell'iframe
	 * @return
	 */
	public void setFormInputControlConfigMap(Map<String, InputControlConfig> formInputControlConfigMap) {
		this._formInputControlConfigMap = formInputControlConfigMap;
	}


	/**
	 * Flag per determinare se la richiesta proviene dal tag (quindi diretta.. e quindi deve essere mostrato almeno una volta il form, se previsto)
	 * oppure dal form per la compilazione dei parametri (quindi se i parametri sono completi è possibile renderizzare il form)
	 * @return
	 */
	public String getShowForm() {
		return _showForm;
	}
	/**
	 * Flag per determinare se la richiesta proviene dal tag (quindi diretta.. e quindi deve essere mostrato almeno una volta il form, se previsto)
	 * oppure dal form per la compilazione dei parametri (quindi se i parametri sono completi è possibile renderizzare il form)
	 * @param showForm
	 */
	public void setShowForm(String showForm) {
		this._showForm = showForm;
	}

	/**
	 * indica l'id dell'oggetto da aggiornare
	 * @return
	 */
	public String getRefreshedControlId() {
		return _refreshedControlId;
	}
	public void setRefreshedControlId(String refreshedControlId) {
		this._refreshedControlId = refreshedControlId;
	}


	protected ConfigInterface getConfigService() {
		return configService;
	}
	public void setConfigService(ConfigInterface configService) {
		this.configService = configService;
	}

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	public String getShowletTitleParam() {
		return _showletTitleParam;
	}
	public void setShowletTitleParam(String showletTitleParam) {
		this._showletTitleParam = showletTitleParam;
	}

	/**
	 * Viene valorizzato dal tag, la forma è {page}_{frame}_reportResul
	 * per la showlet di anteprima/dettaglio serve per decidere se stampare o meno l'url del detaglio
	 * @return
	 */
	public String getReportKey() {
		return _reportKey;
	}
	public void setReportKey(String reportKey) {
		this._reportKey = reportKey;
	}

	private String _actionUriString;
	private String _showletInputControlValues;
	private List<String> _showletDownloadFormats;
	private String _showletTitleParam;

	private JasperResourceDescriptor _currentReport;
	private Map<String, InputControlConfig> _formInputControlConfigMap = new HashMap<String, InputControlConfig>();
	private Map<String, InputControlConfig> _showletInputControlConfigMap = new HashMap<String, InputControlConfig>();

	private String _showForm;
	private String _refreshedControlId;

	private ConfigInterface configService;
	private IJasperServerManager _jasperServerManager;

	private String _reportKey;

}
