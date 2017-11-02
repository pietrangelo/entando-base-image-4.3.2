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
package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

import static junit.framework.Assert.assertNotNull;
import org.entando.entando.plugins.jpconstantcontact.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpconstantcontact.aps.system.JpConstantContactSystemConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConstantContactManager extends ApsPluginBaseTestCase {
  
  private static final Logger _logger = LoggerFactory.getLogger(TestConstantContactManager.class);
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.init();
  }
  
  protected void init() throws Exception {
    try {
      //getService takes the manager bean
      ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
      this._constantcontactManager = (ConstantContactManager) this.getService(JpConstantContactSystemConstants.CONSTANTCONTACT_MANAGER);
      assertNotNull(configManager);
    } catch (Exception e) {
      _logger.info("\n\nerrore quiiiiii");
      throw e;
    }
  }
  
  public void testConstantContactSettigns() throws Throwable{
  
  }
  
  
  
  @Override
  protected void tearDown() throws Exception {
    // TODO Auto-generated method stub
    super.tearDown();
  }
   public ConstantContactManager getConstantcontactManager() {
        return _constantcontactManager;
    }

    public void setConstantcontactManager(ConstantContactManager _constantcontactManager) {
        this._constantcontactManager = _constantcontactManager;
    }

    public IConstantContactManager getIconstantcontactManager() {
        return _iconstantcontactManager;
    }

    public void setIconstantcontactManager(IConstantContactManager _iconstantcontactManager) {
        this._iconstantcontactManager = _iconstantcontactManager;
    }

  protected ConstantContactManager _constantcontactManager;
  protected IConstantContactManager _iconstantcontactManager;

}


