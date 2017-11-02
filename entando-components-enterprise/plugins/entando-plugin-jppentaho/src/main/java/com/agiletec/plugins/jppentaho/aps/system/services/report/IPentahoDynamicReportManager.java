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
package com.agiletec.plugins.jppentaho.aps.system.services.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.xml.rpc.ServiceException;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportDownload;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParamUIInfo;

/**
 * Interface for Dynamic Report Service
 * 
 * */
public interface IPentahoDynamicReportManager {
	
	public boolean isDebugEnabled() throws ApsSystemException;

	public Map<String, String> getOutputs();

	//public String getReportDefinition(ReportHttpParam params, String username, String password);

	//public void removeLocalReportDefinition(ReportHttpParam params);

	//public String getReportParameterDefinition(ReportHttpParam params, String username, String password, Map<String, String> paramsUI);

	public ReportParamUIInfo getReportParamUIInfo(String path, String name, String solution, Map<String, String> params) throws ApsSystemException, ResourceLoadingException;

	//public ReportDownload generateReportPreview(String showletTypeCode, ApsProperties showletProperties, UserDetails currentUser) throws ApsSystemException;

	public ReportDownload generateReport(ReportHttpParam httpParams, Map<String, String> params, String output);

	public ReportDownload generateReport(String path, String name, String solution, Map<String, String> params, String output);

	public ReportDownload generateReport(String showletTypeCode, ApsProperties showletProperties, 
			ReportHttpParam httpParams, Map<String, String> params, String output, boolean useCache) throws ApsSystemException;

	public List<ReportHttpParam> getReportsParam(String showletTypeCode, ApsProperties showletProperties, String username) throws MalformedURLException, RemoteException, ServiceException, ApsSystemException;

	public List<ReportHttpParam> getReportsParamFromUserShowletConfig(String solution, Integer currentFrame, String page, String username) throws ApsSystemException;

	public String generatePentahoServerPublicUrl();
	
	/**
	 * Rewriting on image urls for html report with absolute url path
	 * */
	//public String insertFullPentahoUrlForImages(String html);
	
	//public void extractReportParamUIInfo(ReportParamUIInfo info, String reportDefinitionPath, String reportParamsDefinitionPath)
	//		throws ApsSystemException, ResourceLoadingException;
	
	public ITreeNode getSolutionContents(String solution, String server, String username);

	public void saveUserConfiguration(String name, String configPath, String username, Integer internalServletFrameDest, WidgetType type, String string);

	public PentahoReportUserWidgetConfig loadUserConfiguration(String username, Integer internalServletFrameDest, String pagecode);

	public String getPathForPentaho(PentahoReportUserWidgetConfig config);
	
        /**
         * @deprecated use getReportDefinitionPath(ReportHttpParam)
         */
        public ReportDownload getReportDefinition(ReportHttpParam params);
        
	public ReportDownload getReportDefinitionPath(ReportHttpParam params);
        
	public MasterReport getReportDefinition(String reportPath) throws ResourceLoadingException, ApsSystemException;
	
	//public Boolean getPentahoServerVisibleFromClient();
	
	//public String insertServletUrlForPentahoImages(String html);

	//public void loadLocallyReportImages(String html) throws FileNotFoundException;
	
	//public String generateCompletePentahoServerLocalUrl();

	public void getReportImage(String image, ServletOutputStream out);
	
        @Deprecated
	public String getXactionUrl(String solution, String path, String action);
        
    public String buildActionUrl(ReportHttpParam reportHttpParam);
	
	//public String insertFullPentahoUrlForCSS(String html);

	//public String insertServletUrlForPentahoCSS(String html);

	public String prepareReportHtmlForInclusion(String html) throws FileNotFoundException, IOException;
	
	public void verifyReportErrors(String reportPath, ReportDownload download) throws IOException;

	
	public static final String SHOWLET_SOLUTION_PROPERTY = "solution";
	public static final String SHOWLET_PATH_PROPERTY = "path";
	public static final String SHOWLET_SERVER_PROPERTY = "server";
	public static final String SHOWLET_QUERY_STRING = "queryString";
	public static final String SHOWLET_PROFILE_PARAMS = "profileParams";
	public static final String SHOWLET_REPORT_NAME = "name";

	public static final String SHOWLET_REFRESH_PERIOD = "refreshPeriod";
	public static final String SHOWLET_REFRESH_PERIOD_UNITS_OF_MEASUREMENT = "refreshPeriodUnitsOfMeasurement";
	
	
	
	public static final String USERNAME_PARAM = "userid";
	
	public static final String PASSWORD_PARAM = "password";
	
	public static final String JPPENTAHO_SERVER_USERNAME = "jppentaho_server_username";

	public static final String JPPENTAHO_SERVER_PASSWORD = "jppentaho_server_password";
	
	public static final String JPPENTAHO_SERVER_LOCAL_URL = "jppentaho_server_local_url";

	public static final String JPPENTAHO_SERVER_LOCAL_PORT = "jppentaho_server_local_port";
	
	public static final String JPPENTAHO_SERVER_URL = "jppentaho_server_url";

	public static final String JPPENTAHO_SERVER_PORT = "jppentaho_server_port";

	public static final String JPPENTAHO_SERVER_PROTOCOL = "jppentaho_server_protocol";
	
	public static final String JPPENTAHO_SERVER_LOCAL_PROTOCOL = "jppentaho_server_local_protocol";
	
	public static final String JPPENTAHO_REPORT_DEF_BASE_PATH = "jppentaho_report_def_base_path";
	
	public static final String JPPENTAHO_SERVER_VISIBLE_FROM_CLIENT = "jppentaho_server_visible_from_client";
	
	
	
	public final static String REPORT_PARAM_VALIDATION_ERROR_CODE = "PARAM_VALIDATION";
	public final static String REPORT_NOT_FOUND_ERROR_CODE = "REPORT_NOT_FOUND";
	public final static String WEB_RESOURCE_NOT_FOUND_ERROR_CODE = "WEB_RESOURCE_NOT_FOUND";
	public final static String CONNECTION_PROBLEM_ERROR_CODE = "CONNECTION_PROBLEM";
	
	public static final String HTML_STREAM = "table/html;page-mode=stream";
	public static final String HTML_PAGINATED = "table/html;page-mode=page";
	public static final String PDF = "pageable/pdf";
	public static final String EXCEL = "table/excel;page-mode=flow";
	public static final String EXCEL_2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;page-mode=flow";
	public static final String CSV = "table/csv;page-mode=stream";
	public static final String RTF = "table/rtf;page-mode=flow";
	public static final String TEXT = "pageable/text";
	public static final String HTML_STREAM_KEY = "HTML_STREAM";
	public static final String HTML_PAGINATED_KEY = "HTML_PAGINATED";
	public static final String PDF_KEY = "PDF";
	public static final String EXCEL_KEY = "EXCEL";
	public static final String EXCEL_2007_KEY = "EXCEL_2007";
	public static final String CSV_KEY = "CSV";
	public static final String RTF_KEY = "RTF";
	public static final String TEXT_KEY = "TEXT";
	
	public static final String REPORT_DEF_THUMB = "_thumb";
	public static final String REPORT_DEF_DETT = "_dett";


}