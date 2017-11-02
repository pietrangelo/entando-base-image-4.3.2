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
package org.entando.entando.plugins.jpjasper.aps.services.model;

/**
 * Holds WsType Params constants
 *
 */
public interface WsTypeParams {

	/**
	 * Data source of type Spring bean.
	 */
	public static final String BEAN = "bean";

	/**
	 * The output of a report.
	 */
	public static final String CONTENTRESOURCE = "contentResource"; 

	/**
	 * Generic data source. This type is normally used for a data source ReportUnit child resource when it is not defined locally to the ReportUnit.
	 */
	public static final String DATASOURCE = "datasource"; 

	/**
	 * Datatype (used with the input controls)
	 */
	public static final String DATATYPE = "dataType"; 

	/**
	 * Folder
	 */
	public static final String FOLDER = "folder"; 

	/**
	 * Font file (normally a True Type font)
	 */
	public static final String FONT = "font"; 

	/**
	 * Image file
	 */
	public static final String IMG = "img"; 

	/**
	 * Input control
	 */
	public static final String INPUT_CONTROL = "inputControl";

	/**
	 * JAR file
	 */
	public static final String JAR = "jar";

	/**
	 * Data source of type JDBC
	 */
	public static final String JDBC = "jdbc";

	/**
	 * Data source of type JNDI
	 */
	public static final String JNDI = "jndi";

	/**
	 * JRXML source file
	 */
	public static final String JRXML = "jrxml";

	/**
	 * List of values (used with input controls)
	 */
	public static final String LOV = "lov";

	/**
	 *  OLAP Mondrian connection. A direct connection to an OLAP source.
	 */
	public static final String OLAP_MONDRIAN_CON = "olapMondrianCon";

	/**
	 * OLAP Mondrian Schema
	 */
	public static final String OLAP_MONDRIAN_SCHEMA = "olapMondrianSchema";

	/**
	 * OLAP XMLA connection. A remote connection to an OLAP source.
	 */
	public static final String OLAP_XMLA_CON = "olapXmlaCon";

	/**
	 * Resource bundle file (ending with .properties) for specific reports
	 */
	public static final String PROP = "prop";

	/**
	 * Query used to retrieve data from a data source
	 */
	public static final String QUERY = "query";

	/**
	 * Reference to another resource. References are only present in report units
	 */
	public static final String REFERENCE = "reference";

	/**
	 * A complete report that can be run in JasperReports Server
	 */
	public static final String REPORT_UNIT = "reportUnit";

	/**
	 * XML/A Connection
	 */
	public static final String XMLA_CONNECTION = "xmlaConnection";

	/**
	 * Dashboard
	 */
	public static final String DASHBOARD = "dashboard";
}
