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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.keygenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorDAO;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class KeyGeneratorManager extends AbstractService implements IKeyGeneratorManager {

	private static final Logger _logger =  LoggerFactory.getLogger(KeyGeneratorDAO.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public int getUniqueKeyCurrentValue() throws ApsSystemException {
		int key = 0;
		try {
			key = this.getKeyGeneratorDAO().getUniqueKey();
		} catch (Throwable t) {
			_logger.error("error in getUniqueKeyCurrentValue", t);
		}
		return key;
	}

	public void setKeyGeneratorDAO(IKeyGeneratorDAO keyGeneratorDAO) {
		this._keyGeneratorDAO = keyGeneratorDAO;
	}
	public IKeyGeneratorDAO getKeyGeneratorDAO() {
		return _keyGeneratorDAO;
	}

	private IKeyGeneratorDAO _keyGeneratorDAO;

}
