INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_CONNECTION_ERROR', 'it', 'Errore in connessione SugarCRM');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_CONNECTION_ERROR', 'en', 'Error connecting SugarCRM');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_ERROR_NO_IFRAME', 'it', 'iframe not available');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_ERROR_NO_IFRAME', 'en', 'iframe not available');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_USER_NOT_ALLOWED', 'it', 'Utente non autorizzazto all''accesso a SugarCRM');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_USER_NOT_ALLOWED', 'en', 'User not allowed');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_EXECUTE_REDIRECT', 'it', 'SugarCRM Entry Point');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_EXECUTE_REDIRECT', 'en', 'SugarCRM Entry Point');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_FULLNAME', 'it', 'name');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_FULLNAME', 'en', 'name');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_EMAIL', 'it', 'email');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_EMAIL', 'en', 'email');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_MODIFIED', 'it', 'data modifica');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsugarcrm_LEAD_MODIFIED', 'en', 'modified');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
	VALUES ('jpsugarcrm_iframe','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">IFRAME</property>
<property key="it">IFRAME</property>
</properties>','<config>
	<parameter name="height">Pixel or percent height of iframe</parameter>
	<action name="configSimpleParameter" />
</config>','jpsugarcrm' , NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
	VALUES ('jpsugarcrm_redirect','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Redirect Link</property>
<property key="it">Redirect Link</property>
</properties>', NULL,'jpsugarcrm','formAction','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsugarcrm/introRedirect</property>
</properties>', 1, 'free');


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsugarcrm_lead_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Leads List</property>
<property key="it">Lista Leads</property>
</properties>', '<config>
	<parameter name="maxResults">Max results</parameter>
	<action name="jpsugarcrmLeadListConfig"/>
</config>', 'jpsugarcrm', NULL, NULL, 1, NULL);
