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

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.FileItem;


/*
 * 
<solution>  
	<file type=\"dir\" name=\"steel-wheels\"> 
	    <file type=\"pdf\" name=\"pippo.pdf\" /> 
	    <file type=\"dir\" name=\"reports\"> 
	      <file type=\"doc\" name=\"pluto.doc\" /> 
	    </file> 
	</file> 
</solution>
 * */
public class SolutionDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(SolutionDOM.class);

	public SolutionDOM() {
		this._doc = new Document();
		Element elementRoot = new Element("solution");
		this._doc.setRootElement(elementRoot);
	}
	
	public SolutionDOM(String xml) throws JDOMException, IOException {
		this.decodeDOM(xml);
	}

	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		out.setFormat(format);
		String xml = out.outputString(_doc);
		return xml;
	}
	
	public void addFile(Element parent, FileItem fileItem) {
		Element element = new Element("file");
		if (fileItem.isDirectory()) {
			Attribute attribute = new Attribute("type", "dir");
			element.setAttribute(attribute);
			attribute = new Attribute("name", fileItem.getName());
			element.setAttribute(attribute);
			if (fileItem.getChilds().size() > 0 ) {
				for (FileItem current : fileItem.getChilds()) {
					this.addFile(element, current);
				}
			}
			if (parent == null) {
				this._doc.getRootElement().addContent(element);
			} else {
				parent.addContent(element);
			}
		} else if (!fileItem.isDirectory()) {
			Attribute attribute = new Attribute("type", fileItem.getType());
			element.setAttribute(attribute);
			attribute = new Attribute("name", fileItem.getName());
			element.setAttribute(attribute);
			if (parent == null) {
				this._doc.getRootElement().addContent(element);
			} else {
				parent.addContent(element);
			}
		}		
	}
	
	public FileItem getFileItem() {
		List<Element> fileElements = this._doc.getRootElement().getChildren();
		FileItem item = new FileItem();
		item.setRoot(true);
		item.setName("solution");
		this.loadFileItem(fileElements, item);
		return item;
	}

	private void loadFileItem(List<Element> fileElements, FileItem item) {
		for (Element curr : fileElements) {
			FileItem it = new FileItem();
			String type = curr.getAttributeValue("type");
			String name = curr.getAttributeValue("name");
			it.setName(name);
			it.setType(type);
			if (type.equals("dir")) {
				it.setDirectory(true);
			}
			item.addChild(it);
			if (curr.getChildren() != null && curr.getChildren().size() > 0 ) {
				this.loadFileItem(curr.getChildren(), it);
			}
		}
	}
	
	private void decodeDOM(String xmlText) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		_logger.trace("Decode pentaho solution DOM: {}", xmlText);
		_doc = builder.build(reader);
	}
	
	private Document _doc;
}
