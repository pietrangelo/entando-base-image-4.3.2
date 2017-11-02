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
package org.entando.entando.plugins.jpconstantcontact.aps.system.services.parse;


import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.config.ConstantContactConfig;
import org.jdom.output.Format;

/**
 *<constantContact>
 *	<apikey></apikey>
 *<consumerSecret></consumerSecret>
 * <token></token>
 * <contactPage></contactPage>
 * <contactFrame></contactFrame>
 * <campaignPage></campaignPage>
 * <campaignFrame></campaignFrame>
 * <eventPage></eventPage>
 * <eventFrame></eventFrame>
 *</constantContact>
 *
 */

/**
 * Provides read and update operations for the jpmailgun plugin xml configuration.
 *
 * @author Alberto Piras
 */
public class ConstantContactConfigDOM {
    /**
     * Extracts the jpconstantcontact configuration from an xml.
     * @param xml The xml containing the configuration.
     * @return The onstantcontact configuration.
     * @throws ApsSystemException In case of parsing errors.
     */
    public ConstantContactConfig extractConfig(String xml) throws ApsSystemException {
        ConstantContactConfig config = new ConstantContactConfig();
        Element root = this.getRootElement(xml);
        extractXML(root, config);
        return config;
    }
    
    /**
     * Extracts the apikey and clientSecret from the xml element and save it into the ConstantContactConfig object.
     * @param root The xml root element containing the apikey and clientSecret configuration.
     * @param config The configuration.
     */
    private void extractXML(Element root, ConstantContactConfig config) {
        Element elementAPI = root.getChild(ELEMENT_APIKEY);
        if (null != elementAPI) {
            String APIContent = elementAPI.getText();
            config.setApiKey(APIContent);
        }
        Element elementDomain = root.getChild(ELEMENT_SECRET);
        if (null != elementDomain) {
            String secretContent = elementDomain.getText();
            config.setSecret(secretContent);
        }
        Element elementToken= root.getChild(ELEMENT_TOKEN);
        if (null != elementToken) {
            String tokenContent = elementToken.getText();
            config.setToken(tokenContent);
        }
        //contact widget elements
         Element elementContactPage= root.getChild(ELEMENT_CONTACTPAGE);
        if (null != elementContactPage) {
            String elementContactPageContent = elementContactPage.getText();
            config.setContactPage(elementContactPageContent);
        }
          Element elementContactFrame= root.getChild(ELEMENT_CONTACTFRAME);
        if (null != elementContactFrame) {
            String elementContactFrameContent = elementContactFrame.getText();
            config.setContactFrame(elementContactFrameContent);
        }
         //campaign widget elements
          Element elementCampaignFrame= root.getChild(ELEMENT_CAMPAIGNPAGE);
        if (null != elementCampaignFrame) {
            String elementCampaignFrameContent = elementCampaignFrame.getText();
            config.setCampaignPage(elementCampaignFrameContent);
        }
        Element elementCampaignPage= root.getChild(ELEMENT_CAMPAIGNFRAME);
        if (null != elementCampaignPage) {
            String elementCampaignPageContent = elementCampaignPage.getText();
            config.setCampaignFrame(elementCampaignPageContent);
        }
         //event widget elements
          Element elementEventFrame= root.getChild(ELEMENT_EVENTFRAME);
        if (null != elementEventFrame) {
            String elementEventFrameContent = elementEventFrame.getText();
            config.setEventFrame(elementEventFrameContent);
        }
        Element elementEventPage= root.getChild(ELEMENT_EVENTPAGE);
        if (null != elementEventPage) {
            String elementEventPageContent = elementEventPage.getText();
            config.setEventPage(elementEventPageContent);
        }
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
        Document	_doc =new Document();
        try {
            _doc = builder.build(reader);
        } catch (Throwable t) {
            System.out.println("Error while parsing xml : {}"+ xmlText+t);
            throw new ApsSystemException("Error detected while parsing the XML", t);
        }
        return _doc.getRootElement();
    }
    
    /**
     * Creates an xml containing the jpconstantcontact configuration.
     * @param config The jpconstantcontact configuration.
     * @return The xml containing the configuration.
     * @throws ApsSystemException In case of errors.
     */
    public String createConfigXml(ConstantContactConfig config) throws ApsSystemException {
        Element root = this.createConfigElement(config);
        Document doc = new Document(root);
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setIndent("\t");
        out.setFormat(format);
        return out.outputString(doc);
    }
    
    /**
     * Extracts the configuration from the xml element and save it into the ConstantContactConfig object.
     * @param root The xml root element containing the configuration.
     * @param config The configuration.
     */
    private Element createConfigElement(ConstantContactConfig config) {
        Element configElem = new Element(ROOT);
        Element apiElem = this.createAPIKeyElement(config);
        configElem.addContent(apiElem);
        Element secretElem = this.createSecretElement(config);
        configElem.addContent(secretElem);
        Element tokenElem = this.createTokenElement(config);
        configElem.addContent(tokenElem);
        Element contactPageElement = this.createContactPageElement(config);
        configElem.addContent(contactPageElement);
        Element contactFrameElement = this.createContactFrameElement(config);
        configElem.addContent(contactFrameElement);
        Element campaignPageElement = this.createCampaginPageElement(config);
        configElem.addContent(campaignPageElement);
        Element campaignFrameElement = this.createCampaignFrameElement(config);
        configElem.addContent(campaignFrameElement);
        Element eventPageElement = this.createEventPageElement(config);
        configElem.addContent(eventPageElement);
        Element eventFrameElement = this.createEventFrameElement(config);
        configElem.addContent(eventFrameElement);
        return configElem;
    }
    
    /**
     * Create the api element starting from the given ConstantContactConfig.
     * @param config The configuration.
     */
    private Element createAPIKeyElement(ConstantContactConfig config) {
        Element apiElem = new Element(ELEMENT_APIKEY);
        apiElem.setText(String.valueOf(config.getApiKey()));
        return apiElem;
    }
    
    /**
     * Create the secret element starting from the given ConstantContactConfig.
     * @param config The configuration.
     */
    private Element createSecretElement(ConstantContactConfig config) {
        Element secretElem = new Element(ELEMENT_SECRET);
        secretElem.setText(String.valueOf(config.getSecret()));
        return secretElem;
    }
    
    /**
     * Create the token element starting from the given ConstantContactConfig.
     * @param config The configuration.
     */
    private Element createTokenElement(ConstantContactConfig config) {
        Element tokenElem = new Element(ELEMENT_TOKEN);
        tokenElem.setText(String.valueOf(config.getToken()));
        return tokenElem;
    }
    
    /**
    * Create the Contact page element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createContactPageElement(ConstantContactConfig config) {
        Element element_contactpage = new Element(ELEMENT_CONTACTPAGE);
        element_contactpage.setText(String.valueOf(config.getContactPage()));
        return element_contactpage;
    }
    /**
    * Create the Contact frame element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createContactFrameElement(ConstantContactConfig config) {
        Element element_contactframe = new Element(ELEMENT_CONTACTFRAME);
        element_contactframe.setText(String.valueOf(config.getContactFrame()));
        return element_contactframe;
    }
    
    /**
    * Create the Campaign page element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createCampaginPageElement(ConstantContactConfig config) {
        Element element_campaignpage = new Element(ELEMENT_CAMPAIGNPAGE);
        element_campaignpage.setText(String.valueOf(config.getCampaignPage()));
        return element_campaignpage;
    }
    /**
    * Create the Campaign frame element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createCampaignFrameElement(ConstantContactConfig config) {
        Element element_campaignframe = new Element(ELEMENT_CAMPAIGNFRAME);
        element_campaignframe.setText(String.valueOf(config.getCampaignFrame()));
        return element_campaignframe;
    }
    /**
    * Create the Event page element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createEventPageElement(ConstantContactConfig config) {
        Element element_eventpage = new Element(ELEMENT_EVENTPAGE);
        element_eventpage.setText(String.valueOf(config.getEventPage()));
        return element_eventpage;
    }
    /**
    * Create the Event frame element starting from the given ConstantContactConfig.
    * @param config The configuration.
    */
    private Element createEventFrameElement(ConstantContactConfig config) {
        Element element_eventframe = new Element(ELEMENT_EVENTFRAME);
        element_eventframe.setText(String.valueOf(config.getEventFrame()));
        return element_eventframe;
    }
    
    private final String ROOT = "constantContact";
    private static final String ELEMENT_APIKEY = "apikey";
    private static final String ELEMENT_SECRET = "consumerSecret";
    private static final String ELEMENT_TOKEN = "token";
    private static final String ELEMENT_CONTACTPAGE = "contactPage";
    private static final String ELEMENT_CONTACTFRAME = "contactFrame";
    private static final String ELEMENT_CAMPAIGNPAGE = "campaignPage";
    private static final String ELEMENT_CAMPAIGNFRAME = "campaignFrame";
    private static final String ELEMENT_EVENTPAGE = "eventPage";
    private static final String ELEMENT_EVENTFRAME = "eventFrame";
    
}
