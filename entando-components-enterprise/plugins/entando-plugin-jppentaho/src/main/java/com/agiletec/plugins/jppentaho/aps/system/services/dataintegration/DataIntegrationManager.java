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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.RepositoryObjectType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfig;

public class DataIntegrationManager implements IDataIntegrationManager {

	private static final Logger _logger =  LoggerFactory.getLogger(DataIntegrationManager.class);

	@Override
	public List<RepositoryElementMetaInterface> loadJobs() throws ApsSystemException {
		List<RepositoryElementMetaInterface> jobs = new ArrayList<RepositoryElementMetaInterface>();
		Repository rep = null;
		try {
			rep = this.openRepository();
			RepositoryDirectoryInterface directory = rep.loadRepositoryDirectoryTree();
			this.addDirectoryTreeJobs(directory, rep, jobs);
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error loading jobs", t);
			throw new ApsSystemException("Error loading jobs", t);
		} finally {
			this.closeRepository(rep);
		}
		return jobs;
	}
	
	@Override
	public List<RepositoryElementMetaInterface> loadTransformations() throws ApsSystemException {
		List<RepositoryElementMetaInterface> transformations = new ArrayList<RepositoryElementMetaInterface>();
		Repository rep = null;
		try {
			rep = this.openRepository();
			RepositoryDirectoryInterface directory = rep.loadRepositoryDirectoryTree();
			this.addDirectoryTreeTransformations(directory, rep, transformations);
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error loading transformations", t);
			throw new ApsSystemException("Error loading transformations", t);
		} finally {
			this.closeRepository(rep);
		}
		return transformations;
	}
	
	@Override
	public RepositoryElementInterface loadJob(String name, String directoryId) throws ApsSystemException {
		Repository rep = null;
		JobMeta jobMeta = null;
		try {
			rep = this.openRepository();
			jobMeta = this.loadJobMeta(name, directoryId, rep);
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error loading job {}", name , t);
			throw new ApsSystemException("Errore in recupero del job '" + name + "'", t);
		} finally {
			this.closeRepository(rep);
		}
		return jobMeta;
	}
	
	@Override
	public RepositoryElementInterface loadTransformation(String name, String directoryId) throws ApsSystemException {
		Repository rep = null;
		TransMeta transMeta = null;
		try {
			rep = this.openRepository();
			transMeta = this.loadTransMeta(name, directoryId, rep);
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error loading transformation {}", name , t);
			throw new ApsSystemException("Errore in recupero della trasformazione '" + name + "'", t);
		} finally {
			this.closeRepository(rep);
		}
		return transMeta;
	}
	
	@Override
	public void executeJob(String name, String directoryId, Map<String, String> params) throws ApsSystemException {
		Repository rep = null;
		Job job = null;
		try {
			rep = this.openRepository();
			job = this.prepareJob(name, directoryId, params, rep);
			job.start();
			job.waitUntilFinished();
			this.checkClosed(job);
			if (job.getErrors()>0) {
				throw new ApsSystemException("Si sono verificati degli errori in esecuzione del Job");
			}
		} catch (ApsSystemException t) {
			throw t;
		} catch (Throwable t) {
			_logger.error("Error executing job {}", name, t);
			throw new ApsSystemException("Errore in esecuzione del job '" + name + "'", t);
		} finally {
			this.closeRepository(rep);
		}
	}
	
	@Override
	public void executeTransformation(String name, String directoryId, Map<String, String> params) throws ApsSystemException {
		Repository rep = null;
		Trans trans = null;
		try {
			rep = this.openRepository();
			trans = this.prepareTransformation(name, directoryId, params, rep);
			trans.execute(new String[] { });
			trans.waitUntilFinished();
			this.checkClosed(trans);
			if (trans.getErrors()>0) {
				throw new ApsSystemException("Si sono verificati degli errori in esecuzione della trasformazione");
			}
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error executing transformation {}", name, t);
			throw new ApsSystemException("Errore in esecuzione della trasformazione '" + name + "'", t);
		} finally {
			this.closeRepository(rep);
		}
	}
	
	private Repository openRepository() throws ApsSystemException {
		PentahoConfig config = this.getPentahoConfigManager().getConfig();
                try {
			if (!KettleEnvironment.isInitialized()) {
				KettleEnvironment.init();
			}
			RepositoriesMeta repsinfo = new RepositoriesMeta();
			if (!repsinfo.readData()) {
				throw new ApsSystemException("Errore in apertura repository per 'Data Integration' - Nessun repository trovato");
			}
			//RepositoryMeta repositoryMeta = repsinfo.findRepository(this.getRepositoryName());
                        RepositoryMeta repositoryMeta = repsinfo.findRepository(config.getDataIntegrationRepositoryName());
			if (repositoryMeta == null) {
				throw new ApsSystemException("Errore in apertura repository per 'Data Integration' - "
                                        + "Repository '" + config.getDataIntegrationRepositoryName() + "' non trovato");
			}
			Repository rep = PluginRegistry.getInstance().loadClass(RepositoryPluginType.class, repositoryMeta, Repository.class);
			rep.init(repositoryMeta);
			//rep.connect(this.getUsername(), this.getPassword());
                        rep.connect(config.getDataIntegrationUsername(), config.getDataIntegrationPassword());
			return rep;
		} catch (ApsSystemException t) {
			throw t;
		} catch (Throwable t) {
			_logger.error("Error opening 'Data Integration' repository", t);
			throw new ApsSystemException("Errore in apertura repository '" + config.getDataIntegrationRepositoryName() + "' per 'Data Integration'", t);
		}
	}
	
	private void closeRepository(Repository rep) {
		if (rep!=null && rep.isConnected()) {
			rep.disconnect();
		}
	}
	
	private RepositoryDirectoryInterface loadDirectory(String directoryId, Repository rep) throws ApsSystemException {
		try {
			RepositoryDirectoryInterface directory = rep.loadRepositoryDirectoryTree();
			if (directoryId!=null && directoryId.length()>0 && !directoryId.equals("/")) {
				directory = directory.findDirectory(directoryId);
			}
			return directory;
		} catch (Throwable t) {
			_logger.error("Error loading directory {}",directoryId, t);
			throw new ApsSystemException("Errore in recupero directory '" + directoryId + "'");
		}
	}
	
	private Trans prepareTransformation(String name, String directoryId, Map<String, String> params, Repository rep) throws KettleException, ApsSystemException {
		TransMeta transMeta = this.loadTransMeta(name, directoryId, rep);
		Trans trans = new Trans(transMeta);
		trans.setRepository(rep);
		
		trans.initializeVariablesFrom(null);
		trans.getTransMeta().setInternalKettleVariables(trans);
		if (params!=null) {
			Iterator<Entry<String, String>> paramsIter = params.entrySet().iterator();
			while (paramsIter.hasNext()) {
				Entry<String, String> param = paramsIter.next();
				trans.setParameterValue(param.getKey(), param.getValue());
			}
		}
		trans.activateParameters();
		trans.setSafeModeEnabled(true);
		return trans;
	}
	
	private TransMeta loadTransMeta(String name, String directoryId, Repository rep) throws KettleException, ApsSystemException {
		RepositoryDirectoryInterface directory = this.loadDirectory(directoryId, rep);
		if (directory==null) {
			throw new ApsSystemException("Directory '" + directoryId + "' not found");
		}
		if (!rep.exists(name, directory, RepositoryObjectType.TRANSFORMATION)) {
			throw new ApsSystemException("Transformation '" + name + "' not found on repository");
		}
		TransMeta transMeta = rep.loadTransformation(name, directory, null, true, null);
		return transMeta;
	}
	
	private void addDirectoryTreeTransformations(RepositoryDirectoryInterface directory, 
			Repository rep, List<RepositoryElementMetaInterface> transformations) throws KettleException {
		transformations.addAll(rep.getTransformationObjects(directory.getObjectId(), false));
		Iterator<RepositoryDirectoryInterface> subDirectories = directory.getChildren().iterator();
		while (subDirectories.hasNext()) {
			this.addDirectoryTreeTransformations(subDirectories.next(), rep, transformations);
		}
	}
	
	private void checkClosed(Trans trans) throws ApsSystemException {
		if (trans!=null && trans.isRunning()) {
			for(int i = 0; i < 100; i++) {
				if(!trans.isRunning()) {
					break;
				}
				try{
					Thread.sleep(100);
				} catch (Exception e) {
					break;
				}
			}
			if(trans.isRunning()) {
				throw new ApsSystemException("Transformation '" + trans.getName() + "' not successfully completed");
			}
		}
	}
	
	private Job prepareJob(String name, String directoryId, Map<String, String> params, Repository rep) throws KettleException, ApsSystemException {
		JobMeta jobMeta = this.loadJobMeta(name, directoryId, rep);
		Job job = new Job(rep, jobMeta);
		if (params!=null) {
			Iterator<Entry<String, String>> paramsIter = params.entrySet().iterator();
			while (paramsIter.hasNext()) {
				Entry<String, String> param = paramsIter.next();
				jobMeta.setParameterValue(param.getKey(), param.getValue());
			}
		}
		return job;
	}
	
	private JobMeta loadJobMeta(String name, String directoryId, Repository rep) throws KettleException, ApsSystemException {
		RepositoryDirectoryInterface directory = this.loadDirectory(directoryId, rep);
		if (directory==null) {
			throw new ApsSystemException("Directory '" + directoryId + "' non trovata");
		}
		if (!rep.exists(name, directory, RepositoryObjectType.JOB)) {
			throw new ApsSystemException("Job '" + name + "' non presente sul repository");
		}
		JobMeta jobMeta = rep.loadJob(name, directory, null, null);
		return jobMeta;
	}
	
	private void addDirectoryTreeJobs(RepositoryDirectoryInterface directory, 
			Repository rep, List<RepositoryElementMetaInterface> jobs) throws KettleException {
		jobs.addAll(rep.getJobObjects(directory.getObjectId(), false));
		Iterator<RepositoryDirectoryInterface> subDirectories = directory.getChildren().iterator();
		while (subDirectories.hasNext()) {
			this.addDirectoryTreeJobs(subDirectories.next(), rep, jobs);
		}
	}
	
	private void checkClosed(Job job) throws ApsSystemException {
		if (job!=null && !job.isFinished()) {
			for(int i = 0; i < 100; i++) {
				if(job.isFinished()) {
					break;
				}
				try{
					Thread.sleep(100);
				} catch (Exception e) {
					break;
				}
			}
			if(!job.isFinished()) {
				throw new ApsSystemException("Job '" + job.getName() + "' non terminato correttamente");
			}
		}
	}
	/*
	protected String getRepositoryName() {
		return _repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this._repositoryName = repositoryName;
	}
	
	protected String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	protected String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}
	
	private String _repositoryName;
	private String _username;
	private String _password;
	*/
        
    protected IPentahoConfigManager getPentahoConfigManager() {
        return pentahoConfigManager;
    }

    public void setPentahoConfigManager(IPentahoConfigManager pentahoConfigManager) {
        this.pentahoConfigManager = pentahoConfigManager;
    }
        
        
        private IPentahoConfigManager pentahoConfigManager;

}