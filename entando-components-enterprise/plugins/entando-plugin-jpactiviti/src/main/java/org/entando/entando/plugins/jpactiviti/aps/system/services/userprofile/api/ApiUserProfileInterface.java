package org.entando.entando.plugins.jpactiviti.aps.system.services.userprofile.api;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
//import org.entando.entando.aps.system.services.userprofile.api.model.JAXBUserProfile;

import org.entando.entando.aps.system.services.userprofile.api.model.JAXBUserDetails;
import org.entando.entando.aps.system.services.userprofile.api.model.JAXBUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
//import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class ApiUserProfileInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiUserProfileInterface.class);
	
       
    
    public JAXBUserDetails getUserDetails(Properties properties) throws ApiException, Throwable {
    	JAXBUserDetails jaxbUserDetails = null;
        try {
        	String username = properties.getProperty("username");        	
            UserDetails userDetail = this.getUserManager().getUser(username);
            
            if (null == userDetail) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"User '" + username + "' does not exist", Response.Status.CONFLICT);
            }
            
           // userDetail.addAutorities(this.getGroupManager().getAuthorizationsByUser(username));
           userDetail.addAuthorizations(this.getAuthorizationManager().getUserAuthorizations(username));
           jaxbUserDetails = new JAXBUserDetails(userDetail); 
           return jaxbUserDetails;
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting user", t);
            //ApsSystemUtils.logThrowable(t, this, "getMyUserProfile");
            throw new ApsSystemException("Error extracting user", t);
        }
    }
    
    
    
    
    public JAXBUserDetails getUserDetailsByUsernameAndPassword(Properties properties) throws ApiException, Throwable {
    	JAXBUserDetails jaxbUserDetails = null;
        try {
        	String username = properties.getProperty("username");    
        	String password = properties.getProperty("password"); 
            UserDetails userDetail = this.getUserManager().getUser(username, password);
            
            if (null == userDetail) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"User '" + username + "' does not exist", Response.Status.CONFLICT);
            }
            
                        
            jaxbUserDetails = new JAXBUserDetails(userDetail);           
            return jaxbUserDetails;
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting user", t);
            //ApsSystemUtils.logThrowable(t, this, "getMyUserProfile");
            throw new ApsSystemException("Error extracting user", t);
        }
    }
    
    
    
    protected IUserProfileManager getUserProfileManager() {
        return _userProfileManager;
    }
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this._userProfileManager = userProfileManager;
    }
    
    protected GroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(GroupManager groupManager) {
        this._groupManager = groupManager;
    }
    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }
    
    
    
    
    
    public IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}




	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}





	private IUserProfileManager _userProfileManager;
    private GroupManager _groupManager;
    private IUserManager _userManager;
    private IAuthorizationManager _authorizationManager;
    
    
}