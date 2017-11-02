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
package com.sugarcrm.www.sugarcrm;

public interface Sugarsoap extends javax.xml.rpc.Service {
    public java.lang.String getsugarsoapPortAddress();

    public com.sugarcrm.www.sugarcrm.SugarsoapPortType getsugarsoapPort() throws javax.xml.rpc.ServiceException;

    public com.sugarcrm.www.sugarcrm.SugarsoapPortType getsugarsoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
