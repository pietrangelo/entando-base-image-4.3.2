/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpaltofw.aps.system.service.client;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.entando.entando.plugins.jpaltofw.aps.system.service.user.AltoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class GokibiClient {
	
	private static final Logger _logger = LoggerFactory.getLogger(GokibiClient.class);
	
	public UserDetails getUser(String username, String password, GokibiInstance instance) throws ApsSystemException {
		try {
			String token = this.getToken(instance.getBaseUrl(), true, username, password);
			if (null != token) {
				AltoUser user = new AltoUser();
				user.setInstanceId(instance.getPid());
				user.setUsername(username);
				user.setToken(token);
				return user;
			}
		} catch (Throwable t) {
			_logger.error("error extracting user", t);
			throw new ApsSystemException("error extracting user", t);
		}
		return null;
	}
	
	public List<String> getUsernames(String baseUrl, String text) throws ApsSystemException {
		List<String> usernames = new ArrayList<String>();
		try {
			String urlAddress = baseUrl + "/Service/UserApp";
			Properties headerProperties = new Properties();
			String developToken = this.getToken(baseUrl, false, USERNAME, PASSWORD);
			if (null != developToken) {
				headerProperties.put("Authorization", "Bearer " + developToken);
			}
			String body = this.invokeGetDeleteMethod(urlAddress, true, null, headerProperties); //this.getUsersBody(baseUrl);
			if (null == body) {
				return usernames;
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(body.getBytes(StandardCharsets.UTF_8), JsonNode.class);
			if (null == rootNode) {
				return usernames;
			}
			JsonNode dataNode = rootNode.get("data");
			if (null != dataNode) {
				Iterator<JsonNode> iter = dataNode.getElements();
				while (iter.hasNext()) {
					JsonNode jsonNode = iter.next();
					String username = jsonNode.get("username").asText();
					if (StringUtils.isEmpty(text) || username.contains(text)) {
						usernames.add(username);
					}
				}
			} else {
				JsonNode messageNode = rootNode.get("message");
				if (null != messageNode) {
					_logger.error("invalid request - message '{}'", messageNode.asText());
				} else {
					_logger.error("invalid request");
				}
			}
		} catch (Throwable t) {
			_logger.error("error extracting users", t);
			throw new ApsSystemException("error extracting users", t);
		}
		return usernames;
	}
	
	public String getPlaceholder(String baseUrl, String pid, String widgetCode) throws ApsSystemException {
		//String code = name + "_" + type;
		//form, table, select, search, htmlapp, multiwidget, chart, autocomplete
		String name = null;
		String[] sections = widgetCode.split("_");
		String type = sections[sections.length - 1];
		if (sections.length == 2) {
			name = sections[0];
		} else {
			name = "";
			for (int i = 0; i < sections.length-1; i++) {
				if (i!=0) {
					name += "_";
				}
				name += sections[i];
			}
		}
		String urlAddress = baseUrl + "/Sys?op=placeholder&type=" + type + "&name=" + name + "&pid=" + pid;
		return this.getUrlBody(urlAddress);
	}
	
	public GokibiWidget getWidget(String baseUrl, String pid, String widgetCode) throws ApsSystemException {
		Map<String, GokibiWidget> widgets = this.getWidgets(baseUrl, pid, false);
		return widgets.get(widgetCode);
	}
	
	public Map<String, GokibiWidget> getWidgets(String baseUrl, String pid) throws ApsSystemException {
		return this.getWidgets(baseUrl, pid, true);
	}
	
	private Map<String, GokibiWidget> getWidgets(String baseUrl, String pid, boolean light) throws ApsSystemException {
		Map<String, GokibiWidget> widgets = new HashMap<String, GokibiWidget>();
		try {
			String body = this.getWidgetsBody(baseUrl, pid, light);
			if (null == body) {
				return widgets;
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(body.getBytes(), JsonNode.class);
			Iterator<JsonNode> iter = rootNode.getElements();
			while (iter.hasNext()) {
				JsonNode jsonNode = iter.next();
				GokibiWidget widget = new GokibiWidget();
				String name = jsonNode.get("name").asText();
				String description = jsonNode.get("description").asText();
				widget.setName(name);
				widget.setDescription(name + " - " + description);
				JsonNode placeholederJsonNode = jsonNode.get("placeholder");
				if (null != placeholederJsonNode && StringUtils.isNotEmpty(placeholederJsonNode.asText())) {
					widget.setPlaceholder(placeholederJsonNode.asText());
				}
				String type = jsonNode.get("type").asText();
				widget.setType(type);
				//String code = name + "_" + type;
				widgets.put(widget.getCode(), widget);
			}
		} catch (Throwable t) {
			_logger.error("error extracting widgets", t);
			throw new ApsSystemException("error extracting widgets", t);
		}
		return widgets;
	}
	
	private String getToken(String baseUrl, boolean isLogin, String username, String password) throws ApsSystemException {
		try {
			String url = baseUrl;
			if (isLogin) {
				url += "/Login";
			} else {
				url += "/Signin";
			}
			String body = "{username: \"" + username + "\", password: \"" + password + "\"}";
			String response = this.invokePostPutMethod(url, true, body, null, null, MediaType.APPLICATION_JSON_TYPE);
			if (null == response) {
				return null;
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(response.getBytes(StandardCharsets.UTF_8), JsonNode.class);
			if (null == rootNode) {
				return null;
			}
			JsonNode dataNode = rootNode.get("token");
			if (null != dataNode) {
				return dataNode.asText();
			}
		} catch (Throwable t) {
			_logger.error("error extracting admin token", t);
			throw new ApsSystemException("error extracting admin token", t);
		}
		return null;
	}
	
	private String getWidgetsBody(String baseUrl, String pid, boolean light) throws ApsSystemException {
		String urlAddress = baseUrl + "/Sys?op=widgetcat&pid=" + pid;
		if (light) {
			urlAddress += "&light=1";
		}
		return this.getUrlBody(urlAddress);
	}
	
	private String invokeGetDeleteMethod(String url, boolean isGet, 
			Properties parameters, Properties headerParameters) throws Throwable {
        String result = null;
        HttpMethodBase method = null;
		InputStream inputStream = null;
        try {
            HttpClient client = new HttpClient();
			if (isGet) {
				method = new GetMethod(url);
			} else {
				method = new DeleteMethod(url);
			}
            this.addQueryString(method, parameters);
			this.addHeaders(method, headerParameters);
            client.executeMethod(method);
			inputStream = method.getResponseBodyAsStream();
			result = IOUtils.toString(inputStream, "UTF-8");
        } catch (Throwable t) {
        	_logger.error("Error invoking Get or Delete Method", t);
        } finally {
			if (null != inputStream) {
				inputStream.close();
			}
            if (null != method) {
                method.releaseConnection();
            }
        }
        return result;
    }
	
    private String invokePostPutMethod(String url, boolean isPost, String body, 
			Properties parameters, Properties headerParameters, MediaType mediaType) {
        String result = null;
		EntityEnclosingMethod method = null;
        try {
            HttpClient client = new HttpClient();
			if (isPost) {
				method = new PostMethod(url);
			} else {
				method = new PutMethod(url);
			}
            this.addQueryString(method, parameters);
			method.setRequestBody(body);
			method.setRequestHeader("Content-type", mediaType.toString() + "; charset=UTF-8");
			this.addHeaders(method, headerParameters);
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            result = new String(responseBody);
        } catch (Throwable t) {
        	_logger.error("Error invoking Post or Put Method", t);
        } finally {
            if (null != method) {
                method.releaseConnection();
            }
        }
        return result;
    }
	
	private void addQueryString(HttpMethodBase method, Properties parameters) throws Throwable {
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
    
	private void addHeaders(HttpMethodBase method, Properties parameters) throws Throwable {
		if (null == parameters) return;
		Iterator<Object> keyIter = parameters.keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			method.addRequestHeader(key.toString(), parameters.getProperty(key.toString()));
		}
	}
    
	private String getUrlBody(String urlAddress) throws ApsSystemException {
		String body = null;
		try {
			URL url = new URL(urlAddress);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(3000);
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = (encoding == null) ? "UTF-8" : encoding;
			body = IOUtils.toString(in, encoding);
		} catch (Throwable t) {
			_logger.error("error getting url body - {}",urlAddress, t);
		}
		return body;
	}
	
	private static String USERNAME = "admin";
	private static String PASSWORD = "AltoChangeme123"; 
	
}
