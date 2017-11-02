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
package com.agiletec.plugins.jppentaho.apsadmin.dataintegration;

import java.util.Map;

import org.pentaho.di.repository.RepositoryElementInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jppentaho.aps.system.services.dataintegration.IDataIntegrationManager;

public class DataIntegrationProcessAction extends BaseAction implements IDataIntegrationProcessAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DataIntegrationProcessAction.class);

	@Override
	public String edit() {
		try {
			if (this.getProcess()==null) {
				this.addActionError(this.getText("jppentaho.Errors.task.notFound", new String[] { this.getProcessName() }));
				return this.isTransformation() ? "listKtr" : "listJob";
			}
		} catch (Throwable t) {
			_logger.error("Error editing task {}", this.getProcessName(), t);
			return FAILURE;
		}
		return this.isTransformation() ? "successKtr" : "successJob";
	}
	
	@Override
	public String runProcess() {
		try {
			if (this.getProcess()==null) {
				this.addActionError(this.getText("jppentaho.Errors.task.notFound", new String[] { this.getProcessName() }));
				return this.isTransformation() ? "listKtr" : "listJob";
			}
			if (this.isTransformation()) {
				this.getDataIntegrationManager().executeTransformation(this.getProcessName(), this.getDirectoryId(), this.getParams());
			} else {
				this.getDataIntegrationManager().executeJob(this.getProcessName(), this.getDirectoryId(), this.getParams());
			}
			this.addActionMessage(this.getText("jppentaho.Messages.task.successfullyExecuted"));
		} catch (Throwable t) {
			_logger.error("Error editing task {}", this.getProcessName(), t);
			this.addActionError(this.getText("jppentaho.Errors.task.errors"));
			return this.isTransformation() ? "errorKtr" : "errorJob";
		}
		return this.isTransformation() ? "successKtr" : "successJob";
	}
	
	public RepositoryElementInterface getProcess() {
		if (this._process==null) {
			try {
				if (this.isTransformation()) {
					this._process = this.getDataIntegrationManager().loadTransformation(this.getProcessName(), this.getDirectoryId());
				} else {
					this._process = this.getDataIntegrationManager().loadJob(this.getProcessName(), this.getDirectoryId());
				}
			} catch (Throwable t) {
				_logger.error("Error loading task {}",this.getProcessName(), t);
				throw new RuntimeException("Error loading task " + this.getProcessName(), t);
			}
		}
		return this._process;
	}
	
	public String getProcessName() {
		return _processName;
	}
	public void setProcessName(String processName) {
		this._processName = processName;
	}
	
	public String getDirectoryId() {
		return _directoryId;
	}
	public void setDirectoryId(String directoryId) {
		this._directoryId = directoryId;
	}
	
	public boolean isTransformation() {
		return _transformation;
	}
	public void setTransformation(boolean transformation) {
		this._transformation = transformation;
	}
	
	public Map<String, String> getParams() {
		return _params;
	}
	public void setParams(Map<String, String> params) {
		this._params = params;
	}
	
	protected IDataIntegrationManager getDataIntegrationManager() {
		return _dataIntegrationManager;
	}
	public void setDataIntegrationManager(IDataIntegrationManager dataIntegrationManager) {
		this._dataIntegrationManager = dataIntegrationManager;
	}
	
	private String _processName;
	private String _directoryId;
	private boolean _transformation;
	private Map<String, String> _params;
	
	private RepositoryElementInterface _process;
	
	private IDataIntegrationManager _dataIntegrationManager;
	
}