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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperRunReportResponse;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.InputControlConfig;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interface to manage Jasper Server
 *
 * @author rinaldo
 */
public interface IJasperServerManager {

	public DownloadModel getDownloadModel(String key);
	
	public Map<String, DownloadModel> getDownloadModelsMap();
	
	/**
	 * Effettua un login e restituisce la stringa che deve essere utilizzata nell'header nelle chiamate successive.<br/>Es:<br/>
	 * <code>method.addRequestHeader("Cookie", cookieHeader);</code>
	 * @param userDetails
	 * @return l'header oppure null in caso di errata autenticazione
	 * @throws ApsSystemException
	 */
	public String getCookieHeader(UserDetails userDetails) throws ApsSystemException;


	/**
	 * Search resources with rest api (/rest/resources)
	 * The resources service is a read only service. Requests for PUT, POST, and DELETE operations receive the error 405, method not allowed
	 * Eg: /rest/resources/path/to/folder/
	 * 
	 * @param userDetails User 
	 * @param query (optional)Match only resources having the specified text in the name or description 
	 * @param type (optional) Match only resources of the given type. See {@link WsTypeParams} 
	 * @param folder (optional) starting point 
	 * @param recursive Search for resources recursively and not only in the specified folder. This 
	 * parameter is used only when a search criteria is specified (either query or type).
	 * When not specified, the default is false, meaning only in the specified folder.
	 * 
	 * 
	 * @return List of {@link JasperResourceDescriptor}
	 * @throws ApsSystemException
	 */
	public List<JasperResourceDescriptor> searchRestResources(String cookieHeader, String query, String type, String folder, boolean recursive) throws ApsSystemException;

	
	/**
	 * Search resource with rest api (/rest/resource)
	 * <b>Form JasperReports-Server-Web-Services-Guide.pdf</b><br />
	 * A JasperReport is a complex resource that contains many parts such as a data source, input controls, and file resources. <br />
	 * These can be either references to other resources in the repository or resources that are fully defined internally to the report.<br /> 
	 * The reportUnit, which is the container for all the resources of the report. 
	 * The data source, which is an external link to a data source in the repository.
	 * The main JRXML, which is a file defined internally to this resource.
	 * Two image files, one of which is defined internally to this resource, the other references a file resource in the repository.
	 * @param userDetails
	 * @param uriString
	 * @return
	 * @throws ApsSystemException
	 */
	public JasperResourceDescriptor getRestResource(String cookieHeader, String uriString) throws ApsSystemException;
	

	/**
	 * Run a report using the restV2 api.<br />
	 * Eg: http://[host]:[port]/jasperserver[-pro]/rest_v2/reports/path/to/report.[format]?[arguments]<br />
	 * The new v2/reports service allows clients to receive report output in a single request-response. 
	 * The output format is specified in the URL as a file extension to the report URI.
	 * 
	 * @param cookieHeader
	 * @param path uriString
	 * @param format 
	 * @param arguments an {@link InputControlConfig} expression
	 * @return
	 * @throws ApsSystemException
	 */
	public InputStream runReportRestV2(String cookieHeader, String path, String format, String arguments) throws ApsSystemException;


	/**
	 * Run a report using the rest api.<br />
	 * Eg: http://<host>:<port>/jasperserver[-pro]/rest/report/path/to/report<br />
	 * The report service uses a combination of the PUT, GET, and POST methods to run reports and make them available in multiple ways through the API
	 * <li>The PUT method generates the report in any number of formats, stores the output in the session, and returns an identifier.</li>
	 * <li>The GET method with the identifier and file ID downloads any one of the file outputs.</li>
	 * <li>The POST method can be used to regenerate the report, for example in different formats, and supports page-by-page downloading of the PDF output.</li>
	 * 
	 * @param userDetails
	 * @param path uriString
	 * @param format The format of the report output. Possible values: PDF, HTML, XLS, RTF, CSV,XML, JRPRINT. The Default is PDF.
	 * @param imagesUri Questo parametro determina la directory di salvataggio dell'immagine! The uri prefix used for images when exporting in HTML. The default is images/
	 * @param page (optional) An integer value used to export a specific page
	 * @param ignorePagination (optional) When true, the report output will be generated on a single page in all export formats. When false or omitted, all export formats will be paginated
	 * @param arguments TODO TODO
	 * @return
	 * @throws ApsSystemException
	 */
	public JasperRunReportResponse runReportRest(String cookieHeader, String path, String format, String imagesUri, Integer page, Boolean ignorePagination, Object arguments) throws ApsSystemException;
	
//	/**
//	 * Download into a specified folder all the files. The direcory path is: REPORT_OUT_DIR/USERNAME/UUID
//	 * @param userDetails
//	 * @param jasperRunReportResponse
//	 * @return
//	 * @throws ApsSystemException
//	 */
//	public String downloadReportFiles(UserDetails userDetails, JasperRunReportResponse jasperRunReportResponse) throws ApsSystemException;
	
	@Deprecated
	public String getReportFileAsString(String cookieHeader, String uuid, String file) throws ApsSystemException;

	public InputStream getReportFileAsStream(String cookieHeader, String uuid, String file) throws ApsSystemException;
	
	
	
//    /**
//     * login to jasperserver
//     *
//     * @param UserDetails the UserDetails (username and password)
//     * @throws ApsSystemException in case of error
//     */
//	@Deprecated
//    public String jasperServerLogin(UserDetails currentUser) throws ApsSystemException;
//
//    /**
//     * Return the reportlist that currentUser can execute
//     * @param currentUser the UserDetails (username and password)
//     * @param subFolder the subfolder to navigate by default /
//     * @param resourceType the resource type ot be listed by default reportUnit
//     * @param isRecursive do we need to navigate also subfolder default true
//     * @return the list of reports
//     * @throws ApsSystemException
//     */
//	@Deprecated
//    public List<JasperReportModel> jasperListRepository(UserDetails currentUser, String subFolder, String resourceType, boolean isRecursive) throws ApsSystemException;

    /**
     * return the jasperserver base url
     *
     * @return
     * @throws ApsSystemException
     */
    public String getJasperBaseUrl() throws ApsSystemException;



//    /**
//     * Execute a report
//     * @param currentUser
//     * @param uriString
//     * @return
//     * @throws ApsSystemException 
//     */
//    public int jasperRunReport(UserDetails currentUser, JasperReportModel uriString, HttpSession sessionId) throws ApsSystemException;


    /**
     * 
     * @param cookieHeader
     * @param uriString
     * @param params (opzionale)
     * @return
     * @throws ApsSystemException
     */

    public List<InputControl> getInputControlsV2(String cookieHeader, String uriString, String[][] params) throws ApsSystemException;


    /**
     * Effettua una ricerca e verifica che il risultato xml inizi con <code>&lt;resourceDescriptors&gt;</code>.
     * In caso contratio il cookie non è più valido
     * @param cookieHeader
     * @return
     */
	public boolean ping(String cookieHeader) throws ApsSystemException;

	/**
	 * Get an image of a report 
	 * @param cookieHeader
	 * @param path image path
	 * @return
	 * @throws ApsSystemException
	 */
	public InputStream getReportImageV2(String cookieHeader, String path) throws ApsSystemException;

	public JasperConfig getJasperConfig();

	public void updateJasperConfig(JasperConfig config) throws ApsSystemException;
		
}

