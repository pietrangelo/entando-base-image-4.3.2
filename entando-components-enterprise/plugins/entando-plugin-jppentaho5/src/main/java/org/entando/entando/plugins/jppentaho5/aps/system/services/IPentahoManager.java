package org.entando.entando.plugins.jppentaho5.aps.system.services;

import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoConfiguration;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IPentahoManager {

	/**
	 * Generate URL for report generation. Uses default configuration for the Pentaho instance
	 * @param path path of the report within Pentaho server instance
	 * @param csvArgs
	 * @return
	 * @throws ApsSystemException
	 */
	public String getPentahoReportUrl(String path, String csvArgs) throws ApsSystemException;

	/**
	 * Generate URL for report generation, as needed by the iframe
	 * @param pentahoInstance
	 * @param path path of the report within Pentaho server instance
	 * @param csvArgs TODO
	 * @return
	 * @throws ApsSystemException
	 */
	public String getPentahoReportUrl(String pentahoInstance, String path, String csvArgs) throws ApsSystemException;

	/**
	 * Get report attributes given the report path local to the Pentaho server
	 * @param path
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, PentahoParameter> getReportParameterMap(String path) throws ApsSystemException;

	/**
	 * Get the list of common fields of .prpt reports
	 * @return
	 */
	public List<String> getCommonReportField();

	/**
	 * Return the list of the parameters of a report needed for the generation
	 * @param path
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, PentahoParameter> getReportFormParameterMap(String path) throws ApsSystemException;

	/**
	 *  Get report attributes given the report path local to the Pentaho server
	 * @param path
	 * @return
	 * @throws ApsSystemException
	 */
	public List<PentahoParameter> getReportParameterList(String path) throws ApsSystemException;
	
	/**
	 * Return the list of the parameters of a report needed for the generation
	 * @param path
	 * @param includeOutput TODO
	 * @return
	 * @throws ApsSystemException
	 */
	public List<PentahoParameter> getReportFormParameterList(String path, boolean includeOutput) throws ApsSystemException;

	/**
	 * Generate an URL to the Pentaho instance suitable for report generation, depending on the content
	 * @param path
	 * @param args
	 * @return
	 * @throws ApsSystemException
	 */
	public String getPentahoReportUrl(String path, Map<String, String> args) throws ApsSystemException;
	
	/**
	 * Generate an URL to the Pentaho instance suitable for report generation, depending on the content
	 * @param pentahoInstance
	 * @param path
	 * @param args
	 * @return
	 * @throws ApsSystemException
	 */
	public String getPentahoReportUrl(String pentahoInstance, String path, Map<String, String> args) throws ApsSystemException;

	/**
	 * Update configuration
	 * @param config
	 * @throws ApsSystemException
	 */
	public void updateConfiguration(PentahoConfiguration config) throws ApsSystemException;

	/**
	 * Get Pentaho version
	 * @return
	 * @throws ApsSystemException
	 */
	public String getPentahoVersion() throws ApsSystemException;

	/**
	 * Get configuration
	 * @return
	 * @throws ApsSystemException
	 */
	public PentahoConfiguration getConfiguration() throws ApsSystemException;

}
