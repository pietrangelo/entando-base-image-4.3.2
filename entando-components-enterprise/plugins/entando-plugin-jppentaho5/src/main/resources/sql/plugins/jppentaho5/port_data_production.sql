INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
	VALUES ('jppentaho5_publish_legacy_report', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho 5 - Embed a WCDF or XCDF dashboard or a legacy XACTION</property>
<property key="it">Pentaho 5 - Incorpora una dashboard WCDF o XCDF o un report XACTION</property>
</properties>', '<config>
	<parameter name="pathParam">Report path</parameter>
	<parameter name="argsParam">Arguments list (CSV)</parameter>
	<parameter name="cssClassParam">CSS class name</parameter>
	<action name="legacyReportConfig" />
</config>', 'jppentaho', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
	VALUES ('jppentaho5_publish_report', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho 5 - Publish a PRPT report</property>
<property key="it">Pentaho 5 - Pubblica un report PRPT</property>
</properties>', '<config>
	<parameter name="pathParam">Report path</parameter>
	<parameter name="cssClassParam">CSS class name</parameter>
	<parameter name="argsParam">Arguments list (CSV)</parameter>
	<parameter name="countParam">Dynamic parameters</parameter>
	<action name="reportConfig" />
</config>', 'jppentaho', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
	VALUES ('jppentaho5_download_report', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho 5 - Download a PRPT report</property>
<property key="it">Pentaho 5 - Scarica un report PRPT</property>
</properties>', '<config>
	<parameter name="pathParam">Report path</parameter>
	<parameter name="argsParam">Arguments list (CSV)</parameter>
	<parameter name="countParam">Dynamic parameters</parameter>
	<action name="downloadReportConfig" />
</config>', 'jppentaho', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
	VALUES ('jppentaho5_report_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho 5 - Display only the form of a PRPT report</property>
<property key="it">Pentaho 5 - Visualizza unicamente il form di un report PRPT</property>
</properties>', '<config>
	<parameter name="pathParam">Report path</parameter>
	<parameter name="argsParam">Arguments CSV</parameter>
	<action name="formReportConfig" />
</config>', 'jppentaho', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
	VALUES ('jppentaho5_display_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho 5 - Display a PRPT report without form</property>
<property key="it">Pentaho 5 - Visualizza un report PRPT report senza alcun form</property>
</properties>', '<config>
	<parameter name="pathParam">Report path</parameter>
	<parameter name="cssClassParam">CSS class name</parameter>
	<action name="displayReportConfig" />
</config>', 'jppentaho', NULL, NULL, 1, 'free');

INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('production', 'jppentaho5_config', 'Pentaho 5 plugin configuration', 
'<?xml version="1.0" encoding="UTF-8"?>
<pentaho_config>
   <username>admin</username>
   <pentahoInstance>http://192.168.26.82:8080/pentaho</pentahoInstance>
   <casAuthentication>false</casAuthentication>
   <password>password</password>
</pentaho_config>');
