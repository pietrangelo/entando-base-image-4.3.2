/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.servlet.ServletOutputStream;
import javax.xml.rpc.ServiceException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.entando.entando.plugins.jplicense.aps.system.services.license.ILicenseManager;
import org.entando.entando.plugins.jplicense.aps.system.services.license.ILicenseUtilizer;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;
import org.pentaho.reporting.engine.classic.core.parameters.ReportParameterDefinition;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.cache.ICacheManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jppentaho.aps.system.JpPentahoSystemConstants;
import com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfig;
import com.agiletec.plugins.jppentaho.aps.system.services.config.event.PentahoConfigChangedEvent;
import com.agiletec.plugins.jppentaho.aps.system.services.config.event.PentahoConfigChangedObserver;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.FileItem;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.FileNode;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserShowletConfig;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportDownload;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParamUIInfo;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;
import com.agiletec.plugins.jppentaho.aps.system.services.report.parse.ReportListDOM;
import com.agiletec.plugins.jppentaho.aps.system.services.report.parse.ReportParametersDefinitionDOM;
import com.agiletec.plugins.jppentaho.aps.system.services.report.parse.SolutionDOM;
import com.agiletec.plugins.jppentaho.aps.ws.IPentahoWebService;

/**
 * Manage interaction with the pentaho bi-server
 * @author zuanni - g.cocco
 */
public class PentahoDynamicReportManager extends AbstractService implements IPentahoDynamicReportManager, PentahoConfigChangedObserver, ILicenseUtilizer {
    
    //protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PentahoDynamicReportManager.class);
    @Override
    public void init() throws Exception {
        PentahoConfig config = this.getPentahoConfigManager().getConfig();
        this.setAppBaseUrl(this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL));
        this.setPentahoServerUrl(config.getServerUrl());
        this.setPentahoServerPort(config.getServerPort());
        this.setPentahoServerProtocol(config.getServerProtocol());
        this.setPentahoServerLocalUrl(config.getServerLocalUrl());
        this.setPentahoServerLocalPort(config.getServerLocalPort());
        this.setPentahoServerLocalProtocol(config.getServerLocalProtocol());
        
        this.setPentahoContextPath(config.getServerContextPath());
        
        this._pentahoReportDefBasePath = config.getReportDefBasePath();
        this._pentahoServerVisibleFromClient = config.isServerVisibleFromClient();
        this.loadOutputTypes();
        this.setPentahoServerUsername(config.getServerUsername());
        this.setPentahoServerPassword(config.getServerPassword());
        ApsSystemUtils.getLogger().info("init, server local url  " + getPentahoServerLocalUrl() + " , remote url " + getPentahoServerUrl() + " , base path " + _pentahoReportDefBasePath);
        //LOG.info("init, server local url  " + getPentahoServerLocalUrl() + " , remote url " + getPentahoServerUrl() + " , base path " + _pentahoReportDefBasePath);
    }

    @Override
    public boolean isDebugEnabled() throws ApsSystemException {
		if (!this.checkLicenseWithError()) {
			return false;
		}
        return this.getPentahoConfigManager().getConfig().isDebug();
    }

    @Override
    public void updateFromConfigChanged(PentahoConfigChangedEvent event) {
        try {
            this.release();
            this.refresh();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "updateFromConfigChanged");
        }
    }

    public String getReport(Properties properties) throws Throwable {
        return "only html report";
    }

    public String getReportToHtml(Properties properties) throws Throwable {
        //TODO verifica credenziali accesso ed estrapolazione azienda
        String html = null;
        try {
            String queryString = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_QUERYSTRING);
            Map<String, String> params = new HashMap<String, String>();
            if (null != queryString && queryString.length() > 0) {
                params = this.helperPentahoTag.fromQueryStringToParamsMap(queryString);
            }
            ReportHttpParam httpParams = this.extractReportHttpParamForApi(properties);
            ApsProperties apsProperties = new ApsProperties(properties);
            Integer minuteDelay = this.extractRefreshMinute(apsProperties);
            if (null != minuteDelay) {
                String cackeKey = this.createReportCacheKey(null, httpParams, params, null);
                html = (String) this.getCacheManager().getFromCache(cackeKey, minuteDelay * 60);
                if (null == html) {
                    html = this.generateReportApiResponse(httpParams, params);
                    //String groupName = JpPentahoSystemConstants.SHOWLET_TYPE_CACHE_GROUP_PREFIX + showletTypeCode;
                    this.getCacheManager().putInCache(cackeKey, html/*, new String[]{groupName}*/);
                    //System.out.println("INSERITO RISPOSTA API CACHE - period " +minuteDelay+ " - KEY " + cackeKey);
                } //else {
                //System.out.println("PRESA RISPOSTA API DALLA CACHE - period " +minuteDelay+ " - KEY " + cackeKey);
                //}
            } else {
                html = this.generateReportApiResponse(httpParams, params);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "generate report");
        }
        return html;
    }

    private String generateReportApiResponse(ReportHttpParam httpParams, Map<String, String> params) throws Throwable {
        ReportDownload reportDownload = this.generateReport(httpParams, params, null);
        if (reportDownload.isValidationKO()) {
            // TODO valutare la gestione
            // this.addActionError("validation.failed");
        }
        String html = this.helperPentahoTag.convertStreamToString(reportDownload);
        html = this.prepareReportHtmlForInclusion(html);
        if (html.indexOf("<!DOCTYPE") != -1) {
            html = html.substring(html.indexOf("<!DOCTYPE"), html.length()).trim();
        }
        return html;
    }

    private ReportHttpParam extractReportHttpParamForApi(Properties properties) {
        ReportHttpParam httpParams = null;
        try {
            String path = null;
            String name = null;
            String solution = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_SOLUTION);
            String detail = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_REPORTDETAIL);
            if (detail != null && detail.equalsIgnoreCase("true")) {
                path = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_PATH_DETAIL);
                name = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_NAME_DETAIL);
            } else {
                path = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_PATH_PREVIEW);
                name = properties.getProperty(JpPentahoSystemConstants.PARAM_SERVICE_REPORT_NAME_PREVIEW);
            }
            httpParams = new ReportHttpParam(name, path, solution);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "extractReportHttpParamForApi");
        }
        return httpParams;
    }

    @Override
    public List<ReportHttpParam> getReportsParam(String showletTypeCode, ApsProperties showletProperties, String username) throws MalformedURLException, RemoteException, ServiceException, ApsSystemException {
    	if (!this.checkLicenseWithError()) {
			return null;
		}
    	List<ReportHttpParam> params = null;
        try {
            if (null == showletProperties) {
                throw new ApsSystemException("Null showlet params - reports required from '" + username + "' user");
            }
            String solution = showletProperties.getProperty(SHOWLET_SOLUTION_PROPERTY);
            String path = showletProperties.getProperty(SHOWLET_PATH_PROPERTY);
            String server = this.generateCompletePentahoServerLocalUrl();
            Integer minuteDelay = this.extractRefreshMinute(showletProperties);
            if (null != minuteDelay) {
                String cackeKey = "jppentaho_getReportsParam-ShowletTypeCode:" + showletTypeCode
                        + "-Solution:" + solution + "-Server:" + server
                        + "-User:" + this.getPentahoServerUsername() + "-Path:" + path;
                params = (List<ReportHttpParam>) this.getCacheManager().getFromCache(cackeKey, minuteDelay * 60);
                if (null == params) {
                    params = this.getReportParamInvocation(solution, server, this.getPentahoServerUsername(), path);
                    String groupName = JpPentahoSystemConstants.SHOWLET_TYPE_CACHE_GROUP_PREFIX + showletTypeCode;
                    this.getCacheManager().putInCache(cackeKey, params, new String[]{groupName});
                    if (this.isDebugEnabled()) {
                        ApsSystemUtils.getLogger().info("INSERITO IN CACHE - period " + minuteDelay + " - KEY " + cackeKey);
                    }
                } else {
                    if (this.isDebugEnabled()) {
                        ApsSystemUtils.getLogger().info("PRESO DALLA CACHE - period " + minuteDelay + " - KEY " + cackeKey);
                    }
                }
            } else {
                params = this.getReportParamInvocation(solution, server, this.getPentahoServerUsername(), path);
            }
            if (this.isDebugEnabled()) {
                ApsSystemUtils.getLogger().info(" getReportsParam server: " + server + " solution " + solution + " , path " + path);
            }
        } catch (MalformedURLException e) {
            throw e;
        } catch (RemoteException e) {
            throw e;
        } catch (ServiceException e) {
            throw e;
        }
        return params;
    }

    @Override
    public List<ReportHttpParam> getReportsParamFromUserShowletConfig(String solution, Integer currentFrame, String page, String username) throws ApsSystemException {
    	if (!this.checkLicenseWithError()) {
			return null;
		}
    	List<ReportHttpParam> params = null;
        String path = null;
        try {
            String server = this.generateCompletePentahoServerLocalUrl();
            PentahoReportUserShowletConfig config = new PentahoReportUserShowletConfig();
            config.setUsername(username);
            config.setFramepos(currentFrame);
            config.setPagecode(page);
            config = this.getConfigDAO().getConfig(config);
            if (null != config) {
                path = getPathForPentaho(config);
                params = getReportParamInvocation(solution, server, this.getPentahoServerUsername(), path);
            }
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "getReportsParam");
            throw new ApsSystemException("Error on getReportsParam", e);
        }
        return params;
    }

    @Override
    public String getPathForPentaho(PentahoReportUserShowletConfig config) {
    	if (!this.checkLicense()) {
			return null;
		}
    	String path;
        path = config.getConfig();
        int slashPos = path.indexOf("/");
        if (slashPos > 0) {
            path = path.substring(slashPos, path.length());
        } else {
            path = "";
        }
        return path;
    }

    @Override
    public ReportDownload generateReport(String showletTypeCode, ApsProperties showletProperties,
            ReportHttpParam httpParams, Map<String, String> params, String output, boolean useCache) throws ApsSystemException {
    	if (!this.checkLicenseWithError()) {
			return null;
		}
    	if (!useCache || null == showletProperties) {
            return this.generateReport(httpParams, params, output);
        }
        ReportDownload reportDownload = null;
        try {
            Integer minuteDelay = this.extractRefreshMinute(showletProperties);
            if (null != minuteDelay) {
                String cackeKey = this.createReportCacheKey(showletTypeCode, httpParams, params, output);
                reportDownload = (ReportDownload) this.getCacheManager().getFromCache(cackeKey, minuteDelay * 60);
                if (null == reportDownload) {
                    reportDownload = this.generateReport(httpParams, params, output);
                    String groupName = JpPentahoSystemConstants.SHOWLET_TYPE_CACHE_GROUP_PREFIX + showletTypeCode;
                    this.getCacheManager().putInCache(cackeKey, reportDownload, new String[]{groupName});
                    if (this.isDebugEnabled()) {
                        ApsSystemUtils.getLogger().info("PUT IN CACHE - period " + minuteDelay + " - KEY " + cackeKey);
                    }
                } else {
                    if (this.isDebugEnabled()) {
                        ApsSystemUtils.getLogger().info("GET FROM CACHE - period " + minuteDelay + " - KEY " + cackeKey);
                    }
                }
            } else {
                reportDownload = this.generateReport(httpParams, params, output);
            }
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "getReportsParam");
            throw new ApsSystemException("Error on getReportsParam", e);
        }
        return reportDownload;
    }

    public String createReportCacheKey(String showletTypeCode,
            ReportHttpParam httpParams, Map<String, String> params, String output) throws ApsSystemException {
        StringBuilder cackeKey = new StringBuilder("jppentaho_generateReport");
        if (null != showletTypeCode) {
            cackeKey.append("-ShowletTypeCode:").append(showletTypeCode);
        }
        cackeKey.append("-HTTPPARAMS-");
        if (null != httpParams) {
            cackeKey.append("-action:").append(httpParams.getAction());
            cackeKey.append("-description:").append(httpParams.getDescription());
            cackeKey.append("-name:").append(httpParams.getName());
            cackeKey.append("-path:").append(httpParams.getPath());
            cackeKey.append("-solution:").append(httpParams.getSolution());
            cackeKey.append("-title:").append(httpParams.getTitle());
            cackeKey.append("-type:").append(httpParams.getType());
        } else {
            cackeKey.append("NULL");
        }
        cackeKey.append("-PARAMS");
        if (null != params && !params.isEmpty()) {
            List<String> keys = new ArrayList<String>(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                cackeKey.append("-").append(key).append(":").append(params.get(key));
            }
        } else {
            cackeKey.append("-NULL");
        }
        cackeKey.append("-OUTPUT:").append(output);
        return cackeKey.toString();
    }

    /**
     * Genera la url per interrogare il server pentaho per ottenere il report con i parametri
     * forniti dall'utente
     * */
    @Override
    public ReportDownload generateReport(String path, String name,
            String solution, Map<String, String> paramUI, String output) {
    	if (!this.checkLicense()) {
			return null;
		}
    	ReportHttpParam params = new ReportHttpParam();
        params.setName(name);
        params.setPath(path);
        params.setSolution(solution);
        return this.generateReport(params, paramUI, output);
    }

    @Override
    public ReportDownload generateReport(ReportHttpParam params, Map<String, String> paramUI, String output) {
    	ReportDownload reportPath = null;
        try {
        	if (!this.checkLicenseWithError()) {
        		return null;
        	}
            String url = this.generateReportUrl(params, "REPORT", this.getPentahoServerUsername(), this.getPentahoServerPassword(), paramUI);
            if (this.isDebugEnabled()) {
                ApsSystemUtils.getLogger().info(" report URL " + url);
            }
            reportPath = this.downloadReportFile(url, params, params.getName() + output);
            if (this.isDebugEnabled()) {
                ApsSystemUtils.getLogger().info(" report URL " + url);
            }
            //LOG.debug(" report " + reportPath);
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "generateReport");
            throw new RuntimeException("Error on generateReport", e);
        }
        return reportPath;
    }

    @Override
    public ReportParamUIInfo getReportParamUIInfo(String path, String name, String solution, Map<String, String> paramsUI) throws ApsSystemException, ResourceLoadingException {
		if (!this.checkLicenseWithError()) {
			return null;
		}
    	ReportParamUIInfo info = null;
        try {
            ReportHttpParam params = new ReportHttpParam();
            params.setName(name);
            params.setPath(path);
            params.setSolution(solution);
            /*
            String password = TokenPSHA.getTokenSHA("admin",1,tokenPassword);
            System.out.println(" PASS " + password);
            this.getReportDefinition(params, "admin", password);
            this.getReportParameterDefinition(params, "admin", password);
             */
            // TODO auth
            ReportDownload reportDownload = this.getReportDefinitionPath(params, this.getPentahoServerUsername(), this.getPentahoServerPassword());
            String reportDefinitionPath = reportDownload.getReportPath();
            String reportParamsDefinitionPath = this.getReportDefinitionPath(params, this.getPentahoServerUsername(), this.getPentahoServerPassword(), paramsUI);
            info = new ReportParamUIInfo();
            this.extractReportParamUIInfo(info, reportDefinitionPath, reportParamsDefinitionPath);
        } catch (ResourceLoadingException e) {
            ApsSystemUtils.logThrowable(e, this, "getReportParamUIInfo");
            throw e;
        } catch (ApsSystemException e) {
            ApsSystemUtils.logThrowable(e, this, "getReportParamUIInfo");
            throw new ApsSystemException("Error on getReportParamUIInfo", e);
        }
        return info;
    }

    /**
     * @deprecated use getReportDefinitionPath(ReportHttpParam, String, String)
     */
    protected String getReportDefinition(ReportHttpParam params, String username, String password) {
        ReportDownload download = this.getReportDefinitionPath(params, username, password);
        String reportPath = null;
        if (null != download) {
            reportPath = download.getReportPath();
        }
        return reportPath;
    }

    protected ReportDownload getReportDefinitionPath(ReportHttpParam params, String username, String password) {
        //ReportDownload download = null;
        try {
            return this.getReportDefinitionPath(params, username, password, null, "download", null);
            //String url = this.generateReportUrl(params, "download", this.getPentahoServerUsername(), this.getPentahoServerPassword(), null);
            //download = this.downloadReportFile(url, params, params.getName());
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "getReportDefinitionPath");
            throw new RuntimeException("Error on getReportDefinitionPath", e);
        }
        //return download.getReportPath();
    }

    //@Override
    /**
     * @deprecated use getReportDefinitionPath(ReportHttpParam, String, String, Map<String, String>)
     */
    protected String getReportParameterDefinition(ReportHttpParam params, String username, String password, Map<String, String> paramsUI) throws ApsSystemException {
        ReportDownload download = this.getReportDefinitionPath(params, username, password, paramsUI, "parameter", "_parameter.xml");
        String reportPath = null;
        if (null != download) {
            reportPath = download.getReportPath();
        }
        return reportPath;
    }

    @Deprecated
    protected String getReportDefinitionPath(ReportHttpParam params, String username, String password, Map<String, String> paramsUI) throws ApsSystemException {
        ReportDownload download = this.getReportDefinitionPath(params, username, password, paramsUI, "parameter", "_parameter.xml");
        String reportPath = null;
        if (null != download) {
            reportPath = download.getReportPath();
        }
        return reportPath;
    }

    protected ReportDownload getReportDefinitionPath(ReportHttpParam params, String username,
            String password, Map<String, String> paramsUI, String renderMode, String fileNameSuffix) throws ApsSystemException {
        ReportDownload download = null;
        try {
            String url = this.generateReportUrl(params, renderMode, this.getPentahoServerUsername(), this.getPentahoServerPassword(), paramsUI);
            //LOG.debug("report url " + url);
            String fileName = params.getName();
            if (null != fileNameSuffix) {
                fileName += fileNameSuffix;
            }
            download = this.downloadReportFile(url, params, fileName);
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "getReportDefinition");
            throw new RuntimeException("Error on getReportDefinition", e);
        }
//            String reportPath = download.getReportPath();
        //LOG.debug("report path " + reportPath);
//            return reportPath;
        return download;
    }

    /**
     * @deprecated use getReportDefinitionPath(ReportHttpParam)
     */
    @Override
    public ReportDownload getReportDefinition(ReportHttpParam params) {
        return this.getReportDefinitionPath(params);
    }

    @Override
    public ReportDownload getReportDefinitionPath(ReportHttpParam params) {
    	if (!this.checkLicense()) {
			return null;
		}
    	ReportDownload download = this.getReportDefinitionPath(params, this.getPentahoServerUsername(), this.getPentahoServerPassword());
//    	String reportPath = null;
//    	if (null != download) {
//    		reportPath = download.getReportPath();
//    	}
//    	return reportPath; 
        return download;
    }

    @Override
    public MasterReport getReportDefinition(String reportPath) throws ResourceLoadingException, ApsSystemException {
    	if (!this.checkLicenseWithError()) {
			return null;
		}
    	//ApsSystemUtils.getLogger().info(" REPORT PATH FOR LOAD MASTER REP " + reportPath);
        //LOG.debug("REPORT PATH FOR LOAD MASTER REP " + reportPath);
        ClassicEngineBoot.getInstance().start();
        MasterReport report = null;
        ResourceManager manager = new ResourceManager();
        manager.registerDefaults();
        Resource res;
        try {
            reportPath = "file:" + reportPath;
            res = manager.createDirectly(new URL(reportPath), MasterReport.class);
            report = (MasterReport) res.getResource();
        } //		catch (ResourceLoadingException loadingException) {
        //			ApsSystemUtils.logThrowable(loadingException, this, "getReportDefinition");
        //			//LOG.error(" error loading report ", loadingException);
        //			throw loadingException;
        //		} 
        catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getReportDefinition");
            //LOG.error(" error loading report ", t);
            throw new ApsSystemException("Errore nel caricamento report", t);
        }
        //LOG.info(report.getId() + " " + report.getName() + " " + report.getTitle());
        return report;
    }

    @Override
    public String generatePentahoServerPublicUrl() {
    	if (!this.checkLicense()) {
			return null;
		}
    	StringBuffer url = new StringBuffer(this.getPentahoServerProtocol());
        url.append("://");
        url.append(this.getPentahoServerUrl());
        url.append(":");
        url.append(this.getPentahoServerPort());
        return url.toString();
    }

    //@Override
    protected String generateCompletePentahoServerLocalUrl() {
        StringBuffer url = new StringBuffer();
        url.append(this.getPentahoServerLocalProtocol());
        url.append("://");
        url.append(this.getPentahoServerLocalUrl());
        url.append(":");
        url.append(this.getPentahoServerLocalPort());
        return url.toString();
    }

    /*
    @Override
    public void removeLocalReportDefinition(ReportHttpParam params) {
    String fileDir = this.getReportDefFilePath(params);
    String filePath = fileDir + params.getName();
    File file = new File(filePath);
    file.delete();
    }
     */
    //@Override
    protected void extractReportParamUIInfo(ReportParamUIInfo info, String reportDefinitionPath, String reportParamsDefinitionPath) throws ApsSystemException, ResourceLoadingException {
        try {
            MasterReport masterReport = this.getReportDefinition(reportDefinitionPath);
            String reportTitle = masterReport.getTitle();
            info.setReportTitle(reportTitle);
            this.loadOutPutTypeInfo(masterReport, info);
            String xml = this.readParametersDefFile(reportParamsDefinitionPath);
            ReportParametersDefinitionDOM reportParametersDefinitionDOM = new ReportParametersDefinitionDOM(xml);
            ParameterDefinitionEntry[] uiParamsNames = this.getParamsNamesFromReportDef(masterReport);
            Map<String, ReportParameter> reportParams = reportParametersDefinitionDOM.getReportParams();
            for (int i = 0; i < uiParamsNames.length; i++) {
                ParameterDefinitionEntry defEntry = uiParamsNames[i];
                ReportParameter par = reportParams.get(defEntry.getName());
                info.getParam().add(par);
            }
        } catch (ApsSystemException e) {
            ApsSystemUtils.logThrowable(e, this, "extractReportParamUIInfo");
            throw e;
        } catch (ResourceLoadingException e) {
            ApsSystemUtils.logThrowable(e, this, "extractReportParamUIInfo");
            throw e;
        }
    }

    @Override
    public ITreeNode getSolutionContents(String solution, String server, String username) {
    	if (!this.checkLicense()) {
			return null;
		}
    	ITreeNode root = null;
        try {
            String xml = this.getPentahoWebService().getSolutionDirectoryList(solution, "", 
					server, this.getPentahoContextPath(), username, this.getPentahoServerPassword(), TARGET);
            SolutionDOM solutionDOM = new SolutionDOM(xml);
            FileItem fileItem = solutionDOM.getFileItem();
            root = this.loadTreeNodeFromFileItem(fileItem.getChilds().get(0), null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getSolutionContents");
            throw new RuntimeException("message", t);
        }
        return root;
    }

    @Override
    public void saveUserConfiguration(String name, String configPath, String username, Integer internalServletFrameDest, ShowletType type, String pagecode) {
    	if (!this.checkLicense()) {
			return;
		}
    	PentahoReportUserShowletConfig userShowletConfig = new PentahoReportUserShowletConfig();
        userShowletConfig.setUsername(username);
        userShowletConfig.setConfig(configPath);
        userShowletConfig.setShowletcode(type.getCode());
        userShowletConfig.setFramepos(internalServletFrameDest);
        userShowletConfig.setPagecode(pagecode);
        if (null != name && name.length() > 0) {
            userShowletConfig.setName(name);
        }
        PentahoReportUserShowletConfig loaded = _configDAO.getConfig(userShowletConfig);
        if (null == loaded) {
            this.getConfigDAO().addConfig(userShowletConfig);
        } else {
            this.getConfigDAO().updateConfig(userShowletConfig);
        }
    }

    @Override
    public PentahoReportUserShowletConfig loadUserConfiguration(String username, Integer internalServletFrameDest, String pagecode) {
    	if (!this.checkLicense()) {
			return null;
		}
    	PentahoReportUserShowletConfig config = new PentahoReportUserShowletConfig();
        config.setUsername(username);
        config.setPagecode(pagecode);
        config.setFramepos(internalServletFrameDest);
        config = _configDAO.getConfig(config);
        return config;
    }

    /**
     * Inserisce la url delle immagini in modo che l'inclusione del html del report nella pagina del portale
     * produca
     * */
    //@Override
    protected String insertFullPentahoUrlForImages(String html) {
        String url = this.generatePentahoServerPublicUrl();
        html = this.replaceImagesUrls(html, url);
        return html;
    }

    //@Override
    protected String insertServletUrlForPentahoImages(String html) {
        String appBaseUrl = this.getAppBaseUrl();
        html = this.replaceImagesUrls(html, appBaseUrl);
        return html;
    }

    /**
     * Inserisce la url delle immagini in modo che l'inclusione del html del report nella pagina del portale
     * produca
     * */
    //@Override
    protected String insertFullPentahoUrlForCSS(String html) {
        String url = this.generatePentahoServerPublicUrl();
        html = this.replaceCssUrls(html, url);
        return html;
    }

    //@Override
    protected String insertServletUrlForPentahoCSS(String html) {
        String appBaseUrl = this.getAppBaseUrl();
        html = this.replaceCssUrls(html, appBaseUrl);
        return html;
    }

    /**
     * Scarica un immagine di un report dal server pentaho è la include nella response,
     * senza farne un download locale
     * */
    @Override
    public void getReportImage(String image, ServletOutputStream out) {
    	if (!this.checkLicense()) {
			return;
		}
    	String filePath = _pentahoReportDefBasePath;
        String url = this.generateCompletePentahoServerLocalUrl() + "/" + this.getPentahoContextPath() + "/getImage?image=" + image;
        ApsSystemUtils.getLogger().info(" getReportImage url : " + url);
        this.downloadFile(url, image, filePath, out);
    }

    /**
     * TODO : se diventa necessario fare il caching dei file scaricati dal server pentaho
     * */
    /*
    @Override
    public void loadLocallyReportImages(String html) throws FileNotFoundException {
    List<String> images = this.loadImagesPaths(html);
    if (null != images && images.size() > 0) {
    String imagesDirPath = _pentahoReportDefBasePath;
    File file = new File(imagesDirPath);
    file.mkdirs();
    Iterator it = images.iterator();
    while (it.hasNext()) {
    String tmp = (String) it.next();
    String url = this.generateCompletePentahoServerLocalUrl() + tmp;
    String fileName = this.extractImageFileName(tmp);
    String imagePath = imagesDirPath + File.separator + fileName;
    //ApsSystemUtils.getLogger().info(" Path for saving image on filesystem: " +imagePath);
    FileOutputStream outStream;
    try {
    outStream = new FileOutputStream(imagePath);
    } catch (FileNotFoundException e) {
    ApsSystemUtils.logThrowable(e, this, "loadLocallyReportImages");
    throw e;
    }
    this.downloadFile(url, fileName, imagesDirPath, outStream);
    }
    }
    }
     */
    @Deprecated
    public String getXactionUrl(String solution, String path, String action) {
    	if (!this.checkLicense()) {
			return null;
		}
    	StringBuffer url = new StringBuffer(this.generatePentahoServerPublicUrl());
        url.append("/" + this.getPentahoContextPath() + "/ViewAction?solution=");
        url.append(solution);
        url.append("&path=");
        url.append(path);
        url.append("&action=");
        url.append(action);
        url.append("&");
        url.append(IPentahoDynamicReportManager.USERNAME_PARAM);
        url.append("=");
        url.append(this.getPentahoServerUsername());
        url.append("&");
        url.append(IPentahoDynamicReportManager.PASSWORD_PARAM);
        url.append("=");
        url.append(this.getPentahoServerPassword());
        return url.toString();
    }

    public String buildActionUrl(ReportHttpParam reportHttpParam) {
    	if (!this.checkLicense()) {
			return null;
		}
    	StringBuffer url = new StringBuffer(this.generatePentahoServerPublicUrl());
        int type = reportHttpParam.getType();
        if (type == ReportHttpParam.TYPE_XACTION) {
            url.append("/" + this.getPentahoContextPath() + "/ViewAction");
        } else if (type == ReportHttpParam.TYPE_CDF) {
            url.append("/" + this.getPentahoContextPath() + "/content/pentaho-cdf/RenderXCDF");
        } else {
            throw new RuntimeException("Type not supported - " + reportHttpParam.getType());
        }

        /*
        String viewActionUrl = "pentaho/ViewAction";
        String contentReporting = "pentaho/content/reporting/reportviewer";
        String cdf = "/pentaho/content/pentaho-cdf/RenderXCDF";
         */

        url.append("?solution=");
        url.append(reportHttpParam.getSolution());
        url.append("&path=");
        url.append(reportHttpParam.getPath());
        url.append("&action=");
        url.append(reportHttpParam.getAction());
        url.append("&");
        url.append(IPentahoDynamicReportManager.USERNAME_PARAM);
        url.append("=");
        url.append(this.getPentahoServerUsername());
        url.append("&");
        url.append(IPentahoDynamicReportManager.PASSWORD_PARAM);
        url.append("=");
        url.append(this.getPentahoServerPassword());
        return url.toString();
    }
    
    @Override
    public String prepareReportHtmlForInclusion(String html) throws FileNotFoundException, IOException {
    	if (!this.checkLicense()) {
			return null;
		}
    	ApsSystemUtils.getLogger().info("PentahoDynamicReportManager - prepareReportHtmlForInclusion - HTML before " + html);
        ApsSystemUtils.getLogger().info("PentahoDynamicReportManager - prepareReportHtmlForInclusion - this.getPentahoServerVisibleFromClient() " + this.getPentahoServerVisibleFromClient());
        if (this.getPentahoServerVisibleFromClient()) {
            html = this.insertFullPentahoUrlForImages(html);
            html = this.insertFullPentahoUrlForCSS(html);
        } else {
            // mandatory manage images downloading them to portal from pentaho
            html = this.insertServletUrlForPentahoImages(html);
            html = this.insertServletUrlForPentahoCSS(html);
        }
        ApsSystemUtils.getLogger().info("PentahoDynamicReportManager this.getPentahoServerVisibleFromClient() HTML after " + html);
        //System.out.println("HTML after " + html);
        return "<!-- Start Pentaho Report -->" + html + "<!-- End Pentaho Report -->";
    }

    private List<ReportHttpParam> getReportParamInvocation(String solution, String server, String username, String path) throws MalformedURLException, RemoteException, ServiceException, ApsSystemException {
        List<ReportHttpParam> params;
        String aString = null;
        try {
            aString = getPentahoWebService().getList(solution, path, server, 
					this.getPentahoContextPath(), username, this.getPentahoServerPassword(), TARGET);
            ApsSystemUtils.getLogger().debug("Reports list " + aString);
        } catch (MalformedURLException e) {
            throw e;
        } catch (RemoteException e) {
            throw e;
        } catch (ServiceException e) {
            throw e;
        }
        try {
            ReportListDOM reportListDOM = new ReportListDOM(aString);
            params = reportListDOM.getReportParams();
        } catch (ApsSystemException ase) {
            ApsSystemUtils.logThrowable(ase, this, "getReportParamInvocation");
            throw ase;
        }
        return params;
    }
    /*
    private String extractImageFileName(String fullName) {
    int pos = fullName.indexOf("pentaho/getImage?image=");
    return fullName.substring(pos + "pentaho/getImage?image=".length());
    }
    
    private List<String> loadImagesPaths(String html) {
    List<String> images = new ArrayList<String>();
    Pattern p = Pattern.compile(".*<img src=\\\"(.*.png)\\\"");
    Matcher m = p.matcher(html);
    while ( m.find() ) {
    String value = m.group(1);
    images.add(value);
    }
    return images;
    }
     */

    private String replaceImagesUrls(String html, String appBaseUrl) {
        StringBuffer strBuffer = new StringBuffer("<img src=\"");
        strBuffer.append(appBaseUrl);
        strBuffer.append("/" + this.getPentahoContextPath() + "/getImage\\?");
        String IMG_REG_EXP = "<img\\s+src=\\\"/" + this.getPentahoContextPath() + "/getImage\\?";
        html = html.replaceAll(IMG_REG_EXP, strBuffer.toString());
        return html;
    }

    private String replaceCssUrls(String html, String appBaseUrl) {
        StringBuffer strBuffer = new StringBuffer("<link type=\"text/css\" rel=\"stylesheet\" href=\"");
        strBuffer.append(appBaseUrl);
        strBuffer.append("/" + this.getPentahoContextPath() + "/getImage\\?");
        String CSS_REG_EXP = "<link\\s+type=\\\"text/css\\\"\\s+rel=\\\"stylesheet\\\"\\s+href=\\\"/" + this.getPentahoContextPath() + "/getImage\\?";
        html = html.replaceAll(CSS_REG_EXP, strBuffer.toString());
        return html;
    }
    
    private void loadOutPutTypeInfo(MasterReport masterReport, ReportParamUIInfo info) {
        Boolean outputTypeLock = (Boolean) masterReport.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.LOCK_PREFERRED_OUTPUT_TYPE);
        String outputType = (String) masterReport.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.PREFERRED_OUTPUT_TYPE);
        info.setOutputTypeLock(outputTypeLock);
        String code = this.getCodeFromLabel(outputType);
        info.setOutputType(code);
    }

    private String getCodeFromLabel(String configValue) {
        Set<String> keys = this._outputs.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String currentKey = it.next();
            String currLabel = this._outputs.get(currentKey);
            if (currLabel.equals(configValue)) {
                return currentKey;
            }
        }
        return null;
    }

    private ParameterDefinitionEntry[] getParamsNamesFromReportDef(MasterReport masterReport) {
        ReportParameterDefinition reportParameterDefinition = masterReport.getParameterDefinition();
        ParameterDefinitionEntry[] parameterDefinitionEntries = reportParameterDefinition.getParameterDefinitions();
        return parameterDefinitionEntries;
    }

    private String readParametersDefFile(String reportParamsDefinitionPath) throws ApsSystemException {
        File file = new File(reportParamsDefinitionPath);
        StringBuffer buffer = new StringBuffer();
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Throwable e) {
            ApsSystemUtils.logThrowable(e, this, "readParametersDefFile");
            throw new ApsSystemException(e.getMessage(), e);
        }
        return buffer.toString();
    }

    /*
     *
     * url per il download
     * fileName nome della risorsa
     * filePath nome della directory
     * outStream stream per la scrittura
     *
     * */
    private ReportDownload downloadFile(String url, String fileName, String filePath, OutputStream outStream) {
        String imagePath = null;
        ReportDownload download = new ReportDownload();
        HttpHost target = new HttpHost(this.getPentahoServerLocalUrl(), this.getPentahoServerLocalPort(), this.getPentahoServerLocalProtocol());

        DefaultHttpClient httpclient = null;
        // general setup
        SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        supportedSchemes.register(new Scheme(this.getPentahoServerProtocol(),
                PlainSocketFactory.getSocketFactory(), this.getPentahoServerLocalPort()));
        try {
            // prepare parameters
            HttpParams httpparams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpparams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpparams, "UTF-8");
            HttpProtocolParams.setUseExpectContinue(httpparams, true);
            ClientConnectionManager connMgr =
                    new ThreadSafeClientConnManager(httpparams, supportedSchemes);
            httpclient = new DefaultHttpClient(connMgr, httpparams);
            HttpGet req = new HttpGet(url);
//			System.out.println("executing request to " + target);
            HttpResponse rsp = httpclient.execute(target, req);
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header current = headers[i];
                HeaderElement[] element = current.getElements();
                for (int j = 0; j < element.length; j++) {
                    HeaderElement currentElement = element[j];
                    NameValuePair[] nameValuePair = currentElement.getParameters();
                    for (int k = 0; k < nameValuePair.length; k++) {
                        NameValuePair curr = nameValuePair[k];
                        if (curr.getName().equals("filename")) {
                            download.setFileName(curr.getValue());
                        }
                    }
                }
            }
            HttpEntity entity = rsp.getEntity();
            File file = new File(filePath);
            file.mkdirs();
            entity.writeTo(outStream);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "downloadFile");
            throw new RuntimeException("message", t);
        } finally {
            // shut down the connection manager to ensure
            httpclient.getConnectionManager().shutdown();
        }
        download.setReportPath(imagePath);
        return download;
    }

    private ReportDownload downloadReportFile(String url, ReportHttpParam params, String fileName) throws ApsSystemException {
        String reportPath = null;
        ReportDownload download = new ReportDownload();
        HttpHost target = new HttpHost(this.getPentahoServerLocalUrl(), this.getPentahoServerLocalPort(), this.getPentahoServerLocalProtocol());

        DefaultHttpClient httpclient = null;
        // general setup
        SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        supportedSchemes.register(new Scheme(this.getPentahoServerProtocol(),
                PlainSocketFactory.getSocketFactory(), this.getPentahoServerLocalPort()));
        try {
            // prepare parameters
            HttpParams httpparams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpparams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpparams, "UTF-8");
            HttpProtocolParams.setUseExpectContinue(httpparams, true);
            ClientConnectionManager connMgr =
                    new ThreadSafeClientConnManager(httpparams, supportedSchemes);
            httpclient = new DefaultHttpClient(connMgr, httpparams);
            HttpGet req = new HttpGet(url);
//			System.out.println("executing request to " + target);
            HttpResponse rsp = httpclient.execute(target, req);
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header current = headers[i];
                HeaderElement[] element = current.getElements();
                for (int j = 0; j < element.length; j++) {
                    HeaderElement currentElement = element[j];
                    NameValuePair[] nameValuePair = currentElement.getParameters();
                    for (int k = 0; k < nameValuePair.length; k++) {
                        NameValuePair curr = nameValuePair[k];
                        if (curr.getName().equals("filename")) {
                            download.setFileName(curr.getValue());
                        }
                    }
                }
            }
            HttpEntity entity = rsp.getEntity();
            String filePath = this.getReportDefFilePath(params);
            File file = new File(filePath);
            file.mkdirs();
            fileName = fileName.replaceAll(" ", "_");
            reportPath = filePath + fileName;
            //
            ApsSystemUtils.getLogger().info(" Report path for saving on filesystem: " + reportPath);
            //LOG.debug(" Report path for saving on filesystem: " +reportPath);
            FileOutputStream outStream;
            outStream = new FileOutputStream(reportPath);
            entity.writeTo(outStream);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "downloadReportDefinition");
//			throw new ApsSystemException("message", t);

            download.setValidationKO(true);
            download.setErrorCode(IPentahoDynamicReportManager.CONNECTION_PROBLEM_ERROR_CODE);
            return download;

        } finally {
            // shut down the connection manager to ensure
            httpclient.getConnectionManager().shutdown();
        }
        download.setReportPath(reportPath);
        try {
            this.verifyReportErrors(reportPath, download);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "downloadReportDefinition");
            throw new ApsSystemException("message", t);
        }
        return download;
    }

    /*
     * Read the file to verify if contains the string Report validation failed, 404 not found,
     * */
    @Override
    public void verifyReportErrors(String reportPath, ReportDownload download) throws IOException {
        // TODO inserire controllo sulla dimensione del file.
    	if (!this.checkLicense()) {
			return;
		}
        String tmp = null;
        File file = new File(reportPath);
        //
        ApsSystemUtils.getLogger().info(" Report size :: " + file.length());
        // the file is bigger than file with the message of validation error so is a valid report


        // NOTA :: controllo non più attuabile
		/*
        if (file.length() > REPORT_FILE_WITH_VALIDATION_FAILED_MSG_SIZE) {
        download.setValidationKO(false);
        }
         */
        download.setValidationKO(false);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte data[] = new byte[BUFFER];
            if (fis.available() > 0) {
                fis.read(data);
                byteArrayOutputStream.write(data);
            }
            tmp = byteArrayOutputStream.toString();
        } catch (FileNotFoundException e) {
            ApsSystemUtils.logThrowable(e, this, "verifyValidation");
            throw e;
        } catch (IOException e) {
            ApsSystemUtils.logThrowable(e, this, "verifyValidation");
            throw e;
        }
        if (VALIDATION_FAILED_MSG.equals(tmp.trim())) {
            // is a fake report file with validation error message
            download.setValidationKO(true);
            download.setErrorCode(IPentahoDynamicReportManager.REPORT_PARAM_VALIDATION_ERROR_CODE);
        }

        if (null != tmp && tmp.contains(WEB_RESOURCE_NOT_FOUND)) {
            // is a fake report file with 404 html error page
            download.setValidationKO(true);
            download.setErrorCode(IPentahoDynamicReportManager.WEB_RESOURCE_NOT_FOUND_ERROR_CODE);
        }

        if (REPORT_NOT_FOUND.contains(tmp.trim())) {
            // is a fake report file with only the string java.lang.NullPointerException inside
            download.setValidationKO(true);
            download.setErrorCode(IPentahoDynamicReportManager.REPORT_NOT_FOUND_ERROR_CODE);
        }

        if (download.isValidationKO()) {
            ApsSystemUtils.getLogger().info(" REPORT WITH ERROR CONTENT " + tmp);
        }

    }

    private String getReportDefFilePath(ReportHttpParam params) {
        String path = "";
        StringBuffer filePath = new StringBuffer(_pentahoReportDefBasePath);
        if (null != params.getSolution() && params.getSolution().length() > 0) {
            if (!File.separator.equals(filePath.charAt(filePath.length() - 1))) {
                filePath.append(File.separator);
            }
            filePath.append(params.getSolution());
        }
        if (null != params.getPath() && params.getPath().length() > 0) {
            if (!File.separator.equals(filePath.charAt(filePath.length() - 1))) {
                filePath.append(File.separator);
            }
            filePath.append(params.getPath());
        }
        if (!File.separator.equals(filePath.charAt(filePath.length() - 1))) {
            filePath.append(File.separator);
        }
        path = filePath.toString();
        path.replaceAll(" ", "_");
        //ApsSystemUtils.getLogger().info(" DIRECTORY : " + filePath);
        return path;
    }

    private ITreeNode loadTreeNodeFromFileItem(FileItem fileItem, String sup) {
        FileNode node = new FileNode();
        TreeNode current = null;
        FileItem tmp;
        Lang langDefault = this.getLangManager().getDefaultLang();
        node.setTitle(langDefault.getCode(), fileItem.getName());
        node.setCode(fileItem.getName());
        node.setDirectory(fileItem.isDirectory());
        ITreeNode[] children = new TreeNode[fileItem.getChilds().size()];
        StringBuffer path = null;
        for (int i = 0; i < fileItem.getChilds().size(); i++) {
            tmp = fileItem.getChilds().get(i);
            path = new StringBuffer();
            if (null != sup) {
                path.append(sup);
                path.append("/");
            }
            path.append(fileItem.getName());
            //			path = sup + "/" + fileItem.getName();
            current = (TreeNode) this.loadTreeNodeFromFileItem(tmp, path.toString());
            current.setCode(path + "/" + tmp.getName());
            current.setParent(node);
            children[i] = current;
        }
        node.setChildren(children);
        return node;
    }

    private String generateReportUrl(ReportHttpParam params, String renderMode, String username, String password, Map<String, String> paramsUI) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("/" + this.getPentahoContextPath() + "/content/reporting?renderMode=");
        buffer.append(renderMode);
        buffer.append("&");
        buffer.append(IPentahoDynamicReportManager.USERNAME_PARAM);
        buffer.append("=");
        buffer.append(username);
        buffer.append("&");
        buffer.append(IPentahoDynamicReportManager.PASSWORD_PARAM);
        buffer.append("=");
        buffer.append(password);
        buffer.append("&");
        buffer.append("name");
        buffer.append("=");
        buffer.append(params.getName());
        buffer.append("&");
        buffer.append("path");
        buffer.append("=");
        String path = params.getPath();
        if (null != path && path.length() > 0) {
            buffer.append(path);
        }
        buffer.append("&");
        buffer.append("solution");
        buffer.append("=");
        buffer.append(params.getSolution());
        if (null != paramsUI && paramsUI.size() > 0) {
            Set<String> names = paramsUI.keySet();
            for (String current : names) {
                String currValue = paramsUI.get(current);

                if ("REPORT".equals(renderMode) && current.equals("output-target")) // TODO decidere come trattare HTML paginato currValue.equals(IPentahoDynamicReportManager.HTML_PAGINATED) ||
                {
                    if (currValue.equals(IPentahoDynamicReportManager.HTML_STREAM)) {
                        {
                            buffer.append("&");
                            buffer.append("dashboard-mode");
                            buffer.append("=");
                            buffer.append("true");
                        }
                    }
                }
                buffer.append("&");
                buffer.append(current);
                buffer.append("=");
                buffer.append(currValue);
            }
        }
        String tmp = buffer.toString().replace(" ", "+");
//		System.out.println(" Pentaho Download URL " + tmp);
        //ApsSystemUtils.getLogger().info(" Pentaho Download URL " + tmp);
        //LOG.debug(" Pentaho Download URL " + tmp);
        return tmp;
    }

    private void loadOutputTypes() {
        getOutputs().put(IPentahoDynamicReportManager.CSV_KEY, IPentahoDynamicReportManager.CSV);
        getOutputs().put(IPentahoDynamicReportManager.EXCEL_2007_KEY, IPentahoDynamicReportManager.EXCEL_2007);
        getOutputs().put(IPentahoDynamicReportManager.EXCEL_KEY, IPentahoDynamicReportManager.EXCEL);
//		getOutputs().put(IPentahoDynamicReportManager.HTML_PAGINATED_KEY, IPentahoDynamicReportManager.HTML_PAGINATED);
        getOutputs().put(IPentahoDynamicReportManager.HTML_STREAM_KEY, IPentahoDynamicReportManager.HTML_STREAM);
        getOutputs().put(IPentahoDynamicReportManager.PDF_KEY, IPentahoDynamicReportManager.PDF);
        getOutputs().put(IPentahoDynamicReportManager.RTF_KEY, IPentahoDynamicReportManager.RTF);
        getOutputs().put(IPentahoDynamicReportManager.TEXT_KEY, IPentahoDynamicReportManager.TEXT);
    }

    /**
     * Used for extracting refresh period from showlet properties ad from api properties.
     * Parameter names of refresh period and units of mesaurement have the same names in api and showlet types
     * @param properties
     * @return 
     */
    private Integer extractRefreshMinute(Properties properties) {
        String refreshPeriodString = properties.getProperty(SHOWLET_REFRESH_PERIOD);
        Integer refreshPeriod = null;
        if (null != refreshPeriodString && refreshPeriodString.trim().length() > 0) {
            try {
                refreshPeriod = Integer.parseInt(refreshPeriodString);
            } catch (Throwable t) {
            }
        }
        if (null == refreshPeriod) {
            return null;
        }
        Integer refresh = null;
        String unOfMes = properties.getProperty(SHOWLET_REFRESH_PERIOD_UNITS_OF_MEASUREMENT);
        if (null != unOfMes && unOfMes.trim().length() > 0) {
            if (unOfMes.equals("m")) {
                refresh = refreshPeriod;
            } else if (unOfMes.equals("h")) {
                refresh = refreshPeriod * 60;
            } else if (unOfMes.equals("d")) {
                refresh = refreshPeriod * 60 * 24;
            } else if (unOfMes.equals("M")) {
                refresh = refreshPeriod * 60 * 24 * 30;
            } else if (unOfMes.equals("y")) {
                refresh = refreshPeriod * 60 * 24 * 365;
            }
        }
        return refresh;
    }
	
	// INIZIO - Blocco Licenza
	@Override
	public String getModuleName() {
		return "jpblog";
	}
	
	private boolean checkLicenseWithError() throws ApsSystemException {
		boolean checkResult = this.checkLicense();
		if (!checkResult) {
			throw new ApsSystemException("License not valid");
		}
		return checkResult;
	}
	
	private boolean checkLicense() {
		boolean checkResult = false;
		int status = ILicenseManager.LICENSE_WRONG;
		try {
			if (this.getLicenseStatus()==null) {// Se la licenza non è ancora stata verificata, ne inizializzo lo stato
				status = this.initLicenseStatus();
			} else {
				status = this.getLicenseStatus();
				if (status == ILicenseManager.LICENSE_OK || status == ILicenseManager.LICENSE_EXPIRING) {// La licenza è corretta
					// Il LicenseManager verifica periodicamente la licenza, per cui, se la trovo cambiata, la aggiorno
					int currentStatus = this.getLicenseManager().getKeyStatus(this.getModuleName());
					if (status != currentStatus) {
						status = currentStatus;
						this.setLicenseStatus(status);
					}
				}
			}
			if (status == ILicenseManager.LICENSE_OK || status == ILicenseManager.LICENSE_EXPIRING) {
				checkResult = true;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, this.getModuleName());
		}
//		ApsSystemUtils.getLogger().info(this.getModuleName() + " - license " + (checkResult ? "OK" : "NOT OK"));
		return checkResult;
	}
	
	@SuppressWarnings("rawtypes")
	private int initLicenseStatus() throws ApsSystemException {
		String moduleName = this.getModuleName();
		int status = ILicenseManager.LICENSE_WRONG;
		try {
			ILicenseManager licenseManager = this.getLicenseManager();
			status = licenseManager.getKeyStatus(moduleName);
			if (status == ILicenseManager.LICENSE_OK || status == ILicenseManager.LICENSE_EXPIRING) {
				Map licenseKeys = licenseManager.getLicenseKeys(moduleName);
				// Testo per 2 volte per evitare "distorsioni temporali"
				if (!this.checkLicenseToken(licenseKeys) && !this.checkLicenseToken(licenseKeys)) {
					status = ILicenseManager.LICENSE_WRONG;
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, this.getModuleName());
			status = ILicenseManager.LICENSE_WRONG;
		}
		this.setLicenseStatus(status);
		return status;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean checkLicenseToken(Map keys) throws ApsSystemException {
		String moduleName = this.getModuleName();
		Date date = new Date();
		String token = this.getLicenseManager().getToken(moduleName);
		StringBuffer buffer = new StringBuffer(moduleName);
		buffer.append(DateConverter.getFormattedDate(date, "ddssMMmmHHyyyy"));
		Iterator<Entry> keysIter = keys.entrySet().iterator();
		while (keysIter.hasNext()) {
			Entry current = keysIter.next();
			buffer.append(current.getKey().toString()).append(current.getValue().toString());
		}
		String encrypted = this.encryptMD5(buffer.toString());
		return encrypted.equals(token);
	}
	
	/*
		Controllo incrociato
		LicenseManager chiama il LicenseUtiliser e chiede di restituirgli 
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public String checkLicense(String secret) throws ApsSystemException {
		Date date = new Date();
		StringBuffer buffer = new StringBuffer(this.getModuleName());
		buffer.append(DateConverter.getFormattedDate(date, "ddssMMmmHHyyyy"));
		String decripted = this.encryptString(secret, this.encryptMD5(buffer.toString()));
		return this.encryptMD5(decripted);
	}
	
	private String encryptMD5(String plainText) throws ApsSystemException {
		String encryptedString = null;
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(plainText.getBytes());
			byte messageDigest[] = algorithm.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length()==1) hexString.append('0');
				hexString.append(hex);
			}
			encryptedString = hexString.toString();
		} catch (Throwable t) {
			throw new ApsSystemException("Error detected while encoding a string", t);
		}
		return encryptedString;
	}
	
	private String encryptString(String plainText, String secret) throws ApsSystemException {
		String encryptedString = null;
		try {
			Key key = this.getKey(secret);
			Cipher desCipher = Cipher.getInstance(TRIPLE_DES);
			desCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = plainText.getBytes();
			byte[] ciphertext = desCipher.doFinal(cleartext);
			encryptedString = new String(Base64.encodeBase64(ciphertext));
		} catch (Throwable t) {
			throw new ApsSystemException("Error detected while encoding a string", t);
		}
		return encryptedString;
	}
	private Key getKey(String secret) throws ApsSystemException {
		try {
			byte[] bytes = secret.getBytes();
			DESedeKeySpec pass = new DESedeKeySpec(bytes);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(TRIPLE_DES_KEY_SPEC); 
			SecretKey s = skf.generateSecret(pass); 
			return s;
		} catch (Throwable t) {
			throw new ApsSystemException("Error creating key", t);
		}
	}
	
	private Integer getLicenseStatus() {
		return _licenseStatus;
	}
	private void setLicenseStatus(Integer licenseStatus) {
		this._licenseStatus = licenseStatus;
	}
	
	// FINE - Blocco Licenza
    
    protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }

    public void setOutputs(Map<String, String> outputs) {
        this._outputs = outputs;
    }
    public Map<String, String> getOutputs() {
        return _outputs;
    }

    protected IPentahoWebService getPentahoWebService() {
        return _pentahoWebService;
    }
    public void setPentahoWebService(IPentahoWebService pentahoWebService) {
        this._pentahoWebService = pentahoWebService;
    }

    protected Integer getPentahoServerLocalPort() {
        return _pentahoServerLocalPort;
    }
    public void setPentahoServerLocalPort(Integer pentahoServerPort) {
        this._pentahoServerLocalPort = pentahoServerPort;
    }

    protected String getPentahoServerProtocol() {
        return _pentahoServerProtocol;
    }
    public void setPentahoServerProtocol(String pentahoServerProtocol) {
        this._pentahoServerProtocol = pentahoServerProtocol;
    }

    protected IPentahoReportUserConfigDAO getConfigDAO() {
        return _configDAO;
    }
    public void setConfigDAO(IPentahoReportUserConfigDAO configDAO) {
        this._configDAO = configDAO;
    }

    protected ILangManager getLangManager() {
        return _langManager;
    }
    public void setLangManager(ILangManager langManager) {
        this._langManager = langManager;
    }
    
    protected Boolean getPentahoServerVisibleFromClient() {
        return _pentahoServerVisibleFromClient;
    }
    public void setPentahoServerVisibleFromClient(Boolean pentahoServerVisibleFromClient) {
        this._pentahoServerVisibleFromClient = pentahoServerVisibleFromClient;
    }

    protected String getPentahoServerLocalUrl() {
        return _pentahoServerLocalUrl;
    }
    public void setPentahoServerLocalUrl(String pentahoServerUrl) {
        this._pentahoServerLocalUrl = pentahoServerUrl;
    }

    protected String getPentahoServerUrl() {
        return _pentahoServerUrl;
    }
    public void setPentahoServerUrl(String pentahoServerUrl) {
        this._pentahoServerUrl = pentahoServerUrl;
    }

    protected Integer getPentahoServerPort() {
        return _pentahoServerPort;
    }
    public void setPentahoServerPort(Integer pentahoServerPort) {
        this._pentahoServerPort = pentahoServerPort;
    }
    
    public String getPentahoContextPath() {
        if (null == this._pentahoContextPath) {
            return "pentaho";
        }
        return _pentahoContextPath;
    }
    public void setPentahoContextPath(String pentahoContextPath) {
        this._pentahoContextPath = pentahoContextPath;
    }
    
    protected String getPentahoServerLocalProtocol() {
        return _pentahoServerLocalProtocol;
    }
    public void setPentahoServerLocalProtocol(String pentahoServerLocalProtocol) {
        this._pentahoServerLocalProtocol = pentahoServerLocalProtocol;
    }
    
    protected String getPentahoServerUsername() {
        return _pentahoServerUsername;
    }
    public void setPentahoServerUsername(String username) {
        this._pentahoServerUsername = username;
    }

    protected String getPentahoServerPassword() {
        return _pentahoServerPassword;
    }
    public void setPentahoServerPassword(String password) {
        this._pentahoServerPassword = password;
    }

    protected String getAppBaseUrl() {
        return _appBaseUrl;
    }
    public void setAppBaseUrl(String appBaseUrl) {
        this._appBaseUrl = appBaseUrl;
    }

    protected IShowletTypeManager getShowletTypeManager() {
        return _showletTypeManager;
    }
    public void setShowletTypeManager(IShowletTypeManager showletTypeManager) {
        this._showletTypeManager = showletTypeManager;
    }

    protected IPentahoConfigManager getPentahoConfigManager() {
        return _pentahoConfigManager;
    }
    public void setPentahoConfigManager(IPentahoConfigManager pentahoConfigManager) {
        this._pentahoConfigManager = pentahoConfigManager;
    }

    protected ICacheManager getCacheManager() {
        return _cacheManager;
    }
    public void setCacheManager(ICacheManager cacheManager) {
        this._cacheManager = cacheManager;
    }
	
	protected ILicenseManager getLicenseManager() {
		return _licenseManager;
	}
	public void setLicenseManager(ILicenseManager licenseManager) {
		this._licenseManager = licenseManager;
	}
	
	private static final String TRIPLE_DES_KEY_SPEC = "DESede";
	private static final String TRIPLE_DES = "DESede/ECB/PKCS5Padding";
        
	private Integer _licenseStatus;
	private ILicenseManager _licenseManager;
    
    private String _appBaseUrl;
    private Map<String, String> _outputs = new HashMap<String, String>();
    private String _pentahoContextPath;
    private String _pentahoServerUsername;
    private String _pentahoServerPassword;
    private String _pentahoReportDefBasePath = null;
    private String _pentahoServerLocalUrl;
    private String _pentahoServerUrl;
    private String _pentahoServerProtocol;
    private String _pentahoServerLocalProtocol;
    private Integer _pentahoServerLocalPort;
    private Integer _pentahoServerPort;
    private Boolean _pentahoServerVisibleFromClient;
    private IPentahoWebService _pentahoWebService;
    private ConfigInterface _configManager;
    private ILangManager _langManager;
    private IPentahoReportUserConfigDAO _configDAO;
    private IShowletTypeManager _showletTypeManager;
    private IPentahoConfigManager _pentahoConfigManager;
    private ICacheManager _cacheManager;
    private static final String TARGET = "_blank";
    
    private final int BUFFER = 2048;
    public final static String VALIDATION_FAILED_MSG = "Report validation failed.";
    public final static String WEB_RESOURCE_NOT_FOUND = "<title>404 Not Found</title>";
    public final static String REPORT_NOT_FOUND = "java.lang.NullPointerException";
    public final static long REPORT_FILE_WITH_VALIDATION_FAILED_MSG_SIZE = 25;
    private HelperPentahoTag helperPentahoTag = new HelperPentahoTag();
    
}