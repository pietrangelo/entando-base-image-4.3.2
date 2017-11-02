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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpforum.aps.system.services.message.Message;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.Sanction;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;

public class ForumUserAction extends AbstractForumAction implements IForumUserAction  {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumUserAction.class);

	@Override
	public String viewUser() {
		try {
			if (null == this.getUsername() || this.getUsername().trim().length() == 0) {
				this.setUsername(this.getCurrentUser().getUsername());
			}
//			UserDetails currentUser = this.getUserManager().getUser(this.getUsername());
//			if (null == currentUser) {
//				this.addActionMessage(this.getText("jpforum.error.user.null"));
//				return INPUT;
//			}
		} catch (Throwable t) {
			_logger.error("error in viewUser", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String viewPrivateMessages() {
		try {
//			UserDetails currentUser = this.getUserManager().getUser(this.getCurrentUser().getUsername());
//			if (null == currentUser) {
//				this.addActionMessage(this.getText("jpforum.error.user.null"));
//				return INPUT;
//			}
		} catch (Throwable t) {
			_logger.error("error in viewPrivateMessages", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Restituisce la lista degli identificativi dei threads aperti dall'utente specificato
	 * @param username
	 * @return
	 */
	public List<Integer> getUserThreads(String username) {
		List<Integer> threads = new ArrayList<Integer>();
		try {
			threads = this.getThreadManager().getUserThreads(username);
		} catch (Throwable t) {
			_logger.error("error in getUserThreads", t);
		}
		return threads;
	}

	public List<Attach> getUserAttachs(String username) {
		List<Attach> attachs = new ArrayList<Attach>();
		try {
			attachs = this.getAttachManager().getAttachs(username);
		} catch (Throwable t) {
			_logger.error("error in getUserAttachs", t);
		}
		return attachs;
	}

	public long getUsedSpace(String username) {
		long usedSpace = 0;
		try {
			List<Attach> attachs = this.getAttachManager().getAttachs(username);
			if (null != attachs && attachs.size() > 0) {
				Iterator<Attach> it = attachs.iterator();
				while (it.hasNext()) {
					Attach attach = it.next();
					usedSpace = usedSpace + attach.getFileSize();
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getUsedSpace", t);
		}
		return usedSpace;
	}

	public long getUserAttachSpace(String username) {
		long totalSpace = 0;
		try {
			totalSpace = this.getAttachManager().getAttachFolderMaxSize();
		} catch (Throwable t) {
			_logger.error("error in getUserAttachSpace", t);
		}
		return totalSpace;
	}

	public Map<String, List<Integer>> getMessages() throws Throwable {
		Map<String, List<Integer>> messages = this.getMessageManager().getIncomingMessages(this.getCurrentUser().getUsername());
		return messages;
	}

	public Message getMessage(int code) throws Throwable {
		Message message = this.getMessageManager().getMessage(code);
		return message;
	}

	/**
	 * Restituisce la lista degli identificativi dei posts (thread comppresi) aperti dall'utente specificato
	 * @param username
	 * @return
	 */
	public List<Integer> getUserPosts(String username) {
		List<Integer> posts = new ArrayList<Integer>();
		try {
			posts = this.getThreadManager().getUserPosts(username);
		} catch (Throwable t) {
			_logger.error("error in getUserPosts", t);
		}
		return posts;
	}

	public List<Integer> getSanctions() {
		List<Integer> sanctions = new ArrayList<Integer>();
		try {
			sanctions = this.getSanctionManager().getSanctions(this.getUsername());
		} catch (Throwable t) {
			_logger.error("error in getSanctions", t);
		}
		return sanctions;
	}

	public Sanction getSanction(int id) {
		Sanction sanction = null;
		try {
			sanction = this.getSanctionManager().getSanction(id);
		} catch (Throwable t) {
			_logger.error("error in getSanction", t);
		}
		return sanction;
	}

	public Section getCurrentSection() {
		Section section = null;
		try {
			section = this.getSection(this.getSectionCode());
		} catch (Throwable t) {
			_logger.error("error in getCurrentSection", t);
			throw new RuntimeException("errore in caricamento sezioneCorrente " );
		}
		return section;
	}

	public String[] getBreadCrumbs() {
		this.getRequest().setAttribute(JpforumSystemConstants.ATTR_SHOW_LAST_BREADCRUMB, true);
		return super.getBreadCrumbs(this.getCurrentSection().getCode());
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}

//	public void setUserManager(IUserManager userManager) {
//		this._userManager = userManager;
//	}
//	protected IUserManager getUserManager() {
//		return _userManager;
//	}

	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	public IMessageManager getMessageManager() {
		return _messageManager;
	}


	public String getSectionCode() {
		return _sectionCode;
	}
	public void setSectionCode(String sectionCode) {
		this._sectionCode = sectionCode;
	}


	private String _username;
	private IMessageManager _messageManager;
	private String _sectionCode;

}