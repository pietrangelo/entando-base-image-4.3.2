DELETE FROM guifragment WHERE plugincode = 'jpsubsites';

DELETE FROM widgetconfig WHERE widgetcode = 'jpsubsites_listViewer';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsubsites_listViewer');

DELETE FROM widgetconfig WHERE widgetcode = 'jpsubsites_news_latest';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsubsites_news_latest');

DELETE FROM widgetconfig WHERE widgetcode = 'jpsubsites_breadcrumbs';
DELETE FROM widgetconfig WHERE widgetcode IN (SELECT code FROM widgetcatalog WHERE parenttypecode = 'jpsubsites_breadcrumbs');

DELETE FROM localstrings WHERE keycode LIKE 'jpsubsites_%';

DELETE FROM sysconfig WHERE item = 'jpsubsites_subsiteConfig';