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
package com.agiletec.plugins.jppentaho.aps.system.services.dataintegration;

import java.util.List;
import java.util.Map;

import org.pentaho.di.repository.RepositoryElementInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IDataIntegrationManager {
	
	public List<RepositoryElementMetaInterface> loadJobs() throws ApsSystemException;
	
	public List<RepositoryElementMetaInterface> loadTransformations() throws ApsSystemException;
	
	public RepositoryElementInterface loadJob(String name, String directoryId) throws ApsSystemException;
	
	public RepositoryElementInterface loadTransformation(String name, String directoryId) throws ApsSystemException;
	
	public void executeJob(String name, String directoryId, Map<String, String> params) throws ApsSystemException;
	
	public void executeTransformation(String name, String directoryId, Map<String, String> params) throws ApsSystemException;
	
}