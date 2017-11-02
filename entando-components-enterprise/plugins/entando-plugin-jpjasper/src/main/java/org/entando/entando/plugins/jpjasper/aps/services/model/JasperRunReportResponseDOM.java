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
package org.entando.entando.plugins.jpjasper.aps.services.model;

import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
<uuid>d7bf6c9-9077-41f7-a2d4-8682e74b637e</uuid>
<originalUri>/reports/samples/AllAccounts</originalUri>

<totalPages>43</totalPages>
<startPage>1</startPage>
<endPage>43</endPage>

<file type="image/png">img_0_0_0</file>
 *
 */
public class JasperRunReportResponseDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperRunReportResponseDOM.class);
	
	private static final String ELEMENT_UUID = "uuid";
	private static final String ELEMENT_ORIGINAL_URI = "originalUri";

	private static final String ELEMENT_TOTAL_PAGES = "totalPages";
	private static final String ELEMENT_START_PAGE = "startPage";
	private static final String ELEMENT_END_PAGE = "endPage";

	private static final String ELEMENT_FILE = "file";
	private static final String ATTR_FILE_TYPE = "type";
	
	public JasperRunReportResponse parseXML(String xml) throws ApsSystemException {
		JasperRunReportResponse responseObject = null;
		Element root = this.getRootElement(xml);
		
		responseObject = this.parseElement(root);
		return responseObject;
	}

	private JasperRunReportResponse parseElement(Element root) throws ApsSystemException {
		JasperRunReportResponse responseObject = null;
		try {
			responseObject = new JasperRunReportResponse();
			
			Element uuidElement = root.getChild(ELEMENT_UUID);
			Element originalUriElement = root.getChild(ELEMENT_ORIGINAL_URI);

			Element totalPagesElement = root.getChild(ELEMENT_TOTAL_PAGES);
			Element startPageElement = root.getChild(ELEMENT_START_PAGE);
			Element endPageElement = root.getChild(ELEMENT_END_PAGE);
			
			if (null != uuidElement) {
				responseObject.setUuid(uuidElement.getValue());
			}
			if (null != originalUriElement) {
				responseObject.setOriginalUri(originalUriElement.getValue());
			}
			if (null != totalPagesElement) {
				responseObject.setTotalPages(totalPagesElement.getValue());
			}
			if (null != startPageElement) {
				responseObject.setStartPage(startPageElement.getValue());
			}
			if (null != endPageElement) {
				responseObject.setEndPage(endPageElement.getValue());
			}
			List<Element> filesElement = root.getChildren(ELEMENT_FILE);
			if (null != filesElement && filesElement.size() > 0) {
				for (int i = 0; i < filesElement.size(); i++) {
					Element fileElement = filesElement.get(i);
					String type = fileElement.getAttributeValue(ATTR_FILE_TYPE);
					String fileName = fileElement.getValue();
					JasperRunReportFileElement jasperRunReportFileElement = new JasperRunReportFileElement();
					jasperRunReportFileElement.setName(fileName);
					jasperRunReportFileElement.setType(type);
					responseObject.getFiles().add(jasperRunReportFileElement);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error parsing xml element", t);
			throw new ApsSystemException("Error parsing xml element", t);
		}
		return responseObject;
	}

	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Error parsing xml {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
}
