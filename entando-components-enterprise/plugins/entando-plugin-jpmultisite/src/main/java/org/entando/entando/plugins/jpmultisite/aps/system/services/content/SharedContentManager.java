/*
 *
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpmultisite.aps.system.services.content;

import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.event.SharedContentChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedContentManager extends AbstractService implements ISharedContentManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SharedContentManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public SharedContent getSharedContent(int id) throws ApsSystemException {
		SharedContent sharedContent = null;
		try {
			sharedContent = this.getSharedContentDAO().loadSharedContent(id);
		} catch (Throwable t) {
			_logger.error("Error loading sharedContent with id '{}'", id,  t);
			throw new ApsSystemException("Error loading sharedContent with id: " + id, t);
		}
		return sharedContent;
	}

	@Override
	public List<Integer> getSharedContents() throws ApsSystemException {
		List<Integer> sharedContents = new ArrayList<Integer>();
		try {
			sharedContents = this.getSharedContentDAO().loadSharedContents();
		} catch (Throwable t) {
			_logger.error("Error loading SharedContent list",  t);
			throw new ApsSystemException("Error loading SharedContent ", t);
		}
		return sharedContents;
	}

	@Override
	public List<Integer> searchSharedContents(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> sharedContents = new ArrayList<Integer>();
		try {
			sharedContents = this.getSharedContentDAO().searchSharedContents(filters);
		} catch (Throwable t) {
			_logger.error("Error searching SharedContents", t);
			throw new ApsSystemException("Error searching SharedContents", t);
		}
		return sharedContents;
	}

	@Override
	public void addSharedContent(SharedContent sharedContent) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			sharedContent.setId(key);
			this.getSharedContentDAO().insertSharedContent(sharedContent);
			this.notifySharedContentChangedEvent(sharedContent, SharedContentChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding SharedContent", t);
			throw new ApsSystemException("Error adding SharedContent", t);
		}
	}
 
	@Override
	public void updateSharedContent(SharedContent sharedContent) throws ApsSystemException {
		try {
			this.getSharedContentDAO().updateSharedContent(sharedContent);
			this.notifySharedContentChangedEvent(sharedContent, SharedContentChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating SharedContent", t);
			throw new ApsSystemException("Error updating SharedContent " + sharedContent, t);
		}
	}

	@Override
	public void deleteSharedContent(int id) throws ApsSystemException {
		try {
			SharedContent sharedContent = this.getSharedContent(id);
			this.getSharedContentDAO().removeSharedContent(id);
			this.notifySharedContentChangedEvent(sharedContent, SharedContentChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting SharedContent with id {}", id, t);
			throw new ApsSystemException("Error deleting SharedContent with id:" + id, t);
		}
	}

	private void notifySharedContentChangedEvent(SharedContent sharedContent, int operationCode) {
		SharedContentChangedEvent event = new SharedContentChangedEvent();
		event.setSharedContent(sharedContent);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setSharedContentDAO(ISharedContentDAO sharedContentDAO) {
		 this._sharedContentDAO = sharedContentDAO;
	}
	protected ISharedContentDAO getSharedContentDAO() {
		return _sharedContentDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ISharedContentDAO _sharedContentDAO;
}
