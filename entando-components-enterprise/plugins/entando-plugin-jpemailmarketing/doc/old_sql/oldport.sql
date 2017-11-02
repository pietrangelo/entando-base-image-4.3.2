-- ============================================================================
--
--			The Course is not meant to be published in frontend
--
-- ============================================================================

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingCourse', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Course</property>
<property key="it">Pubblica Course</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="jpemailmarketingCourseConfig"/>
</config>','jpemailmarketing', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingCourse_list_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Course List and Form</property>
<property key="it">Lista e Form Course</property>
</properties>', NULL, 'jpemailmarketing', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/jpemailmarketing/Course/list.action</property>
</properties>', 1, 'free');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ID', 'en', 'id');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ID', 'it', 'id');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NAME', 'en', 'name');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NAME', 'it', 'name');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_SENDER', 'en', 'sender');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_SENDER', 'it', 'sender');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ACTIVE', 'en', 'active');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ACTIVE', 'it', 'active');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CRONEXP', 'en', 'cronexp');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CRONEXP', 'it', 'cronexp');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CRONTIMEZONEID', 'en', 'crontimezoneid');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CRONTIMEZONEID', 'it', 'crontimezoneid');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDATSTART', 'en', 'createdat start');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDATSTART', 'it', 'createdat start');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDATEND', 'en', 'createdat end');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDATEND', 'it', 'createdat end');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDAT', 'en', 'createdat');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_CREATEDAT', 'it', 'createdat');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDATSTART', 'en', 'updatedat start');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDATSTART', 'it', 'updatedat start');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDATEND', 'en', 'updatedat end');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDATEND', 'it', 'updatedat end');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDAT', 'en', 'updatedat');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_UPDATEDAT', 'it', 'updatedat');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ACTIONS', 'it', 'Actions');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_ACTIONS', 'en', 'Actions');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NEW', 'it', 'Course New');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NEW', 'en', 'Course New');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_EDIT', 'it', 'Course Edit');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_EDIT', 'en', 'Course Edit');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_TRASH', 'it', 'Course Trash');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_TRASH', 'en', 'Course Trash');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_TRASH_CONFIRM', 'it', 'Course Trash confirm');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_TRASH_CONFIRM', 'en', 'Course Trash confirm');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_SEARCH_COURSE', 'it', 'Course search');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_SEARCH_COURSE', 'en', 'Course search');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NOT_FOUND', 'it', 'Course not found');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_COURSE_NOT_FOUND', 'en', 'Course not found');



-- ============================================================================
--
--			The 'Publish Form' Form is internaservlet AND not meant to be handled in frontend with the classic 'Form List and Form'
--
-- ============================================================================

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingForm', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Form</property>
<property key="it">Pubblica Form</property>
</properties>', '<config>
	<parameter name="courseId">courseId</parameter>
	<action name="jpemailmarketingFormConfig"/>
</config>','jpemailmarketing', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingForm_list_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Form List and Form</property>
<property key="it">Lista e Form Form</property>
</properties>', NULL, 'jpemailmarketing', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/jpemailmarketing/Form/list.action</property>
</properties>', 1, 'free');


-- ============================================================================
--
--			The Subscriber is not meant to be published in frontend
--
-- ============================================================================

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingSubscriber', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Subscriber</property>
<property key="it">Pubblica Subscriber</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="jpemailmarketingSubscriberConfig"/>
</config>','jpemailmarketing', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpemailmarketingSubscriber_list_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Subscriber List and Form</property>
<property key="it">Lista e Form Subscriber</property>
</properties>', NULL, 'jpemailmarketing', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/list.action</property>
</properties>', 1, 'free');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_ID', 'en', 'id');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_ID', 'it', 'id');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_EMAIL', 'en', 'email');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_EMAIL', 'it', 'email');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CAMPAIGNID', 'en', 'campaignId');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CAMPAIGNID', 'it', 'campaignId');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_FULLNAME', 'en', 'fullName');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_FULLNAME', 'it', 'fullName');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_STATUS', 'en', 'status');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_STATUS', 'it', 'status');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDATSTART', 'en', 'createdat start');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDATSTART', 'it', 'createdat start');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDATEND', 'en', 'createdat end');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDATEND', 'it', 'createdat end');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDAT', 'en', 'createdat');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_CREATEDAT', 'it', 'createdat');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDATSTART', 'en', 'updatedat start');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDATSTART', 'it', 'updatedat start');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDATEND', 'en', 'updatedat end');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDATEND', 'it', 'updatedat end');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDAT', 'en', 'updatedat');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_UPDATEDAT', 'it', 'updatedat');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_ACTIONS', 'it', 'Actions');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_ACTIONS', 'en', 'Actions');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_NEW', 'it', 'Subscriber New');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_NEW', 'en', 'Subscriber New');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_EDIT', 'it', 'Subscriber Edit');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_EDIT', 'en', 'Subscriber Edit');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_TRASH', 'it', 'Subscriber Trash');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_TRASH', 'en', 'Subscriber Trash');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_TRASH_CONFIRM', 'it', 'Subscriber Trash confirm');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_TRASH_CONFIRM', 'en', 'Subscriber Trash confirm');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_SEARCH_SUBSCRIBER', 'it', 'Subscriber search');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_SEARCH_SUBSCRIBER', 'en', 'Subscriber search');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_NOT_FOUND', 'it', 'Subscriber not found');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpemailmarketing_SUBSCRIBER_NOT_FOUND', 'en', 'Subscriber not found');

