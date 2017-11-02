package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


@XmlRootElement(name = "response")
public class SocialPostResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = true)
    public SocialPostResponseResult getResult() {
        return (SocialPostResponseResult) super.getResult();
    }
    
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new SocialPostResponseResult();
    }
    
}