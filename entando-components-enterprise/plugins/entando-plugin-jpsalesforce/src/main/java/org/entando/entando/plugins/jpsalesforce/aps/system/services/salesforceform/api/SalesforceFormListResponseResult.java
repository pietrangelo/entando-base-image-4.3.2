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
*/package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBSalesforceForm.class})
public class SalesforceFormListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBSalesforceForm> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBSalesforceForm> salesforceForms = new ArrayList<JAXBSalesforceForm>();
            salesforceForms.addAll((Collection<JAXBSalesforceForm>) this.getMainResult());
            ListResponse<JAXBSalesforceForm> entity = new ListResponse<JAXBSalesforceForm>(salesforceForms) {};
            return entity;
        }
        return null;
    }

}