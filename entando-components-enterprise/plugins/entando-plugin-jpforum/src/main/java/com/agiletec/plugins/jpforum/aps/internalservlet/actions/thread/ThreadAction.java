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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

public class ThreadAction extends AbstractForumAction implements IThreadAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ThreadAction.class);

	@Override
	public String newThread() {
		try {
			if (null == this.getCurrentSection()) {
				this.addActionError(this.getText("jpforum.error.section.null"));
				return INPUT;
			}
			if (!this.isAllowedOnCreateThread()) {
				this.addActionError(this.getText("jpforum.error.user.notAllowedOnCreateThread"));
				return INPUT;
			}
			if (!this.getCurrentSection().isOpen()) {
				this.addActionError(this.getText("jpforum.error.section.closed"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			this.setOpen(true);
			this.setPin(false);
			String xml = this.getMarkupParser().markupToXML("");
			this.setBody(xml);
		} catch (Throwable t) {
			_logger.error("error in newThread", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String viewThread() {
		try {
			Thread thread = this.getThreadManager().getThread(this.getThread());
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

	@Override
	public String changePin() {
		try {
			Thread thread = this.getThreadManager().getThread(this.getThread());
			String check = this.checkModerateOperation(thread);
			if (null != check) return check;
			thread.setPin(!thread.isPin());
			this.getThreadManager().updateThread(thread);
		} catch (Throwable t) {
			_logger.error("error in changePin", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String changeStatus() {
		try {
			Thread thread = this.getThreadManager().getThread(this.getThread());
			String check = this.checkModerateOperation(thread);
			if (null != check) return check;
			thread.setOpen(!thread.isOpen());
			this.getThreadManager().updateThread(thread);
		} catch (Throwable t) {
			_logger.error("error in changeStatus", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String deleteThread() {
		try {
			Thread thread = this.getThreadManager().getThread(this.getThread());
			String check = this.checkModerateOperation(thread);
			if (null != check) return check;
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getThreadManager().deleteThread(thread.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in deleteThread", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String trashThread() {
		try {
			Thread thread = this.getThreadManager().getThread(this.getThread());
			String check = this.checkModerateOperation(thread);
			if (null != check) return check;
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);

		} catch (Throwable t) {
			_logger.error("error in trashThread", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public Thread getCurrentThread() {
		return this.getThread(this.getThread());
	}

	public Section getCurrentSection() {
		Section section = null;
		try {
			if (0 != this.getThread()) {
				Thread thread = this.getThreadManager().getThread(this.getThread());
				section = this.getSection(thread.getSectionId());
			} else {
				section = this.getSection(this.getSection());
			}

		} catch (Throwable t) {
			_logger.error("errore in caricamento sezioneCorrente ", t);
			throw new RuntimeException("errore in caricamento sezioneCorrente " );
		}
		return section;
	}

	public String[] getBreadCrumbs() {
		this.getRequest().setAttribute(JpforumSystemConstants.ATTR_SHOW_LAST_BREADCRUMB, true);
		return super.getBreadCrumbs(this.getCurrentSection().getCode());
	}

	private String checkModerateOperation(Thread thread) {
		String check = null;
		if (null == thread) {
			this.addActionError(this.getText("jpforum.error.thread.null"));
			return INPUT;
		}
		Section section = this.getSection(thread.getSectionId());
		if (null == section) {
			this.addActionError(this.getText("jpforum.error.section.null"));
			return INPUT;
		}
		boolean isUserAllowed = this.isAllowedOnModerateThread();
		if (!isUserAllowed) {
			this.addActionError(this.getText("jpforum.error.user.notAllowedOnModerate"));
			return INPUT;
		}
		return check;
	}

	public void setSection(String section) {
		this._section = section;
	}
	public String getSection() {
		return _section;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setThread(int code) {
		this._thread = code;
	}
	public int getThread() {
		return _thread;
	}

	public void setOpen(boolean open) {
		this._open = open;
	}
	public boolean  getOpen() {
		return _open;
	}

	public void setPin(boolean  pin) {
		this._pin = pin;
	}
	public boolean  getPin() {
		return _pin;
	}

	public void setTitle(String title) {
		this._title = title;
	}
	public String getTitle() {
		return _title;
	}

	public void setBody(String body) {
		this._body = body;
	}
	public String getBody() {
		return _body;
	}

	private int _strutsAction;
	private String _title;
	private String _body;
	private int _thread;
	private boolean _open;
	private boolean _pin;
	private String _section;
}
