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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.parse.JasperConfigDOM;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptorDOM;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperRunReportResponse;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperRunReportResponseDOM;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.JasperInputControlDOM;
import org.entando.entando.plugins.jpjasper.aps.services.util.QueryStringBuilder;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.InputControlConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;

public class JasperServerManager extends AbstractService implements IJasperServerManager {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperServerManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
		this.loadConfigs();
		this.initDownloadModels();
	}
	
	/**
	 * Load the XML configuration for the plugin
	 * @throws ApsSystemException
	 */
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpJasperSystemConstants.JASPER_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpJasperSystemConstants.JASPER_CONFIG_ITEM);
			}
			JasperConfigDOM configDOM = new JasperConfigDOM();
			this.setJasperConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading config parameters", t);
			throw new ApsSystemException("Error loading config parameters", t);
		}
	}
	
	@Override
	public void updateJasperConfig(JasperConfig config) throws ApsSystemException {
		try {
			String xml = new JasperConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpJasperSystemConstants.JASPER_CONFIG_ITEM, xml);
			this.setJasperConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating {}", JpJasperSystemConstants.JASPER_CONFIG_ITEM, t);
			throw new ApsSystemException("Error in " + JpJasperSystemConstants.JASPER_CONFIG_ITEM + " update", t);
		}
	}

	private void initDownloadModels() {
		Map<String, DownloadModel> map = new HashMap<String, DownloadModel>();
		DownloadModel pdf = new DownloadModel("pdf", "application/pdf");
		DownloadModel csv = new DownloadModel("csv", "text/csv");
		DownloadModel html = new DownloadModel("html", "text/html");
		DownloadModel odt = new DownloadModel("odt", "application/vnd.oasis.opendocument.text");
		DownloadModel xml = new DownloadModel("xml", "text/xml");
		
		map.put("pdf", pdf);
		map.put("csv", csv);
		map.put("html", html);
		map.put("odt", odt);
		map.put("xml", xml);
		this.setDownloadModelsMap(map);
	}

	@Override
	public DownloadModel getDownloadModel(String key) {
		return this.getDownloadModelsMap().get(key);
	}

	public String getRestEndpointUrl() {
		String baseUrl = this.getJasperConfig().getBaseURL();
		if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
		String restEndpointUrl = baseUrl + JpJasperSystemConstants.BASE_REST_URL;
		return restEndpointUrl;
	}

	public String getRestV2ReportsEndpointUrl() {
		String baseUrl = this.getJasperConfig().getBaseURL();
		if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
		String v2 = JpJasperSystemConstants.REST_V2_REPORTS;
		if (v2.startsWith("/")) v2 = v2.substring(1);
		String restEndpointUrl = baseUrl + v2;
		return restEndpointUrl;
	}

	@Override
	public boolean ping(String cookieHeader) throws ApsSystemException {
		boolean alive = false;
		try {
		HttpClient client = new HttpClient();
		String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_RESOURCES;
		HttpMethod method = new GetMethod(endpoint);
		method.addRequestHeader("Cookie", cookieHeader);

		Properties params = new Properties();
		params.put("q", "entando_to_check_session_" );
		this.addQueryString(method, params);
		client.executeMethod(method);

		byte[] responseBody = method.getResponseBody();
		String resourceXMLResponse = new String(responseBody);
		alive = resourceXMLResponse.startsWith("<resourceDescriptor");
		} catch (Throwable t) {
			_logger.error("Error trying to ping jasper server", t);
			throw new ApsSystemException("Error trying to ping jasper server", t);
		}
		return alive;
	}
		
	@Override
	public List<JasperResourceDescriptor> searchRestResources(String cookieHeader, String query, String type, String folder, boolean recursive) throws ApsSystemException {
		List<JasperResourceDescriptor> resourceDescriptors = null;
		try {
			HttpClient client = new HttpClient();
			//1 endpoint: e comprensivo del path passata come parametro
			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_RESOURCES;
			if (!StringUtils.isBlank(folder)) {
				if (!folder.startsWith("/")) folder = "/"+ folder;
				endpoint = endpoint + folder;
			}

			//2 client client a partire dall'utente 
			/*HttpClient client = this.getDefaultHttpClient(userDetails);*/

			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);

			Properties params = new Properties();
			if (!StringUtils.isBlank(type)) {
				params.put("type", type);
			}
			if (!StringUtils.isBlank(query)) {
				params.put("q", query);
			}
			if (!params.isEmpty()) {
				int recursiveSearch = 0;
				if (recursive) recursiveSearch = 1;
				params.put("recursive", new Integer(recursiveSearch).toString());
			}
			this.addQueryString(method, params);
			
			_logger.debug("GET - searchRestResources:  {}?{}",endpoint ,method.getQueryString());
			client.executeMethod(method);

			byte[] responseBody = method.getResponseBody();
			String resourceXMLResponse = new String(responseBody);
		
			JasperResourceDescriptorDOM parser = new JasperResourceDescriptorDOM();
			resourceDescriptors = parser.parseXML(resourceXMLResponse);

		} catch (Throwable t) {
			_logger.error("Error in  searchRestResources", t);
			throw new ApsSystemException("Error in  searchRestResources", t);
		}
		return resourceDescriptors;
	}


	/**
	 * Effettua una richiesta di una risporsa.
	 * http://<host>:<port>/jasperserver[-pro]/rest/resource/path/to/resource/
	 * @param userDetails
	 * @param uriString
	 * @param client
	 * @return
	 * @throws ApsSystemException
	 */
	public String getRestResourceXML(String cookieHeader, String uriString, String paramsString) throws ApsSystemException {
		String resourceResponse = null;
		try {
			HttpClient client = new HttpClient();
			
			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_RESOURCE;
			endpoint = endpoint + uriString;

			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);

			client.executeMethod(method);
			_logger.debug("GET - getRestResourceXML  endopoint: {}?{}", endpoint, method.getQueryString());

			byte[] responseBody = method.getResponseBody();
			resourceResponse = new String(responseBody);

			resourceResponse = this.addParametersToResource(resourceResponse, paramsString);
		} catch (Throwable t) {
			_logger.error("Error in getRestResourceXML for uriString {}", uriString, t);
			throw new ApsSystemException("Error in getRestResourceXML for uriString " + uriString, t);
		}

		return resourceResponse;
	}

	private String addParametersToResource(String resourceResponseXml, String paramsString) throws ApsSystemException {
		String newXml = resourceResponseXml;
		try {
			if (!StringUtils.isBlank(paramsString)) {
				Map<String, InputControlConfig> params = InputControlConfig.buildInputControlMapFromConfig(paramsString);
				if (null != params && params.size() > 0) {
					SAXBuilder builder = new SAXBuilder();
					builder.setValidation(false);
					StringReader reader = new StringReader(resourceResponseXml);
					Document doc = builder.build(reader);
					Element root = doc.getRootElement();

					for (Map.Entry<String, InputControlConfig> entry : params.entrySet()) {
						InputControlConfig configParam = entry.getValue();
						for (int i = 0; i < configParam.getValue().size(); i++) {
							Element paramElement= new Element("parameter");
							paramElement.setAttribute("name", configParam.getId());
							if (configParam.getListValue().equalsIgnoreCase("true")) {
								paramElement.setAttribute("isListItem", "true");
							}
							if (configParam.getDataType() == InputControlConfig.DT_TYPE_DATE) {
								Date d =DateConverter.parseDate(configParam.getValue().get(i), "yyyy-MM-dd");
								paramElement.setText(new Long(d.getTime()).toString());
							} else {
								paramElement.setText(configParam.getValue().get(i));
							}
							root.addContent(paramElement);
						}
					}
					XMLOutputter outp = new XMLOutputter();
					newXml = outp.outputString(root);
				}
			}

		} catch (Throwable t) {
			_logger.error("Error adding paramenters to resource", t);
			throw new ApsSystemException("Error in adding paramenters to resource ", t);
		}
		return newXml;
	}

	@Override
	public JasperResourceDescriptor getRestResource(String cookieHeader, String uriString) throws ApsSystemException {
		JasperResourceDescriptor resourceDescriptor = null;
		try {
			String resourceXMLResponse = this.getRestResourceXML(cookieHeader, uriString, null);
			JasperResourceDescriptorDOM parser = new JasperResourceDescriptorDOM();
			resourceDescriptor = parser.parseResource(resourceXMLResponse);

		} catch (Throwable t) {
			_logger.error("error in getRestResource {}", uriString, t);
			throw new ApsSystemException("error in getRestResource", t);
		}
		return resourceDescriptor;
	}

	/**
	 * Run a report using the restV2 api.<br />
	 * Eg: http://[host]:[port]/jasperserver[-pro]/rest_v2/reports/path/to/report.[format]?[arguments]<br />
	 * The new v2/reports service allows clients to receive report output in a single request-response. The output format is specified in the URL as a file extension to the report URI.
	 * @param userDetails User
	 * @param path
	 * @param format can be html, pdf, xls, rtf, csv, xml, jrprint //TODO mettere in un file di costanti
	 * @param arguments approfindire
	 * @return
	 * @throws ApsSystemException
	 */
	@Override
	public InputStream runReportRestV2(String cookieHeader, String path, String format, String inputControlParams) throws ApsSystemException {
		InputStream is = null;
		try {
			HttpClient client = new HttpClient();

			String endpoint = this.getRestV2ReportsEndpointUrl();

			if (!path.startsWith("/")) path = "/"+ path;
			endpoint = endpoint + path;
			endpoint = endpoint + "." + format;
			
			Map<String, InputControlConfig> icMap = InputControlConfig.buildInputControlMapFromConfig(inputControlParams);
			String[][] icArray = InputControlConfig.inputControlConfigToArray(icMap);
			
			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);

			String qs = QueryStringBuilder.buildQueryString(icArray);
			method.setQueryString(qs);

			_logger.debug("GET - runReportRestV2 enpoint: {}?{}", endpoint, method.getQueryString());

			client.executeMethod(method);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			is = method.getResponseBodyAsStream();
			IOUtils.copy(is, out);
			is.close();

			is = new ByteArrayInputStream(out.toByteArray());

		} catch (Throwable t) {
			_logger.error("Error running report:{} format: {}", path, format);
			throw new ApsSystemException("Error running report:" + path + "  format:" + format, t);
		}
		return is;
	}

	/**
	 * Run a report using the rest api.<br />
	 * Eg: http://<host>:<port>/jasperserver[-pro]/rest/report/path/to/report<br />
	 * The report service uses a combination of the PUT, GET, and POST methods to run reports and make them available in multiple ways through the API
	 * <li>The PUT method generates the report in any number of formats, stores the output in the session, and returns an identifier.</li>
	 * <li>The GET method with the identifier and file ID downloads any one of the file outputs.</li>
	 * <li>The POST method can be used to regenerate the report, for example in different formats, and supports page-by-page downloading of the PDF output.</li>
	 * 
	 * @param userDetails
	 * @param path uriString
	 * @param format The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,XML, JRPRINT. The Default is PDF.
	 * @param imagesUri Questo parametro determina la directory di salvataggio dell'immagine! The uri prefix used for images when exporting in HTML. The default is images/
	 * @param page (optional) An integer value used to export a specific page
	 * @param ignorePagination (optional) When true, the report output will be generated on a single page in all export formats. When false or omitted, all export formats will be paginated
	 * @param arguments input controls
	 * @return
	 * @throws ApsSystemException
	 */
	@Override
	public JasperRunReportResponse runReportRest(String cookieHeader, String path, String format, String imagesUri, Integer page, Boolean ignorePagination, Object arguments) throws ApsSystemException {
		JasperRunReportResponse response = null;
		try {
			HttpClient client = new HttpClient();//.getLoggedClient(userDetails);

			String resourceXMLResponse = this.getRestResourceXML(cookieHeader, path, (String) arguments);

			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_REPORT;

			if (!path.startsWith("/")) path = "/"+ path;
			endpoint = endpoint + path;

			EntityEnclosingMethod method = new PutMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);
			//TODO 5 GESTIRE I PARAMETRI arguments
			if (StringUtils.isBlank(format)) {
				format = "pdf";
			}
			Properties params = new Properties();
			params.put("RUN_OUTPUT_FORMAT", format);
			if (!StringUtils.isBlank(imagesUri)) {
				params.put("IMAGES_URI", imagesUri);
			}
			if (null != page && page > 0) {
				params.put("PAGE", page.toString());
			}
			if (null != ignorePagination) {
				params.put("ignorePagination", ignorePagination.booleanValue());
			}

			this.addQueryString(method, params);
			method.setRequestEntity(new StringRequestEntity(resourceXMLResponse, "application/xml",  "UTF-8"));
			client.executeMethod(method);

			String runResponseXML = method.getResponseBodyAsString();
			
			JasperRunReportResponseDOM parser = new JasperRunReportResponseDOM();
			response = parser.parseXML(runResponseXML);

			response.setImagesUri(imagesUri);
			response.setReportFormat(format);

		} catch (Throwable t) {
			_logger.error("error in runReportRest", t);
			throw new ApsSystemException("Error in runReportRest", t);
		}

		return response;
	}

	public JasperRunReportResponse regenerateReportByUUIDAsStream(String cookieHeader, String uuid, String reportFile, String imagesUri,String format, String paramsString) throws ApsSystemException {
		JasperRunReportResponse response = null;
		try {
			HttpClient client = new HttpClient();//.getLoggedClient(userDetails);

			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_REPORT;

			if (!endpoint.endsWith("/")) {
				endpoint = endpoint+ "/";
			}
			endpoint = endpoint + uuid;

			EntityEnclosingMethod method = new PostMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);

			if (StringUtils.isBlank(format)) {
				format = "pdf";
			}

			Properties params = new Properties();
			//TODO 5 GESTIRE I PARAMETRI arguments

			params.put("RUN_OUTPUT_FORMAT", format);
			if (!StringUtils.isBlank(imagesUri)) {
				params.put("IMAGES_URI", imagesUri);
			}

			this.addQueryString(method, params);

			//System.out.println("regenerateReportByUUIDAsStream POST ENDOPOINT: "  + endpoint + method.getQueryString());
			client.executeMethod(method);

			String runResponseXML = method.getResponseBodyAsString();
			JasperRunReportResponseDOM parser = new JasperRunReportResponseDOM();
			response = parser.parseXML(runResponseXML);

			//response.setClient(client);
			response.setImagesUri(imagesUri);
			response.setReportFormat(format);

		} catch (Throwable t) {
			_logger.error("error in runReportRest", t);
			throw new ApsSystemException("error in runReportRest", t);
		}
		return response;
	}

	/**
	 * Scarica un file del JasperRunReportResponse
	 * @param userDetails
	 * @param jasperRunReportResponse
	 * @param client
	 * @param file
	 * @throws ApsSystemException
	 */

	@Override
	public String getReportFileAsString(String cookieHeader, String uuid, String file) throws ApsSystemException {
		OutputStream out = null;
		InputStream is = null;
		String response = null;
		try {
			HttpClient client = new HttpClient();

			//1 endopint con uuid
			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_REPORT;

			if (!endpoint.endsWith("/")) endpoint = endpoint + "/";
			endpoint = endpoint + uuid;
			System.out.println("getReportFileAsString GET ENDOPOINT: "  + endpoint);

			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);
			Properties params = new Properties();
			params.put("file", file);

			this.addQueryString(method, params);

			client.executeMethod(method);

			is = method.getResponseBodyAsStream();
			response = IOUtils.toString(is, "UTF-8");

		} catch (Throwable t) {
			_logger.error("error in getReportFileAsString", t);
			throw new ApsSystemException("error in getReportFileAsString", t);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
		}
		return response;
	}

	@Override
	public InputStream getReportFileAsStream(String cookieHeader, String uuid, String file) throws ApsSystemException {
		InputStream is = null;
		try {
			HttpClient client = new HttpClient();

			//1 endopint con uuid
			String endpoint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_REPORT;

			if (!endpoint.endsWith("/")) endpoint = endpoint + "/";
			endpoint = endpoint + uuid;

			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);
			Properties params = new Properties();
			params.put("file", file);
			this.addQueryString(method, params);

			client.executeMethod(method);

			is = method.getResponseBodyAsStream();
			System.out.println("getReportFileAsStream GET ENDOPOINT: "  + endpoint + "?" + method.getQueryString() + " >> " + is.hashCode());

		} catch (Throwable t) {
			_logger.error("error in getReportFileAsStream", t);
			throw new ApsSystemException("Error in getReportFileAsStream", t);
		}
		return is;
	}

	@Override
	public InputStream getReportImageV2(String cookieHeader, String path) throws ApsSystemException {
		InputStream is = null;
		try {
			HttpClient client = new HttpClient();
			String endpoint = this.getRestV2ReportsEndpointUrl() + "/" + path;
			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);
			client.executeMethod(method);
			is = method.getResponseBodyAsStream();
		} catch (Throwable t) {
			_logger.error("Error getting image {}", path, t);
			throw new ApsSystemException("getReportImageV2 - Error getting image " + path, t);
		}
		return is;
	}

	@Override
	public List<InputControl> getInputControlsV2(String cookieHeader, String uriString, String[][] inputControlParams) throws ApsSystemException {
		List<InputControl> icList = null;
		try {
			HttpClient client = new HttpClient();

			//2 endpoint: e comprensivo della folder passata come parametro
			String endpoint = this.getRestV2ReportsEndpointUrl();

			//3 costruisco la parte finale del path con il formato
			if (!uriString.startsWith("/")) uriString = "/"+ uriString;
			endpoint = endpoint + uriString;
			endpoint = endpoint + "/inputControls";

			//4 costruisco il metodo
			HttpMethod method = new GetMethod(endpoint);
			method.addRequestHeader("Cookie", cookieHeader);
			//TODO 5 PARAMETRI ???
			//Properties params = new Properties();
			//params.putAll(inputControlParams);
			//this.addQueryString(method, inputControlParams);
			
			String qs = QueryStringBuilder.buildQueryString(inputControlParams);
			method.setQueryString(qs);
			//System.out.println("getInputControlsV2 GET ENDOPOINT: "  + endpoint + "?" + method.getQueryString());
			_logger.debug("GET - getInputControlsV2 endpoint: {}?{}",endpoint, method.getQueryString());
			
			//6 effettuo la chiamata e recupero l'xml
			client.executeMethod(method);


			byte[] responseBody = method.getResponseBody();
			if (null != responseBody) {
				String icXMLResponse = new String(responseBody);
				JasperInputControlDOM parser = new JasperInputControlDOM();
				icList = parser.parseXML(icXMLResponse);
			} else {
				_logger.info("getInputControlsV2 for report {} returned null", uriString);
			}


		} catch (Throwable t) {
			_logger.error("error in getInputControlsV2", t);
			throw new ApsSystemException("error in getInputControlsV2", t);
		}
		return icList;
	}

	@Override
	public String getCookieHeader(UserDetails userDetails) throws ApsSystemException {
		String cookieHeader = null;
		try {
			HttpClient client = new HttpClient();
			String loginEndopint = this.getRestEndpointUrl() + JpJasperSystemConstants.BASE_REST_LOGIN;
			HttpMethod loginMethod = new PostMethod(loginEndopint);
			Properties params = new Properties();
			//USERNAME == PASSWORD
			//System.out.println("FAKE: USERNAME == PASSWORD");

			String username = userDetails.getUsername();
			
			String organization = this.getOrganizationValue(userDetails);
			if (StringUtils.isNotBlank(organization)) {
				username = username + "|" + organization;
			}

			params.put(JpJasperSystemConstants.JASPER_PARAM_USERNAME, username);
			if (!StringUtils.isBlank(userDetails.getPassword())) {
				params.put(JpJasperSystemConstants.JASPER_PARAM_PASSWORD, userDetails.getPassword());
			}

			this.addQueryString(loginMethod, params);
			int status = client.executeMethod(loginMethod);
			if (status != 200) {
				_logger.warn("INVALID LOGIN: STATUS: {}", status);
			} else {
				Header cookieResponseHeader = loginMethod.getResponseHeader("Set-Cookie");
				cookieHeader = cookieResponseHeader.getValue();
				_logger.debug("login ok for user {}", userDetails.getUsername());
			}

		} catch (ConnectException t) {
			_logger.error("Error in getCookieHeader", t);
			throw new ApsSystemException("Error in getCookieHeader ", t);
		} catch (Throwable t) {
			_logger.error("Error in getCookieHeader", t);
			throw new ApsSystemException("Error in getCookieHeader ", t);
		}
		return cookieHeader;
	}

	/**
	 * Extract the organization value given a {@link UserDetails}<br />
	 * @param userDetails the user to extract the organization from
	 * @return the organization if exists or null
	 * @throws ApsSystemException
	 */
	protected String getOrganizationValue(UserDetails userDetails) throws ApsSystemException {
		String organization = null;
		IUserProfile userProfile = this.getUserProfileManager().getProfile(userDetails.getUsername());
		if (null != userProfile) {
			AttributeInterface attr = userProfile.getAttributeByRole(JpJasperSystemConstants.ROLE_ATTRIBUTE_ORGANIZATION);
			if (null != attr && attr instanceof AbstractTextAttribute)
			organization = ((AbstractTextAttribute)attr).getText();
		}
		return organization;
	}

	private void addQueryString(HttpMethod method, Properties parameters) throws Throwable {
		if (null == parameters) return;
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		Iterator<Object> keyIter = parameters.keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			if (!first) builder.append("&");
			builder.append(key.toString()).append("=").append(parameters.getProperty(key.toString()));
			if (first) first = false;
		}
		method.setQueryString(URIUtil.encodeQuery(builder.toString()));
	}

	@Override
	public String getJasperBaseUrl() throws ApsSystemException {
		return this.getJasperConfig().getBaseURL();
	}

	public JasperConfig getJasperConfig() {
		return _jasperConfig;
	}
	public void setJasperConfig(JasperConfig jasperConfig) {
		this._jasperConfig = jasperConfig;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public Map<String, DownloadModel> getDownloadModelsMap() {
		return downloadModelsMap;
	}
	public void setDownloadModelsMap(Map<String, DownloadModel> downloadModelsMap) {
		this.downloadModelsMap = downloadModelsMap;
	}

	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	private JasperConfig _jasperConfig;
	private ConfigInterface _configManager;
	private Map<String, DownloadModel> downloadModelsMap;
	private IUserProfileManager _userProfileManager;

}
