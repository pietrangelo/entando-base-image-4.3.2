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
package com.agiletec.plugins.jpforum.aps.system.services.config;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;

public class ForumConfigDOM {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ForumConfigDOM.class);

	
	/*
<jpforum>
	<postsPerPage>3</postsPerPage>
	<attachsPerPost>2</attachsPerPost>
	<profileTypecode>PFL</profileTypecode>
	<profileNickAttributeName>Nickname</profileNickAttributeName>
</jpforum>
	 */

	public ForumConfig extractConfig(String xml) throws ApsSystemException {
		ForumConfig config = new ForumConfig();
		Element root = this.getRootElement(xml);
		
		String postsPerPageValue = this.extractValue(root, JpforumSystemConstants.POSTS_PER_PAGE_CONFIG_ITEM);
		int postsPerPage = 3;
		if (null != postsPerPageValue) {
			postsPerPage = new Integer(postsPerPageValue).intValue();
		}
		config.setPostsPerPage(postsPerPage);

		String attachsPerPostValue = this.extractValue(root, JpforumSystemConstants.ATTACHS_PER_POST_CONFIG_ITEM);
		int attachsPerPost = 3;
		if (null != attachsPerPostValue) {
			attachsPerPost = new Integer(attachsPerPostValue).intValue();
		}
		config.setAttachsPerPost(attachsPerPost);

		String profileTypecodeValue = this.extractValue(root, JpforumSystemConstants.PROFILE_TYPECODE_CONFIG_ITEM);
		if (null == profileTypecodeValue || profileTypecodeValue.trim().length() == 0) {
			profileTypecodeValue = "PFL";
		}
		config.setProfileTypecode(profileTypecodeValue);
		
		String profileNickAttributeName = this.extractValue(root, JpforumSystemConstants.PROFILE_NICK_ATTRIBUTENAME_CONFIG_ITEM);
		if (null == profileNickAttributeName || profileNickAttributeName.trim().length() == 0) {
			profileNickAttributeName = "Nickname";
		}
		config.setProfileNickAttributeName(profileNickAttributeName);
		return config;
	}
	
	private String extractValue(Element root, String configItem) {
		String value = null;
		Element el = root.getChild(configItem);
		if (null != el) {
			String extracted = el.getValue();
			if (null != extracted && extracted.trim().length() > 0) {
				value = extracted;
			}
		}
		return value;
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
			_logger.error("Error parsing xml: {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

	public String createConfigXml(ForumConfig config) {
		Element root = new Element("jpforum");
		
		Element postsPerPageEl = new Element(JpforumSystemConstants.POSTS_PER_PAGE_CONFIG_ITEM);
		postsPerPageEl.setText(new Integer(config.getPostsPerPage()).toString());

		Element attachsPerPostEl = new Element(JpforumSystemConstants.ATTACHS_PER_POST_CONFIG_ITEM);
		attachsPerPostEl.setText(new Integer(config.getAttachsPerPost()).toString());

		Element profileTypeCodeEl = new Element(JpforumSystemConstants.PROFILE_TYPECODE_CONFIG_ITEM);
		profileTypeCodeEl.setText(config.getProfileTypecode());

		Element profileNickAttrNameEl = new Element(JpforumSystemConstants.PROFILE_NICK_ATTRIBUTENAME_CONFIG_ITEM);
		profileNickAttrNameEl.setText(config.getProfileNickAttributeName());
		root.addContent(postsPerPageEl);
		root.addContent(attachsPerPostEl);
		root.addContent(profileTypeCodeEl);
		root.addContent(profileNickAttrNameEl);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}
}
