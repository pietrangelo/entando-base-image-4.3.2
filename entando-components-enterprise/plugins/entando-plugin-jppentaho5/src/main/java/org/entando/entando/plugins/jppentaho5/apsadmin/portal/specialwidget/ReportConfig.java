package org.entando.entando.plugins.jppentaho5.apsadmin.portal.specialwidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoFilesystemManager;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;

public class ReportConfig extends SimpleReportConfig {

	private static final Logger _logger = LoggerFactory.getLogger(ReportConfig.class);

	@Override
	public void initFilesList() throws ApsSystemException{
		
		_logger.info("------------");
		_logger.info("--> initFilesList <--");
		_logger.info("------------");

		
		_logger.info("ReportConfig Init");
		_filesList = new HashMap<String, String>();
		
		List<String> prptFiles = _pentahoFileSystemManager.getReportsPath(".prpt"); 
	
		_logger.info("files:"+ prptFiles.size());

		for (int i = 0; i < prptFiles.size(); i++) {			
			_filesList.put(prptFiles.get(i) , prptFiles.get(i));
		}
				
	}
	
/*
	@Override
	public String init() {
		String result = super.init();
		
		_logger.info("------------");
		_logger.info("--> INIT <--");
		_logger.info("------------");

		try {
			
			if (result == SUCCESS) {
				_logger.debug("ReportConfig Init");
				_filesList = new HashMap<String, String>();
				
				List<String> prptFiles = _pentahoFileSystemManager.getReportsPath(".prpt"); 
				
				_logger.info("files:"+ prptFiles.size());

				for (int i = 0; i < prptFiles.size(); i++) {			
					_filesList.put(prptFiles.get(i) , prptFiles.get(i));
				}
				//this.getRequest().getSession().setAttribute(SESSION_PARAM_FILE_MAP, _filesList);
			
			}
		} catch (Throwable t) {
			_logger.error("error entering configuration action", t);
			return FAILURE;
		}
		return result;
	}
*/
	@Override
	public String save() {
	/*	System.out.println("***** FILE_ID : "+this.getFileId());
		setFilesList((Map<String, String>) this.getRequest().getSession().getAttribute(SESSION_PARAM_FILE_MAP));
		System.out.println("***** FILE_VAL : "+_filesList.get(this.getFileId()));
		String pathReport = _filesList.get(this.getFileId());
		
		System.out.println("***** SAVE PATH PARAM : "+pathReport);
		setPathParam(pathReport);
		
		this.getRequest().getSession().removeAttribute(SESSION_PARAM_FILE_MAP);
		*/return super.save();
	}

	@Override
	protected void createValuedShowlet() throws Exception {
		Widget widget = this.createNewShowlet();
		List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
		for (int i=0; i<parameters.size(); i++) {
			WidgetTypeParameter param = parameters.get(i);
			String paramName = param.getName();
			String value = this.getRequest().getParameter(paramName);
			if (value != null && value.trim().length() > 0) {
				widget.getConfig().setProperty(paramName, value);
			}
		}
		List<PentahoParameter> params = getPentahoManager().getReportFormParameterList(getPathParam(), false);
		StringBuilder csv = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			PentahoParameter parameter = params.get(i);

			csv.append(parameter.getName());
			if (i < params.size() - 1) {
				csv.append(",");
			}
		}
		widget.getConfig().setProperty(PentahoSystemConstants.CONFIG_REPORT_PARAMCOUNT, csv.toString());
		_logger.info("This report needs the following parameters: " + csv.toString());
		this.setShowlet(widget);
	}

	public Integer getCountParam() {
		return _countParam;
	}

	public void setCountParam(Integer countParam) {
		this._countParam = countParam;
	}
	public IPentahoFilesystemManager getPentahoFileSystemManager() {
		return _pentahoFileSystemManager;
	}

	public void setPentahoFileSystemManager(IPentahoFilesystemManager _pentahoFileSystemManager) {
		this._pentahoFileSystemManager = _pentahoFileSystemManager;
	}
	
	public IPentahoManager getPentahoManager() {
		return _pentahoManager;
	}

	public void setPentahoManager(IPentahoManager pentahoManager) {
		this._pentahoManager = pentahoManager;
	}


	public Map<String, String> getFileList() {
		return _filesList;
	}

	public void setFilesList(Map<String, String> filesList) {
		this._filesList = filesList;
	}
	
	private Map<String, String> _filesList;
	

	private Integer _countParam;
        
	private IPentahoManager _pentahoManager;
	private IPentahoFilesystemManager _pentahoFileSystemManager;

}
