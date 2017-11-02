package org.entando.entando.plugins.jppentaho5.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoFilesystemManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.PentahoFilesystemManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.PentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class PentahoTransparentAuthTicketTag  extends TagSupport  {
    
    private static final Logger _logger = LoggerFactory.getLogger(PentahoTransparentAuthTicketTag.class);
    
    
    public String appendPath(String address, String path) {
        if (null != path) {
            if (address.endsWith("/")
                    ^ path.startsWith("/")) {
                address = address.concat(path);
            } else if (address.endsWith("/")
                    && path.startsWith("/")) {
                address = address.concat(path.substring(1));
            } else {
                address = address.concat("/" + path);
            }
        }
        return address;
    }
    
    @Override
    public int doStartTag() throws JspException {
        IPentahoManager pentahoManager = (IPentahoManager) ApsWebApplicationUtils.getBean(PentahoManager.MANAGER_ID, this.pageContext);
        
        IPentahoFilesystemManager pentahoFilesManager = (IPentahoFilesystemManager) ApsWebApplicationUtils.getBean(PentahoFilesystemManager.MANAGER_ID, this.pageContext);
        
        
        try {
            PentahoConfiguration conf = pentahoManager.getConfiguration();
            //String url=conf.getInstance().concat("/Home?autologin=true");
            String url = appendPath(conf.getInstance(), "/Home?autologin=true");
            String logoutUrl = appendPath(conf.getInstance(), "/Logout");
            
            Boolean tap =conf.isTapPlugin();
            
            if (tap){
                String ticket=pentahoFilesManager.getAuthTicket();
                this.pageContext.setAttribute("pthturl", url);
                this.pageContext.setAttribute("pthtticket", ticket);
                this.pageContext.setAttribute("pthttapplugin", "1");   
            }
            else
            {
                this.pageContext.setAttribute("pthturl", "");
                this.pageContext.setAttribute("pthtticket", "");
                this.pageContext.setAttribute("pthttapplugin", "0");
            }
            this.pageContext.setAttribute("pthlogout", logoutUrl);
            
        } catch (ApsSystemException e) {
            _logger.error("error in Pentaho authentication", e);
        }
        
        return EVAL_BODY_INCLUDE;
    }
    
}