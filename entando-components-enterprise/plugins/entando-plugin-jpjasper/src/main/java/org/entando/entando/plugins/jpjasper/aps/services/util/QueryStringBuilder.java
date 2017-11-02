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
package org.entando.entando.plugins.jpjasper.aps.services.util;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.httpclient.util.URIUtil;

public class QueryStringBuilder {


	public static String buildQueryString (Properties parameters) throws Throwable {
		if (null == parameters) return "";
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		Iterator<Object> keyIter = parameters.keySet().iterator();
		while (keyIter.hasNext()) {
			Object key = keyIter.next();
			if (!first) builder.append("&");
			builder.append(key.toString()).append("=").append(parameters.getProperty(key.toString()));
			if (first) first = false;
		}
		return URIUtil.encodeQuery(builder.toString());
	}

	public static String buildQueryString (String[][] parameters) throws Throwable {
		if (null == parameters) return "";
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (int i=0; i<parameters.length; i++) {
			if (!first) builder.append("&");
			String value = java.net.URLEncoder.encode(parameters[i][1].trim(), "utf8");
			builder.append(parameters[i][0].toString()).append("=").append(value);
			if (first) first = false;
		}
		return builder.toString();
	}
	
}
