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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class MultisiteManager extends AbstractService implements IMultisiteManager {
	
	private static final Logger _logger =  LoggerFactory.getLogger(MultisiteManager.class);
	
	@Override
	public void init() throws ApsSystemException{
		// empty the detected IP list
		if (null != _ipconfig && !_ipconfig.isEmpty()) {
			_ipconfig.clear();
		}
		// scan network interfaces
		this.scanNetworkInterfaces();
		// lead client configuration
		this.loadConfigs();
		// match client
		//this.identifyNode();
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	private void scanNetworkInterfaces() {
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String ipAddress = localhost.getHostAddress();
			_ipconfig.add(ipAddress);
			// check if we have multiple ip addresses
			InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
			if (allMyIps != null && allMyIps.length > 1) {
				for (int i = 0; i < allMyIps.length; i++) {
					_ipconfig.add(allMyIps[i].getHostAddress());
				}
			}
			// enumerate interfaces
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress currentInetAddr = enumIpAddr.nextElement();
					_ipconfig.add(currentInetAddr.getHostAddress());
				}
			}
		} catch (Throwable t) {
			_logger.error("error in scanNetworkInterfaces", t);
		}
	}
	/*
	private void identifyNode() throws ApsSystemException {
		boolean detected = false;
		try {
			Iterator<Site> sitr = this.getSites().iterator();
			while (sitr.hasNext()) {
				Site curSite = sitr.next();
				Iterator<String> iitr = this._ipconfig.iterator();
				while (iitr.hasNext()) {
					String curIp = iitr.next();
					System.out.println("CURRENT IP = " + curIp);
					if (curIp.equals(curSite.getIp())) {
						this.setCurrentSiteCode(curSite.getCode());
						detected = true;
					}
				}
			}
			if (!detected) {
				_logger.error("Could not match this node with any instance contained in configuration");
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Error identifying the current cluster node", t);
		}
	}
	*/
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration Item not found: " + CONFIG_ITEM);
			}
			MultisiteConfigDOM dom = new MultisiteConfigDOM(xml);
			this._sites = dom.getSites();
			this._useSan = dom.isUseSan();
		} catch (Throwable t) {
			_logger.error("Error in init", t);
			throw new ApsSystemException("Error in init", t);
		}
	}

	protected String getCurrentSiteCode() {
		return _currentSiteCode;
	}
	
	/**
	 * Set the current node ID, taken from the configuration.
	 * @param currentSiteCode the current Site Code
	 * @throws ApsSystemException an exception if invoked with two different values; this usually
	 * means an improper network configuration
	 */
	@Override
	public void setCurrentSiteCode(String currentSiteCode) throws ApsSystemException {
		/*
		if (null != _currentSiteCode && !_currentSiteCode.trim().isEmpty()) {
			if (!currentSiteCode.equals(this._currentSiteCode)) {
				throw new ApsSystemException("message");
			}
			if (currentSiteCode.equals(this._currentSiteCode)) {
				// DO NOTHING
				return;
			}
		}
		*/
		this._currentSiteCode = currentSiteCode;
	}
	
	@Override
	public List<Site> getSites() {
		List<Site> sites = new ArrayList<Site>();
		sites.addAll(this._sites.values());
		return sites;
	}

	@Override
	public Site getSite(String code) {
		return (Site) this._sites.get(code);
	}

	@Override
	public Site getCurrentSite() {
		return this.getSite(this.getCurrentSiteCode());
	}

	@Override
	public List<Site> getParents() {
		List<Site> parents = new ArrayList<Site>(this._sites.values());
		parents.remove(this.getCurrentSite());
		return parents;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface manager) {
		this._configManager = manager;
	}

	public void setUseSan(boolean useSan) {
		this._useSan = useSan;
	}
	@Override
	public boolean isUseSan() {
		return _useSan;
	}
	
	private String _currentSiteCode;
	
	private Map<String, Site> _sites = new HashMap<String, Site>();
	private boolean _useSan;

	private ConfigInterface _configManager;
	private List<String> _ipconfig = new ArrayList<String>(); 

	private static final String CONFIG_ITEM = "jpremotenotify_sitesConfig";

}
