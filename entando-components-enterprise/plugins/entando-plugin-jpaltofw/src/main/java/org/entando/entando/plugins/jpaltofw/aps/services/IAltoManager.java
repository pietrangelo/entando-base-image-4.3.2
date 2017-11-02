package org.entando.entando.plugins.jpaltofw.aps.services;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IAltoManager {

    AltoConfig getAltoConfig() throws ApsSystemException;

    void updateAltoConfig(final AltoConfig config) throws ApsSystemException;


}
