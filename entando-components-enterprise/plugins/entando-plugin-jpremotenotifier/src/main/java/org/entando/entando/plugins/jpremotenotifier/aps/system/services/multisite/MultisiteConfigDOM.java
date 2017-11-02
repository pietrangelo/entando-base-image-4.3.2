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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * 
 * @version 1.0
 * @author E.Santoboni
 * 
<sitesConfig>
	<site code="1">
		<descr>LAPAM PORTAL</descr>
		<fullBaseUrl>http://localhost:8080/lapam/</fullBaseUrl>
	</site>
	....
	....
</sitesConfig>

 * 
 */
public class MultisiteConfigDOM {
	
	public MultisiteConfigDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
		this.extractConfig();
	}
	
	private void extractConfig() {
		this.setResourceManagement();
		List<Element> siteElements = this._doc.getRootElement().getChildren();
		if (null != siteElements && siteElements.size() > 0) {
			for (int i=0; i<siteElements.size(); i++){
				Site site = new Site();
				Element siteElem = (Element) siteElements.get(i);
				site.setCode(siteElem.getAttributeValue(CODE_ATTRIB));
				Element descrElem = siteElem.getChild(DESCR_CHILD);
				site.setDescr(descrElem.getText());
				
				Element urlElem = siteElem.getChild(FULLBASEURL_CHILD);
				site.setFullApplBaseURL(urlElem.getText());
				
				Element baseUrl = siteElem.getChild(BASEURL_CHILD);
				site.setBaseUrl(baseUrl.getText());
				
				Element ipAddr = siteElem.getChild(IP_CHILD);
				if (null != ipAddr) {
					site.setIp(ipAddr.getText());
				}
				
				this._sites.put(site.getCode(), site);
			}
		}
	}

	private void setResourceManagement() {
		Attribute san = this._doc.getRootElement().getAttribute(SAN_ATTRIB);
		if (null != san && san.getValue().equalsIgnoreCase("true")) {
			this.setUseSan(true);
		}
	}
	
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			throw new ApsSystemException("Errore nel parsing", t);
		}
	}
	
	protected Map<String, Site> getSites() {
		return _sites;
	}
	
	public void setUseSan(boolean useSan) {
		this._useSan = useSan;
	}
	public boolean isUseSan() {
		return _useSan;
	}
	
	private Document _doc;
	private Map<String, Site> _sites = new HashMap<String, Site>();
	private boolean _useSan;
	
	public static final String IP_CHILD = "ip";
	public static final String BASEURL_CHILD = "baseUrl";
	public static final String FULLBASEURL_CHILD = "fullBaseUrl";
	public static final String DESCR_CHILD = "descr";
	
	public static final String CODE_ATTRIB = "code";
	public static final String SAN_ATTRIB = "useSan";
}
