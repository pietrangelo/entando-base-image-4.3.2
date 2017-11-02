/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.user;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteUserFinderAction  extends AbstractApsEntityFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteUserFinderAction.class);
	
	public String list() {
		this.setUsername(null);
		return this.execute();
	}
    
	@Override
    public String execute() {
        try {
            EntitySearchFilter filter = null;
			String searchName = (StringUtils.isNotBlank(this.getUsername()))? this.getUsername().trim() + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()):  MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()); 
			_logger.warn("********** {} ********", searchName);
            filter = new EntitySearchFilter(IEntityManager.ENTITY_ID_FILTER_KEY, false, searchName, true);
            filter.setOrder(EntitySearchFilter.ASC_ORDER);
            this.addFilter(filter);
        } catch (Throwable t) {
        	_logger.error("error in execute", t);
            return FAILURE;
        }
        return super.execute();
    }
    
	@Override
    public List<String> getSearchResult() {
        List<String> searchResult = null;
        try {
            Integer withProfile = this.getWithProfile();
            List<String> profileSearchResult = super.getSearchResult();
            if (super.isAddedAttributeFilter() || 
					(null != withProfile && withProfile.intValue() == WITH_PROFILE_CODE) || 
                    (null != super.getEntityTypeCode() && super.getEntityTypeCode().trim().length() > 0)) {
				this.setWithProfile(WITH_PROFILE_CODE);
                return profileSearchResult;
            }
			String searchName = (null != this.getUsername())? this.getUsername() + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()):  MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()); 
			_logger.warn("********** {} ********", searchName);
			List<String> usernames = this.getUserManager().searchUsernames(searchName);
            searchResult = new ArrayList<String>();
            for (int i = 0; i < usernames.size(); i++) {
                String username = usernames.get(i);
                if (null == withProfile || 
                        (withProfile.intValue() == WITHOUT_PROFILE_CODE && !profileSearchResult.contains(username))) {
                    searchResult.add(username);
                }
            }
            if (null == withProfile || withProfile.intValue() == WITH_PROFILE_CODE) {
                profileSearchResult.removeAll(usernames);
                if (!profileSearchResult.isEmpty()) {
                    searchResult.addAll(profileSearchResult);
                    Collections.sort(searchResult);
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error while searching entity Ids", t);
            throw new RuntimeException("Error while searching entity Ids", t);
        }
        return searchResult;
    }
    
    public UserDetails getUser(String username) {
        UserDetails user = null;
        try {
            user = this.getUserManager().getUser(username);
        } catch (Throwable t) {
        	_logger.error("Error extracting user {}", username, t);
            throw new RuntimeException("Error extracting user " + username, t);
        }
        return user;
    }
    
    public String getEmailAttributeValue(String username) {
        IUserProfile userProfile = (IUserProfile) this.getEntity(username);
		return (String) userProfile.getValue(userProfile.getMailAttributeName());
    }
    
    public Lang getDefaultLang() {
        return this.getLangManager().getDefaultLang();
    }
    
    public String viewProfile() {
        return SUCCESS;
    }
    
    @Override
    protected void deleteEntity(String entityId) throws Throwable {
        //Not supported
    }
    
    public IUserProfile getUserProfile(String username) {
        return (IUserProfile) this.getEntity(username);
    }
    
    @Override
    protected IEntityManager getEntityManager() {
        return (IEntityManager) this.getUserProfileManager();
    }
    
    public String getUsername() {
        return _username;
    }
    public void setUsername(String username) {
        this._username = username;
    }
	
    public Integer getWithProfile() {
            return _withProfile;
    }
    public void setWithProfile(Integer withProfile) {
            this._withProfile = withProfile;
    }
    
    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }
    
    protected IUserProfileManager getUserProfileManager() {
        return _userProfileManager;
    }
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this._userProfileManager = userProfileManager;
    }
    
    private String _username;
	private Integer _withProfile;
    
    private IUserManager _userManager;
    private IUserProfileManager _userProfileManager;
    
    private static final int WITHOUT_PROFILE_CODE = 0;
    private static final int WITH_PROFILE_CODE = 1;
    
}