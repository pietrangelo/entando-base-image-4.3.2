INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('test', 'jpjasper_config', 'jasper plugin configuration', '<jpjasper><baseURL>http://localhost:7080/jasperserver-pro</baseURL></jpjasper>');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_report', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Publish Report</property>
<property key="it">jpjasper - Pubblica Report</property>
</properties>', '<config>
	<parameter name="uriString">report path</parameter>
	<parameter name="inputControls">input controls</parameter>
	<parameter name="downloadFormats">download formats</parameter>
	<action name="jpjasperReportViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);