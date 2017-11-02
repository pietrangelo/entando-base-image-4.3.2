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
package org.entando.entando.plugins.jpsocial.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.FacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ISocialBaseManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.TwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author S.Loru
 */
public class SocialLinkTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialLinkTag.class);

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		ISocialPostManager socialPostManager = (ISocialPostManager) ApsWebApplicationUtils.getBean("jpsocialSocialPostManager", request);
		String socialLink = "";
		if (StringUtils.isNotBlank(this.getIdSocialPost())) {
			try {

				SocialPost socialPost = socialPostManager.getSocialPost(Integer.parseInt(this.getIdSocialPost()));
				if (socialPost != null && StringUtils.isNotBlank(socialPost.getSocialid())) {
					ISocialBaseManager socialManager = this.getSocialManager(socialPost.getProvider());
					socialLink = socialManager.getLinkBySocialId(socialPost.getSocialid());
				}
			} catch (ApsSystemException ex) {
				_logger.error("error in doStartTag", ex);
			}
		}

		if (null != getVar() && !"".equals(getVar().trim())) {
			this.pageContext.setAttribute(getVar(), socialLink);
		} else {
			this.pageContext.setAttribute(VAR_SOCIALINK, socialLink);
		}

		return EVAL_PAGE;
	}

	private ISocialBaseManager getSocialManager(String provider) {
		ISocialBaseManager socialManager = null;
		switch (PROVIDER.valueOf(provider)) {
			case facebook:
				socialManager = (FacebookManager) ApsWebApplicationUtils.getBean("jpsocialFacebookManager", (HttpServletRequest) this.pageContext.getRequest());
				break;

			case twitter:
				socialManager = (TwitterManager) ApsWebApplicationUtils.getBean("jpsocialTwitterManager", (HttpServletRequest) this.pageContext.getRequest());
				break;
		}
		return socialManager;
	}

	public String getIdSocialPost() {
		return idSocialPost;
	}

	public void setIdSocialPost(String idSocialPost) {
		this.idSocialPost = idSocialPost;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	private static final String VAR_SOCIALINK = "socialLink";
	private String idSocialPost;
	private String var;
}
