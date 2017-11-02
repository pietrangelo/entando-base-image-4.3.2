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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.sanction;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.Sanction;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class SanctionAction extends AbstractForumAction implements ISanctionAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SanctionAction.class);

	@Override
	public String newSanction() {
		this.setModerator(this.getCurrentUser().getUsername());
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}

	@Override
	public String trashSanction() {
		try {
			Sanction sanction = this.getSanctionManager().getSanction(this.getId());
			if (null == sanction) {
				this.addActionError(this.getText("jpforum.error.sanction.null"));
				return INPUT;
			}
			this.setUsername(sanction.getUsername());
			this.setModerator(sanction.getModerator());
			this.setStartDate(sanction.getStartDate());
			this.setEndDate(sanction.getEndDate());
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trashSanction", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String deleteSanction() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getSanctionManager().deleteSanction(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in deleteSanction", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String saveSanction() {
		String checkDates = this.checkDates();
		if (null != checkDates) {
			return checkDates;
		}
		String checkUsername = this.checkUsername();
		if (null != checkUsername) {
			return checkUsername;
		}
		try {
			Sanction sanction = this.createSanction();
			this.getSanctionManager().addSanction(sanction);
		} catch (Throwable t) {
			_logger.error("error in saveSanction", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private Sanction createSanction() {
		Sanction sanction = new Sanction();
		sanction.setEndDate(this.getEndDate());
		sanction.setModerator(this.getModerator());
		sanction.setStartDate(this.getStartDate());
		sanction.setUsername(this.getUsername());
		return sanction;
	}

	private String checkDates() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		if (this.getStartDate().before(today)) {
			this.addFieldError("startDate", this.getText("jpforum.error.date.in.the.past"));
			return INPUT;
		}
		if (this.getEndDate().before(this.getStartDate())) {
			this.addFieldError("endDate", this.getText("jpforum.error.invalid.dateRange"));
			return INPUT;
		}
		return null;
	}

	private String checkUsername() {
		//username deve avere il ruolo forum user.
		String retval = null;
		try {
			if (null == this.getUsername() || this.getUsername().trim().length() == 0) {
				this.addFieldError("username", this.getText("jpforum.error.nullUserParameter"));
				return INPUT;
			}
			if (this.getUsername().equals(this.getCurrentUser().getUsername())) {
				this.addFieldError("username", this.getText("jpforum.error.currentUser"));
				return INPUT;
			}
//			UserDetails theUser = this.getUserManager().getUser(this.getUsername());
//			UserDetails user = this.getAuthenticationProviderManager().getUser(theUser.getUsername(), theUser.getPassword());
//			if (null == user) {
//				this.addFieldError("username", this.getText("jpforum.error.user.null"));
//				return INPUT;
//			}
//			boolean isAuthOnPermission = this.getAuthorizationManager().isAuthOnPermission(user, JpforumSystemConstants.PERMISSION_FORUM_USER);
//			if (!isAuthOnPermission) {
//				this.addFieldError("username", this.getText("jpforum.error.user.notForumUser"));
//				return INPUT;
//			}
		} catch (Throwable t) {
			_logger.error("error in checkRecipient", t);
			throw new RuntimeException("errore in controllo utente");
		}
		return retval;
	}

	@Override
	public String[] getBreadCrumbs() {
		return null;
	}

	@Override
	public Section getCurrentSection() {
		return null;
	}

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public Date getStartDate() {
		return _startDate;
	}
	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}

	public Date getEndDate() {
		return _endDate;
	}
	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}

	public String getModerator() {
		return _moderator;
	}
	public void setModerator(String moderator) {
		this._moderator = moderator;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

//	public void setUserManager(IUserManager userManager) {
//		this._userManager = userManager;
//	}
//	protected IUserManager getUserManager() {
//		return _userManager;
//	}

	public void setAuthenticationProviderManager(IAuthenticationProviderManager authenticationProviderManager) {
		this._authenticationProviderManager = authenticationProviderManager;
	}
	protected IAuthenticationProviderManager getAuthenticationProviderManager() {
		return _authenticationProviderManager;
	}

	private int _strutsAction;
	private int _id;
	private String _username;
	private Date _startDate;
	private Date _endDate;
	private String _moderator;
	//private IUserManager _userManager;
	private IAuthenticationProviderManager _authenticationProviderManager;
}
