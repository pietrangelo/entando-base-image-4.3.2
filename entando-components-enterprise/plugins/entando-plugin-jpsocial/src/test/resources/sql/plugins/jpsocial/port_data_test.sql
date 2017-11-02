INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpsocial_fblogin', 
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Login with facebook auth</property>
<property key="it">Social - Login with facebook auth</property>
</properties>', NULL, 'jpsocial', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpsocial_twlogin', 
'<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Login with twitter auth</property>
<property key="it">Social - Login with twitter auth</property>
</properties>', NULL, 'jpsocial', NULL, NULL, 1);

INSERT INTO widgetcatalog(code, plugincode, titles, parameters, parenttypecode, defaultconfig, locked, maingroup)
    VALUES ('jpsocial_fbPost', 'jpsocial', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Social - Share with Facebook</property>
<property key="it">Social - Condividi con Facebook</property>
</properties>', null, null, null, 1, null);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TWITTER', 'en', 'TWITTER');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TWITTER', 'it', 'TWITTER');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_FACEBOOK', 'en', 'FACEBOOK');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_FACEBOOK', 'it', 'FACEBOOK');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TITLE', 'en', 'Log in with');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_LOGIN_TITLE', 'it', 'Log in con');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_FB_POST_TITLE', 'en', 'Share with Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_FB_POST_TITLE', 'it', 'Condividi con Facebook');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_TEXT', 'en', 'Message');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_TEXT', 'it', 'Messaggio');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_IMAGE', 'en', 'Image');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_IMAGE', 'it', 'Immagine');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SHARE', 'en', 'Share');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SHARE', 'it', 'Condividi');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_MUST_LOGIN', 'en', 'Sign in to share with Facebook');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_MUST_LOGIN', 'it', 'Accedi a Facebook per condividere');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_SUCCESS', 'en', 'The post was successfully published');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_SUCCESS', 'it', 'Il post è stato condiviso con successo');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_ANOTHER', 'en', 'Publish another post');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_SEND_ANOTHER', 'it', 'Pubblica un altro post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_FAILURE', 'en', 'Error publishing the post');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpsocial_POST_FAILURE', 'it', 'Errore rilevato pubblicando post');

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
