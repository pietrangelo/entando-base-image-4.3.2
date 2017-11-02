INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpmultisiteMultisite', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Multisite</property>
<property key="it">Pubblica Multisite</property>
</properties>', '<config>
	<parameter name="code">code</parameter>
	<action name="jpmultisiteMultisiteConfig"/>
</config>','jpmultisite', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpmultisiteMultisite_list_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Multisite List and Form</property>
<property key="it">Lista e Form Multisite</property>
</properties>', NULL, 'jpmultisite', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/jpmultisite/Multisite/list.action</property>
</properties>', 1, 'free');


INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('jpmultisite_home', 'Multisite', '<frames>
	<frame pos="0">
		<descr>Main Navigation</descr>
	</frame>
	<frame pos="1">
		<descr>Small Column I</descr>
	</frame>
	<frame pos="2">
		<descr>Small Column II</descr>
	</frame>
	<frame pos="3">
		<descr>Small Column III</descr>
	</frame>
	<frame pos="4">
		<descr>Small Column IV</descr>
	</frame>
	<frame pos="5" main="true">
		<descr>Main Column I</descr>
	</frame>
	<frame pos="6">
		<descr>Main Column II</descr>
	</frame>
	<frame pos="7">
		<descr>Main Column III</descr>
	</frame>
	<frame pos="8">
		<descr>Main Column IV</descr>
	</frame>
	<frame pos="9">
		<descr>Main Column V</descr>
	</frame>
	<frame pos="10">
		<descr>Footer</descr>
	</frame>
</frames>', 'jpmultisite');
