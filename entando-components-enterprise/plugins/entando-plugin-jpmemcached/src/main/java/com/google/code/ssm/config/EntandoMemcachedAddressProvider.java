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

package com.google.code.ssm.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

/**
 * @author S.Loru
 */
public class EntandoMemcachedAddressProvider extends DefaultAddressProvider {

	private static final Logger _logger =  LoggerFactory.getLogger(EntandoMemcachedAddressProvider.class);

	public EntandoMemcachedAddressProvider() {
	}

	
	public void init(){
		_logger.debug("init - AddressProvider");
		String configMemcachedServerAddress = this.getConfigManager().getParam(MEMCACHED_ADDRESS);
		if(StringUtils.isEmpty(configMemcachedServerAddress)){
			this.setAddress(DEFAULT_SERVER);
		} else {
			this.setAddress(configMemcachedServerAddress);
		}
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private ConfigInterface _configManager;
	
	public final static String MEMCACHED_ADDRESS = "jpmemcached_memcachedAddress";
	public final static String DEFAULT_SERVER = "127.0.0.1:11211";

	
	

}
