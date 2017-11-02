/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBNotify.class})
public class NotifyListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBNotify> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBNotify> notifys = new ArrayList<JAXBNotify>();
            notifys.addAll((Collection<JAXBNotify>) this.getMainResult());
            ListResponse<JAXBNotify> entity = new ListResponse<JAXBNotify>(notifys) {};
            return entity;
        }
        return null;
    }

}