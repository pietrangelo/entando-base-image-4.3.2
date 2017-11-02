/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;
import com.agiletec.plugins.jacms.aps.tags.ContentListTag;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads a list of contents IDs by applying the the filter by the category of the corrent subsite and any other filters (if any). 
 * Only the IDs of the contents accessible in the portal can be loaded.
 * @author E.Santoboni
 */
public class SubsiteContentListTag extends ContentListTag {
	
	private static final Logger _logger = LoggerFactory.getLogger(SubsiteContentListTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentListWidgetHelper helper = (IContentListWidgetHelper) ApsWebApplicationUtils.getBean(JpsubsitesSystemConstants.CONTENT_LIST_HELPER, this.pageContext);
			List<UserFilterOptionBean> defaultUserFilterOptions = helper.getConfiguredUserFilters(this, reqCtx);
			this.addUserFilterOptions(defaultUserFilterOptions);
			this.extractExtraWidgetParameters(reqCtx);
			if (null != this.getUserFilterOptions() && null != this.getUserFilterOptionsVar()) {
				this.pageContext.setAttribute(this.getUserFilterOptionsVar(), this.getUserFilterOptions());
			}
			List<String> contents = this.getContentsId(helper, reqCtx);
			this.pageContext.setAttribute(this.getListName(), contents);
		} catch (Throwable t) {
			_logger.error("error in end tag", t);
			throw new JspException("Error detected while finalising the tag", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
}
