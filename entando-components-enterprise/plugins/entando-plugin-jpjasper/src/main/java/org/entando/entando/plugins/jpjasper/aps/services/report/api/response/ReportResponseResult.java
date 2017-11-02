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
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.plugins.jpjasper.aps.services.report.api.model.JAXBResourceDescriptor;

@XmlSeeAlso({ArrayList.class, HashMap.class})
public class ReportResponseResult extends AbstractApiResponseResult {

    @Override
    @XmlElement(name = "report", required = false)
    public JAXBResourceDescriptor getResult() {
        return (JAXBResourceDescriptor) this.getMainResult();
    }
}
