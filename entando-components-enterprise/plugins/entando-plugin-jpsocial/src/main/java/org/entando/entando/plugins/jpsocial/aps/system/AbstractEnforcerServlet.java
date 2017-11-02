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
package org.entando.entando.plugins.jpsocial.aps.system;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author entando
 */
public class AbstractEnforcerServlet extends HttpServlet {


	protected String getAndRemoveNoLoginAction(HttpServletRequest request){
		String currentAction = (String) request.getSession().getAttribute(JpSocialSystemConstants.NO_LOGIN);
		request.getSession().removeAttribute(JpSocialSystemConstants.NO_LOGIN);
		return currentAction;
	}
	
  /**
   * Return the current user in session
   * @param request
   * @return 
   */
  protected UserDetails getCurrentUser(HttpServletRequest request) {
      UserDetails currentUser =
            (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    return currentUser;
  }
  
  /**
   * Check whether the current user has the permission to share contents
   * @param request
   * @return 
   */
  protected boolean isSocialUser(HttpServletRequest request) {
    IAuthorizationManager authManager =
            (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, request);
    UserDetails  currentUser = 
            getCurrentUser(request);
    if (null != currentUser
            && authManager.isAuthOnPermission(currentUser, JpSocialSystemConstants.PERMISSION_POST)) {
      return true;
    }
    return false;
  }
  
  protected boolean isPortalUrl(String pathInfo) {
    
    return (null != pathInfo && !"".equals(pathInfo.trim()) 
            && (pathInfo.contains(".page") || pathInfo.contains(".wp")));
    
  }
  
}

