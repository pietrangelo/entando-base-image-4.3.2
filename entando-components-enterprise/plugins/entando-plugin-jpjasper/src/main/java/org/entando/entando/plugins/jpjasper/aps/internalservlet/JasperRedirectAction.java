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
package org.entando.entando.plugins.jpjasper.aps.internalservlet;

import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperReportModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class JasperRedirectAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperRedirectAction.class);
	
    public String executeRedirect() throws Exception {
        try {
            //String url = this.getJasperServerManager().jasperServerLogin(this.getCurrentUser());
            //this.setJasperRedirectUrl(url);
        } catch (Throwable t) {
        	_logger.error("Error redirection to JasperServer", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public JasperReportModel getUriString() {
        return _uriString;
    }
    public void setUriString(JasperReportModel uriString) {
        this._uriString = uriString;
    }

    public String getJasperRedirectUrl() {
        return _jasperRedirectUrl;
    }
    public void setJasperRedirectUrl(String jasperRedirectUrl) {
        this._jasperRedirectUrl = jasperRedirectUrl;
    }

    public IJasperServerManager getJasperServerManager() {
        return _jasperServerManager;
    }
    public void setJasperServerManager(IJasperServerManager jasperServerManager) {
        this._jasperServerManager = jasperServerManager;
    }
    
    private IJasperServerManager _jasperServerManager;
    private String _jasperRedirectUrl;
    private JasperReportModel _uriString;
}
