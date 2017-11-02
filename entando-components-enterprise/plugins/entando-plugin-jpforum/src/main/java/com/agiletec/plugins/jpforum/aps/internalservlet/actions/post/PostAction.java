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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.post;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.markup.tag.IForumMarkupTag;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.Attach;

public class PostAction extends AbstractForumAction implements IPostAction {

	private static final Logger _logger =  LoggerFactory.getLogger(PostAction.class);

	@Override
	public String replyPost() {
		try {
			if (!this.isAllowedOnCreateThread()) {
				this.addActionError(this.getText("jpforum.error.cannot.create.thread"));
				return INPUT;
			}
			if (!this.getCurrentSection().isOpen()) {
				this.addActionError(this.getText("jpforum.error.section.closed"));
				return INPUT;
			}
			Post parent = this.getPost(this.getPost());
			if (null == parent) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			if (!this.getThread(parent.getThreadId()).isOpen()) {
				this.addActionError(this.getText("jpforum.error.thread.closed"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			String xml = this.getMarkupParser().markupToXML("");
			if (null != this.getRequest().getParameter("q") && this.getRequest().getParameter("q").equalsIgnoreCase("true")) {
				IForumMarkupTag quoteTag = (IForumMarkupTag) this.getMarkupParser().getMarkupLanguage().getDefaultQuoteTag();
				if (null != quoteTag) {
					String parentMarkup = this.getMarkupParser().XMLToMarkup(parent.getText());
					String parentPostUser = parent.getUsername();
					String raw = parentPostUser + " " + this.getText("jpforum_WROTE") + " " + quoteTag.getRestoreExp().replace("getText", parentMarkup);
					xml = this.getMarkupParser().markupToXML(raw);
				}
			}

			this.setBody(xml);
		} catch (Throwable t) {
			_logger.error("error in replyPost", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String savePost() {
		try {
			Post post = this.createPost();
			Thread thread = null;
			List<Attach> attachs = this.createAttachs(post);
			String checkAttachs = this.checkAttachs(post.getUsername(), attachs);
			if (null != checkAttachs) return checkAttachs;
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				if (0 == this.getThread() && null != this.getSection() && this.getSection().trim().length() > 0) {
					//IL THREAD DEVE AVERE IL TITOLO
					if (null == this.getTitle() || this.getTitle().trim().length() == 0) {
						this.addFieldError("title", this.getText("jpforum.thread.title.required"));
						this.setBody(this.getMarkupParser().markupToXML(this.getBody()));
						return INPUT;
					}
					thread = this.createThread();
					thread.setPost(post);
					this.getThreadManager().addThread(thread);
				} else {
					this.getThreadManager().addPost(post);
				}
			} else {
				this.getThreadManager().updatePost(post);
			}
			attachs = this.createAttachs(post);
			if (null != attachs && attachs.size() > 0) {
				this.getAttachManager().saveAttachs(attachs);
			}
			thread = this.getThreadManager().getThread(post.getThreadId());
			this.setThread(thread.getCode());
			this.setPost(post.getCode());
			int pageItemNumber = this.getPageItemNumber(post.getCode(), thread.getPostCodes());
			super.getRequest().setAttribute(JpforumSystemConstants.ATTR_PAGER_ITEM_NUMBER, pageItemNumber);
		} catch (Throwable t) {
			_logger.error("error in savePost", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private String checkAttachs(String username, List<Attach> attachs) throws ApsSystemException {
		String retVal = null;
		List<Attach> userAttachs = this.getAttachManager().getAttachs(username);
		long usedBytes = 0;
		if (null != userAttachs && userAttachs.size() > 0) {
			for (int i = 0; i < userAttachs.size(); i++) {
				Attach currentAttach = userAttachs.get(i);
				usedBytes = usedBytes + currentAttach.getFileSize();
			}
		}
		long uploadBytes = 0;
		if (null != attachs && attachs.size() > 0) {
			for (int i = 0; i < attachs.size(); i++) {
				Attach currentAttach = attachs.get(i);
				uploadBytes = uploadBytes + currentAttach.getFileSize();
			}
		}
		if (usedBytes + uploadBytes > this.getAttachManager().getAttachFolderMaxSize()) {
			String[] args = new String[] {new Integer(this.getAttachManager().getAttachFolderMaxSize()).toString(), new Long(usedBytes).toString(), new Long(uploadBytes).toString()};
			this.addActionError(this.getText("jpforum.error.attachs.maxsize", args));
			retVal = INPUT;
		}
		return retVal;
	}

	private List<Attach> createAttachs(Post post) {
		List<Attach> attachs = new ArrayList<Attach>();
		try {
			if (null != this.getMyDoc() && this.getMyDoc().length > 0) {
				for (int i = 0; i < this.getMyDoc().length; i++) {
					File currentFile = this.getMyDoc()[i];
					if (null != currentFile && currentFile.length() > 0) {
						Attach attach = new Attach();
						attach.setFileName(this.getMyDocFileName()[i]);
						attach.setFileSize(currentFile.length());
						attach.setUserName(post.getUsername());
						attach.setInputStream(new FileInputStream(currentFile));
						attach.setPostCode(post.getCode());
						attachs.add(attach);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in createAttachs", t);
		}
		return attachs;
	}

	@Override
	public String viewPost() {
		try {
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			Thread thread = this.getThread(post.getThreadId());
			Section section = this.getSectionManager().getSection(thread.getSectionId());
			boolean isUserAllowed = this.isAllowedOnGroups(section, this.getCurrentUser());
			if (!isUserAllowed && section.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_HIDDEN) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			this.setThread(thread.getCode());
			int pageItemNumber = this.getPageItemNumber(post.getCode(), thread.getPostCodes());
			super.getRequest().setAttribute(JpforumSystemConstants.ATTR_PAGER_ITEM_NUMBER, pageItemNumber);
			this.getStatisticManager().viewThread(super.getRequest().getSession().getId(), post.getThreadId());
		} catch (Throwable t) {
			_logger.error("error in viewPost", t);
			return FAILURE;
		}
		return SUCCESS;
	}


	public String viewThread() {
		try {/*
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}*/
			Thread thread = this.getThread(this.getThread());
			if (null == thread) {
				this.addActionError(this.getText("jpforum.error.thread.null"));
				return INPUT;
			}
			//Nota: la sezione viene recuperata in funzione dei gruppi dell'utente corrente.
			//Se l'utente non ha il gruppo necessario per visualizzare la sezione, questa sarà null, e quindi il thread
			Section section = this.getSection(thread.getSectionId());
			if (null == section) {
				this.addActionError(this.getText("jpforum.error.thread.null"));
				return INPUT;
			}
			boolean isUserAllowed = this.getHelper().isUserAllowed(section, this.getCurrentUser());
			if (!isUserAllowed && section.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_HIDDEN) {
				this.addActionError(this.getText("jpforum.error.thread.null"));
				return INPUT;
			}
			this.getStatisticManager().viewThread(super.getRequest().getSession().getId(), thread.getCode());
		} catch (Throwable t) {
			_logger.error("error in viewThread", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Thread getCurrentThread() {
		return this.getThread(this.getThread());
	}

	@Override
	public String editPost() {
		try {
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			Thread thread = this.getThread(post.getThreadId());

			if (!this.isAllowedOnEditPost(post.getCode())) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			if (!thread.isOpen()) {
				this.addActionError(this.getText("jpforum.error.thread.closed"));
				return INPUT;
			}
			if (!this.getCurrentSection().isOpen()) {
				this.addActionError(this.getText("jpforum.error.thread.closed"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			this.setTitle(post.getTitle());
			this.setBody(post.getText());
		} catch (Throwable t) {
			_logger.error("error in editPost", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String trashPost() {
		try {
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			Thread thread = this.getThread(post.getThreadId());
			this.setThread(thread.getCode());
			boolean isUserAllowed = this.isAllowedOnModerateThread();
			if (!isUserAllowed) {
				this.addActionError(this.getText("jpforum.error.user.notAllowedOnModerate"));
				return INPUT;
			}
			if (post.getCode() == thread.getCode()) {
				this.addActionError(this.getText("jpforum.error.cannot.delete.root"));
				return INPUT;
			}
			if (!thread.isOpen()) {
				this.addActionError(this.getText("jpforum.error.thread.closed"));
				return INPUT;
			}
			if (!this.getCurrentSection().isOpen()) {
				this.addActionError(this.getText("jpforum.error.section.closed"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trashPost", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String deletePost() {
		try {
			Post post = this.getPost(this.getPost());
			if (null == post) {
				this.addActionError(this.getText("jpforum.error.post.null"));
				return INPUT;
			}
			Thread thread = this.getThread(post.getThreadId());
			this.setThread(thread.getCode());
			boolean isUserAllowed = this.isAllowedOnModerateThread();
			if (!isUserAllowed) {
				this.addActionError(this.getText("jpforum.error.user.notAllowedOnModerate"));
				return INPUT;
			}
			if (post.getCode() == thread.getCode()) {
				this.addActionError(this.getText("jpforum.error.cannot.delete.root"));
				return INPUT;
			}
			if (!thread.isOpen()) {
				this.addActionError(this.getText("jpforum.error.thread.closed"));
				return INPUT;
			}
			if (!this.getCurrentSection().isOpen()) {
				this.addActionError(this.getText("jpforum.error.section.closed"));
				return INPUT;
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getThreadManager().deletePost(this.getPost());
			}
			this.setThread(thread.getCode());
		} catch (Throwable t) {
			_logger.error("error in deletePost", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String[] getBreadCrumbs() {
		this.getRequest().setAttribute(JpforumSystemConstants.ATTR_SHOW_LAST_BREADCRUMB, true);
		String sectionCode = this.getThread(this.getThread()).getSectionId();
		return this.getBreadCrumbs(sectionCode);
	}

	@Override
	public Section getCurrentSection() {
		Post parentPost = this.getPost(this.getPost());
		this.setThread(parentPost.getThreadId());
		String sectionCode = this.getThread(this.getThread()).getSectionId();
		return this.getSection(sectionCode);
	}

	private int getPageItemNumber(int postCode, List<Integer> posts) {
		int posInList = 0;
		for (int i = 0; i < posts.size(); i++) {
			int currentPost = posts.get(i);
			if (currentPost == postCode) {
				posInList = i;
				break;
			}
		}
		int block = this.getForumConfigManager().getPostsPerPage();
		long x = posInList % block;
		int page = Math.round(posInList / block);
		if (x >= 0 || posInList == 0) {
			page = page + 1;
		}
		return page;
	}

	private Thread createThread() {
		Thread thread = new Thread();
		thread.setCode(this.getThread());
		thread.setOpen(true);
		thread.setPin(false);
		thread.setSectionId(this.getSection());
		thread.setUsername(this.getCurrentUser().getUsername());
		return thread;
	}

	private Post createPost() {
		Post post = new Post();
		post.setCode(this.getPost());
		post.setText(this.getBody());
		post.setThreadId(this.getThread());
		post.setTitle(this.getTitle());
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			post.setUsername(this.getCurrentUser().getUsername());
		} else {
			Post oldPost = this.getPost(this.getPost());
			post.setUsername(oldPost.getUsername());
		}
		return post;
	}

	public int getPost() {
		return _post;
	}
	public void setPost(int post) {
		this._post = post;
	}

	public Date getPostDate() {
		return _postDate;
	}
	public void setPostDate(Date postDate) {
		this._postDate = postDate;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	public String getBody() {
		return _body;
	}
	public void setBody(String body) {
		this._body = body;
	}

	public int getThread() {
		return _thread;
	}
	public void setThread(int thread) {
		this._thread = thread;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setSection(String section) {
		this._section = section;
	}
	public String getSection() {
		return _section;
	}

	public void setMyDoc(File[] myDoc) {
		this._myDoc = myDoc;
	}
	public File[] getMyDoc() {
		return _myDoc;
	}

	public void setMyDocContentType(String[] myDocContentType) {
		this._myDocContentType = myDocContentType;
	}
	public String[] getMyDocContentType() {
		return _myDocContentType;
	}

	public void setMyDocFileName(String[] myDocFileName) {
		this._myDocFileName = myDocFileName;
	}
	public String[] getMyDocFileName() {
		return _myDocFileName;
	}

	private int _strutsAction;
	private int _post;
	private Date _postDate;
	private String _title;
	private String _body;
	private int _thread;
	private String _section;
	private File[] _myDoc;
	private String[] _myDocContentType;
	private String[] _myDocFileName;

}