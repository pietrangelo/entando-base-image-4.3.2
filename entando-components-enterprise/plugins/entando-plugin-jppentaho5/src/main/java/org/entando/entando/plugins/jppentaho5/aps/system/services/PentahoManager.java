package org.entando.entando.plugins.jppentaho5.aps.system.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoAttributeDOM;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoConfiguration;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

public class PentahoManager extends AbstractService implements IPentahoManager, PentahoSystemConstants {
    
    private static Logger _logger = LoggerFactory.getLogger(PentahoManager.class);
    
    @Override
    public void init() throws Exception {
        _logger.info(this.getClass().getName() + "initialized correctly");
        loadConfig();
        createCommonReportFields();
    }
    
    public void loadConfig() throws ApsSystemException {
        try {
            ConfigInterface configManager = this.getConfigManager();
            String xml = configManager.getConfigItem(PentahoSystemConstants.JPPENTAHO5_CONFIG_ITEM);
            _config = new PentahoConfiguration(xml);
            // notify the configuration to the connection helper
        } catch (Throwable t) {
            _logger.error("Error in loadConfig", t);
            throw new ApsSystemException("Error in loadConfig", t);
        }
    }
    
    /**
     * Create a list of common report fields
     */
    // FIXME this should be based on pentaho version
    protected void createCommonReportFields() {
        _commonReportFields = Arrays.asList(PentahoSystemConstants.COMMON_REPORT_PARAMETERS);
    }
    
    @Override
    public List<String> getCommonReportField() {
        List<String> fields = new ArrayList<String>();
        fields.addAll(_commonReportFields);
        return fields;
    }
    
    @Override
    public String getPentahoReportUrl(String path, String csvArgs) throws ApsSystemException {
        PentahoConfiguration config = getConfiguration();
        Map<String, String> arguments = new HashMap<String, String>();
        
        // append arguments
        if (StringUtils.isNotBlank(csvArgs)
                && !PentahoSystemConstants.CONFIG_ARG_NONE.equals(csvArgs)) {
            String[] args = csvArgs.split(",");
            for (String arg: args) {
                String[] params = arg.split("=");
                
                if (params.length != 2) {
                    throw new RuntimeException("Malformed arguments pair [" + arg +"]");
                }
                arguments.put(params[0], params[1]);
            }
        }
        
        return getPentahoReportUrl(config.getInstance(), path, arguments);
    }
    
    @Override
    public String getPentahoReportUrl(String path, Map<String, String> args) throws ApsSystemException {
        PentahoConfiguration config = getConfiguration();
        
        return getPentahoReportUrl(config.getInstance(), path, args);
    }
    
    @Override
    public String getPentahoReportUrl(String pentahoInstance, String path, String csvArgs) throws ApsSystemException {
        URIBuilder url = null;
        String urlPath = null;
        
        try {
            urlPath = path.replace("/", PentahoSystemConstants.PENTAHO_PATH_SEPARATOR);
            url = new URIBuilder(pentahoInstance);
            
            if (path.contains(".xaction")) {
                urlPath = String.format(PENTAHO_API_GENERATE_CONTENT, urlPath);
            } else if (path.contains(".prpt")) {
                urlPath = String.format(PENTAHO_API_REPORT, urlPath);
            } else if (path.contains(".wcdf")
                    || path.contains(".xcdf")) {
                urlPath = String.format(PENTAHO_API_GENERATE_CONTENT, urlPath);
            } else {
                throw new RuntimeException("unknown report type [" + path + "]");
            }
            // TODO check for trailing backslashes in Pentaho base URL
            urlPath = url.getPath().concat(urlPath);
            url.setPath(urlPath);
            // append arguments
            if (StringUtils.isNotBlank(csvArgs)
                    && !PentahoSystemConstants.CONFIG_ARG_NONE.equals(csvArgs)) {
                String[] args = csvArgs.split(",");
                for (String arg: args) {
                    String[] params = arg.split("=");
                    
                    if (params.length != 2) {
                        throw new RuntimeException("Malformed arguments pair [" + arg +"]");
                    }
                    url.addParameter(params[0], params[1]);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error composing query url", t);
            throw new ApsSystemException("Error composing query URL", t);
        }
        return url.toString();
    }
    
    @Override
    public String getPentahoReportUrl(String pentahoInstance, String path, Map<String, String> args) throws ApsSystemException {
        URIBuilder url = null;
        String urlPath = null;
        
        try {
            urlPath = path.replace("/", PentahoSystemConstants.PENTAHO_PATH_SEPARATOR);
            url = new URIBuilder(pentahoInstance);
            
            if (path.contains(".xaction")) {
                urlPath = String.format(PENTAHO_API_GENERATE_CONTENT, urlPath);
            } else if (path.contains(".prpt")) {
                urlPath = String.format(PENTAHO_API_REPORT, urlPath);
            } else if (path.contains(".wcdf")
                    || path.contains(".xcdf")) {
                urlPath = String.format(PENTAHO_API_GENERATE_CONTENT, urlPath);
            } else {
                throw new RuntimeException("unknown report type [" + path + "]");
            }
            // TODO check for trailing backslashes in Pentaho base URL
            urlPath = url.getPath().concat(urlPath);
            url.setPath(urlPath);
            // append arguments
            if (null != args
                    && !args.isEmpty()) {
                
                for (String key: args.keySet()) {
                    String value = args.get(key);
                    
                    url.addParameter(key, value);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error composing query url", t);
            throw new ApsSystemException("Error composing query URL", t);
        }
        return url.toString();
    }
    
    @Override
    public Map<String, PentahoParameter> getReportParameterMap(String path) throws ApsSystemException {
        PentahoConfiguration config = getConfiguration();
        Map<String, PentahoParameter> parameters = new HashMap<String, PentahoParameter>();
        String username = null;
        String password = null;
        String response = null;
        
        try {
            path = path.replace("/", PentahoSystemConstants.PENTAHO_PATH_SEPARATOR);
            String url = String.format(PENTAHO_API_PARAMETERS, path);
            url = appentToInstanceUrl(url);
            if (!config.isCasAuthentication()) {
                username = config.getUsername();
                password = config.getPassword();
            }
            response = queryPentahoServer(url, username, password);
            parseReportParameter(response, parameters);
            
        } catch (Throwable t) {
            throw new ApsSystemException("Error qurying for report parameters", t);
        }
        return parameters;
    }
    
    @Override
    public Map<String, PentahoParameter> getReportFormParameterMap(String path) throws ApsSystemException {
        Map<String, PentahoParameter> formParams = new HashMap<String, PentahoParameter>();
        try {
            Map<String, PentahoParameter> parameters = getReportParameterMap(path);
            for (String parameterName: parameters.keySet()) {
                if (!_commonReportFields.contains(parameterName)) {
                    PentahoParameter parameter = parameters.get(parameterName);
                    formParams.put(parameterName, parameter);
                }
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error qurying for FORM report parameters", t);
        }
        return formParams;
    }
    
    @Override
    public List<PentahoParameter> getReportParameterList(String path) throws ApsSystemException {
        PentahoConfiguration config = getConfiguration();
        List<PentahoParameter> parameters = new ArrayList<PentahoParameter> ();
        String username = null;
        String password = null;
        String response = null;
        
        try {
            path = path.replace("/", PentahoSystemConstants.PENTAHO_PATH_SEPARATOR).replace(" ", "%20");
            String url = String.format(PENTAHO_API_PARAMETERS, path);
            url = appentToInstanceUrl(url);
            if (!config.isCasAuthentication()) {
                username = config.getUsername();
                password = config.getPassword();
            }
            response = queryPentahoServer(url, username, password);
            parseReportParameter(response, parameters);
            
        } catch (Throwable t) {
            throw new ApsSystemException("Error qurying for report parameters", t);
        }
        return parameters;
    }
    
    @Override
    public List<PentahoParameter> getReportFormParameterList(String path, boolean includeOutput) throws ApsSystemException {
        List<PentahoParameter> formParams = new ArrayList<PentahoParameter>();
        try {
            List<PentahoParameter> parameters = getReportParameterList(path);
            for (PentahoParameter parameter: parameters) {
                if (!_commonReportFields.contains(parameter.getName())
                        || (parameter.getName().equals(PentahoSystemConstants.PENTAHO_OUTPUT_PARAMETER) && includeOutput)) {
                    formParams.add(parameter);
                }
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error querying for FORM report parameters", t);
        }
        return formParams;
    }
    
    @Override
    public PentahoConfiguration getConfiguration() throws ApsSystemException {
        PentahoConfiguration config = null;
        try {
            config = new PentahoConfiguration(_config.toXml());
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the configuration",t );
        }
        return config;
    }
    
    /**
     * Query the Pentaho server instance using default plugin configuration
     * @param url
     * @return
     * @throws ApsSystemException
     */
    protected String queryPentahoServer(String url) throws ApsSystemException {
        String response = null;
        PentahoConfiguration config = getConfiguration();
        
        try {
            url = appentToInstanceUrl(url);
            response = queryPentahoServer(url, config.getUsername(), config.getPassword());
        } catch (Throwable t) {
            _logger.error("Error querying pentaho server", t);
        }
        return response;
    }
    
    /**
     * Query the Pentaho server instance
     * @param url
     * @param username
     * @param password
     * @return
     * @throws ApsSystemException
     */
    protected String queryPentahoServer(String url, String username, String password) throws ApsSystemException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String response = null;
        
        if (StringUtils.isNotBlank(password)
                && StringUtils.isNotBlank(username)) {
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
            UsernamePasswordCredentials authCredentials = new UsernamePasswordCredentials(username, password);
            client.getCredentialsProvider().setCredentials(authScope, authCredentials);
        } else {
            _logger.warn("authentication disabled");
        }
        try {
//			System.out.println("Querying " + httpGet.getRequestLine()); // FIXME
_logger.debug("Querying {}" + httpGet.getRequestLine());
HttpResponse httpResponse = client.execute(httpGet);

if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    _logger.debug("HTTP response ok, parsing results");
    response = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
} else {
    _logger.error("HTTP response: " + httpResponse.getStatusLine().getStatusCode());
    throw new RuntimeException("Unexpected HTTP status [" + httpResponse.getStatusLine().getStatusCode() + "]");
}
httpGet.releaseConnection();
        } catch (Throwable t) {
            throw new ApsSystemException("jppentaho5: error querying Pentaho instance", t);
        } finally {
            httpGet.releaseConnection();
        }
        return response;
    }
    
    /**
     * Append the given url to the Pentaho instance in the configuration
     * @param url
     * @return
     * @throws ApsSystemException
     */
    private String appentToInstanceUrl(String url) throws Throwable {
        PentahoConfiguration config = getConfiguration();
        StringBuilder base = new StringBuilder(config.getInstance());
        
        if (!config.getInstance().endsWith("/")
                && !url.startsWith("/")) {
            base.append("/");
        } else if (config.getInstance().endsWith("/")
                && url.startsWith("/")) {
            url = url.substring(1);
        }
        base.append(url);
        return base.toString();
    }
    
    /**
     * Parse the report parameter XML returned by Pentaho
     * @param xml
     * @return
     * @throws Throwable
     */
    private final void parseReportParameter(String xml, Object params) throws Throwable {
        //		Map<String, PentahoParameter> params = new HashMap<String, PentahoParameter>();
        if (null != xml) {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            PentahoAttributeDOM handler = new PentahoAttributeDOM();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            saxParser.parse(is, handler);
            if (params instanceof Map) {
                ((Map)params).putAll(handler.getParameterMap());
            } else if (params instanceof List) {
                ((List)params).addAll(handler.getParameterList());
            } else {
                // DO NOTHING
            }
        }
    }
    
    @Override
    public void updateConfiguration(PentahoConfiguration config) throws ApsSystemException {
        try {
            String xml = config.toXml();
            this.getConfigManager().updateConfigItem(PentahoSystemConstants.JPPENTAHO5_CONFIG_ITEM, xml);
            _config = config;
        } catch (Throwable t) {
            throw new ApsSystemException("Error updating Pentaho configuration", t);
        }
    }
    
    @Override
    public String getPentahoVersion() throws ApsSystemException {
        String version = null;
        boolean isNumber = true;
        boolean isLengthy = false;
        
        try {
            version = queryPentahoServer(PENTAHO_API_VERSISON);
            if (!Character.isDigit(version.charAt(0))) {
                _logger.warn("version does not start with a number [" + version + "]");
                isNumber = false;
            }
            if (version.length() > 15) {
                _logger.warn("Abnormal version string length detected [" + version + "]");
                isLengthy = true;
            }
            if (isLengthy && !isNumber) {
                throw new RuntimeException("Invalid server signature detected");
            }
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting Penthao server information", t);
        }
        return version;
    }
    
    public ConfigInterface getConfigManager() {
        return _configManager;
    }
    
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    
    private PentahoConfiguration _config;
    // list of common report parameters
    private List<String> _commonReportFields;
    //
    private ConfigInterface _configManager;
    
    // .xaction
    public final static String PENTAHO_API_GENERATE_CONTENT = "/api/repos/%s/generatedContent";
    // .prpt
    public final static String PENTAHO_API_REPORT = "/api/repos/%s/report";
    
    public final static String PENTAHO_API_PARAMETERS = "/api/repos/%s/parameter";
    
    public final static String PENTAHO_API_VERSISON = "api/version/show";
    
    public final static String MANAGER_ID = "jppentaho5Manager";
    
}
