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
package org.entando.entando.plugins.jpsubsites.aps.system.services.content;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SubsiteContentManager extends ContentManager {
    
    private static final Logger _logger = LoggerFactory.getLogger(SubsiteContentManager.class);
	
    @Override
    public List getResourceUtilizers(String resourceId) throws ApsSystemException {
        List<String> contentIds = null;
        try {
            contentIds = this.getContentDAO().getResourceUtilizers(resourceId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : resource " + resourceId, t);
        }
        return contentIds;
    }
	
	@Override
    public List getPageUtilizers(String pageCode) throws ApsSystemException {
        List<String> contentIds = null;
        try {
            contentIds = this.getContentDAO().getPageUtilizers(pageCode);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced contents : page " + pageCode, t);
        }
        return contentIds;
    }
    
}
