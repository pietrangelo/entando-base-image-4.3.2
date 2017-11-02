INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('constantContactInternalServlet', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Constant Contant Contacts</property>
<property key="it">Constant Contant Contatti</property>
</properties>', NULL, 'jpconstantcontact', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Ctct/list.action</property>
</properties>', 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('ctctInternalServletCampaigns', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Constant Contant Campaigns</property>
<property key="it">Constant Contant Campagne</property>
</properties>', NULL, 'jpconstantcontact', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Ctct/listCampaigns.action</property>
</properties>', 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('ctctInternalServletReports', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Constant Contant Campaign Reports</property>
<property key="it">Constant Contant Rapporti Campagne</property>
</properties>', NULL, 'jpconstantcontact', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Ctct/reports.action</property>
</properties>', 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('ctctInternalServletEvents', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Constant Contant Events</property>
<property key="it">Constant Contant Eventi</property>
</properties>', NULL, 'jpconstantcontact', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Ctct/listEvents.action</property>
</properties>', 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('ctctInternalServletEventReports', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Constant Contant Events Reports</property>
<property key="it">Constant Contant Rapporti Eventi</property>
</properties>', NULL, 'jpconstantcontact', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Ctct/eventReports.action</property>
</properties>', 1, 'free');
INSERT INTO sysconfig VALUES ('production', 'jpconstantcontact_config', 'Constant Contact configuration', '<?xml version="1.0" encoding="UTF-8"?>
<constantContact>
<apikey></apikey>
<consumerSecret></consumerSecret>
<token></token>
<contactPage></contactPage>
<contactFrame></contactFrame>
<campaignPage></campaignPage>
<campaignFrame></campaignFrame>
<eventPage></eventPage>
<eventFrame></eventFrame>
</constantContact>');


INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_DRAFT','en','DRAFT');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_DRAFT','it','BOZZA');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_ACTIVE','en','ACTIVE');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_ACTIVE','it','ATTIVO');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CANCELLED','en','CANCELLED');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CANCELLED','it','CANCELLATO');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_COMPLETE','en','COMPLETED');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_COMPLETE','it','COMPLETATO');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CONTACTLISTS','en','Contact lists');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CONTACTLISTS','it','Liste di Contatti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CAMPAIGNS','en','Campaigns');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CAMPAIGNS','it','Campagne');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_EVENTS','en','Events');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_EVENTS','it','Eventi');


INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CONTACTDETAILS','en','Contact details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CONTACTDETAILS','it','Dettagli Contatto');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CAMPAIGNDETAILS','en','Campaign details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_CAMPAIGNDETAILS','it','Dettagli Campagna');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_EVENTDETAILS','en','Event details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpconstantcontact_EVENTDETAILS','it','Dettagli Evento');

