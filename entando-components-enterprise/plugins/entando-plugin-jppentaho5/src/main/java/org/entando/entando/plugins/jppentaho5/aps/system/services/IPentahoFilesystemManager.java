package org.entando.entando.plugins.jppentaho5.aps.system.services;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IPentahoFilesystemManager {
	
	public List<String> getReportsPath(String filter) throws ApsSystemException;
	public String getAuthTicket() throws ApsSystemException;
	
}
