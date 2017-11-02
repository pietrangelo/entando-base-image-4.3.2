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

package org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteUtils {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteUtils.class);
	/**
	 * Return current multisite code suffix. it includes {@literal @}
	 * @param req
	 * @return 
	 */
	public static String getMultisiteCodeSuffix(HttpServletRequest req) {
		String multisiteCode = (String) req.getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		String multisiteSuffix = addCodeSuffix(multisiteCode);
		return multisiteSuffix;
	}
	
	public static String getMultisiteSuffixByString(String code){
		String result = null;
		if(code != null && code.contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)){
			result = code.substring(code.indexOf(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR));
		}
		return result;
	}
	
	public static String getMultisiteCodeByString(String code){
		String result = null;
		if(code != null && code.contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)){
			int startIndex = code.indexOf(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR) + 1;
			result = code.substring(startIndex);
		}
		return result;
	}
	
	
	/**
	 * Return current multisite code suffix. it doesn't include {@literal @}
	 * @param req
	 * @return 
	 */
	public static String getMultisiteCode(HttpServletRequest req) {
		String multisiteCode = (String) req.getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		return multisiteCode;
	}
	
	private static String addCodeSuffix(String multisiteSuffix) {
		if(StringUtils.isNotBlank(multisiteSuffix)){
			multisiteSuffix = JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisiteSuffix;
		}
		return multisiteSuffix;
	}
	
	public static String getRootGroupForMultisite(String code) {
		return JpmultisiteSystemConstants.MULTISITE_GROUP_PREFIX + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + code;
	}
	
	
	public static String replaceMultisiteCodeWithSuffix(String string, String originalCode, String substitute){
		return (string != null)?string.replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + originalCode, JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + substitute):null;
	}
	
	public static String cutMultisiteCodeWithSuffix(String string, String multisiteCode){
		if(StringUtils.isNotBlank(string) && string.endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisiteCode)){
			StringBuilder newString = new StringBuilder(string);
			newString.replace(string.lastIndexOf("@"), string.length(), "" );
			string = newString.toString();
		}
		return string;
	}
}
