INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoDynamicReports', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="en">Pentaho - Navigable Solution</property>
	<property key="it">Pentaho - Solution Navigabile</property>
</properties>', '<config>
	<parameter name="solution">Pentaho Solution id</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoDynamicSingleReport', '<?xml version="1.0" encoding="UTF-8"?><properties>
	<property key="en">Pentaho - Configurable Report</property>
	<property key="it">Pentaho - Report Configurable</property>
</properties>', '<config>
	<parameter name="solution">Pentaho Solution id</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoReportDetail', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="en">Pentaho - Report Details</property>
	<property key="it">Pentaho - Dettaglio di un Report</property>
</properties>', '<config>
	<parameter name="solution">Pentaho Solution id</parameter>
	<parameter name="path">Path id</parameter>
	<parameter name="name">il nome del report</parameter>
	<parameter name="queryString">Report params (a query string in url format, example: param1=value1&amp;param2=value2&amp;param3=value3)</parameter>
	<parameter name="profileParams">i parametri per il report stile url con reporParam1=profileAttributeName1&amp;reporParam2=profileAttributeName2&amp;reporParam3=profileAttributeName3</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoLockedListReportDetails', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="en">Pentaho - Reports List preview</property>
	<property key="it">Pentaho - Lista di Report anteprima</property>
</properties>', '<config>
	<parameter name="solution">Pentaho Solution id</parameter>
	<parameter name="path">Path id</parameter>
	<parameter name="queryString">Report params (a query string in url format, example: param1=value1&amp;param2=value2&amp;param3=value3)</parameter>
	<parameter name="profileParams">Report params from the User Profile (a query string in url format, example: reporParam1=profileAttributeName1&amp;reporParam2=profileAttributeName2&amp;reporParam3=profileAttributeName3</parameter>
	<parameter name="refreshPeriod">Refresh period</parameter>
	<parameter name="refreshPeriodUnitsOfMeasurement">Units of measurement of refresh period. Can be m (minutes), h (hours), d (days), M (months), y (years)</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoLockedListReportDownload', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="en">Pentaho - Reports List for download</property>
	<property key="it">Pentaho - Lista di Report per download</property>
</properties>', '<config>
	<parameter name="solution">Pentaho Solution id</parameter>
	<parameter name="path">Path id</parameter>
	<parameter name="queryString">Report params (a query string in url format, example: param1=value1&amp;param2=value2&amp;param3=value3)</parameter>
	<parameter name="profileParams">Report params from the User Profile (a query string in url format, example: reporParam1=profileAttributeName1&amp;reporParam2=profileAttributeName2&amp;reporParam3=profileAttributeName3</parameter>
	<parameter name="refreshPeriod">Refresh period</parameter>
	<parameter name="refreshPeriodUnitsOfMeasurement">Units of measurement of refresh period. Can be m (minutes), h (hours), d (days), M (months), y (years)</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jppentahoReportPreview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Pentaho - Report Preview</property>
<property key="it">Pentaho - Anteprima di un Report</property>
</properties>
', '<config>
	<parameter name="solution">Solution</parameter>
	<parameter name="previewPath">Preview Path</parameter>
	<parameter name="previewName">Preview report name</parameter>
	<parameter name="detailPath">Detail Path</parameter>
	<parameter name="detailName">Detail report name</parameter>
	<parameter name="queryString">Report params (a query string in url format, example: param1=value1&amp;param2=value2&amp;param3=value3)</parameter>
	<parameter name="profileParams">i parametri per il report stile url con reporParam1=profileAttributeName1&amp;reporParam2=profileAttributeName2&amp;reporParam3=profileAttributeName3</parameter>
	<parameter name="refreshPeriod">Refresh period</parameter>
	<parameter name="refreshPeriodUnitsOfMeasurement">Units of measurement of refresh period. Can be m (minutes), h (hours), d (days), M (months), y (years)</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jppentaho', NULL, NULL, 1, 'intranet');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_REPORTS_LIST', 'it', 'Lista Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_REPORTS_LIST', 'en', 'Reports List');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_NOREPORT_FOR_SOLUTION', 'it', 'Nessun report per la cartella scelta, oppure nessuna cartella è stata selezionata.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_NOREPORT_FOR_SOLUTION', 'en', 'No report for this folder or no folder selected.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHOOSE_A_REPORT', 'it', 'Seleziona un report dalla solution');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHOOSE_A_REPORT', 'en', 'Choose a report from the solution');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_SAVE', 'it', 'Seleziona');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_SAVE', 'en', 'Select');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_PATH_SAVED_OK', 'it', 'Cartella selezionata correttamente.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_PATH_SAVED_OK', 'en', 'Folder was successfully selected.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_BACK_TO_REPORTS', 'it', 'Vai alla Lista dei Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_BACK_TO_REPORTS', 'en', 'Go to the Reports List');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_WAS_SELECTED', 'it', 'È stato selezionato');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_WAS_SELECTED', 'en', 'You have selected');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_GET_THE_REPORT', 'it', 'Elabora il report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_GET_THE_REPORT', 'en', 'Get the report');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_TITLE_CONFIGURE_REPORT', 'it', 'Configura il Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_TITLE_CONFIGURE_REPORT', 'en', 'Configure Report');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_BUTTON_CHOOSE', 'it', 'Scegli');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_BUTTON_CHOOSE', 'en', 'Choose');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_LABEL_REPORT', 'it', 'Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_LABEL_REPORT', 'en', 'Report Name');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_LABEL_OUTPUTTYPE', 'it', 'Tipo di output');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_LABEL_OUTPUTTYPE', 'en', 'Output Format');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHANGEREPORT', 'it', 'Cambia Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHANGEREPORT', 'en', 'Change Report');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_PLEASE_LOGIN', 'it', 'Per accedere a questa funzionalità è necessario effettuare l''accesso.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_PLEASE_LOGIN', 'en', 'Please login.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_BACKTO_THE_REPORT', 'it', 'Torna al Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_BACKTO_THE_REPORT', 'en', 'Back to the Report');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_EXECUTE', 'it', 'Esegui');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_EXECUTE', 'en', 'Execute');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_ERROR', 'it', 'Si è verificato un errore durante l''elaborazione del report.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_ERROR', 'en', 'There was an error while processing the report.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CONFIGURE_PATH', 'it', 'Cambia Cartella');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CONFIGURE_PATH', 'en', 'Change Folder');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_CONFIGURE_PATH', 'it', 'Seleziona una cartella dalla lista. Cliccando è possibile sfogliare i sottolivelli e vedere i report presenti.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_CONFIGURE_PATH', 'en', 'Choose a Folder from the list below. Browse the tree, click to open and close the branches and see the reports in that folder.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_CONFIGURE_REPORT', 'it', 'Scegli un report dalla lista. Cliccando è possibile navigare l''albero delle Cartelle e vedere i report presenti.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_MSG_CONFIGURE_REPORT', 'en', 'Choose a report from the list below. Clicking it''s possible to navigate the Folder tree and see reports inside the folders.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHANGE_PATH', 'it', 'Cambia Cartella');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_CHANGE_PATH', 'en', 'Change Folder');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_TITLE_CONFIGURE_PATH', 'it', 'Seleziona una cartella');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_TITLE_CONFIGURE_PATH', 'en', 'Choose a folder');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_GODETAILS', 'it', 'visualizza i dettagli');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_GODETAILS', 'en', 'Details');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_REPORTS', 'it', 'Report');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jppentaho_REPORTS', 'en', 'Reports');

INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('production', 'jppentaho_config', 'Pentaho config', '<pentahoConfig />');