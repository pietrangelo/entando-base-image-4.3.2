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

package com.google.code.ssm;

import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.RefreshableBean;
import com.google.code.ssm.config.EntandoMemcachedAddressProvider;

/**
 * @author S.Loru
 */
public class EntandoMemcachedCacheFactory extends CacheFactory implements RefreshableBean {

	private static final Logger _logger =  LoggerFactory.getLogger(EntandoMemcachedCacheFactory.class);
	
	@Override
	public void refresh() throws Throwable {
		List<InetSocketAddress> addresses = getAddressProvider().getAddresses();
			_logger.trace("********************** CHANGE ADDRESS **********************");
		for (InetSocketAddress inetSocketAddress : addresses) {
			_logger.trace("address: {}", inetSocketAddress.toString());
		}
			_logger.trace("********************** CHANGE ADDRESS **********************");
		((EntandoMemcachedAddressProvider) getAddressProvider()).init();
		addresses = getAddressProvider().getAddresses();
		_logger.trace("********************** CHANGE ADDRESS **********************");
		for (InetSocketAddress inetSocketAddress : addresses) {
			_logger.trace("address: {}", inetSocketAddress.toString());
		}
		_logger.trace("********************** CHANGE ADDRESS **********************");
		super.changeAddresses(addresses);
	}

}
