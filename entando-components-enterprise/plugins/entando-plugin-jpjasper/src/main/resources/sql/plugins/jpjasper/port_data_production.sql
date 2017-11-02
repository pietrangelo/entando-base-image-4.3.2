INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('production', 'jpjasper_config', 'jasper plugin configuration', '<jpjasper><baseURL>http://localhost:7080/jasperserver-pro</baseURL></jpjasper>');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_CONNECTION_ERROR', 'it', 'Errore connessione JasperSoft');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_CONNECTION_ERROR', 'en', 'Error connecting Jasper');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_ERROR_NO_IFRAME', 'it', 'iframe not available');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_ERROR_NO_IFRAME', 'en', 'iframe not available');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_USER_NOT_ALLOWED', 'it', 'Utente non autorizzato all''accesso');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_USER_NOT_ALLOWED', 'en', 'User not allowed');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_EXECUTE_REDIRECT', 'it', 'JasperServer Entry Point');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_EXECUTE_REDIRECT', 'en', 'JasperServer Entry Point');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_NAME', 'it', 'Nome Report');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_NAME', 'en', 'Report Name');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORTS_LIST_SUMMARY', 'it', 'I Tuoi Reports');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORTS_LIST_SUMMARY', 'en', 'You Reports');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_DESCRIPTION', 'it', 'Descrizione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_DESCRIPTION', 'en', 'Description');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_PARENTFOLDER', 'it', 'Cartella');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_PARENTFOLDER', 'en', 'Folder');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_LABEL_PAGE', 'it', 'Pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_LABEL_PAGE', 'en', 'Page');


INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REFRESH_INPUT_CONTROLS', 'it', 'Aggiorna');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REFRESH_INPUT_CONTROLS', 'en', 'Refresh');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_RENDER_REPORT', 'it', 'Render');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_RENDER_REPORT', 'en', 'Render');


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_report', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Publish Report</property>
<property key="it">jpjasper - Pubblica Report</property>
</properties>', '<config>
	<parameter name="uriString">report path</parameter>
	<parameter name="inputControls">input controls</parameter>
	<parameter name="downloadFormats">download formats</parameter>
	<parameter name="title_{lang}">titles</parameter>
	<action name="jpjasperReportViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_reports_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Publish Report List</property>
<property key="it">jpjasper - Pubblica Lista Report</property>
</properties>', '<config>
	<parameter name="folder">report folder</parameter>
	<parameter name="filter">filter</parameter>
	<parameter name="recursive">recursive earch</parameter>
	<parameter name="title_{lang}">titles</parameter>
	<action name="jpjasperReportListViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog VALUES ('jpjasper_report_preview', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Publish Report Preview</property>
<property key="it">jpjasper - Pubblica Anteprima Report</property>
</properties>', '<config>
	<parameter name="uriString">report path</parameter>
	<parameter name="inputControls">input controls</parameter>

	<parameter name="uriStringDett">dett report path</parameter>
	<parameter name="inputControlsDett">dett input controls</parameter>
	<parameter name="downloadFormatsDett">download</parameter>
	<parameter name="titleDett_{lang}">titles</parameter>
	<action name="jpjasperReportPreviewViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_embeddedConfiguration', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Embedded Configuration</property>
<property key="it">jpjasper - Embedded Configuration</property>
</properties>', '<config>
	<parameter name="serverEndpoint">server Endpoint</parameter>
	<parameter name="jasperTheme">jasperTheme</parameter>
	<parameter name="frameHeight">frameHeight</parameter>
	<action name="jpjasperEmbeddedConfigViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_embeddedNewReport', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Embedded New Report</property>
<property key="it">jpjasper - Embedded New Report</property>
</properties>', '<config>
	<parameter name="serverEndpoint">server Endpoint</parameter>
	<parameter name="jasperTheme">jasperTheme</parameter>
	<parameter name="frameHeight">frameHeight</parameter>
	<action name="jpjasperEmbeddedNewReportViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpjasper_embeddedDashboard', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">jpjasper - Embedded Dashboard</property>
<property key="it">jpjasper - Embedded Dashboard</property>
</properties>', '<config>
	<parameter name="serverEndpoint">server Endpoint</parameter>
	<parameter name="jasperTheme">jasperTheme</parameter>
	<parameter name="frameHeight">frameHeight</parameter>
	<parameter name="dashboardResource">dashboardResource</parameter>
	<parameter name="extraParams">extraParams</parameter>
	<action name="jpjasperEmbeddedDashboardViewerConfig"/>
</config>', 'jpjasper', NULL, NULL, 1, NULL);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_NOREPORTS_ON_LIST', 'it', 'Nessun report trovato.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_NOREPORTS_ON_LIST', 'en', 'No report found.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_DOWNLOAD_AS', 'it', 'Scarica il report nel formato:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_DOWNLOAD_AS', 'en', 'Download as:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_GO_REPORT_DETAIL', 'it', 'Vai al dettaglio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_GO_REPORT_DETAIL', 'en', 'Go to details');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_MANDATORY_CONTROL','en','Mandatory');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_MANDATORY_CONTROL','it','Obbligatorio');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_IS_NULL','en','The searched report was not found.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpjasper_REPORT_IS_NULL','it','Il report richiesto non è stato trovato');
