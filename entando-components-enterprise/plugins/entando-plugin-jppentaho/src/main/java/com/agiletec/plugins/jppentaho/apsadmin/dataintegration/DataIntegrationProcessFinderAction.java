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

import java.util.List;

import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jppentaho.aps.system.services.dataintegration.IDataIntegrationManager;

public class DataIntegrationProcessFinderAction extends BaseAction implements IDataIntegrationProcessFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DataIntegrationProcessFinderAction.class);
	
	@Override
	public List<RepositoryElementMetaInterface> getJobs() {
		try {
			List<RepositoryElementMetaInterface> jobs = this.getDataIntegrationManager().loadJobs();
			return jobs;
		} catch (Throwable t) {
			_logger.error("Error loading list of jobs", t);
			throw new RuntimeException("Error loading list of jobs", t);
		}
	}
	
	@Override
	public List<RepositoryElementMetaInterface> getTransformations() {
		try {
			List<RepositoryElementMetaInterface> transformations = this.getDataIntegrationManager().loadTransformations();
			return transformations;
		} catch (Throwable t) {
			_logger.error("Error loading list of transformations", t);
			throw new RuntimeException("Error loading list of transformations", t);
		}
	}
	
	protected IDataIntegrationManager getDataIntegrationManager() {
		return _dataIntegrationManager;
	}
	public void setDataIntegrationManager(IDataIntegrationManager dataIntegrationManager) {
		this._dataIntegrationManager = dataIntegrationManager;
	}
	
	private IDataIntegrationManager _dataIntegrationManager;
	
}