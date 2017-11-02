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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;

@XmlRootElement(name = "response")
@XmlSeeAlso({ArrayList.class, HashMap.class})
public class ReportResponse extends AbstractApiResponse {


    @Override
    protected ReportResponseResult createResponseResultInstance() {
        return new ReportResponseResult();
    }
    
    @Override
    @XmlElement(name = "result", required = true)
    public ReportResponseResult getResult() {
        return (ReportResponseResult) super.getResult();
    }
}
