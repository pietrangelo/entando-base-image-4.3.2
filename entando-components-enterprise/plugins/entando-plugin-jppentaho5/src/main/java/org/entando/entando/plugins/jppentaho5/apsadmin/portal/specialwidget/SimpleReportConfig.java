package org.entando.entando.plugins.jppentaho5.apsadmin.portal.specialwidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoFilesystemManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public class SimpleReportConfig extends SimpleWidgetConfigAction {

	
	HttpServletResponse response;

	// getter/setter methods of cname,cvalue
	
	private static final Logger _logger = LoggerFactory.getLogger(SimpleReportConfig.class);
	

		
	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().size() > 0 ) {
			try {
				this.createValuedShowlet();
			} catch (Throwable t) {
				_logger.error("error in validate", t);
				this.addActionError("error.genericError");
			}
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		
		try {
			
			if (result == SUCCESS) {
				initFilesList();
				String controllerIpParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_PATH);
				setPathParam(controllerIpParam);
				String portParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_ARGS);
				setArgsParam(portParam);
				String widthParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_WIDTH);
				setWidthParam(widthParam);
				String lengthParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_LENGTH);
				setLengthParam(lengthParam);
				String cssClassParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_CSS_CLASS);
				setCssClassParam(cssClassParam);
			}
		} catch (Throwable t) {
			_logger.error("error entering configuration action", t);
			return FAILURE;
		}
		return result;
	}
	
	
	public void initFilesList() throws ApsSystemException{
		
		
		_logger.info("------------");
		_logger.info("--> SimpleReportConfig initFilesList <--");
		_logger.info("------------");

		_logger.debug("ReportConfig Init");
		_filesList = new HashMap<String, String>();
		
		List<String> wcdfFiles = _pentahoFileSystemManager.getReportsPath(".wcdf"); 
		List<String> xcdfFiles = _pentahoFileSystemManager.getReportsPath(".xcdf"); 
		 
		//setCookie(_pentahoFileSystemManager.getAuthCookie());
		
		_logger.info("wcdfFiles:"+ wcdfFiles.size());
		_logger.info("xcdfFiles:"+ xcdfFiles.size());
		
		wcdfFiles.addAll(xcdfFiles);
		for (int i = 0; i < wcdfFiles.size(); i++) {			
			_filesList.put(wcdfFiles.get(i) , wcdfFiles.get(i));
		}		
	
	}
	
	@Override
	public String save() {
		if (StringUtils.isBlank(_argsParam)) {
			_argsParam = PentahoSystemConstants.CONFIG_ARG_NONE;
		}		
		return super.save();
	}
	
	public String getPathParam() {
		return _pathParam;
	}
	public void setPathParam(String pathParam) {
		this._pathParam = pathParam;
	}
	public String getArgsParam() {
		return _argsParam;
	}
	public void setArgsParam(String argsParam) {
		this._argsParam = argsParam;
	}
	public String getWidthParam() {
		return _widthParam;
	}
	@Deprecated
	public void setWidthParam(String widthParam) {
		this._widthParam = widthParam;
	}
	@Deprecated
	public String getLengthParam() {
		return _lengthParam;
	}
	@Deprecated
	public void setLengthParam(String lengthParam) {
		this._lengthParam = lengthParam;
	}
	public String getCssClassParam() {
		return _cssClassParam;
	}
	public void setCssClassParam(String cssClassParam) {
		this._cssClassParam = cssClassParam;
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
	
	public IPentahoFilesystemManager getPentahoFileSystemManager() {
		return _pentahoFileSystemManager;
	}

	public void setPentahoFileSystemManager(IPentahoFilesystemManager _pentahoFileSystemManager) {
		this._pentahoFileSystemManager = _pentahoFileSystemManager;
	}
	
	public boolean getTapPlugin() {
		return _tapPlugin;
	}

	public void setTapPlugin(boolean _tapPlugin) {
		this._tapPlugin = _tapPlugin;
	}

	
	private String _pathParam;
	private String _argsParam;
	@Deprecated
	private String _widthParam;
	@Deprecated
	private String _lengthParam;
	private String _cssClassParam;

	// boolean  _tapPlugin e' il flag per verificare se transparent Authentication Pentaho Plugin e' Installato
	
	private boolean _tapPlugin;

	private IPentahoManager _pentahoManager;
	private Map<String, String> _filesList;
	
	private IPentahoFilesystemManager _pentahoFileSystemManager;
	
	public static final String SESSION_PARAM_FILE_MAP = "sessionParamPentahoFileMap";


}
