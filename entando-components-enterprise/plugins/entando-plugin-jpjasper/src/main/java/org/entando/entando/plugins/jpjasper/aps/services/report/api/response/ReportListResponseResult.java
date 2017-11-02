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
package org.entando.entando.plugins.jpjasper.aps.services.report.api.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.plugins.jpjasper.aps.services.report.api.model.JAXBBaseResourceDescriptor;

/**
 * 
 *
 */
public class ReportListResponseResult extends AbstractApiResponseResult {

	
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBBaseResourceDescriptor> getResult() {
        if (null != this.getMainResult()) {
            List<JAXBBaseResourceDescriptor> strings = new ArrayList<JAXBBaseResourceDescriptor>();
            strings.addAll((Collection<JAXBBaseResourceDescriptor>) this.getMainResult());
            ListResponse<JAXBBaseResourceDescriptor> entity = new ListResponse<JAXBBaseResourceDescriptor>(strings) {};
            return entity;
        }
        return null;
    }
}