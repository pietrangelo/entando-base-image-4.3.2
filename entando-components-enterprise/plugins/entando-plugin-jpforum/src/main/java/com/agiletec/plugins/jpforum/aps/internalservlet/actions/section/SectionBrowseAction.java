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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.section;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class SectionBrowseAction extends AbstractForumAction implements ISectionBrowseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionBrowseAction.class);

	@Override
	public String viewSection() {
		/*
		 * se c'è il parametro sezione va alla sezione
		 * oppure
		 * se c'è il parametro post va al post
		 * */
		try {
			if (null == this.getSection() || this.getSection().trim().length() == 0) {
				this.setSection(this.getSectionManager().getRoot().getCode());
			}
			if (null != this.getPost()) {
				if (null != this.getPost(this.getPost())) {
					return "viewPost";
				}
			}
		} catch (Throwable t) {
			_logger.error("error in viewSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Section getCurrentSection() {
		Section section = super.getSection(this.getSection());
		return section;
	}

	public String[] getBreadCrumbs() {
		return super.getBreadCrumbs(this.getSection());
	}

	public List<Integer> getThreads(boolean pin) throws Throwable {
		List<Integer> threads = new ArrayList<Integer>();
		try {
			threads = this.getThreadManager().getThreads(this.getSection(), pin);
		} catch (Throwable t) {
			_logger.error("error in getThreads", t);
			throw new RuntimeException("errore in recupero identificativi threads per la sezione " + this.getSection());
		}
		return threads;
	}

	public void setSection(String section) {
		this._section = section;
	}
	public String getSection() {
		return _section;
	}

	public void setPost(Integer post) {
		this._post = post;
	}
	public Integer getPost() {
		return _post;
	}

	private String _section;
	private Integer _post;

}
