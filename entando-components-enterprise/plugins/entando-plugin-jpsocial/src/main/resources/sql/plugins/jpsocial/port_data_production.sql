INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsocial_content_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Publish a Content</property>
<property key="it">Social - Pubblica un Contenuto</property>
</properties>', '<config>
	<parameter name="contentId">Content ID</parameter>
	<parameter name="modelId">Content Model ID</parameter>
	<action name="viewerConfig"/>
</config>', 'jpsocial', NULL, NULL, 1, NULL);


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsocial_entryPost', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - New Post</property>
<property key="it">Social - Nuovo Post</property>
</properties>', NULL, 'jpsocial', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsocial/Front/Share/intro.action</property>
</properties>', 1, NULL);


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsocial_connect', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Connect Social Network</property>
<property key="it">Social - Connetti Social Network</property>
</properties>', NULL, 'jpsocial', NULL, NULL, 1, NULL);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpsocial_fblogin', 
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Facebook Sign In</property>
<property key="it">Social - Accesso tramite Facebook</property>
</properties>', NULL, 'jpsocial', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpsocial_twlogin', 
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Twitter Sign In</property>
<property key="it">Social - Accesso tramite Twitter</property>
</properties>', NULL, 'jpsocial', NULL, NULL, 1);

INSERT INTO widgetcatalog(code, plugincode, titles, parameters, parenttypecode, defaultconfig, locked, maingroup)
    VALUES ('jpsocial_fbPost', 'jpsocial', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Share with Facebook</property>
<property key="it">Social - Condividi con Facebook</property>
</properties>', null, null, null, 1, null);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpsocial_twPost', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Share with Twitter</property>
<property key="it">Social - Condividi con Twitter</property>
</properties>', NULL, 'jpsocial', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsocial/Front/Post/tweetStart.action</property>
</properties>', 1, NULL);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TWITTER', 'en', 'Twitter');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TWITTER', 'it', 'Twitter');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_FACEBOOK', 'en', 'Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_FACEBOOK', 'it', 'Facebook');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TITLE', 'en', 'Sign In with');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TITLE', 'it', 'Accedi con');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_FB_POST_TITLE', 'en', 'Share with Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_FB_POST_TITLE', 'it', 'Condividi con Facebook');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_TEXT', 'en', 'Message');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_TEXT', 'it', 'Messaggio');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_IMAGE', 'en', 'Image');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_IMAGE', 'it', 'Immagine');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SHARE', 'en', 'Share');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SHARE', 'it', 'Condividi');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_MUST_LOGIN', 'en', 'Sign In to share with Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_MUST_LOGIN', 'it', 'Accedi a Facebook per condividere');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_SUCCESS', 'en', 'The post was successfully published');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_SUCCESS', 'it', 'Il post è stato condiviso con successo');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_ANOTHER', 'en', 'Publish another post');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_ANOTHER', 'it', 'Pubblica un altro post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_FAILURE', 'en', 'Error publishing the post');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_FAILURE', 'it', 'Errore rilevato nella pubblicazione del post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TRY_AGAIN', 'en', 'Try again');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TRY_AGAIN', 'it', 'Riprova');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_TWITTER', 'en', 'Share with Twitter');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_TWITTER', 'it', 'Condividi con Twitter');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN', 'en', 'Sign in to share with Twitter');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN', 'it', 'Accedi a Twitter per condividere'); 

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TWEET_TEXT', 'en', 'Message');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TWEET_TEXT', 'it', 'Message');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_TWEET', 'en', 'Tweet');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_TWEET', 'it', 'Tweet');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_ENTANDO_FIRST','en','You must sign in the portal in order to use this functionality.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_ENTANDO_FIRST','it','Devi accedere al portale per poter usare questa funzionalità.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_CONNECT_WIDGET','en','Connect Social Network');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_CONNECT_WIDGET','it','Connetti Social Network');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_LOGOUT_FB','en','Logout from Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_LOGOUT_FB','it','Disconnetti Facebook');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_LOGOUT_TW','en','Logout from Twitter');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_LOGOUT_TW','it','Disconnetti Twitter');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_SIGNIN_FB','en','Sign In Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_SIGNIN_FB','it','Connetti Facebook');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_SIGNIN_TW','en','Sign In Twitter');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_CONNECT_SIGNIN_TW','it','Connetti Twitter');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGOUT','en','Logout');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGOUT','it','Disconnetti');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGOUT_ALL','en','Logout from all');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGOUT_ALL','it','Disconnetti tutti');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_SHARE', 'en', 'Check and Share');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_TITLE_SHARE', 'it', 'Verifica e condividi');