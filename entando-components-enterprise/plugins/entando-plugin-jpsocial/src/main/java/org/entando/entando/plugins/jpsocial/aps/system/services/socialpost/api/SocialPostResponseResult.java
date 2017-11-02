package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class SocialPostResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "socialPost", required = false)
    public JAXBSocialPost getResult() {
        return (JAXBSocialPost) this.getMainResult();
    }
    
}