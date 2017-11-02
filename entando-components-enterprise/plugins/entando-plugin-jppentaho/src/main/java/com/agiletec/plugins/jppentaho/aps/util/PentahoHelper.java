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
package com.agiletec.plugins.jppentaho.aps.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportDownload;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParamUIInfo;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

public class PentahoHelper {
	
	public String convertStreamToString(ReportDownload reportDownload) throws IOException {
		InputStream is = new FileInputStream(reportDownload.getReportPath());
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	public void addReportParamsFromProfile(UserDetails user, String profileParams, Map<String, String> params) {
		Map<String, String> paramsFromProfile = null;
		if (null != profileParams && profileParams.length() > 0 ) {
			paramsFromProfile = this.fromQueryStringToParamsMap(profileParams);
		}
		if (null == paramsFromProfile) {
			paramsFromProfile = new HashMap<String, String>();
		}
		if (null != paramsFromProfile && paramsFromProfile.size() > 0 ) {
			Set<String> paramName = paramsFromProfile.keySet();
			Iterator<String> param = paramName.iterator();
			IUserProfile profile = (IUserProfile) user.getProfile();
			if (null != profile) {
				while (param.hasNext()) {
					String current = param.next();
					String attributeName = paramsFromProfile.get(current);
					MonoTextAttribute monoTextAttribute = (MonoTextAttribute) profile.getAttribute(attributeName);
					String text = monoTextAttribute.getText();
					params.put(current, text);
				}
			}
		}
	}
	
	public Map<String, String> fromQueryStringToParamsMap(String queryString) {
		Map<String, String> params = new HashMap<String, String>();
		String[] couples = queryString.split("&");
		for (int i = 0 ; i < couples.length; i++) {
			String couple = couples[i];
			String[] nameAndValue = couple.split("=");
			params.put(nameAndValue[0], nameAndValue[1]);
		}
		return params;
	}
	
	public Map<String, String> loadParams(ServletRequest request, ReportParamUIInfo paramUI) {
		return this.loadParams(request, paramUI.getParam());
	}
	
	public Map<String, String> loadParams(ServletRequest request, List<ReportParameter> params) {
		Map<String, String> paramsForUrl = new HashMap<String, String>();
		Iterator<ReportParameter> paramsIter = params.iterator();
		while (paramsIter.hasNext()) {
			ReportParameter current = paramsIter.next();
			String name = current.getName();
			String value = request.getParameter(name);
			if (current.getType().equals("java.util.Date") && null != value && value.trim().length() > 0 ) {
				value = this.valueDateStringToTimestamp(value);
			}
			paramsForUrl.put(name, value);
		}
		return paramsForUrl;
	}

	public String extractOnlyHtmlBody(String html) {
		int indexStartBody = html.indexOf("<body>");
		html = html.substring(indexStartBody + "<body>".length());
		int indexEndBody = html.indexOf("</body>");
		html = html.substring(0, indexEndBody);
		return html;
	}
	
	public String valueDateStringToTimestamp(String date) {
		Date dateToConvert = DateConverter.parseDate(date, "dd/MM/yyyy");
		Long timestamp = dateToConvert.getTime();
		return timestamp.toString();
	}
	
}