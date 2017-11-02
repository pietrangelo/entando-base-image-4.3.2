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
package org.entando.entando.plugins.jpsocial.aps.system.services;

import static com.rosaloves.bitlyj.Bitly.shorten;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Bitly.Provider;
import com.rosaloves.bitlyj.Url;

public class BitLyManager extends AbstractService implements IBitLyManager {

	private static final Logger _logger =  LoggerFactory.getLogger(BitLyManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready ", this.getClass().getName());
	}
	
	@Override
	public Url shortUrl(String urlToBeShorted) throws ApsSystemException {
		String userId = this.getConfigManager().getParam(JpSocialSystemConstants.BITLY_USERNAME);
    String apiKey = this.getConfigManager().getParam(JpSocialSystemConstants.BITLY_APIKEY);
		Provider bitLyProv = Bitly.as(userId, apiKey);
		Url url = bitLyProv.call(shorten(urlToBeShorted));
		return url;
	}
	
	/**
	 * @param configManager the configManager to set
	 */
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	/**
	 * @return the configManager
	 */
	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	private ConfigInterface _configManager;
	
}