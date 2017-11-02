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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

/**
 * This class implements Entando APIs for salesforce. It's important to
 * understand that along this plugin there are two ways interact with 
 * Salesforce, manned and unmanned:
 *  - manned means that there is a user which explicitly initiates the 
 *    connection to salesforce; users are redirected to the login page of 
 *    Salesforce where username and password are required, and permissions
 *    granted to an app which will work on their behalf. Then they are 
 *    redirected back to the Entando portal.
 *  - unmanned means that operations on Salesforce are not supervised by a
 *    human being, thus the data required for the login have to be present
 *    in the configuration.
 * Manned and unmanned login require different parameters in the configuration
 * and we don't force users to enter the complete configuration: to check
 * whether the configuration is enough for manned or unmanned operations there
 * are two methods that developer can invoke on the 'SalesforceConfigVO' object
 * to make sure all the data needed are at least present.
 * Furthermore developer are completely free to override the default 
 * configuration providing methods with the proper configuration data to
 * perform the desired operation.
 * The login operation always returns a SalesforceAccessDescriptor object which
 * must be included in all the invocations of this manager.
 * 
 * IMPORTANT:
 * 
 * Salesforce makes available to users different versions of their API; the system
 * enumerates them and tries to load the rest resources associated to the 
 * preferred version declared in the configuration.
 * Please note that currently if the preferred version is neither declared nor 
 * present the oldest (that is, the first returned) is used.
 * 
 * The object SalesforceRestResources is set up on the first login and used
 * for every interaction with Salesforce. If developers wish to use another
 * version they have to login again providing a different version in the
 * configuration. 
 * 
 * @author entando
 * 
 */
public class SalesforceManager extends AbstractService implements ISalesforceManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceManager.class);

	/**
	 * Load the system configuration and get the version of the API to use.
	 */
	@Override
	public void init() throws Exception {
		try {
			loadConfiguration();
		} catch (Throwable t) {
			_logger.error("Error detected during service initialization", t);
			throw new ApsSystemException("Error detected during Salesforce service initialization");
		}
		_logger.info("{} ready", this.getClass().getName());
	}
	
	/**
	 * Load the configuration for the service
	 * @throws ApsSystemException 
	 */
	private void loadConfiguration() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpSalesforceConstants.SALESFORCE_CONFIG_ITEM);
			_config = new SalesforceConfig(xml);
			if (_config.isValidForUnmannedLogin()) {
				_logger.warn("The configuration is not sufficient for unmanned connection to Salesforce");
			}
			if (_config.isValidForMannedLogin()) {
				_logger.warn("The configuration is not sufficient for manned connection to Salesforce");
			}
		} catch (Throwable t) {
			_logger.error("Error loading Salesforce configuration from database");
			throw new ApsSystemException("Error loading Salesforce configuration from database", t);
		}
		_logger.info("Configuration loaded succesfully");
	}
	
	
	/**
	 * Get from the configuration the preferred version of the API to use.
	 * NOTE: this gets called on the first login
	 * @param config
	 * @param sad
	 * @throws Throwable
	 */
	private void setApiVersionToUse(SalesforceConfig config, SalesforceAccessDescriptor sad) throws Throwable {
		_logger.info("Trying to load API descriptor '{}'", config.getPreferredVersion());
		ApiVersionDescriptor avd = this.getVersionForUse(config.getPreferredVersion());
		_logger.info("Using version '{}'", avd.getVersion());
		_sarr = getLoginDAO().getRestResource(avd, sad);
	}
	
	/**
	 * Get the API version to use within the manager. If the preferred version is
	 * neither specified nor present we use the first returned by Salesforce which
	 * we __assume__ to be the oldest available.
	 * @param preferred
	 * @return
	 */
	protected ApiVersionDescriptor getVersionForUse(String preferred) throws Throwable {
		ApiVersionDescriptor avd = null;
		List<ApiVersionDescriptor> aav = getLoginDAO().listAvailableApiVersion();
		avd = aav.get(0);
		if (StringUtils.isNotBlank(preferred)) {
			for (ApiVersionDescriptor cur : aav) {
				if (preferred.equals(cur.getVersion())) {
					avd = cur;
					break;
				}
			}
		}
		return avd;
	}

	@Override
	public SalesforceAccessDescriptor login() throws Throwable {
		SalesforceConfig config = this.getConfig();
		return login(config);
	}

	@Override
	public SalesforceAccessDescriptor login(SalesforceConfig config) throws Throwable {
		SalesforceAccessDescriptor sad = null;

		try {
			sad = this.getLoginDAO().login(config);
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
		} catch (Throwable t) {
			_logger.error("Login failed", t);
			throw new ApsSystemException("Login failed");
		}
		return sad;
	}
	
	@Override
	public List<Lead> getLeads(SalesforceAccessDescriptor sad) throws Throwable {
		List<Lead> list = null;
		SalesforceConfig config = getConfig();

		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			list = this.getLeadDAO().getLeads(_sarr, sad);
		} catch (Throwable t) {
			_logger.error("Error getting leads list", t);
			throw new ApsSystemException("Error getting the list of leads", t);
		}
		return list;
	}
	
	@Override
	public Lead getLead(String id, SalesforceAccessDescriptor sad) throws Throwable {
		Lead lead = null;
		SalesforceConfig config = getConfig();

		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			JSONObject json = this.getLeadDAO().getLead(id, _sarr, sad);
			if (null != json) {
				lead = new Lead(json);
			}
		} catch (Throwable t) {
			_logger.error("Error loading lead", t);
			throw new ApsSystemException("Error loading lead");
		}
		return lead;
	}
	
	@Override
	public void deleteLead(String id, SalesforceAccessDescriptor sad) throws Throwable {
		SalesforceConfig config = getConfig();

		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			getLeadDAO().deleteLead(id, _sarr, sad);
		} catch (Throwable t) {
			_logger.error("Error deleting lead", t);
			throw new ApsSystemException("Error deleting lead");
		}
	}
	
	@Override
	public void updateLead(Lead lead, SalesforceAccessDescriptor sad) throws Throwable {
		JSONObject jobj = null;
		SalesforceConfig config = getConfig();
		
		try {
			jobj = new JSONObject(lead.getJSON().toString());
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			getLeadDAO().updateLead(jobj, _sarr, sad);
		} catch (Throwable t) {
			_logger.error("Error loading lead", t);
			throw new ApsSystemException("Error updating lead");
		}
	}
	
	@Override
	public String addLead(JSONObject obj, SalesforceAccessDescriptor sad) throws Throwable {
		SalesforceConfig config = getConfig();
		String id = null;

		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			id = getLeadDAO().addLead(obj, _sarr, sad);
		} catch (Throwable t) {
			_logger.error("New lead creation failed", t);
			throw new ApsSystemException("New lead creation failed");
		}
		return id;
	}
	
	@Override
	public String addLead(Lead lead, SalesforceAccessDescriptor sad) throws Throwable {
		SalesforceConfig config = getConfig();
		String id = null;
		
		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			id = getLeadDAO().addLead(lead.getJSON(), _sarr, sad);
		} catch (Throwable t) {
			_logger.error("New lead creation failed", t);
			throw new ApsSystemException("New lead creation failed");
		}
		return id;
	}
	
	@Override
	public List<String> searchLeadAllFields(String text, SalesforceAccessDescriptor sad) throws Throwable {
		SalesforceConfig config = getConfig();
		List<String> list = new ArrayList<String>();

		try {
			if (null == _sarr) {
				setApiVersionToUse(config, sad);
			}
			
			if (StringUtils.isNotBlank(text)) {
				list = getLeadDAO().searchAllFields(text, _sarr, sad);
			} else {
				/*
				 * NOTE! This will lead (no pun!) to an error if the search text length is smaller than two chars.
				 * Furthermore to keep the standard behavior, if a null search string is passed we return all
				 * leads
				 */
				List<Lead> leads = getLeadDAO().getLeads(_sarr, sad);
				for (Lead lead: leads) {
					list.add(lead.getId());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error searching for leads", t);
			throw new ApsSystemException("Error searching for leads");
		}
		return list;
	}
	
	@Override
	public List<Campaign> getCampaigns(SalesforceAccessDescriptor sad) throws Throwable {
		List<Campaign> list = null;

		try {
			list = this.getCampaignDAO().getCampaigns(_sarr, sad);
		} catch (Throwable t) {
			_logger.error("Error getting campaigns list", t);
			throw new ApsSystemException("Error getting the list of campaigns", t);
		}
		return list;
	}
	
	@Override
	public Lead getLeadPrototype(SalesforceAccessDescriptor sad) throws Throwable {
		Lead lead = null;

		try {
			JSONObject jdescr = getLeadDAO().describe(_sarr, sad);
			lead = new Lead(jdescr);
		} catch (Throwable t) {
			_logger.error("Error getting lead prototype", t);
			throw new ApsSystemException("Error getting Lead prototype", t);
		}
		return lead;
	}
	
	@Override
	public boolean submitWeb2LeadForm(String campaignId, Map<String, String> map) throws Throwable {
		boolean submitted = false;
		SalesforceConfig config = getConfig();
		String instance = null;
		
		try {
			if (_w2l == null) {
				_w2l = login();
			}
			instance = _w2l.getInstaceUrl();
			submitted = getLeadDAO().submitWeb2LeadForm(instance, config.getOid(), campaignId, map, config.getUserId());
		} catch (Throwable t) {
			_logger.error("Error submitting a web2lead form", t);
			throw new ApsSystemException("Error submitting a web2lead form", t);
		}
		return submitted;
	}
	
	/**
	 * Return the URL needed in order to login a user with the given configuration
	 * @param config
	 * @return null if the configuration is insufficient, a url otherwise
	 * @throws Throwable
	 */
	protected final String getLoginUrl(SalesforceConfig config) throws Throwable {
		String url = null;
		StringBuilder logUrl = new StringBuilder();

		if (null != config
				&& config.isValidForUnmannedLogin()) {
			logUrl.append(config.getTokenEndpoint());
			logUrl.append("?grant_type=password");
			logUrl.append("&client_id=");
			logUrl.append(config.getAppId());
			logUrl.append("&client_secret=");
			logUrl.append(config.getAppSecret());
			logUrl.append("&username=");
			logUrl.append(config.getUserId());
			logUrl.append("&password=");
			logUrl.append(config.getUserPwd());
			logUrl.append(config.getSecurityToken());
			url = logUrl.toString();
		} else {
			_logger.debug("warning: could not generate the login url for unmanned operation");
		}
		return url;
	}
	
	/**
	 * Return the login address from the default configuration
	 * @return
	 * @throws Throwable
	 */
	protected final String getLoginUrl() throws Throwable {
		SalesforceConfig config = this.getConfig();
		return getLoginUrl(config);
	}

	@Override
	public void updateConfiguration(SalesforceConfig config) throws ApsSystemException {
		try {
			String xml = config.toXML();
			
			this.getConfigManager().updateConfigItem(JpSalesforceConstants.SALESFORCE_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			throw new ApsSystemException("Error updating Salesforce configuration", t);
		}
	}
	
	@Override
	public SalesforceConfig resetEndpoints(SalesforceConfig config) throws ApsSystemException {
		try {
			if (null == config) {
				config = _config;
			}
			config.setLoginEndpoint(JpSalesforceConstants.CONFIG_DEFAULT_AUTHORIZATION_NEW);
			config.setTokenEndpoint(JpSalesforceConstants.CONFIG_DEFAULT_AUTHORIZATION_TOKEN);
			updateConfiguration(config);
		} catch (Throwable t) {
			throw new ApsSystemException("Error resetting Salesforce configuration", t);
		}
		return config;
	}
	
	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	public ILoginDAO getLoginDAO() {
		return _loginDAO;
	}
	
	public void setLoginDAO(ILoginDAO loginDAO) {
		this._loginDAO = loginDAO;
	}
	
	public ILeadDAO getLeadDAO() {
		return _leadDAO;
	}
	
	public void setLeadDAO(ILeadDAO leadDAO) {
		this._leadDAO = leadDAO;
	}
	
	public ICampaignDAO getCampaignDAO() {
		return _campaignDAO;
	}

	public void setCampaignDAO(ICampaignDAO campaignDAO) {
		this._campaignDAO = campaignDAO;
	}
	
	@Override
	public SalesforceConfig getConfig() {
		String xml = _config.toXML();
		SalesforceConfig copy = new SalesforceConfig(xml);
		return copy;
	}
	
	protected void setConfig(SalesforceConfig config) {
		this._config = config;
	}
	
	
	// current configuration of the system
	private SalesforceConfig _config;
	// current REST API descriptor in use 
	private SalesforceRestResources _sarr;
	// Access descriptor used only for the web-to-lead operation
	private SalesforceAccessDescriptor _w2l;
	
	private ConfigInterface _configManager;
	private ILeadDAO _leadDAO;
	private ILoginDAO _loginDAO;
	private ICampaignDAO _campaignDAO;
	
}
