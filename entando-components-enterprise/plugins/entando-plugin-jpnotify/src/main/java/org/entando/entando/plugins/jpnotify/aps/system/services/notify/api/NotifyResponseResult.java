/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class NotifyResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "notify", required = false)
    public JAXBNotify getResult() {
        return (JAXBNotify) this.getMainResult();
    }
    
}