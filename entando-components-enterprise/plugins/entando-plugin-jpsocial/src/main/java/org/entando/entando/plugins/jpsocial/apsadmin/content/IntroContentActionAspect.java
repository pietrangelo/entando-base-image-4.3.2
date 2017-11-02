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
package org.entando.entando.plugins.jpsocial.apsadmin.content;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.apsadmin.social.helper.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction;

/**
 * @author E.Santoboni
 */
@Aspect
public class IntroContentActionAspect {

	private static final Logger _logger =  LoggerFactory.getLogger(IntroContentActionAspect.class);
	
	@After("execution(* com.agiletec.plugins.jacms.apsadmin.content.IntroNewContentAction.createNewVoid())")
	public void prepareIntroContentEditing(JoinPoint joinPoint) {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			IntroNewContentAction action = (IntroNewContentAction) joinPoint.getTarget();
			Content content = action.getContent();
			if (null != content) {
				Post post = this.getPendingPost(content, request);
				post.setContent(content);
			} else {
				_logger.debug("could not update the content of the post in session");
			}
		} catch (Throwable t) {
			_logger.error("Error checking Pending Post", t);
			throw new RuntimeException("Error checking Pending Post", t);
		}
	}
	
	@After("execution(* com.agiletec.plugins.jacms.apsadmin.content.ContentAction.edit())")
	public void introEdit(JoinPoint joinPoint) {
		HttpServletRequest request = ServletActionContext.getRequest();
		ContentAction action = (ContentAction) joinPoint.getTarget();
		Content content = action.getContent();
		try {
			Post post = this.getPendingPost(content, request);
			String origin = request.getParameter("origin");
			// if we are coming from a list then reset the post in session
			if (null == origin
					|| JpSocialSystemConstants.ORIGIN_LIST == Integer.valueOf(origin)) {
				_logger.debug("jpsocial: resetting post in session");
				this.emptyPostInSession(content, request);
			}
			// update content of the post
			post.setContent(content);
		} catch (NumberFormatException e) {
			_logger.error("Somebody's messed up with the \"origin\" parameter, resetting post by default for security reasons", e);
			this.emptyPostInSession(content, request);
		} catch (Throwable t) {
			_logger.error("Error editing content", t);
			throw new RuntimeException("Error editing content", t);
		}
	}
	
	public Post getPendingPost(Content content, HttpServletRequest request) {
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String username = currentUser.getUsername();
		Post pendingPost = (Post) request.getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING);
		// create a new post object if the case
		if (null == pendingPost
				|| !pendingPost.getUsername().equals(username)) {
			pendingPost = new Post(username);
			request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING);
			request.getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING, pendingPost);
		}
		return pendingPost;
	}
	
	protected Post emptyPostInSession(Content content, HttpServletRequest request) {
		Post pendingPost = getPendingPost(content, request);
		if (null != pendingPost.getQueue() && !pendingPost.getQueue().isEmpty()) {
			pendingPost.getQueue().clear();
		}
		pendingPost.setContent(null);
		pendingPost.setFast(false);
		return pendingPost;
	}
	
}
