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
package com.agiletec.plugins.jppentaho.apsadmin.pentaho;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jppentaho.aps.system.services.report.IPentahoDynamicReportManager;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportDownload;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParamUIInfo;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;
import com.agiletec.plugins.jppentaho.aps.util.PentahoHelper;

/**
 * Action to collect report param and generate report from biserver invocation
 *
 * @author zuanni G.Cocco
 * */
public class PentahoOutputGenerationAction extends PentahoAbstractBaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoOutputGenerationAction.class);
	
	@Override
	public void validate() {
		super.validate();
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		ReportParamUIInfo paramUI = this.getReportParamUIInfo();
		Map<String, String> paramValues = _helperPentahoTag.loadParams(this.getRequest(), paramUI);
		List<ReportParameter> params =  paramUI.getParam();
		for (ReportParameter current : params) {
			String name = current.getName();
			String value = paramValues.get(name);
			if (current.isMandatory()) {
				if (null == value || value.trim().length() == 0 ) {
					this.addFieldError(name ,this.getText("requiredstring", new String[]{name}));
				}
			}
		}
		this.setValidateFrame(currentFrame.toString());
		this.setReportParamUIInfo(paramUI);
	}

	public String getReportsParamForLockedList() {
		List<ReportHttpParam> params = null;

		UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String username = currentUser.getUsername();
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Widget currentShowlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		WidgetType WidgetType = currentShowlet.getType();

		ApsProperties showletProperties = (WidgetType.isLogic()) ? WidgetType.getConfig() : currentShowlet.getConfig();
		try {
			params = this.getDynamicReportManager().getReportsParam(WidgetType.getCode(), showletProperties, username);
		} catch (MalformedURLException e) {
			_logger.error("error in getReportsParamForLockedList", e);
			this.addActionError(" ERRORE CONNESSIONE");

			System.out.println(" ################# ERRORE CONNESSIONE ################");

		} catch (RemoteException e) {
			_logger.error("error in getReportsParamForLockedList", e);
			this.addActionError(" ERRORE CONNESSIONE");

			System.out.println(" ################# ERRORE CONNESSIONE ################");

		} catch (ServiceException e) {
			_logger.error("error in getReportsParamForLockedList", e);
			this.addActionError(" ERRORE CONNESSIONE");

			System.out.println(" ################# ERRORE CONNESSIONE ################");

		} catch (ApsSystemException e) {
			_logger.error("error in getReportsParamForLockedList", e);
			this.addActionError(" ERRORE GENERICO");

			System.out.println(" ################# ERRORE GENERICO ################");

		} catch (Throwable t) {
			_logger.error("error in getReportsParamForLockedList", t);
			this.addActionError(" ERRORE GENERICO");
			System.out.println(" ################# ERRORE GENERICO ################");

		} finally {
			this.setParams(params);
			return SUCCESS;
		}
	}

	public List<ReportHttpParam> getReportsParam() {
		List<ReportHttpParam> params = null;
		try {
			UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			String username = currentUser.getUsername();
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
			Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
			String solution = this.extractSolutionFromConfig(username, reqCtx, currentFrame, page);
			//ApsSystemUtils.getLogger().info(" get report param: solution " + solution);
			params = this.getDynamicReportManager().getReportsParamFromUserShowletConfig( solution, currentFrame, page.getCode(), username );
		} catch(Throwable t) {
			_logger.error("error in getReportsParam", t);
		}
		return params;
	}

	public String initList() {
		ReportParamUIInfo paramUI = null;
		this.setType(LIST);
		try {
			paramUI = this.getReportParamUIInfo();
			if (null == paramUI) {
				paramUI = new ReportParamUIInfo();
			}
			//ApsSystemUtils.getLogger().info(" init report from request params with path: " + this.getPath() + " , name " + this.getName() + " , solution " + this.getSolution());
			paramUI = this.getDynamicReportManager().getReportParamUIInfo(this.getPath(), this.getName(), this.getSolution(), null);
		} catch (ResourceLoadingException t) {
			_logger.error("error in initList", t);
			return ERROR;
		} catch (Throwable t) {
			_logger.error("error in initList", t);
			return FAILURE;
		}
		this.setReportParamUIInfo(paramUI);
		return SUCCESS;
	}

	public String initSingle() {
		ReportParamUIInfo paramUI = null;
		this.setType(SINGLE);
		Integer currentFrame = null;
		try {
			//ApsSystemUtils.getLogger().info("init single");
			UserDetails user = this.getCurrentUser();
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
			Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
			String solution = this.extractSolutionFromConfig(user.getUsername(), reqCtx, currentFrame, page);
			PentahoReportUserWidgetConfig config =
				this.getDynamicReportManager().loadUserConfiguration(user.getUsername(), currentFrame, page.getCode());
			if (null != config && null != config.getConfig() && null != config.getName() ) {
				//ApsSystemUtils.getLogger().info(" init report from db config with path: " + config.getConfig() + " , name " + config.getName() + " , solution " + solution);
				String path =  this.getDynamicReportManager().getPathForPentaho(config);
				paramUI = this.getDynamicReportManager().getReportParamUIInfo(path, config.getName(), solution, null);
				this.setName(config.getName());
				this.setSolution(solution);
				this.setPath(path);
			} else {
				return CONFIG;
			}
		}
		catch (ResourceLoadingException t) {
			_logger.error("error in initSingle", t);
			return ERROR;
		}
		catch (Throwable t) {
			_logger.error("error in initSingle", t);
			return FAILURE;
		}
		this.setReportParamUIInfo(paramUI);
		return SUCCESS;
	}

	/*
	 */
	public String generateFromList() {
		UserDetails user = this.getCurrentUser();
		Map<String, String> params = null;
		//ApsSystemUtils.getLogger().info(" report generation with code: " + this.getCode() + " path: " + this.getPath() + " , name " + this.getName() + " , solution " + this.getSolution());
		ApsProperties userShowletProp = null;
		WidgetType WidgetType = this.getWidgetTypeManager().getWidgetType(this.getCode());
		String path = this.getPath();
		if (null == path || path.length() == 0) {
			path = "";
		}
		if (null != WidgetType) {
			userShowletProp = WidgetType.getConfig();
			if (null != userShowletProp) {
				String profileParams = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
				String queryString = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
				if (null != queryString && queryString.length() > 0 ) {
					params = _helperPentahoTag.fromQueryStringToParamsMap(queryString);
				}
				if (null != user) {
					_helperPentahoTag.addReportParamsFromProfile(user, profileParams, params);
				}
			}
			ReportDownload reportDownload = this.getDynamicReportManager().generateReport(path, this.getName(), this.getSolution(), params, this.getOutput());
			this.setReportPath(reportDownload.getReportPath());
			this.setNameFile(reportDownload.getFileName());
			if (reportDownload.isValidationKO()) {

				// TODO
                this.addActionError(this.getText("validation.failed"));
                // TODO
                this.addActionError(reportDownload.getErrorCode());

				return ERROR;
			}

			// TODO move to manager ::

			if (IPentahoDynamicReportManager.HTML_STREAM_KEY.equals(this.getOutput())) {
				String html = null;
				try {
					html = _helperPentahoTag.convertStreamToString(reportDownload);
//					if (!this.getDynamicReportManager().getPentahoServerVisibleFromClient()) {
//						this.getDynamicReportManager().loadLocallyReportImages(html);
//					}
					html = this.getDynamicReportManager().prepareReportHtmlForInclusion(html);
					this.setHtml(html);
				} catch (Throwable t) {
					_logger.error("error in generate", t);
					return FAILURE;
				}
				return "include_html";
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	/*
	 */
	public String generate() {
		this.setDetailReport(true);
		UserDetails user = this.getCurrentUser();
		//ApsSystemUtils.getLogger().info(" report generation with path: " + this.getPath() + " , name " + this.getName() + " , solution " + this.getSolution());
		ReportParamUIInfo paramUI = null;
		paramUI = this.getReportParamUIInfo();
		Map<String, String> params = _helperPentahoTag.loadParams(this.getRequest(), paramUI);
		params.put("output-target", this.getDynamicReportManager().getOutputs().get(this.getOutput()));
		Integer currentFrame = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
		Widget currShowlet =  (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		String solution = this.extractSolutionFromConfig(user.getUsername(), reqCtx, currentFrame, page);
		String profileParams = currShowlet.getConfig().getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
		if (null != user) {
			_helperPentahoTag.addReportParamsFromProfile(user, profileParams, params);
		}
		ReportDownload reportDownload = this.getDynamicReportManager().generateReport(this.getPath(), this.getName(), this.getSolution(), params, this.getOutput());
		this.setReportPath(reportDownload.getReportPath());
		this.setNameFile(reportDownload.getFileName());

		if (reportDownload.isValidationKO()) {
			// TODO
            this.addActionError(this.getText("validation.failed"));
            // TODO
            this.addActionError(reportDownload.getErrorCode());

			PentahoReportUserWidgetConfig config =
				this.getDynamicReportManager().loadUserConfiguration(user.getUsername(), currentFrame, page.getCode());
			String path =  this.getDynamicReportManager().getPathForPentaho(config);
			try {
				paramUI = this.getDynamicReportManager().getReportParamUIInfo(path, this.getName(), solution, params);
				this.setReportParamUIInfo(paramUI);
			} catch (ResourceLoadingException e) {
				_logger.error("error in generate", e);
				return FAILURE;
			} catch (ApsSystemException e) {
				_logger.error("error in generate", e);
				return FAILURE;
			}
			return INPUT;
		}
		if (IPentahoDynamicReportManager.HTML_STREAM_KEY.equals(this.getOutput())) {
			String html = null;
			try {
				html = _helperPentahoTag.convertStreamToString(reportDownload);
//				if (!this.getDynamicReportManager().getPentahoServerVisibleFromClient()) {
//					this.getDynamicReportManager().loadLocallyReportImages(html);
//				}
				html = this.getDynamicReportManager().prepareReportHtmlForInclusion(html);
				this.setHtml(html);
			} catch (Throwable t) {
				_logger.error("error in generate", t);
				return FAILURE;
			}
			return "include_html";
		}
		return SUCCESS;
	}

	public String generateDirectly() {
		return generateDirectlyFromConfig();
	}

	public InputStream getInputStream() {
		//ApsSystemUtils.getLogger().info("getInputStream " +  this.getReportPath() + " nameFile " + this.getNameFile() );
		InputStream documentInputStream = null;
		try {
			documentInputStream = new FileInputStream(this.getReportPath());
		} catch (Throwable t) {
			_logger.error("error in getInputStream", t);
		}
		return documentInputStream;
	}

	public String getMimeType() {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		return fileNameMap.getContentTypeFor(this.getReportPath());
	}

	public String xaction() {
		String url = this.getDynamicReportManager().getXactionUrl(this.getSolution(), this.getPath(), this.getAction());
		this.setXactionUrl(url);
		return SUCCESS;
	}


	private String generateDirectlyFromConfig() {
		/*
		try {
			String solution = null;
			String path = null;
			String name = null;
			boolean paramsFromRequest = false;
			ApsProperties config = this.extractShowletConfig();
			if (config==null) {
				String code = this.getCode();
				if (code!=null && code.length() > 0) {
					this.setDetailReport(true);
					WidgetType WidgetType = this.getShowletTypeManager().getShowletType(this.getCode());
					if (null != WidgetType) {
						config = WidgetType.getConfig();
					}
					paramsFromRequest = true;
				}
			}
			if (config!=null) {
				solution = config.getProperty(IPentahoDynamicReportManager.SHOWLET_SOLUTION_PROPERTY);
				path = config.getProperty(IPentahoDynamicReportManager.SHOWLET_PATH_PROPERTY);
				name = config.getProperty(IPentahoDynamicReportManager.SHOWLET_REPORT_NAME);
				if (paramsFromRequest) {
					String tmp = this.getPath();
					if (null != tmp && tmp.length() > 0) {
						path = this.getPath();
					}
					tmp = this.getName();
					if (null != tmp && tmp.length() > 0) {
						name = this.getName();
					}
					tmp = this.getSolution();
					if (null != tmp && tmp.length() > 0) {
						solution = this.getSolution();
					}
				}
			}
			Map<String, String> params = this.prepareParams(config);

			String action = this.getAction();
			if (null != action && action.length() > 0 ) {
				String url = this.getDynamicReportManager().getXactionUrl(solution, path, action);
				this.setXactionUrl(url);
				return "xaction_include";
			}

			//ApsSystemUtils.getLogger().info(" report generation with path: " + path + " , name " + name + " , solution " + solution);
			params.put("output-target", IPentahoDynamicReportManager.HTML_STREAM);
			ReportHttpParam reportHttpParam = new ReportHttpParam();
			reportHttpParam.setSolution(solution);
			reportHttpParam.setPath(path!=null && path.length()>0 ? path : "");
			reportHttpParam.setName(name);
			String reportDefPath = this.getDynamicReportManager().getReportDefinition(reportHttpParam);
			try {
				MasterReport masterReport = this.getDynamicReportManager().getReportDefinition(reportDefPath);
				this.setTitle(masterReport.getTitle());
			} catch (ResourceLoadingException e) {
				ApsSystemUtils.logThrowable(e, this, "generateDirectlyFromConfig");
				return FAILURE;
			} catch (ApsSystemException e) {
				ApsSystemUtils.logThrowable(e, this, "generateDirectlyFromConfig");
				return FAILURE;
			}
			ReportDownload reportDownload = this.getDynamicReportManager().generateReport(path, name, solution, params, IPentahoDynamicReportManager.HTML_STREAM_KEY);
			this.setReportPath(reportDownload.getReportPath());
			this.setNameFile(reportDownload.getFileName());
			String html = null;
			if (null == reportDownload || null == reportDownload.getReportPath() || null == reportDownload.getFileName()) {
				return ERROR;
			}
			try {
				html = _helperPentahoTag.convertStreamToString(reportDownload);
				html = this.getDynamicReportManager().prepareReportHtmlForInclusion(html);
				this.setHtml(html);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "generateDirectlyFromConfig");
				return FAILURE;
			}
			return "include_html";
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getReportsParam");
		}
		//*/

		// gestire xaction
		String path = null;
		String name = null;
		String queryString = null;
		String profileParams = null;
		String solution = null;
		String tmp = null;
		UserDetails user = this.getCurrentUser();
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		boolean configuredFromDb = false;

		// caricamento dalle configurazioni

		Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));

		Widget currShowlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		ApsProperties showletProp = currShowlet.getConfig();
		if (null != showletProp) {
			tmp = (String) showletProp.get(IPentahoDynamicReportManager.SHOWLET_SOLUTION_PROPERTY);
			if (null != tmp && tmp.length() > 0) {
				solution = tmp;
			}
			tmp = showletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_PATH_PROPERTY);
			if (null != tmp && tmp.length() > 0) {
				path = tmp;
			}
			tmp = showletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_REPORT_NAME);
			if (null != tmp && tmp.length() > 0) {
				name = tmp;
			}
			tmp = showletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
			if (null != tmp && tmp.length() > 0) {
				queryString = tmp;
			}
			tmp = showletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
			if (null != tmp && tmp.length() > 0) {
				profileParams = tmp;
			}
			configuredFromDb = true;
		}
		if (!configuredFromDb) {
			if (null != this.getCode() && this.getCode().length() > 0) {
				this.setDetailReport(true);
				ApsProperties userShowletProp = null;
				WidgetType WidgetType = this.getWidgetTypeManager().getWidgetType(this.getCode());
				if (null != WidgetType) {
					userShowletProp = WidgetType.getConfig();

					if (null != userShowletProp) {
						solution = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_SOLUTION_PROPERTY);
						path = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_PATH_PROPERTY);

						// TODO con parametri di conf
						path = "details";

						name = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_REPORT_NAME);
						queryString = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
						profileParams = userShowletProp.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
					}
				}
			}
			tmp = this.getPath();
			if (null != tmp && tmp.length() > 0) {
				path = this.getPath();
			}
			tmp = this.getName();
			if (null != tmp && tmp.length() > 0) {
				name = this.getName();
			}
			tmp = this.getSolution();
			if (null != tmp && tmp.length() > 0) {
				solution = this.getSolution();
			}
		}
		Map<String, String> params = null;
		if (null != queryString && queryString.length() > 0 ) {
			params = _helperPentahoTag.fromQueryStringToParamsMap(queryString);
		}
		if (null == params) {
			params = new HashMap<String, String>();
		}
		if (null != user) {
			_helperPentahoTag.addReportParamsFromProfile(user, profileParams, params);
		}

		if (null != this.getAction() && this.getAction().length() > 0 ) {
			String url = this.getDynamicReportManager().getXactionUrl(solution, path, this.getAction());
			this.setXactionUrl(url);
			return "xaction_include";
		}

		//ApsSystemUtils.getLogger().info(" report generation with path: " + path + " , name " + name + " , solution " + solution);
		params.put("output-target", IPentahoDynamicReportManager.HTML_STREAM);
		ReportHttpParam reportHttpParam = new ReportHttpParam();
		reportHttpParam.setName(name);
		if (null != path && path.length() > 0 ) {
			reportHttpParam.setPath(path);
		} else {
			reportHttpParam.setPath("");
		}
		reportHttpParam.setSolution(solution);
		try {
            String reportDefPath = null;
            ReportDownload reportDownload = this.getDynamicReportManager().getReportDefinitionPath(reportHttpParam);
            reportDefPath = reportDownload.getReportPath();
			MasterReport masterReport = this.getDynamicReportManager().getReportDefinition(reportDefPath);
			this.setTitle(masterReport.getTitle());
		} catch (ResourceLoadingException e) {
			_logger.error("error in generateDirectlyFromConfig", e);
			return FAILURE;
		} catch (ApsSystemException e) {
			_logger.error("error in generateDirectlyFromConfig", e);
			return FAILURE;
		}
		ReportDownload reportDownload = this.getDynamicReportManager().generateReport(path, name, solution, params, IPentahoDynamicReportManager.HTML_STREAM_KEY);

		// TODO gestire errori nel download della definizione del report


		this.setReportPath(reportDownload.getReportPath());
		this.setNameFile(reportDownload.getFileName());
		String html = null;
		if (null == reportDownload || null == reportDownload.getReportPath() || null == reportDownload.getFileName()) {
			return ERROR;
		}
		try {
			html = _helperPentahoTag.convertStreamToString(reportDownload);
			html = this.getDynamicReportManager().prepareReportHtmlForInclusion(html);
			this.setHtml(html);
		} catch (Throwable t) {
			_logger.error("error in generateDirectlyFromConfig", t);
			return FAILURE;
		}
		return "include_html";
	}

	// TODO ragionarci ::
	// scaricare il file dei parametri dandogli un nome che lo leghi
	// all'utente alla pagina ed alla showlet, salvare il file e mettere in sessione solo
	// il nome del file
	private void removeReportParamUIInfoSessionAttribute() {
		String name = this.getReportParamUIInfoAttributeName();
		this.getRequest().getSession().removeAttribute(name);
	}

	public void setReportParamUIInfo(ReportParamUIInfo reportParamUIInfo) {
		String name = this.getReportParamUIInfoAttributeName();
		this.getRequest().getSession().setAttribute(name, reportParamUIInfo);
	}
	public ReportParamUIInfo getReportParamUIInfo() {
		String name = this.getReportParamUIInfoAttributeName();
		return (ReportParamUIInfo) this.getRequest().getSession().getAttribute(name);
	}

	private String getReportParamUIInfoAttributeName() {
		String name = null;
		Integer currentFrame = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
		name = "reportParamUIInfo_"+page.getCode()+"_"+currentFrame;
		return name;
	}

	public String getPath() {
		return _path;
	}
	public void setPath(String path) {
		this._path = path;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	public String getSolution() {
		return _solution;
	}
	public void setSolution(String solution) {
		this._solution = solution;
	}

	public String getOutput() {
		return _output;
	}
	public void setOutput(String output) {
		this._output = output;
	}

	public String getReportPath() {
		return _reportPath;
	}
	public void setReportPath(String reportPath) {
		this._reportPath = reportPath;
	}

	public String getNameFile() {
		return _nameFile;
	}
	public void setNameFile(String nameFile) {
		this._nameFile = nameFile;
	}

	public String getHtml() {
		return _html;
	}
	public void setHtml(String html) {
		this._html = html;
	}

	public ITreeNode getRoot() {
		return _root;
	}
	public void setRoot(ITreeNode root) {
		this._root = root;
	}

	public String getType() {
		return _type;
	}
	public void setType(String type) {
		this._type = type;
	}

	public String getValidateFrame() {
		return _validateFrame;
	}
	public void setValidateFrame(String validateFrame) {
		this._validateFrame = validateFrame;
	}

	public boolean isDetailReport() {
		return _isDetailReport;
	}
	public void setDetailReport(boolean isDetailReport) {
		this._isDetailReport = isDetailReport;
	}

	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	public String getAction() {
		return _action;
	}
	public void setAction(String action) {
		this._action = action;
	}

	public String getXactionUrl() {
		return _xactionUrl;
	}
	public void setXactionUrl(String xactionUrl) {
		this._xactionUrl = xactionUrl;
	}

	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	public List<ReportHttpParam> getParams() {
		return _params;
	}
	public void setParams(List<ReportHttpParam> params) {
		this._params = params;
	}

	private boolean _isDetailReport;
	private String _html;
	private String _reportPath;
	private String _path;
	private String _name;
	private String _solution;
	private String _output;
	private String _nameFile;
	private String _title;
	private String _code;
	private ITreeNode _root;
	private String _validateFrame;
	private String _type;
	private String _action;
	private String _xactionUrl;

    private static final String CONFIG = "config";

    public static final String LIST = "list";
    public static final String SINGLE = "single";

    private List<ReportHttpParam> _params = null;

    private IWidgetTypeManager _widgetTypeManager;
    private PentahoHelper _helperPentahoTag = new PentahoHelper();

}