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
package com.agiletec.plugins.jpforum.aps.system.services.searchengine;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class ForumSearchDAOFactory implements IForumSearchDAOFactory, BeanFactoryAware {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumSearchDAOFactory.class);

	@Override
	public void init() throws Exception {
		this._subDirectory = this.getConfigManager().getConfigItem(SUB_INDEX_DIR_ITEM_NAME);
		if (_subDirectory == null) {
			throw new ApsSystemException("Item configurazione assente: " + SUB_INDEX_DIR_ITEM_NAME);
		}
	}

	@Override
	public IForumIndexerDAO getIndexer(boolean newIndex) throws ApsSystemException {
		return this.getIndexer(newIndex, this._subDirectory);
	}

	@Override
	public IForumSearcherDAO getSearcher() throws ApsSystemException {
		return this.getSearcher(this._subDirectory);
	}

	@Override
	public IForumIndexerDAO getIndexer(boolean newIndex, String subDir) throws ApsSystemException {
		IForumIndexerDAO indexerDao = (IForumIndexerDAO) this._beanFactory.getBean("jpforumIndexerDAO");
		indexerDao.init(this.getDirectory(subDir), newIndex);
		return indexerDao;
	}

	@Override
	public IForumSearcherDAO getSearcher(String subDir) throws ApsSystemException {
		IForumSearcherDAO searcherDao = (IForumSearcherDAO) this._beanFactory.getBean("jpforumSearcherDAO");
		searcherDao.init(this.getDirectory(subDir));
		return searcherDao;
	}

	@Override
	public void updateSubDir(String newSubDirectory) throws ApsSystemException {
		if (null == newSubDirectory) return;
		//System.out.println(_subDirectory + " ---- " + newSubDirectory);
		
		this.getConfigManager().updateConfigItem(SUB_INDEX_DIR_ITEM_NAME, newSubDirectory);
		String oldDir = _subDirectory;
		this._subDirectory = newSubDirectory;
		this.deleteSubDirectory(oldDir);
	}

	private File getDirectory(String subDirectory) throws ApsSystemException {
		String dirName = this.getIndexDiskRootFolder();
		if (!dirName.endsWith("/")) dirName += "/";
		dirName += subDirectory;
		_logger.debug("Directory indici: {}", dirName);
		File dir = new File(dirName);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
			_logger.debug("Creata Directory indici");
		}
		if (!dir.canRead() || !dir.canWrite()) {
			throw new ApsSystemException(dirName + " does not have r/w rights");
		}
		return dir;
	}

	@Override
	public void deleteSubDirectory(String subDirectory) {
		String dirName = this.getIndexDiskRootFolder();
		if (!dirName.endsWith("/") || !dirName.endsWith(File.separator)) 
			dirName += File.separator;
		dirName += subDirectory;
		File dir = new File(dirName);
		if (dir.exists() || dir.isDirectory()) {
			String[] filesName = dir.list();
			for (int i=0; i<filesName.length; i++) {
				File fileToDelete = new File(dirName + File.separator + filesName[i]);
				fileToDelete.delete();
			}
			dir.delete();
			_logger.debug("Cancellata SottoDirectory indici {}", subDirectory);
		}
	}

	protected String getIndexDiskRootFolder() {
		return _indexDiskRootFolder;
	}
	public void setIndexDiskRootFolder(String indexDiskRootFolder) {
		this._indexDiskRootFolder = indexDiskRootFolder;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configService) {
		this._configManager = configService;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}

	private BeanFactory _beanFactory;
	private String _indexDiskRootFolder;
	private String _subDirectory;
	private ConfigInterface _configManager;
	private final String SUB_INDEX_DIR_ITEM_NAME = "jpforumSubIndexDir";
}
