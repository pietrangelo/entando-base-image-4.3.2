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
package org.entando.entando.plugins.jpseo.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentViewerHelper;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;

/**
 * @author E.Santoboni
 */
public class ContentTag extends com.agiletec.plugins.jacms.aps.tags.ContentTag {

	private static final Logger _logger =  LoggerFactory.getLogger(ContentTag.class);

	public ContentTag() {
		super();
	}
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IContentViewerHelper helper = (IContentViewerHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_VIEWER_HELPER, this.pageContext);
			String contentId = (this.getContentId() != null && this.getContentId().trim().length() > 0) ? this.getContentId() : null;
			ContentRenderizationInfo renderInfo = helper.getRenderizationInfo(contentId, this.getModelId(), this.isPublishExtraTitle(), reqCtx);
			String renderedContent = (null != renderInfo) ? renderInfo.getRenderedContent() : "";
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), renderedContent);
			} else {
				this.pageContext.getOut().print(renderedContent);
			}
			if (null != renderInfo) {
				if (null != this.getAttributeValuesByRoleVar()) {
					this.pageContext.setAttribute(this.getAttributeValuesByRoleVar(), renderInfo.getAttributeValues());
				}
				if (this.isPublishExtraDescription() && null != renderInfo.getAttributeValues()) {
					Object values = renderInfo.getAttributeValues().get(JpseoSystemConstants.ATTRIBUTE_ROLE_DESCRIPTION);
					if (null != values) {
						reqCtx.addExtraParam(JpseoSystemConstants.EXTRAPAR_EXTRA_PAGE_DESCRIPTIONS, values);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		super.release();
		this.setPublishExtraDescription(false);
	}
	
	/**
	 * Return true if the extra descriptions will be insert into Request Context.
	 * @return The property.
	 */
	public boolean isPublishExtraDescription() {
		return _publishExtraDescription;
	}
	
	/**
	 * Specify if the extra descriptions will be insert into Request Context.
	 * @param publishExtraTitle The property to set.
	 */
	public void setPublishExtraDescription(boolean publishExtraDescription) {
		this._publishExtraDescription = publishExtraDescription;
	}
	
	private boolean _publishExtraDescription;
	
}