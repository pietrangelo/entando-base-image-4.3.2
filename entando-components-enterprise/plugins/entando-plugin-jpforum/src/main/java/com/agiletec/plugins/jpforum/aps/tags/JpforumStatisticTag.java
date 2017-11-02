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
package com.agiletec.plugins.jpforum.aps.tags;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;

public class JpforumStatisticTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(JpforumStatisticTag.class);

    @Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			ISectionManager sectionManager = (ISectionManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.SECTION_MANAGER, pageContext);
			IThreadManager threadManager = (IThreadManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.THREAD_MANAGER, pageContext);

			String targetSection = null;
			
			if (null == this.getSectionCode() || this.getSectionCode().trim().length() == 0) {
				if (null != request.getParameter("section") && request.getParameter("section").trim().length() > 0) {
					targetSection = request.getParameter("section");
				} else {
					targetSection = sectionManager.getRoot().getCode();
				}
			} else {
				targetSection = this.getSectionCode();
			}
			Section section = sectionManager.getSection(targetSection);

			Object value = this.getSectionStatistics(section, threadManager);
			this.pageContext.getRequest().setAttribute(this.getVar(), value);
			this.pageContext.getRequest().setAttribute(this.getVar() + "_section", section);
			
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Errore inizializzazione tag", t);
		}
		return EVAL_PAGE;
	}

	/**
	 * Restituisce un array di interi di lunghezza 2 <br>
	 * Nella posizione 0 setta il conteggio dei threads di questa sezione e delle sezioni figlie<br>
	 * Nella posizione1 setta il conteggio dei posts
	 * @param sectionCode
	 * @return
	 * @throws Throwable
	 */
	public Integer[] getSectionStatistics(Section section, IThreadManager threadManager) throws Throwable {
		Integer[] statistics = new Integer[]{0,0};
		try {
			this.addStat(statistics, section, threadManager);
		} catch (Throwable t) {
			_logger.error("error in getSectionStatistics", t);
			throw new RuntimeException("Error building statistics for section " + section.getCode());
		}
		return statistics;
	}

	private void addStat(Integer[] statistics, Section section, IThreadManager threadManager) throws Throwable {
		Map<Integer, List<Integer>> threads = new HashMap<Integer, List<Integer>>();
		threads = threadManager.getThreads(section.getCode());
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
			this.addStat(statistics, (Section) child, threadManager);
		}
	}
	
	public String getSectionCode() {
		return _sectionCode;
	}
	public void setSectionCode(String sectionCode) {
		this._sectionCode = sectionCode;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _sectionCode;
	private String _var;
}
