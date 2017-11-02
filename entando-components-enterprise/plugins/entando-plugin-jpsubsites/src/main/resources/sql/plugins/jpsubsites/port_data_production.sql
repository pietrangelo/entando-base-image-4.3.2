INSERT INTO jpsubsites_subsites (code, titles, descriptions, homepage, groupname, catcode, contentviewerpage, cssfilename, imgfilename) VALUES ('jpsubsite_template', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite Template</property><property key="it">Sottosito Template</property></properties>
', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Description of Subsite Template</property><property key="it">Descrizione del Sottosito Template</property></properties>
', 'jpsubsites_template', 'jpsubsites_template', 'jpsubsites_template', 'jpsubsite_template_news_view', 'graphic_theme/subsite1.css', 'header_subsite1.jpg');




INSERT INTO sysconfig (version, item, descr, config) values ('production', 'jpsubsites_subsiteConfig', 'Subsite Service Configuration', '<?xml version="1.0" encoding="UTF-8"?>
<subsitesConfig rootPageCode="jpsubsites" pageModel="jpsubsites_template" categoryRoot="jpsubsites" />');




INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('jpsubsites_template', 'Subsites - Default template', '<frames>
	<frame pos="0">
		<descr>Navigation Breadcrumbs</descr>
		<defaultWidget code="jpsubsites_breadcrumbs" />
	</frame>
	<frame pos="1">
		<descr>First Column: Box 1</descr>
	</frame>
	<frame pos="2">
		<descr>First Column: Box 2</descr>
	</frame>
	<frame pos="3">
		<descr>First Column: Box 3</descr>
	</frame>
	<frame pos="4">
		<descr>First Column: Box 4</descr>
	</frame>
	<frame pos="5">
		<descr>First Column: Box 5</descr>
	</frame>
	<frame pos="6" main="true">
		<descr>Main Column: Box 1</descr>
	</frame>
	<frame pos="7">
		<descr>Main Column: Box 2</descr>
	</frame>
	<frame pos="8">
		<descr>Main Column: Box 3</descr>
	</frame>
	<frame pos="9">
		<descr>Main Column: Box 4</descr>
	</frame>	
	<frame pos="10">
		<descr>Main Column: Box 5</descr>
	</frame>
</frames>', 'jpsubsites', NULL);




INSERT INTO widgetcatalog (code, plugincode, titles, parameters, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_listViewer', 'jpsubsites', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish a List of Contents</property>
<property key="it">Contenuti - Pubblica una Lista di Contenuti</property>
</properties>', '<config>
	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="modelId">Content Model</parameter>
	<parameter name="userFilters">Front-End user filter options</parameter>
	<parameter name="category">Content Category **deprecated**</parameter>
	<parameter name="categories">Content Category codes (comma separeted)</parameter>
	<parameter name="orClauseCategoryFilter" />
	<parameter name="maxElemForItem">Contents for each page</parameter>
	<parameter name="maxElements">Number of contents</parameter>
	<parameter name="filters" />
	<parameter name="title_{lang}">Widget Title in lang {lang}</parameter>
	<parameter name="pageLink">The code of the Page to link</parameter>
	<parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
	<action name="listViewerConfig"/>
</config>', NULL, NULL, 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_news_latest', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">News - Latest News</property>
<property key="it">Notizie - Ultime Notizie</property>
</properties>', NULL, 'jpsubsites', 'jpsubsites_listViewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElements">3</property>
<property key="filters">(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_it">Ultime Notizie</property>
<property key="title_en">Latest News</property>
<property key="contentType">NWS</property>
<property key="modelId">10021</property>
</properties>', 0, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_breadcrumbs', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Navigation breadcrumbs</property>
<property key="it">Briciole di Pane</property>
</properties>', NULL, 'jpsubsites', NULL, NULL, 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_intro_test_main', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Intro Main Page</property><property key="it">Intro Main Page</property></properties>
', NULL, 'jpsubsites', NULL, NULL, 0, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_intro_test_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Intro Page 1</property><property key="it">Intro Page 1</property></properties>
', NULL, 'jpsubsites', NULL, NULL, 0, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsubsites_intro_test_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Intro Page 2</property><property key="it">Intro Page 2</property></properties>
', NULL, 'jpsubsites', NULL, NULL, 0, 'free');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpsubsites_content_viewer_list', 'jpsubsites_listViewer', 'jpsubsites', NULL, '<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign jpss=JspTaglibs["/jpsubsites-core"]>
<@wp.headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js" />
<@jpss.contentList listName="contentList" titleVar="titleVar"
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar" />
<#if (titleVar??)>
	<h1><@c.out value="${titleVar}" /></h1>
</#if>
<@wp.freemarkerTemplateParameter var="userFilterOptionsVar" valueName="userFilterOptionsVar" removeOnEndTag=true >
<@wp.fragment code="jacms_content_viewer_list_userfilters" escapeXml=false />
</@wp.freemarkerTemplateParameter>
<#if (contentList??) && (contentList?has_content) && (contentList?size > 0)>
	<@wp.pager listName="contentList" objectName="groupContent" pagerIdFromFrame=true advanced=true offset=5>
		<@wp.freemarkerTemplateParameter var="group" valueName="groupContent" removeOnEndTag=true >
		<@wp.fragment code="default_pagerBlock" escapeXml=false />
<#list contentList as contentId>
<#if (contentId_index >= groupContent.begin) && (contentId_index <= groupContent.end)>
	<@jacms.content contentId="${contentId}" />
</#if>
</#list>
		<@wp.fragment code="default_pagerBlock" escapeXml=false />
		</@wp.freemarkerTemplateParameter>
	</@wp.pager>
<#else>
	<#if (userFilterOptionsVar?size > 0)>
		<p class="alert alert-info"><@wp.i18n key="LIST_VIEWER_EMPTY" /></p>
	</#if>
</#if>
<#if (pageLinkVar??) && (pageLinkDescriptionVar??)>
	<p class="text-right"><a class="btn btn-primary" href="<@wp.url page="${pageLinkVar}"/>"><@c.out value="${pageLinkDescriptionVar}" /></a></p>
</#if>
<@c.set var="contentList" scope="request" />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpsubsites_intro_test_main', 'jpsubsites_intro_test_main', 'jpsubsites', '<h1>Hello, world! Subsite Template Intro</h1>
<p>Main information of Subsite.</p>
', NULL, 0);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpsubsites_intro_test_1', 'jpsubsites_intro_test_1', 'jpsubsites', '<h1>Hello, world! Subsite Template Intro Page 1</h1>
<p>Main information of Subsite.</p>
', NULL, 0);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpsubsites_intro_test_2', 'jpsubsites_intro_test_2', 'jpsubsites', '<h1>Hello, world! Subsite Template Intro Page 2</h1>
<p>Main information of Subsite.</p>
', NULL, 0);




INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_LATEST_NEW', 'it', 'Ultime Notizie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_LATEST_NEW', 'en', 'Last News');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_COPYRIGHT', 'en', 'Copyright &copy; MyCompany 2017');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_COPYRIGHT', 'it', 'Copyright &copy; MyCompany 2017');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_CREDITS_POWERED_BY', 'en', '<span lang="en">Powered by </span><a href="http://www.entando.org/"><span lang="en">Entando, version 4.3</span></a>');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_CREDITS_POWERED_BY', 'it', '<span lang="en">Powered by </span><a href="http://www.entando.org/"><span lang="en">Entando, version 4.3</span></a>');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_SUBSITE_TITLE', 'it', 'Titolo Sottosito Default');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_SUBSITE_TITLE', 'en', 'Title Default Subsite');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_BACK_TO_PRINCIPAL_SITE', 'it', 'Torna all''Homepage Principale');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_BACK_TO_PRINCIPAL_SITE', 'en', 'Back to main portal');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_LOGO', 'it', 'Logo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_LOGO', 'en', 'Logo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_TITLE', 'it', 'Titolo Sottosito Default');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_TITLE', 'en', 'Title Default Subsite');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_SERVICES', 'it', 'In questa pagina sono disponibili i seguenti servizi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_SERVICES', 'en', 'Services');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_GO_TO_PAGE', 'it', 'Vai alla Pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_GO_TO_PAGE', 'en', 'Go To Page');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_NEWS_ARCHIVE', 'it', 'Archivio Notizie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_NEWS_ARCHIVE', 'en', 'News Archive');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_NAVIGATION_MENU', 'it', 'Menù di Navigazione');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_NAVIGATION_MENU', 'en', 'Navigation Menu');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_JUMP_TO_CONTENTS', 'it', 'Salta ai contenuti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_JUMP_TO_CONTENTS', 'en', 'Jump to contents');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_CONTENTS', 'it', 'Contenuti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_CONTENTS', 'en', 'Contents');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_BACK_TO_TOP', 'it', 'Torna all''inizio della pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsubsites_BACK_TO_TOP', 'en', 'Back to top');




INSERT INTO categories (catcode, parentcode, titles) VALUES ('jpsubsites', 'home', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite root</property><property key="it">Subsite root</property></properties>
');
INSERT INTO categories (catcode, parentcode, titles) VALUES ('jpsubsites_template', 'jpsubsites', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsites - Site Template</property><property key="it">Subsites - Site Template</property></properties>
');




INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsites', 'homepage', 5, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsites_template', 'jpsubsites', 1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsite_template_page1', 'jpsubsites_template', 1, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsite_template_page2', 'jpsubsites_template', 2, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsite_template_news', 'jpsubsites_template', 3, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('jpsubsite_template_news_view', 'jpsubsites_template', 1, 'free');



INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsites', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite root</property><property key="it">Subsite root</property></properties>
', 'jpsubsites_template', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsites_template', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsites - Site Template</property><property key="it">Subsite - Site Template</property></properties>
', 'jpsubsites_template', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsite_template_page1', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite - Site Template - Page 1</property><property key="it">Subsite - Site Template - Pagina 1</property></properties>
', 'jpsubsites_template', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsite_template_page2', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite - Site Template - Page 2</property><property key="it">Subsite - Site Template - Pagina 2</property></properties>
', 'jpsubsites_template', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsite_template_news', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite - Site Template - News</property><property key="it">Subsite - Site Template - Notizie</property></properties>
', 'jpsubsites_template', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsite_template_news_view', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite - Site Template - View News</property><property key="it">Subsite - Site Template - Visualizza Notizia</property></properties>
', 'jpsubsites_template', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config><useextratitles>false</useextratitles></config>', '2017-05-01 13:00:00');




INSERT INTO pages_metadata_online (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('jpsubsites', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Subsite root</property><property key="it">Subsite root</property></properties>
', 'jpsubsites_template', 0, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <extragroups>
    <group name="free" />
  </extragroups>
</config>', '2017-05-01 13:00:00');




INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page2', 2, 'entando-widget-navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">parent.children</property></properties>
');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news', 0, 'jpsubsites_breadcrumbs', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news', 2, 'entando-widget-navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">parent.children</property></properties>
');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsites_template', 8, 'jpsubsites_news_latest', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news_view', 0, 'jpsubsites_breadcrumbs', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news_view', 2, 'entando-widget-navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">parent.children</property></properties>
');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news_view', 6, 'content_viewer', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsites_template', 6, 'jpsubsites_intro_test_main', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_news', 6, 'jpsubsites_listViewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="title_it">Archivio News</property><property key="userFilters">(attributeFilter=true;key=Title)</property><property key="filters">(order=DESC;attributeFilter=true;key=Date)</property><property key="title_en">News Archive</property><property key="contentType">NWS</property><property key="modelId">10021</property></properties>
');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsites_template', 0, 'jpsubsites_breadcrumbs', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page1', 6, 'jpsubsites_intro_test_1', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page2', 6, 'jpsubsites_intro_test_2', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsites_template', 2, 'entando-widget-navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">current.children</property></properties>
');
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page1', 0, 'jpsubsites_breadcrumbs', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page2', 0, 'jpsubsites_breadcrumbs', NULL);
INSERT INTO widgetconfig_draft (pagecode, framepos, widgetcode, config) VALUES ('jpsubsite_template_page1', 2, 'entando-widget-navigation_menu', '<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="navSpec">parent.children</property></properties>
');



