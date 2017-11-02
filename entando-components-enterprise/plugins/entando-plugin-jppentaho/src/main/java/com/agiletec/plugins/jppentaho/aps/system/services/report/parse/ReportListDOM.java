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
package com.agiletec.plugins.jppentaho.aps.system.services.report.parse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;


/**
 * DOM for reading reports list
 * 
 * @author zuanni G.Cocco
 * */
/*
 * 
 
 <tree>
	<leaf>
		<leafText>Elenco Progetti</leafText>
		<link>http://192.168.184.13:8080/pentaho/content/reporting/reportviewer/report.html?solution=agiletec&amp;path=&amp;name=Elenco
			Progetti.prpt&amp;userid=admin&amp;password=admin|1288196683464|1|I2jgsSZnmAo4sgZuuiDJ9tbmexk=
		</link>
		<target>_blank</target>
		<title>/pentaho-solutions/agiletec/Elenco Progetti.prpt</title>
	</leaf>
	<leaf>
		<leafText>ProgettiDipOreCostoXLS</leafText>
		<link>http://192.168.184.13:8080/pentaho/content/reporting/reportviewer/report.html?solution=agiletec&amp;path=&amp;name=ProgettiDipOreCostoXLS.prpt&amp;userid=admin&amp;password=admin|1288196683464|1|I2jgsSZnmAo4sgZuuiDJ9tbmexk=
		</link>
		<target>_blank</target>
		<title>/pentaho-solutions/agiletec/ProgettiDipOreCostoXLS.prpt</title>
	</leaf>
  </tree>
  
  
  
  leaf sample in caso di XAction
  
  
  	<leaf>
		<leafText>Flash Chart List</leafText>
		<link>/pentaho/ViewAction?solution=steel-wheels&amp;path=charts&amp;action=pentahoxml_picker.xaction&amp;userid=joe&amp;password=joe|1304529736821|1|U95xSmg%2Fmyd5MEAqql57xOoCt8I=
		</link>
		<target>_blank</target>
		<title>This demonstrates multiple chart types using the ChartComponent
			in the action sequence</title>
	</leaf>
	
	<!-- -->
  <leaf>
		<leafText>Chart Examples</leafText>
		<link>ChartSamplesDashboard?userid=joe&amp;password=joe|1304529736821|1|U95xSmg%2Fmyd5MEAqql57xOoCt8I=
		</link>
		<target>_blank</target>
		<title>This dashboard shows the variety of chart types that are
			supported by Pentaho Dashboards.</title>
	</leaf>
  
 
 leaf sample in caso di CDF
<leaf>
<leafText>Map dashboard</leafText>
<link>/pentaho/content/pentaho-cdf/RenderXCDF?solution=bi-developers&amp;path=cdf-samples%2F20-samples%2Fmap_dashboard&amp;action=map.xcdf&amp;template=mantle&amp;userid=joe&amp;password=joe|1325071484904|1|q1b4bIx1fKtPGu5cJz1jrlK%2BnGA=</link>
<target>_blank</target>
<title>Map dashboard</title>
</leaf>


 */
public class ReportListDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportListDOM.class);
	
    public ReportListDOM(String xml) throws ApsSystemException {
        this.decodeDOM(xml);
    }
    
    public List<ReportHttpParam> getReportParams() {
        List<ReportHttpParam> rep = null;
        List<Element> elements = _document.getRootElement().getChildren("leaf");
        for (int i = 0; i < elements.size(); i++) {
            if (rep == null) {
                rep = new ArrayList<ReportHttpParam>();
            }
            Element current = elements.get(i);
            Element currentLinkEl = current.getChild("link");
            ReportHttpParam repParam = this.extractReportInfo(currentLinkEl);
            if (null != repParam) {
                Element currentLeafTextEl = current.getChild("leafText");
                String descr = currentLeafTextEl.getText();
                if (null != descr && descr.length() > 0) {
                    repParam.setDescription(descr);
                } else {
                    repParam.setDescription(repParam.getName());
                }
                Element currentTitleEl = current.getChild("title");
                String title = currentTitleEl.getText();
                if (null != title && title.length() > 0) {
                    repParam.setTitle(title);
                } else {
                    repParam.setTitle(repParam.getName());
                }
                rep.add(repParam);
            }
        }
        return rep;
    }
    
    protected ReportHttpParam extractReportInfo(Element currentLinkEl) {
        String link = currentLinkEl.getText();
        Map<String, String> params = this.extractParamsFromLink(link);
        ReportHttpParam reportParam = new ReportHttpParam();
        String baseUrl = params.get(BASE_URL);
        String viewActionUrl = "pentaho/ViewAction";
        String contentReporting = "pentaho/content/reporting/reportviewer";
        String cdf = "pentaho/content/pentaho-cdf/RenderXCDF";
        if (baseUrl.contains(viewActionUrl)) {
            reportParam.setType(ReportHttpParam.TYPE_XACTION);
            reportParam.setAction(params.get("action"));
        } else if (baseUrl.contains(contentReporting)) {
            reportParam.setType(ReportHttpParam.TYPE_PRPT);
            reportParam.setName(params.get("name"));
        } else if (baseUrl.contains(cdf)) {
            reportParam.setType(ReportHttpParam.TYPE_CDF);
            reportParam.setAction(params.get("action"));
        } else {
            return null;
        }
        reportParam.setPath(params.get("path"));
        reportParam.setSolution(params.get("solution"));
        return reportParam;
    }
    
    protected Map<String, String> extractParamsFromLink(String link) {
        Map<String, String> params = new HashMap<String, String>();
        int questionMarkIndex = link.indexOf("?");
        String baseUrl = link.substring(0, questionMarkIndex);
        params.put(BASE_URL, baseUrl);
        String paramsSubStr = link.substring(questionMarkIndex + 1, link.length());
        String[] paramItem = paramsSubStr.split("&");
        for (String item : paramItem) {
            String[] keyAndValue = item.split("=");
            String value = "";
            if (keyAndValue.length == 2) {
                value = keyAndValue[1];
            }
            params.put(keyAndValue[0], value);
        }
        return params;
    }
    
    private void decodeDOM(String xmlText) throws ApsSystemException {
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setValidation(false);
            StringReader reader = new StringReader(xmlText);
            this._document = builder.build(reader);
        } catch (Throwable t) {
            _logger.error("Error parsing xml: {}", xmlText, t);
            throw new ApsSystemException("Error on parsing reports list - XML " + xmlText, t);
        }
    }
    
    private Document _document;
    private final static String BASE_URL = "baseUrl";
    
}
