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

import java.util.Iterator;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of the available REST resource for a given version. Consider that
 * some fields might not be present (hence not valorized) in older versions of
 * the API.
 * @version API v29.0
 * @author entando
 */
public class SalesforceRestResources {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceRestResources.class);

	public SalesforceRestResources(JSONObject obj) throws Throwable {
		parse(obj);
	}

	protected void parse(JSONObject obj) throws Throwable {
		try {
			_sobjects = obj.getString(SOBJECTS);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_licensing = obj.getString(LICENSING);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_identity = obj.getString(IDENTITY);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_connect = obj.getString(CONNECT);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_search = obj.getString(SEARCH);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_query = obj.getString(QUERY);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_tooling = obj.getString(TOOLING);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_chatter = obj.getString(CHATTER);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(RECENT);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(THEME);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(QUERYALL);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(ANALYTICS);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(FLEXIPAGE);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(QUICKACTIONS);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		try {
			_recent = obj.getString(APPMENU);
		}
		catch (org.json.JSONException j) {
			// FIXME DEBUG HERE!
		}
		Iterator itr = obj.keys();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			_logger.info("Available '{}' REST resource in '{}'", key, obj.getString(key));
		}
	}

	public String getSobjects() {
		return _sobjects;
	}

	public void setSobjects(String sobjects) {
		this._sobjects = sobjects;
	}

	public String getLicensing() {
		return _licensing;
	}

	protected void setLicensing(String licensing) {
		this._licensing = licensing;
	}

	public String getConnect() {
		return _connect;
	}

	protected void setConnect(String connect) {
		this._connect = connect;
	}

	public String getSearch() {
		return _search;
	}

	protected void setSearch(String search) {
		this._search = search;
	}

	public String getQuery() {
		return _query;
	}

	protected void setQuery(String query) {
		this._query = query;
	}

	public String getTooling() {
		return _tooling;
	}

	protected void setTooling(String tooling) {
		this._tooling = tooling;
	}

	public String getChatter() {
		return _chatter;
	}

	protected void setChatter(String chatter) {
		this._chatter = chatter;
	}

	public String getRecent() {
		return _recent;
	}

	protected void setRecent(String recent) {
		this._recent = recent;
	}

	public String getTheme() {
		return _theme;
	}

	protected void setTheme(String theme) {
		this._theme = theme;
	}

	public String getQueryAll() {
		return _queryAll;
	}

	protected void setQueryAll(String queryAll) {
		this._queryAll = queryAll;
	}

	public String getAnalytics() {
		return _analytics;
	}

	protected void setAnalytics(String analytics) {
		this._analytics = analytics;
	}

	public String getFlexiPage() {
		return _flexiPage;
	}

	protected void setFlexiPage(String flexiPage) {
		this._flexiPage = flexiPage;
	}

	public String getQuickActions() {
		return _quickActions;
	}

	protected void setQuickActions(String quickActions) {
		this._quickActions = quickActions;
	}

	public String getAppMenu() {
		return _appMenu;
	}

	protected void setAppMenu(String appMenu) {
		this._appMenu = appMenu;
	}

	public String getIdentity() {
		return _identity;
	}

	protected void setIdentity(String identity) {
		this._identity = identity;
	}

	private String _sobjects;
	private String _licensing;
	private String _connect;
	private String _search;
	private String _query;
	private String _tooling;
	private String _chatter;
	private String _recent;
	private String _identity;
	private String _theme;
	private String _queryAll;
	private String _analytics;
	private String _flexiPage;
	private String _quickActions;
	private String _appMenu;

	public final String SOBJECTS = "sobjects";
	public final String LICENSING = "licensing";
	public final String IDENTITY = "identity";
	public final String CONNECT = "connect";
	public final String SEARCH = "search";
	public final String QUERY = "query";
	public final String TOOLING = "tooling";
	public final String CHATTER = "chatter";
	public final String RECENT = "recent";
	public final String THEME = "theme";
	public final String QUERYALL = "queryAll";
	public final String ANALYTICS = "tooling";
	public final String FLEXIPAGE = "flexiPage";
	public final String QUICKACTIONS = "quickActions";
	public final String APPMENU = "appMenu";
}
