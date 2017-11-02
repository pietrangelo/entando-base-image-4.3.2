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
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.user.UserDetails;

@Aspect
public class SanctionManager extends AbstractService implements ISanctionManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SanctionManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void addSanction(Sanction sanction) throws ApsSystemException {
		try {
			int code = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			sanction.setId(code);
			this.getSanctionDAO().insertSanction(sanction);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta Sanzione", t);
			throw new ApsSystemException("Errore in aggiunta Sanzione", t);
		}
	}

	@Override
	public void deleteSanction(int id) throws ApsSystemException {
		try {
			this.getSanctionDAO().removeSanction(id);
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione sanzione {}", id, t);
			throw new ApsSystemException("Errore in eliminazione sanzione " + id, t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void deleteSanctions(Object key) throws ApsSystemException {
		String username = "";
		if (key instanceof String) {
			username = key.toString();
		} else if (key instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) key;
			username = userDetails.getUsername();
		}
		this.deleteSanctions(username);
	}
	
	@Override
	public void deleteSanctions(String username) throws ApsSystemException {
		try {
			this.getSanctionDAO().removeSanctions(username);
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione sanzioni per {}", username, t);
			throw new ApsSystemException("Errore in eliminazione sanzioni per " + username, t);
		}
	}

	@Override
	public Sanction getSanction(int id) throws ApsSystemException {
		Sanction sanction = null;
		try {
			sanction = this.getSanctionDAO().loadSanction(id);
		} catch (Throwable t) {
			_logger.error("Errore in recupero sanzione {}", id, t);
			throw new ApsSystemException("Errore in recupero sanzione " + id, t);
		}
		return sanction;
	}

	@Override
	public boolean isUnderSanction(String username) throws ApsSystemException {
		boolean underSanction = false;
		try {
			underSanction = this.getSanctionDAO().isUnderSanction(username);
		} catch (Throwable t) {
			_logger.error("Errore in isUnderSanction per {}",username, t);
			throw new ApsSystemException("Errore in isUnderSanction " + username, t);
		}
		return underSanction;
	}

	@Override
	public List<Integer> getSanctions(String username) throws ApsSystemException {
		List<Integer> sanctions = new ArrayList<Integer>();
		try {
			sanctions = this.getSanctionDAO().loadSanctions(username);
		} catch (Throwable t) {
			_logger.error("Errore in recupero sanzioni per {}", username, t);
			throw new ApsSystemException("Errore in recupero sanzioni per " + username, t);
		}
		return sanctions;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	protected ISanctionDAO getSanctionDAO() {
		return _sanctionDAO;
	}
	public void setSanctionDAO(ISanctionDAO sanctionDAO) {
		this._sanctionDAO = sanctionDAO;
	}
	
	private ISanctionDAO _sanctionDAO;
	private IKeyGeneratorManager _keyGeneratorManager;

}
