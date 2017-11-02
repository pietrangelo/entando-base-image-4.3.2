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

package org.entando.entando.plugins.jpmultisite.aps.system.services.content.model;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteSharedContentWrapper extends Content {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteSharedContentWrapper.class);

	public Set<SharedContent> getSharedContent() {
		return _sharedContent;
	}

	public void setSharedContent(Set<SharedContent> sharedContent) {
		this._sharedContent = sharedContent;
	}
	
	Set<SharedContent> _sharedContent = new HashSet<SharedContent>();

}
