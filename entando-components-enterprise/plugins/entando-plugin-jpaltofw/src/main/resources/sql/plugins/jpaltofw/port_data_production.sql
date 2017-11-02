INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpaltofw_instances', 'Alto Framework Instances', '<instances>
	<instance>
		<id>alto1</id>
		<baseUrl>http://localhost:8081/alto/</baseUrl>
		<pid>alto</pid>
		<projectPassword>adminadmin</projectPassword>
	</instance>
</instances>');




INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpaltofw_widget', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Publish an Alto Widget</property><property key="it">Pubblica un widget Alto</property></properties>
', '<config>
	<parameter name="baseUrl">The BaseURL of Alto server</parameter>
	<parameter name="pid">The PID of Alto instance</parameter>
	<parameter name="widgetCode">The Widget Code - code = name + ''_'' + type</parameter>
	<action name="gokibiWidgetConfig"/>
</config>', 'jpaltofw', NULL, NULL, 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpaltofw_administration', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Alto Administration</property>
<property key="it">Alto Administration</property>
</properties>', '<config>
	<parameter name="baseUrl">The BaseURL of Alto server instance (with context name)</parameter>
	<action name="configSimpleParameter"/>
</config>', 'jpaltofw', NULL, NULL, 1, 'free');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpaltofw_widget', 'jpaltofw_widget', 'jpaltofw', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign jpgk=JspTaglibs["/jpgokibi-aps-core"]>
<@wp.currentWidget param="config" configParam="baseUrl" var="gokibiBaseUrlVar" />
<#assign gokibiScriptVar>
	$(document).ready(function() {
	alto_init("<@c.out value="${gokibiBaseUrlVar}" />", "<@wp.currentWidget param="config" configParam="pid" />",  "<#if (Session.currentUser.entandoUser) >token<#else>${Session.currentUser.token}</#if>");
	});
</#assign>
<@wp.headInfo type="JS_RAW" var="gokibiScriptVar" />
<#--
<@c.set var="gokibiExtCssVar"><@c.out value="${gokibiBaseUrlVar}" />/static/assets/vendor/bootstrap/css/bootstrap.min.css</@c.set>
<@wp.headInfo type="CSS_EXT" var="gokibiExtCssVar" />
-->
<#assign gokibiExtJsVar><@c.out value="${gokibiBaseUrlVar}" />/static/js/alto_api.js</#assign>
<@wp.headInfo type="JS_EXT" var="gokibiExtJsVar" />
<@jpgk.gokibiWidget escapeXml=false />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpaltofw_administration', 'jpaltofw_administration', 'jpaltofw', '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<@wp.currentWidget param="config" configParam="baseUrl" var="gokibiBaseUrlVar" />
<div id="gokibi">
	&nbsp;</div>
<script type="text/javascript" src="<@c.out value="${gokibiBaseUrlVar}" />/static/connector/gokibiconnector.js?host=<@c.out value="${gokibiBaseUrlVar}" />&canvas=gokibi"></script>', NULL, 0);