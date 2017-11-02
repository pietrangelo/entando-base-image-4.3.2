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
package org.entando.entando.plugins.jpsalesforce.aps.system;

public interface JpSalesforceConstants {
	
	static final public String SALESFORCE_CONFIG_ITEM ="jpsalesforce_config";
	
	// configuration defaults
	static final public String CONFIG_DEFAULT_API_VERSION ="24.0";
	static final public String CONFIG_DEFAULT_AUTHORIZATION_NEW ="https://login.salesforce.com/services/oauth2/authorize";
	static final public String CONFIG_DEFAULT_AUTHORIZATION_TOKEN ="https://login.salesforce.com/services/oauth2/token";
	
	static final public String SALESFORCE_MANAGER = "jpSalesforceManager";
	public static final String SALESFORCE_FORM_MANAGER = "jpsalesforceSalesforceFormManager";

	public static final String NO_PROFILE_KEY = "no-prof_";
	public static final String NO_BINDING_KEY = "no-binding";
	
	public static final String CONFIG_FORM_ID = "formId"; 
	
	public static final String SESSIONPARAM_UNMANNED_SAD = "jpsalesforce_unmanned_sad";
	public static final String SESSIONPARAM_MANNED_SAD = "jpsalesforce_manned_sad";
	public static final String SESSIONPARAM_FORM_CONFIG = "jpsalesforce_form config";

	// Web to lead form parameters
	public static final String FIELD_CAMPAIGN_ID = "Campaign_ID";
	public static final String FIELD_ENCODING = "encoding";
	public static final String FIELD_OID = "oid";
	public static final String FIELD_DEBUG = "debug";
	public static final String FIELD_DEBUG_EMAIL = "debugEmail";
	
	// Labels 
	public static final String WEB_DYNAMIC_FORM_LABEL_BASE = "jpwebdynamicform_%s_%s";
	
	// taken from the DB script
	public static final String WIDGET_LEAD_NEW_CODE = "jpsalesforce_lead_new";
	public static final String WIDGET_LEAD_SEARCH_CODE = "jpsalesforce_lead_search";
}

