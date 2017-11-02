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
package org.entando.entando.plugins.jpseo.aps.system.services.content.widget;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author E.Santoboni
 */
public class ContentViewerHelper extends com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentViewerHelper {
	
	@Override
	protected String extractContentId(String contentId, ApsProperties widgetConfig, RequestContext reqCtx) {
		String extractedContentId = super.extractContentId(contentId, widgetConfig, reqCtx);
		if (this.isCurrentFrameMain(reqCtx)) {
			String extractedId = reqCtx != null ? (String) reqCtx.getExtraParam(JpseoSystemConstants.EXTRAPAR_HIDDEN_CONTENT_ID) : null;
			if (extractedId != null) {
				extractedContentId = extractedId;
			}
		}
		return extractedContentId;
	}
	
	protected boolean isCurrentFrameMain(RequestContext reqCtx) {
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		int mainFrame = currentPage.getModel().getMainFrame();
		return (currentFrame.intValue() == mainFrame);
	}
	
}