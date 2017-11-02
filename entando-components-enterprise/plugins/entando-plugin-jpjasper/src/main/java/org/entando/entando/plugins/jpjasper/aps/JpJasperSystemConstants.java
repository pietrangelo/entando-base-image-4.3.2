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
package org.entando.entando.plugins.jpjasper.aps;

/**
 * @author R.Bonazzo
 */
public interface JpJasperSystemConstants {

	/**
	 * Config item stored in *Port database, table sysconfig
	 */
	public static final String JASPER_CONFIG_ITEM = "jpjasper_config";

	// login parameters
	public static final String JASPER_PARAM_USERNAME = "j_username";
	public static final String JASPER_PARAM_PASSWORD = "j_password";
	public static final Object JASPER_PARAM_ORGANIZATION = "orgId";
	
	
    // system parameters
    public static final String JASPER_CLIENT_MANAGER = "jpjasperServerManager";
    public static final String JASPER_PARAM_NAME = "jpjasper_baseUrl";
    
    /**
     * 0 = config
     * 1 = new report
     * 2 = dashboard [supermart]
     */
    public static final String[] JASPER_ACTION = {
    	"/flow.html?_flowId=homeFlow&viewAsAdhocFrame=true&decorate=no&theme=entando&",
        "/flow.html?_flowId=adhocFlow&viewAsAdhocFrame=true&decorate=no&theme=entando&",
        "/flow.html?_flowId=dashboardRuntimeFlow&dashboardResource=%2Fsupermart%2FSupermartDashboard30&theme=entando&"};
    // server parameters
    

    static final String BASE_REST_URL = "rest/";
    static final String BASE_REST_LOGIN = "login";
    static final String BASE_REST_RESOURCES = "resources";
    static final String BASE_REST_RESOURCE = "resource";
    static final String BASE_REST_PERMISSION = "permission/";
    static final String BASE_REST_REPORT = "report";
    static final String BASE_REST_USER = "user/";
    static final String BASE_REST_ATTRIBUTE = "attribute/";
    static final String BASE_REST_ROLE = "role/";
    
    /**
     * Path to call Run report with the v2/reports service<br />
     * <pre>http://[host]:[port]/jasperserver[-pro]/<b>rest_v2/reports</b>/path/to/report.[format]?[arguments]</pre>
     */
    public static final String REST_V2_REPORTS = "rest_v2/reports";

	public static final String SESSION_PARAM_COOKIE_HEADER = "jpjasper_server_cookie";

	public static final String ROLE_ATTRIBUTE_ORGANIZATION = "jpjasper:organization";


}