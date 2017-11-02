INSERT INTO sysconfig(
            version, item, descr, config)
    VALUES ('production','jpsalesforce_config','Salesforce configuration','<config>
	<username></username>
	<oid></oid>
	<app_secret></app_secret>
	<app_id></app_id>
	<auth_url_token>https://login.salesforce.com/services/oauth2/token</auth_url_token>
	<auth_url_new>https://login.salesforce.com/services/oauth2/authorize</auth_url_new>
	<api_version>24.0</api_version>
	<security_token></security_token>
	<password></password>
</config>');

INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('production', 'entandoSubscriptionConfig', 'The component installation report', 
'<subscriptionConfig>
	<userProfile typeCode="PFL" redirectPageCode="download" />
	<activation>
		<template lang="it">
			<subject>[entando.com] : Activate your account</subject>
			<body><![CDATA[Dear, 
thank you for subscribing. To activate your account simply click on the following link: 
{link}
Enjoy!.]]></body>
		</template>
		<template lang="en">
			<subject>[entando.com] : Activate your account</subject>
			<body><![CDATA[Dear, 
thank you for subscribing. To activate your account simply click on the following link: 
{link}
Enjoy!.]]></body>
		</template>
	</activation>
 </subscriptionConfig>');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
    VALUES ('jpsalesforce_publish_form','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Salesforce connector - Publish a form</property>
<property key="it">Connettore Salesforce - Pubblica un form</property>
</properties>','<config>
	<parameter name="formId">Form configuration ID</parameter>
	<parameter name="typeCode">Code of the Message Type</parameter>
	<parameter name="formProtectionType">Protection type of the form</parameter>
	<action name="salesforceFormConfig"/>
</config>','jpsalesforce',null,null,1,null);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpsalesforce_login', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Salesforce connector - Login</property><property key="it">Connettore salesforce - Login</property></properties>',
 null, 'jpsalesforce', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/salesforce/intro.action</property>
</properties>',1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpsalesforce_lead_search', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Salesforce connector - Leads search</property><property key="it">Connettore salesforce - Ricerca Lead</property></properties>',
 null, 'jpsalesforce', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/salesforce/leads/intro.action</property>
</properties>',1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpsalesforce_lead_new', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Salesforce connector - Create a new Lead</property><property key="it">Connettore salesforce - Creazione lead</property></properties>',
 null, 'jpsalesforce', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/salesforce/leads/new.action</property>
</properties>',1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
    VALUES ('jpsalesforce_message_form','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Salesforce connector - Publish an existing form</property>
<property key="it">Connettore Salesforce - Pubblica un form esistente</property>
</properties>','<config>
	<parameter name="formId">Form configuration ID</parameter>
	<parameter name="typeCode">Code of the Message Type</parameter>
	<parameter name="formProtectionType">Protection type of the form</parameter>
	<action name="salesforceFormChoose"/>
</config>','jpsalesforce',null,null,1,null);

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGIN_OK', 'en', 'Signed in');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGIN_OK', 'it', 'Connesso');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGOUT', 'en', 'Sign out');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGOUT', 'it', 'Esci');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_TITLE_LOGIN', 'en', 'Salesforce Connection');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_TITLE_LOGIN', 'it', 'Connessione con Salesforce');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGIN_NOW', 'it', 'Connettiti a Salesforce');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LOGIN_NOW', 'en', 'Sign in to Salesforce');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_EDIT','en','Edit Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_EDIT','it','Modifica Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_INFO','en','Lead Details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_INFO','it','Dettaglio Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_SAVE','en','Save');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_SAVE','it','Salva');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_NOT_LOGGED','en','Sign in to Salesforce');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_NOT_LOGGED','it','Connettiti a Salesforce');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_NEW','en','New Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_NEW','it','Nuovo Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_SEARCH','en','Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_SEARCH','it','Cerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_SEARCH_TITLE','en','Leads List');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_SEARCH_TITLE','it','Lista Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_SEARCH_NO_RESULT','en','No lead found.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_SEARCH_NO_RESULT','it','Nessun lead trovato.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_GOTO_LIST','en','Go to list');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_GOTO_LIST','it','Vai alla lista');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_VIEW','en','Details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_VIEW','it','Dettagli');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_DELETE_CONFIRM','en','Are you sure you want to delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_DELETE_CONFIRM','it','Sicuro di voler rimuovere');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_DELETE','en','Delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_DELETE','it','Rimuovi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_EMPTYFIELD','en','Empty field');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_EMPTYFIELD','it','Campo vuoto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_NAME','en','Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_NAME','it','Nome');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_COMPANY','en','Company');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_COMPANY','it','Azienda');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTIONS','en','Actions');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTIONS','it','Azioni');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_TRASH','en','Delete Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_LEAD_TRASH','it','Rimuovi Lead');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTION_ERRORS','en','Error');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTION_ERRORS','it','Si sono verificati degli errori');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_ERRORS','en','Error');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_FIELD_ERRORS','it','Si sono verificati degli errori nell''inserimento dei dati');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTION_MESSAGES','en','Info');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsalesforce_ACTION_MESSAGES','it','Info');