/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpsubsites.apsadmin.subsite.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;

/**
 *
 * @author io
 */
public class SubsiteUtils {

	private static final Logger _logger = LoggerFactory.getLogger(SubsiteUtils.class);
	/**
	 * Return current multisite code suffix. it includes {@literal @}
	 * @param req
	 * @return 
	 */
	public static String getMultisiteCodeSuffix(HttpServletRequest req) {
		String multisiteCode = (String) req.getSession().getAttribute(JpsubsitesSystemConstants.SESSION_PAR_CURRENT_SUBSITE);
		String multisiteSuffix = addCodeSuffix(multisiteCode);
		return multisiteSuffix;
	}
	
	public static String getMultisiteSuffixByString(String code){
		String result = null;
		if(code != null && code.contains("@")){
			result = code.substring(code.indexOf("@"));
		}
		return result;
	}
	
	public static String getMultisiteCodeByString(String code){
		String result = null;
		if(code != null && code.contains("@")){
			int startIndex = code.indexOf("@") + 1;
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
		String multisiteCode = (String) req.getSession().getAttribute(JpsubsitesSystemConstants.SESSION_PAR_CURRENT_SUBSITE);
		return multisiteCode;
	}
	
	private static String addCodeSuffix(String multisiteSuffix) {
		if(StringUtils.isNotBlank(multisiteSuffix)){
			multisiteSuffix = "@" + multisiteSuffix;
		}
		return multisiteSuffix;
	}
	
	public static String getRootGroupForMultisite(String code) {
		return JpsubsitesSystemConstants.SUBSITE_GROUP_PREFIX + "@" + code;
	}
	
	
	public static String replaceMultisiteCodeWithSuffix(String string, String originalCode, String substitute){
		return (string != null)?string.replace("@" + originalCode, "@" + substitute):null;
	}
	
	public static String cutMultisiteCodeWithSuffix(String string, String multisiteCode){
		if(StringUtils.isNotBlank(string) && string.endsWith("@" + multisiteCode)){
			StringBuilder newString = new StringBuilder(string);
			newString.replace(string.lastIndexOf("@"), string.length(), "" );
			string = newString.toString();
		}
		return string;
	}
}
