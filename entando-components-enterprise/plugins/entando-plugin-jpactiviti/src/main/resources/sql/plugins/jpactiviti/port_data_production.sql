INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpactiviti_ERROR_NO_IFRAME', 'it', 'iframe not available');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpactiviti_ERROR_NO_IFRAME', 'en', 'iframe not available');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES (
'jpactiviti_iframe',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - IFRAME</property>
		<property key="it">Activiti - IFRAME</property>
	</properties>',
'<config>
	<parameter name="height">Pixel or percent height of iframe</parameter>
	<parameter name="section">Section of activiti-explorer</parameter>
	<action name="activitiIFrameConfig" />
</config>',
'jpactiviti',
NULL,
NULL,
1,
'free');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES(
'jpactiviti_tasks',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - Task list</property>
		<property key="it">Activiti - Lista di task</property>
	</properties>',
NULL,
'jpactiviti',
NULL,
NULL,
1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES(
'jpactiviti_processes',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - Process list</property>
		<property key="it">Activiti - Lista dei processi</property>
	</properties>',
NULL,
'jpactiviti',
NULL,
NULL,
1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES(
'jpactiviti_processDefinitionList',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - Process Definition List</property>
		<property key="it">Activiti - Process Definition List</property>
	</properties>',
NULL,
'jpactiviti',
NULL,
NULL,
1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES(
'jpactiviti_historicProcessInstanceList',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - Historic Process Instance List</property>
		<property key="it">Activiti - Historic Process Instance List</property>
	</properties>',
NULL,
'jpactiviti',
NULL,
NULL,
1);

INSERT INTO widgetcatalog ( code, plugincode, titles, parenttypecode, defaultconfig, locked ) VALUES ( 
'jpactiviti_task_entry', 
'jpactiviti', 
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="en">Activiti - Task Entry</property>
		<property key="it">Activiti - Task Entry</property>
	</properties>',
'formAction',
'<?xml version="1.0" encoding="UTF-8"?>
	<properties>
		<property key="actionPath">/ExtStr2/do/jpactiviti/Front/Task/introTaskEntry.action</property>
	</properties>',
1 );