/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpforum.aps.internalservlet.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpforum.aps.internalservlet.helper.IForumApsActionHelper;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.ISanctionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.IStatisticManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.IAttachManager;

public abstract class AbstractForumAction extends BaseAction implements IApsForumAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractForumAction.class);

	@Override
	protected UserDetails getCurrentUser() {
		UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		if (null == currentUser) {
			currentUser = this.getUserManager().getGuestUser();
		}
		return currentUser;
	}

	/**
	 * Restituisce un post in base all'indetificativo
	 * @param code
	 * @return
	 */
	public Post getPost(int code) {
		Post post= null;
		try {
			post = this.getThreadManager().getPost(code);
		} catch (Throwable t) {
			_logger.error("error in getPost", t);
			throw new RuntimeException("errore in caricamento post con codice [" + code + "] nella sezione");
		}
		return post;
	}

	public List<Attach> getAttachs(int code) {
		List<Attach> attachs = new ArrayList<Attach>();
		try {
			attachs = this.getAttachManager().getAttachs(code);
		} catch (Throwable t) {
			_logger.error("error in getAttachs", t);
			throw new RuntimeException("errore in caricamento allegati per post [" + code + "]");
		}
		return attachs;
	}

	/**
	 * Restituisce il numero di visite per il post identificato dal codice passato come parametro
	 * @param postCode
	 * @return
	 */
	public int getViews(int postCode) {
		int views = 0;
		try {
			views = this.getStatisticManager().getViews(postCode);
		} catch (Throwable t) {
			_logger.error("error in getViews", t);
			throw new RuntimeException("errore in caricamento views per il post con codice [" + postCode + "]" );
		}
		return views;
	}

	public String getHtml(String xml) {
		String html = null;
		try {
			html = this.getMarkupParser().XMLtoHTML(xml);
		} catch (Throwable t) {
			_logger.error("error in getHtml", t);
			throw new RuntimeException("Errore in conversione html", t);
		}
		return html;
	}

	public String getMarkup(String xml) {
		String markup = null;
		try {
			if (null == xml || xml.trim().length() == 0) return "";
			markup = this.getMarkupParser().XMLToMarkup(xml);
		} catch (Throwable t) {
			_logger.error("error in getMarkup", t);
			throw new RuntimeException("Errore in conversione Markup", t);
		}
		return markup;
	}

	/**
	 * restituisce un thread in funzione dell'identificativo
	 * @param code
	 * @return
	 */
	public Thread getThread(int code) {
		Thread thread = null;
		try {
			thread = this.getThreadManager().getThread(code);
		} catch (Throwable t) {
			_logger.error("error in getThread", t);
			throw new RuntimeException("errore in caricamento thread con codice [" + code + "]");
		}
		return thread;
	}

	/**
	 * restituisce una sezione in funzione dell'identificatovo
	 * @param code
	 * @return
	 */
	public Section getSection(String code) {
		Section section = null;
		try {
			Section requestedSection = null;
			if (null == code || code.trim().length() == 0) {
				requestedSection = (Section) this.getSectionManager().getRoot();
			} else {
				requestedSection = this.getSectionManager().getSection(code);
			}
			if (null != requestedSection) {
				section = this.getWrappedSection(requestedSection);
			} else {
				_logger.debug("LA SEZIONE {} NON ESISTE", code);
			}
		} catch (Throwable t) {
			_logger.error("error in getSection", t);
		}
		return section;
	}

	/**
	 * Restituisce un array di interi di lunghezza 2 <br>
	 * Nella posizione 0 setta il conteggio dei threads di questa sezione e delle sezioni figlie<br>
	 * Nella posizione1 setta il conteggio dei posts
	 * @param sectionCode
	 * @return
	 * @throws Throwable
	 */
	public Integer[] getSectionStatistics(String sectionCode) throws Throwable {
		Integer[] statistics = new Integer[]{0,0};
		try {
			Section theSection = this.getSection(sectionCode);
			this.addStat(statistics, theSection);
		} catch (Throwable t) {
			_logger.error("errore in recupero statistiche per la sezione {}", sectionCode, t);
			throw new RuntimeException("errore in recupero statistiche per la sezione " + sectionCode);
		}
		return statistics;
	}

	private void addStat(Integer[] statistics, Section section) throws Throwable {
		Map<Integer, List<Integer>> threads = new HashMap<Integer, List<Integer>>();
		threads = this.getThreadManager().getThreads(section.getCode());
		statistics[0] = statistics[0] + threads.keySet().size();
		if (null != threads.values()) {
			Iterator<List<Integer>> it = threads.values().iterator();
			while (it.hasNext()) {
				List<Integer> posts = it.next();
				statistics[1] = statistics[1] + posts.size();
			}
		}
		ITreeNode[] children = section.getChildren();
		for (ITreeNode child : children) {
			this.addStat(statistics, (Section) child);
		}
	}

	protected Section getWrappedSection(Section requestedSection) {
		Section wrappedSection = null;
		boolean userAlloewd = this.getHelper().isUserAllowed(requestedSection, this.getCurrentUser());
		if (userAlloewd || requestedSection.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_READONLY) {
			wrappedSection = requestedSection.clone();
			ITreeNode[] children = wrappedSection.getChildren();
			if (null != children && children.length > 0) {
				ITreeNode[] filteredChildren = children;
				for (int i = 0; i < children.length; i++) {
					Section child = (Section) children[i];
					boolean userAlloewdOnChild = this.getHelper().isUserAllowed(child, this.getCurrentUser());
					if (!userAlloewdOnChild && child.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_HIDDEN) {
						filteredChildren = (ITreeNode[]) ArrayUtils.removeElement(filteredChildren, child);
					}
				}
				wrappedSection.setChildren(filteredChildren);
			}
		} else {
			_logger.info("LA SEZIONE " + requestedSection.getCode() + " NON PUO ESSERE VISUALIZZATA DALL'UTENTE " + this.getCurrentUser().getUsername());
		}
		return wrappedSection;
	}

	/**
	 * restituisce un array contenente i codici delle sezione dalla root a quella richiesta
	 * @param sectionCode
	 * @return
	 */
	public String[] getBreadCrumbs(String sectionCode) {
		String separator = "/";
		Section currentSection = this.getSectionManager().getSection(sectionCode);
		String path = currentSection.getFullPath(separator);
		if (!currentSection.isRoot()) {
			path = this.getSectionManager().getRoot().getCode().concat(separator).concat(path);
		}
		return path.split(separator);
	}

	/**
	 * Restituisce un valore booleano che indica se l'utente corrrente è abilitato alla crezione di un thread o un post
	 * @return
	 */
	public boolean isAllowedOnCreateThread() {
		boolean allowed = false;
		try {
			Section currentSection = this.getCurrentSection();
			boolean isUnderSanction = this.getSanctionManager().isUnderSanction(this.getCurrentUser().getUsername());
			boolean isAllowedOnGroups = this.getHelper().isUserAllowed(currentSection, this.getCurrentUser());
			boolean isAuthOnPermission = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), JpforumSystemConstants.PERMISSION_FORUM_USER);
			allowed = !isUnderSanction && currentSection.isOpen() && !currentSection.isRoot() && isAllowedOnGroups && isAuthOnPermission;

		} catch (Throwable t) {
			_logger.error("error in isAllowedOnCreateThread", t);
		}
		return allowed;
	}

	/**
	 * Restituisce un valore booleano che indica se l'utente corrrente è abilitato all'inserimento di un post (fratello del post identificato dal parametro postId)
	 * @param postId
	 * @return
	 */
	public boolean isAllowedOnEditPost(int postId)  {
		Section currentSection = this.getCurrentSection();
		try {
			boolean isUnderSanction = this.getSanctionManager().isUnderSanction(this.getCurrentUser().getUsername());
			if (isUnderSanction) return false;
			Post currentPost = this.getThreadManager().getPost(postId);
			if (null == currentSection) {
				int threadId = currentPost.getThreadId();
				Thread thread = this.getThreadManager().getThread(threadId);
				currentSection = this.getSectionManager().getSection(thread.getSectionId());
			}
			boolean isSectionAvailable = currentSection.isOpen() && !currentSection.isRoot();
			//ADMIN
			boolean isSuperuser = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), Permission.SUPERUSER);
			if (isSuperuser) return isSuperuser && isSectionAvailable;
			//FORUM_SUPERUSER
			boolean isForumSuperuser = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), JpforumSystemConstants.PERMISSION_SUPERUSER);
			if (isForumSuperuser) return isForumSuperuser && isSectionAvailable;
			//IL MODERATORE DELLA SEZIONE
			boolean isSectionModerator = this.getSectionManager().getModerators(currentSection).contains(this.getCurrentUser().getUsername());
			if (isSectionModerator) return isSectionModerator && isSectionAvailable;
			//AUTORE DEL POST
			boolean isAllowedOnGroups = this.getHelper().isUserAllowed(currentSection, this.getCurrentUser());
			boolean isAuthOnPermission = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), JpforumSystemConstants.PERMISSION_FORUM_USER);
			//Post currentPost = this.getThreadManager().getPost(postId);
			if (isAllowedOnGroups && isAuthOnPermission && currentPost.getUsername().equals(this.getCurrentUser().getUsername())) return true && isSectionAvailable;
		} catch (Throwable t) {
			_logger.error("error in isAllowedOnEditPost", t);
		}
		return false;
	}

	public boolean isAllowedOnGroups(Section section, UserDetails user) throws Throwable {
		return this.getHelper().isUserAllowed(section, user);
	}

	/**
	 * Restituisce un valore booleano che indica se l'utente corrrente è abilitato alla moderazione della sezione corrente
	 * @return
	 */
	public boolean isAllowedOnModerateThread()  {
		Section currentSection = this.getCurrentSection();
		try {
			boolean isUnderSanction = this.getSanctionManager().isUnderSanction(this.getCurrentUser().getUsername());
			if (isUnderSanction) return false;
			//ADMIN
			boolean isSuperuser = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), Permission.SUPERUSER);
			if (isSuperuser) return true;
			//FORUM_SUPERUSER
			boolean isForumSuperuser = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), JpforumSystemConstants.PERMISSION_SUPERUSER);
			if (isForumSuperuser) return true;
			//IL MODERATORE DELLA SEZIONE
			boolean isSectionModerator = this.getSectionManager().getModerators(currentSection).contains(this.getCurrentUser().getUsername());
			if (isSectionModerator) return true;
		} catch (Throwable t) {
			_logger.error("error in isAllowedOnModerateThread", t);
		}
		return false;
	}

	public String getUserNick(String username) {
		String usernick = null;
		try {
			UserDetails user = null;
			if (null == username) {
				user = this.getCurrentUser();
			} else {
				user = this.getUserManager().getUser(username);
			}
			if (null == user) {
				return username;
			}
			IUserProfile userprofile = (IUserProfile) user.getProfile();
			if (null == userprofile) {
				return username;
			}
			usernick = (String) userprofile.getValue(this.getForumConfigManager().getConfig().getProfileNickAttributeName());
			if (null == usernick) {
				return username;
			}
		} catch (Throwable t) {
			_logger.error("error in getUserNick", t);
		}
		return usernick;
	}

	public UserDetails getUser(String username) {
		UserDetails user = null;
		try {
			if (null == username) {
				user = this.getCurrentUser();
			} else {
				user = this.getUserManager().getUser(username);
			}
		} catch (Throwable t) {
			_logger.error("error in getUser", t);
		}
		return user;
	}

	@Deprecated
	public Map<String, Integer> getNewMessages() throws Throwable {
		Map<String, Integer> messages = this.getMessageManager().getNewMessages(this.getCurrentUser().getUsername());
		return messages;
	}

	public int getNewMessagesCount(String type) throws Throwable {
		int count = 0;
		Map<String, Integer> messages = this.getMessageManager().getNewMessages(this.getCurrentUser().getUsername());
		if (null != messages && messages.containsKey(type)) {
			count = messages.get(type).intValue();
		}
		return count;
	}
	
	public int getPostsPerPage() {
		return this.getForumConfigManager().getPostsPerPage();
	}
	
	public int getAttachsPerPost() {
		return this.getForumConfigManager().getAttachsPerPost();
	}

	public void setHelper(IForumApsActionHelper helper) {
		this._helper = helper;
	}
	protected IForumApsActionHelper getHelper() {
		return _helper;
	}

	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	protected ISectionManager getSectionManager() {
		return _sectionManager;
	}

	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	protected IThreadManager getThreadManager() {
		return _threadManager;
	}

	public void setStatisticManager(IStatisticManager statisticManager) {
		this._statisticManager = statisticManager;
	}
	protected IStatisticManager getStatisticManager() {
		return _statisticManager;
	}

	public void setMarkupParser(IMarkupParser markupParser) {
		this._markupParser = markupParser;
	}
	protected IMarkupParser getMarkupParser() {
		return _markupParser;
	}

	public void setAttachManager(IAttachManager attachManager) {
		this._attachManager = attachManager;
	}
	protected IAttachManager getAttachManager() {
		return _attachManager;
	}

	public void setSanctionManager(ISanctionManager sanctionManager) {
		this._sanctionManager = sanctionManager;
	}
	protected ISanctionManager getSanctionManager() {
		return _sanctionManager;
	}

	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	protected IUserManager getUserManager() {
		return _userManager;
	}

	public void setForumConfigManager(IConfigManager forumConfigManager) {
		this._forumConfigManager = forumConfigManager;
	}
	protected IConfigManager getForumConfigManager() {
		return _forumConfigManager;
	}

	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}

	private IForumApsActionHelper _helper;
	private ISectionManager _sectionManager;
	private IThreadManager _threadManager;
	private IStatisticManager _statisticManager;
	private IMarkupParser _markupParser;
	private IAttachManager _attachManager;
	private ISanctionManager _sanctionManager;
	private IUserManager _userManager;
	private IConfigManager _forumConfigManager;
	private IMessageManager _messageManager;

}
