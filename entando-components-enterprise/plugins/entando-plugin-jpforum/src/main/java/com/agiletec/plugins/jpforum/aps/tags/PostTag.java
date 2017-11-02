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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.markup.IMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

public class PostTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(PostTag.class);

    @Override
	public int doStartTag() throws JspException {
		try {
			IThreadManager threadManager = (IThreadManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.THREAD_MANAGER, pageContext);
			Post post = threadManager.getPost(this.getPostId());
			IMarkupParser parser = (IMarkupParser) ApsWebApplicationUtils.getBean(JpforumSystemConstants.MARKUP_PARSER, pageContext);
			String htmlText = parser.XMLtoHTML(post.getText());
			//TODO FIXME
			post.setText(htmlText);
			this.pageContext.setAttribute(this.getVar(), post);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Errore inizializzazione tag", t);
		}
		return EVAL_PAGE;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public Integer getPostId() {
		return _postId;
	}
	public void setPostId(Integer postId) {
		this._postId = postId;
	}


	private Integer _postId;
	private String _var;
}
