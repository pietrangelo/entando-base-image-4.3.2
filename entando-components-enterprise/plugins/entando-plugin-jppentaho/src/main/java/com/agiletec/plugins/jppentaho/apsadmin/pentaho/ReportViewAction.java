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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jppentaho.aps.system.services.report.IPentahoDynamicReportManager;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportDownload;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParamUIInfo;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;

public class ReportViewAction extends PentahoAbstractBaseAction implements IReportViewAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportViewAction.class);
	
    @Override
    public String preview() {
        
            ReportHttpParam reportHttpParam = new ReportHttpParam();
            Map<String, String> params = this.initPreviewParams(reportHttpParam);
            MasterReport masterReport = null;
            try {
            	String reportDefPath = null;
            	ReportDownload reportDownload = this.getDynamicReportManager().getReportDefinitionPath(reportHttpParam);
            	if (null != reportDownload && reportDownload.isValidationKO()) {
            		this.addActionError(reportDownload.getErrorCode());
            		return INPUT;
            	}
            	reportDefPath = reportDownload.getReportPath();
            	this.getDynamicReportManager().verifyReportErrors(reportDefPath, reportDownload);
            	if (reportDownload.isValidationKO()) {
            		this.addActionError(reportDownload.getErrorCode());
            		return INPUT;
            	}
            	masterReport = this.getDynamicReportManager().getReportDefinition(reportDefPath);
            	this.setTitle(masterReport.getTitle());
            } catch (ResourceLoadingException resourceLoadingException) {
            	_logger.error("error in preview", resourceLoadingException);
                this.addActionError(this.getDynamicReportManager().CONNECTION_PROBLEM_ERROR_CODE);
                return INPUT;
            }  catch (Throwable t) {
                this.addActionError("Errore Generico");
                _logger.error("Generic Error downloading the report defintion", t);
                return INPUT;
            }
            
            /*
            Widget currentShowlet = this.extractCurrentShowlet();
            WidgetType WidgetType = currentShowlet.getType();
            params.put("output-target", this.getDynamicReportManager().getOutputs().get(IPentahoDynamicReportManager.HTML_STREAM_KEY));
            ApsProperties config = (WidgetType.isLogic()) ? WidgetType.getConfig() : currentShowlet.getConfig();
            ReportDownload report = this.getDynamicReportManager().generateReport(WidgetType.getCode(), 
                    config, reportHttpParam, params, IPentahoDynamicReportManager.HTML_STREAM_KEY, true);
            */
            ReportDownload report = null;
            try {
            	report = this.extractReport(reportHttpParam, params, true);
            	if (null == report || null == report.getReportPath() || null == report.getFileName()) {
            		return ERROR;
            	}
            	if (report.isValidationKO()) {
            		this.addActionError(this.getText("validation.failed"));
            		return INPUT;
            	}
            	this.prepareHtmlOutput(report);
            } catch (ResourceLoadingException resourceLoadingException) {
            	_logger.error("error in preview", resourceLoadingException);
                this.addActionError(this.getDynamicReportManager().CONNECTION_PROBLEM_ERROR_CODE);
                return INPUT;
           }  catch (Throwable t) {
        	   this.addActionError("Errore Generico");
        	   _logger.error("Generic Error downloading the report defintion", t);
               return INPUT;
           }
        return SUCCESS;
    }
    
    @Override
    public String view() {
        try {
            ReportHttpParam reportHttpParam = new ReportHttpParam();
            Map<String, String> params = this.initViewParams(reportHttpParam);
            
            //String name = reportHttpParam.getName();
            //String action = name != null && name.endsWith(".xaction") ? name : this.getAction();
            /*
            if (null != reportHttpParam.getAction() && reportHttpParam.getAction().length() > 0 
                        || reportHttpParam.getName().endsWith(".xaction")) {// TODO Verificare
                String url = this.getDynamicReportManager().getXactionUrl(reportHttpParam.getSolution(), reportHttpParam.getPath(), reportHttpParam.getAction());
                this.setXactionUrl(url);
                return "xaction";
            }
             */
            if (reportHttpParam.getType()>0) {
                String url = this.getDynamicReportManager().buildActionUrl(reportHttpParam);
                this.setXactionUrl(url);
                return "xaction";
            }
            List<ReportParameter> paramsToConfigure = this.extractParamsToConfigure(reportHttpParam, params);
            if (paramsToConfigure.size() > 0) {
                this.setParamsToConfigure(paramsToConfigure);
                return "configure";
            }
            
            //return this.prepareReport(reportHttpParam, params);
            ReportDownload report = this.extractReport(reportHttpParam, params, true);
            if (null == report || null == report.getReportPath() || null == report.getFileName()) {
                return ERROR;
            }
            if (report.isValidationKO()) {
                this.addActionError(this.getText("validation.failed"));
                this.addActionError(report.getErrorCode());
                return INPUT;
            }
            this.prepareHtmlOutput(report);
            
        } catch (Throwable t) {
        	_logger.error("Error in report viewing", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    /*
    private String prepareReport(ReportHttpParam reportHttpParam, Map<String, String> params) throws IOException, ResourceLoadingException, ApsSystemException {
        params.put("output-target", this.getDynamicReportManager().getOutputs().get(IPentahoDynamicReportManager.HTML_STREAM_KEY));
        ReportDownload report = this.getDynamicReportManager().generateReport(reportHttpParam.getPath(), 
        		reportHttpParam.getName(), reportHttpParam.getSolution(), params, IPentahoDynamicReportManager.HTML_STREAM_KEY);
        if (report==null || report.getReportPath()==null || report.getFileName()==null) {
                return ERROR;
        }
        this.prepareHtmlOutput(report);
        return SUCCESS;
    }
    */
    private ReportDownload extractReport(ReportHttpParam reportHttpParam, Map<String, String> params, boolean useCache) throws IOException, ResourceLoadingException, ApsSystemException {
        params.put("output-target", this.getDynamicReportManager().getOutputs().get(IPentahoDynamicReportManager.HTML_STREAM_KEY));
        Widget currentShowlet = this.extractCurrentShowlet();
        WidgetType WidgetType = currentShowlet.getType();
        ApsProperties config =  this.extractShowletConfig(this.getCode());//(WidgetType.isLogic()) ? WidgetType.getConfig() : currentShowlet.getConfig();
        return this.getDynamicReportManager().generateReport(WidgetType.getCode(), 
                config, reportHttpParam, params, IPentahoDynamicReportManager.HTML_STREAM_KEY, useCache);
    }
    
    
    @Override
    public String configure() {
        try {
            ReportHttpParam reportHttpParam = new ReportHttpParam();
            Map<String, String> params = this.initViewParams(reportHttpParam);
            
            //String name = reportHttpParam.getName();
            //String action = (name!=null && name.endsWith(".xaction")) ? name : this.getAction();
            /*
            if (null != reportHttpParam.getAction() && reportHttpParam.getAction().length() > 0 
                        || reportHttpParam.getName().endsWith(".xaction")) {// TODO Verificare
                    String url = this.getDynamicReportManager().getXactionUrl(reportHttpParam.getSolution(), reportHttpParam.getPath(), reportHttpParam.getAction());
                    this.setXactionUrl(url);
                    return "xaction";
            }
            */
            if (reportHttpParam.getType()>0) {
                String url = this.getDynamicReportManager().buildActionUrl(reportHttpParam);
                this.setXactionUrl(url);
                return "xaction";
            }
            List<ReportParameter> paramsToConfigure = this.extractParamsToConfigure(reportHttpParam, params);
            this.setParamsToConfigure(paramsToConfigure);
            if (!this.checkRequiredParams(paramsToConfigure)) {
                    return INPUT;
            }
            params.putAll(this._helperPentahoTag.loadParams(this.getRequest(), paramsToConfigure));
            /*
            params.put("output-target", this.getDynamicReportManager().getOutputs().get(IPentahoDynamicReportManager.HTML_STREAM_KEY));
            ReportDownload report = this.getDynamicReportManager().generateReport(reportHttpParam.getPath(),
                                            reportHttpParam.getName(), reportHttpParam.getSolution(), params, IPentahoDynamicReportManager.HTML_STREAM_KEY);
            */
            ReportDownload report = this.extractReport(reportHttpParam, params, false);
            if (null == report || null == report.getReportPath() || null == report.getFileName()) {
                return ERROR;
            }
            this.prepareHtmlOutput(report);
            if (report.isValidationKO()) {
            	
            	// TODO gestione codice errori
                this.addActionError(this.getText("validation.failed"));
                // TODO
                this.addActionError(this.getText(report.getErrorCode()));
                return INPUT;
            }
        } catch (Throwable t) {
        	_logger.error("Error in report viewing", t);
            return FAILURE;
        }
        return SUCCESS;
    }
	
    private boolean checkRequiredParams(List<ReportParameter> paramsToConfigure) {
    	boolean checkResult = true;
    	try {
    		Map<String, String> paramValues = _helperPentahoTag.loadParams(this.getRequest(), paramsToConfigure);
    		Iterator<ReportParameter> paramsIter =  paramsToConfigure.iterator();
    		while (paramsIter.hasNext()) {
    			ReportParameter current = paramsIter.next();
    			String name = current.getName();
    			String value = paramValues.get(name);
    			if (current.isMandatory()) {
    				if (null == value || value.trim().length() == 0) {
    					this.addFieldError(name ,this.getText("requiredstring", new String[] {name}));
    					checkResult = false;
    				}
    			}
    		}
    	} catch (Throwable t) {
    		_logger.error("error in checkRequiredParams", t);
    		this.addActionError(this.getText("validation.failed"));
    		checkResult = false;
    	}
    	return checkResult;
    }
	
	private List<ReportParameter> extractParamsToConfigure(ReportHttpParam reportHttpParam, Map<String, String> showletParams) throws ResourceLoadingException, ApsSystemException {
		ReportParamUIInfo paramUI = this.getDynamicReportManager().getReportParamUIInfo(reportHttpParam.getPath(), 
				reportHttpParam.getName(), reportHttpParam.getSolution(), null);
		this.setTitle(paramUI.getReportTitle());
		
		List<ReportParameter> paramsToConfigure = new ArrayList<ReportParameter>();
		Iterator<ReportParameter> paramsIter = paramUI.getParam().iterator();
		while (paramsIter.hasNext()) {
			ReportParameter currentParam = paramsIter.next();
			if (!showletParams.containsKey(currentParam.getName())) {
				paramsToConfigure.add(currentParam);
			}
		}
		return paramsToConfigure;
	}
	
	private Map<String, String> initPreviewParams(ReportHttpParam reportHttpParam) {
		ApsProperties config = this.extractShowletConfig();
		String solution = config.getProperty("solution");
		String path = config.getProperty("previewPath");
		String name = config.getProperty("previewName");
		String queryString = config.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
		String profileParams = config.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
		this.initReportHttpParam(reportHttpParam, solution, path, name);
//		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> params = this.prepareParams(queryString, profileParams);
		
		this.setSolution(solution);
		this.setPath(config.getProperty("detailPath"));
		this.setName(config.getProperty("detailName"));
		this.setQueryString(queryString);
		this.setProfileParams(profileParams);
		
		return params;
	}
	
	private Map<String, String> initViewParams(ReportHttpParam reportHttpParam) throws ApsSystemException {
		String solution = null;
		String path = null;
		String name = null;
		String queryString = null;
		String profileParams = null;
		ApsProperties config = this.extractShowletConfig(this.getCode());
		if (config != null) {
			solution = config.getProperty("solution");
			path = config.getProperty("detailPath");
			name = config.getProperty("detailName");
			if (path==null && name== null) {
				path = config.getProperty("path");
				name = config.getProperty("name");
			}
			queryString = config.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
			profileParams = config.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
		}
		String tmp = this.getSolution();
		if (null != tmp && tmp.length() > 0) {
			solution = tmp;
		}
		tmp = this.getPath();
		if (null != tmp && tmp.length() > 0) {
			path = tmp;
		}
		tmp = this.getName();
		if (null != tmp && tmp.length() > 0) {
			name = tmp;
		}
		// Modificare per parametri nascosti
		tmp = this.getQueryString();
		if (null != tmp && tmp.length() > 0) {
			queryString = tmp;
		}
		tmp = this.getProfileParams();
		if (null != tmp && tmp.length() > 0) {
			profileParams = tmp;
		}
		this.setQueryString(queryString);
		this.setProfileParams(profileParams);
		this.initReportHttpParam(reportHttpParam, solution, path, name);
		Map<String, String> params = this.prepareParams(queryString, profileParams);
                
                String action = name != null && name.endsWith(".xaction") ? name : this.getAction();
                reportHttpParam.setAction(action);
                if (null != this.getType()) {
                    reportHttpParam.setType(this.getType().intValue());
                }
		return params;
	}
	
	private ApsProperties extractShowletConfig(String showletCode) throws ApsSystemException {
		ApsProperties config = null;
		if (showletCode != null) {
			try {
				WidgetType WidgetType = this.getWidgetTypeManager().getWidgetType(showletCode);
				if (WidgetType != null) {
					config = WidgetType.getConfig();
				}
			} catch(Throwable t) {
				_logger.error("Error extracting config for showlet of type {}", showletCode, t);
				throw new ApsSystemException("Error extracting config for showlet of type " + showletCode);
			}
		}
		if (config == null) {
			config = this.extractShowletConfig();
		}
		return config;
	}
        
	private void initReportHttpParam(ReportHttpParam reportHttpParam, String solution, String path, String name) {
		reportHttpParam.setSolution(solution);
		reportHttpParam.setName(name);
		reportHttpParam.setPath(path!=null && path.length()>0 ? path : "");
		this.setReport(reportHttpParam);
	}
	
	private void prepareHtmlOutput(ReportDownload report) throws IOException {
		String html = _helperPentahoTag.convertStreamToString(report);
		html = this.getDynamicReportManager().prepareReportHtmlForInclusion(html);
		this.setHtml(html);
	}
	
	public String getTitle() {
		return _title;
	}
	protected void setTitle(String title) {
		this._title = title;
	}
	
	public String getHtml() {
		return _html;
	}
	protected void setHtml(String html) {
		_logger.debug(" setting html for report " + html);
		this._html = html;
	}
	
	public String getXactionUrl() {
		return _xactionUrl;
	}
	public void setXactionUrl(String xactionUrl) {
		this._xactionUrl = xactionUrl;
	}
	
	public String getOutput() {
		return _output;
	}
	public void setOutput(String output) {
		this._output = output;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	public String getAction() {
		return _action;
	}
	public void setAction(String action) {
		this._action = action;
	}
	
	public String getSolution() {
		return _solution;
	}
	public void setSolution(String solution) {
		this._solution = solution;
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

    public Integer getType() {
        return _type;
    }
    public void setType(Integer type) {
        this._type = type;
    }
	
	public String getQueryString() {
		return _queryString;
	}
	public void setQueryString(String queryString) {
		this._queryString = queryString;
	}
	
	public String getProfileParams() {
		return _profileParams;
	}
	public void setProfileParams(String profileParams) {
		this._profileParams = profileParams;
	}
	
	public ReportHttpParam getReport() {
		return _report;
	}
	protected void setReport(ReportHttpParam report) {
		this._report = report;
	}
	
	public List<ReportParameter> getParamsToConfigure() {
		return _paramsToConfigure;
	}
	public void setParamsToConfigure(List<ReportParameter> paramsToConfigure) {
		this._paramsToConfigure = paramsToConfigure;
	}

	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}



	private String _title;
	private String _html;
	private String _xactionUrl;
	private String _output;
	private String _code;
	private String _action;
	private String _solution;
	private String _path;
	private String _name;
    private Integer _type;
	private String _queryString;
	private String _profileParams;
	private ReportHttpParam _report;
	private List<ReportParameter> _paramsToConfigure;
	
    private IWidgetTypeManager _widgetTypeManager;
	
}