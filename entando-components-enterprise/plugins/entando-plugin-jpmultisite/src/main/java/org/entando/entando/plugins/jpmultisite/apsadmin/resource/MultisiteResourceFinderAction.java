/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.resource;

import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteResourceFinderAction extends ResourceFinderAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteResourceFinderAction.class);
	
	@Override
    public List<String> getResources() throws Throwable {
        List<String> resourceIds = null;
		List<String> filteredResources = new ArrayList<String>();
        try {
			List<String> codesForSearch = super.getGroupCodesForSearch();
			resourceIds = this.getResourceManager().searchResourcesId(this.getResourceTypeCode(), 
					this.getText(), this.getFileName(), this.getCategoryCode(), codesForSearch);
			for (String id : resourceIds) {
				if (id.endsWith(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()))) {
					filteredResources.add(id);
				}
			}
		} catch (Throwable t) {
        	_logger.error("error in getResources", t);
            throw t;
        }
        return filteredResources;
    }
	
}
