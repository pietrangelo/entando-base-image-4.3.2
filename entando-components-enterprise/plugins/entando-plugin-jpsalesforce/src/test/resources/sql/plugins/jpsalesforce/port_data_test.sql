INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup)
    VALUES ('jpsalesforce_publish_form','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Salesforce connector - Publish a form</property>
<property key="it">Connettore Salesforce - Pubblica un form</property>
</properties>','<config>
	<parameter name="campaignId">
		Campaign ID
	</parameter>
	<parameter name="profileId">
		Profile ID
	</parameter>
	<action name="salesforceFormConfig"/>
</config>',null,null,null,1,null);

INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('test', 'entandoSubscriptionConfig', 'The component installation report',
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
 
 INSERT INTO sysconfig(
            version, item, descr, config)
    VALUES ('test','jpsalesforce_config','Salesforce configuration','<config>
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