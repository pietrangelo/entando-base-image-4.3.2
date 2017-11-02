package org.entando.entando.plugins.jppentaho5.aps.system;

public interface PentahoSystemConstants {

	public static final String CONFIG_REPORT_PATH = "pathParam";
	public static final String CONFIG_REPORT_PARAM_PREFIX = "outputParam";
	public static final String CONFIG_REPORT_ARGS = "argsParam";
	@Deprecated
	public static final String CONFIG_REPORT_WIDTH = "widthParam";
	@Deprecated
	public static final String CONFIG_REPORT_LENGTH = "lengthParam";
	public static final String CONFIG_REPORT_PARAMCOUNT = "countParam";
	public static final String CONFIG_ARG_NONE = "none";
	public static final String CONFIG_CSS_CLASS = "cssClassParam";
	
	// Separator used by API for path
	public static final String PENTAHO_PATH_SEPARATOR = ":";
	
	// parameter for the report output
	public final static String PENTAHO_OUTPUT_PARAMETER = "output-target";
	
	// fields common to every report for Pentaho 5.0.1 stable
	public static final String COMMON_REPORT_PARAMETERS[] = {
			"printer-name",
			PENTAHO_OUTPUT_PARAMETER,
			"autoSubmitUI",
			"id",
			"autoSubmit",
			"dashboard-mode",
			"::TabActive",
			"renderMode",
			"layout",
			"path",
			"htmlProportionalWidth",
			"ignoreDefaultDates",
			"accepted-page",
			"content-handler-pattern",
			"output-type",
			"print",
			"showParameters",
			"::TabName",
			"::cl",
			"::session",
			"yield-rate",
			"paginate"
			};
	
	// PARTIAL value that the parameter 'output-target' assumes when displaying an HTML form
	public static final String PENTAHO_OUTPUT_PARAMETER_VALUE_HTML = "table/html;page-mode=";
	
	public static final String JPPENTAHO5_CONFIG_ITEM = "jppentaho5_config";

}
