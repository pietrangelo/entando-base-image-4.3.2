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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.attach;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;

public class AttachAction extends AbstractForumAction implements IAttachAction {

	private static final Logger _logger = LoggerFactory.getLogger(AttachAction.class);

	@Override
	public String downloadAttach() {
		try {
			//FIXME downloadAttach
			Lang lang = this.getLangManager().getLang(super.getRequest().getParameter("lang"));
			IPage page = this.getPageManager().getPage(super.getParameter("page"));
			Map<String, String> params = new HashMap<String, String>();
			params.put("actionPath", "/ExtStr2/do/jpforum/Front/Post/viewPost.action?post=" + this.getPost());
			this.setInputPage(this.getUrlManager().createURL(page, lang, params));
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionMessage("jpforum.error.post.null");
				return INPUT;
			}
			int threadId = post.getThreadId();
			String sectionCode = this.getThread(threadId).getSectionId();
			Section section = this.getSection(sectionCode);
			if (this.isAllowedOnGroups(section, this.getCurrentUser()) || section.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_READONLY) {
				List<Attach> attachs = this.getAttachManager().getAttachs(this.getPost());
				Attach downloadAttach = null;
				if (null != attachs && attachs.size() > 0) {
					for (int i = 0; i < attachs.size(); i++) {
						Attach attach = attachs.get(i);
						if (attach.getName().equals(this.getName())) {
							downloadAttach = attach;
							break;
						}
					}
					if (null == downloadAttach) {
						this.addActionMessage("jpforum.error.download.null");
						return INPUT;
					}
					String fileSeparator = System.getProperty("file.separator");
					File userDir = new File(this.getAttachManager().getAttachDiskFolder() + post.getUsername());
					File currFile = new File(userDir + fileSeparator + downloadAttach.getFileName());
					this.setInputStream(new FileInputStream(currFile));
					if (null != this.getInputStream()) {
						return SUCCESS;
					}
				}
			} else {
				return "jpforumlogin";
			}
		} catch (Throwable t) {
			_logger.error("error in downloadAttach", t);
			return FAILURE;

		}
		return INPUT;
	}

	@Override
	public String trashAttach() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trashAttach", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String deleteAttach() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getAttachManager().deleteAttach(this.getPost(), this.getName());
			}
		} catch (Throwable t) {
			_logger.error("error in deleteAttach", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String[] getBreadCrumbs() {
		this.getRequest().setAttribute(JpforumSystemConstants.ATTR_SHOW_LAST_BREADCRUMB, true);
		Post parentPost = this.getPost(this.getPost());
		int threadId = parentPost.getThreadId();
		String sectionCode = this.getThread(threadId).getSectionId();
		return this.getBreadCrumbs(sectionCode);
	}

	@Override
	public Section getCurrentSection() {
		Post parentPost = this.getPost(this.getPost());
		int threadId = parentPost.getThreadId();
		String sectionCode = this.getThread(threadId).getSectionId();
		return this.getSection(sectionCode);
	}

	public void setUsername(String username) {
		this._username = username;
	}

	public String getUsername() {
		return _username;
	}

	public void setPost(int post) {
		this._post = post;
	}

	public int getPost() {
		return _post;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getName() {
		return _name;
	}

	public void setInputStream(FileInputStream inputStream) {
		this._inputStream = inputStream;
	}

	public FileInputStream getInputStream() {
		return _inputStream;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}

	protected IURLManager getUrlManager() {
		return _urlManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public void setInputPage(String inputPage) {
		this._inputPage = inputPage;
	}

	public String getInputPage() {
		return _inputPage;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	private int _strutsAction;
	private int _post;
	private String _name;
	private String _username;
	private FileInputStream _inputStream;
	private IPageManager _pageManager;
	private IURLManager _urlManager;
	private String _inputPage;

}
