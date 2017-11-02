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

package org.entando.entando.plugins.jpmultisite.aps.system.services.resource;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteResourceManager extends ResourceManager{
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteResourceManager.class);
	
	public ResourceInterface addResource(ResourceDataBean bean, String multisiteCode) throws ApsSystemException {
		ResourceInterface newResource = this.createResource(bean);
		try {
			this.generateAndSetResourceId(newResource, bean.getResourceId(), multisiteCode);
			newResource.saveResourceInstances(bean);
    		this.getResourceDAO().addResource(newResource);
    	} catch (Throwable t) {
			newResource.deleteResourceInstances();
			_logger.error("Error adding resource", t);
			throw new ApsSystemException("Error adding resource", t);
    	}
		return newResource;
    }
	
	protected void generateAndSetResourceId(ResourceInterface resource, String id, String multisiteCode) throws ApsSystemException {
		if (null == id || id.trim().length() == 0) {
			IKeyGeneratorManager keyGenerator = 
					(IKeyGeneratorManager) this.getBeanFactory().getBean(SystemConstants.KEY_GENERATOR_MANAGER);
			int newId = keyGenerator.getUniqueKeyCurrentValue();
			resource.setId(String.valueOf(newId)+JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+multisiteCode);
		}
	}

	
}
