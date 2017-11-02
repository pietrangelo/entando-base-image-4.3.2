package org.entando.entando.plugins.jppentaho5.aps.system.internalservlet.report;

import java.util.Map;

import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;

import com.agiletec.apsadmin.system.BaseAction;

public class ReportAction extends BaseAction implements IReportAction {
	
	@Override
	public String intro() {
		try {
			/*
			_dynamicParams = _pentahoManager.getReportFormParameterMap(getPath());
			// if we have no dynamic parameters other then output type and the latter is defined, render the report
			if (_dynamicParams.size() == 1
//					&& null != _outputType // TODO pass output type as parameter
					) {
				return "render";
			}
			*/
			System.out.println("§§§\\n§§§\\n§§§\\n" + _path);
		} catch (Throwable t) {
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public IPentahoManager getPentahoManager() {
		return _pentahoManager;
	}

	public void setPentahoManager(IPentahoManager pentahoManager) {
		this._pentahoManager = pentahoManager;
	}
	
	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		this._path = path;
	}

	public Map<String, PentahoParameter> getDynamicParams() {
		return _dynamicParams;
	}

	public void setDynamicParams(Map<String, PentahoParameter> dynamicParams) {
		this._dynamicParams = dynamicParams;
	}

	// report path
	private String _path;
	// report parameters (evaluated given the path)
	private Map<String, PentahoParameter> _dynamicParams;
	
	private IPentahoManager _pentahoManager;
	
}
