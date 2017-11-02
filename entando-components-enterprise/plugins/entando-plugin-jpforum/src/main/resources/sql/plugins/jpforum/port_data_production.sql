INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('production', 'jpforumSubIndexDir', 'Sottocartella file indicizzazion forum', 'forumindexdir');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpforum_forum', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Forum</property>
<property key="it">Forum</property>
</properties>', NULL, 'jpforum', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpforum/Front/Browse/viewSection.action</property>
</properties>
', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpforum_search_posts_results', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Forum - Search Results</property>
<property key="it">Forum - Risultati della Ricerca</property>
</properties>', NULL, 'jpforum', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpforum_search_posts', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Forum - Search Form</property>
<property key="it">Forum - Form della Ricerca</property>
</properties>', NULL, 'jpforum', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpforum_section_stats', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Forum - Statistics</property>
<property key="it">Forum - Statistiche</property>
</properties>', NULL, 'jpforum', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpforum_frontSectionTree', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Forum - Sections Tree</property>
<property key="it">Forum - Albero delle Sezioni</property>
</properties>', NULL, 'jpforum', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpforum/Front/Sections/tree.action</property>
</properties>', 1);

insert into sysconfig (version, item, descr, config) values 
	('production', 'jpforum_config', 'parametri di configurazione forum','<jpforum><postsPerPage>3</postsPerPage><profileTypecode>PFL</profileTypecode><profileNickAttributeName>Nickname</profileNickAttributeName></jpforum>');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WELCOME', 'it', 'Benvenuto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WELCOME', 'en', 'Welcome');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOUHAVE', 'it', 'Hai');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOUHAVE', 'en', 'You have');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_NOT_HAVE', 'it', 'Non hai');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_NOT_HAVE', 'en', 'You have not');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PIMs', 'it', 'Messaggi Personali');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PIMs', 'en', 'Personal Messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WARNINGS', 'it', 'Ammonizioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WARNINGS', 'en', 'Warnings');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TOREAD', 'it', 'da leggere');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TOREAD', 'en', 'to read');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_UR_PANEL', 'it', 'Vai al tuo pannello');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_UR_PANEL', 'en', 'Go to your panel');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH', 'it', 'Cerca nel Forum');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH', 'en', 'Forum Search');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_POST', 'it', 'Cerca tra i  messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_POST', 'en', 'Search in posts');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEARCH', 'it', 'Inizia Ricerca');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEARCH', 'en', 'Start Search');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_USERS', 'it', 'Cerca fra gli utenti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_USERS', 'en', 'Search in users');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_UR_HERE', 'it', 'Sei qui');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_UR_HERE', 'en', 'You are');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION', 'it', 'Sezione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION', 'en', 'Section');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_IN_THIS_SECTION', 'it', 'messaggi in questa sezione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_IN_THIS_SECTION', 'en', 'messages in this section');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_ORGANIZED_IN_start', 'it', 'organizzati in');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_ORGANIZED_IN_start', 'en', 'organized in');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_ORGANIZED_IN_end', 'it', 'discussioni.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POSTS_ORGANIZED_IN_end', 'en', 'threads.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SUB_SECTIONS', 'it', 'Sottosezioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SUB_SECTIONS', 'en', 'Subsections');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_TITLE', 'it', 'Titolo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_TITLE', 'en', 'Title');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_SUBJECT', 'it', 'Argomento');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_SUBJECT', 'en', 'Subject');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS', 'it', 'Stato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS', 'en', 'Status');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_THREADS', 'it', 'Discussioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_THREADS', 'en', 'Threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_MESSAGES', 'it', 'Messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_MESSAGES', 'en', 'Messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS_OPEN', 'it', 'Aperta');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS_OPEN', 'en', 'Open');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS_CLOSED', 'it', 'Chiusa');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SECTION_STATUS_CLOSED', 'en', 'Closed');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_SUBSECTIONS', 'it', 'Non ci sono sottosezioni.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_SUBSECTIONS', 'en', 'No subsections.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS', 'it', 'Discussioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS', 'en', 'Threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_LOGIN_TO_CREATE', 'it', 'Per creare una nuova discussione è necessarrio effettuare l''accesso.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_LOGIN_TO_CREATE', 'en', 'In order to create a new thread please login.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_LOGIN', 'it', 'Effettua l''accesso');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_LOGIN', 'en', 'Go to login');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_CLOSED_CANNOT_CREATE_THREAD', 'it', 'Non è possibile creare nuove discussioni poiché la sezione corrente è chiusa.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_CLOSED_CANNOT_CREATE_THREAD', 'en', 'You can''t create new threads because this section is closed.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_STICKY', 'it', 'Discussioni Importanti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_STICKY', 'en', 'Sticky Threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_STYCKY_THREADS_HERE', 'it', 'Nessuna discussione importante.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_STYCKY_THREADS_HERE', 'en', 'No Sticky Threads here.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_NORMAL', 'it', 'Discussioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_NORMAL', 'en', 'Threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_NORMAL_THREADS_HERE', 'it', 'Non ci sono discussioni.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_NORMAL_THREADS_HERE', 'en', 'No threads here.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_CREATE_NEW_THREAD', 'it', 'Crea una nuova discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_CREATE_NEW_THREAD', 'en', 'Create a new thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_INSERT_DATE', 'it', 'Data inserimento');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_INSERT_DATE', 'en', 'Insert Date');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_AUTHOR', 'it', 'Autore');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_AUTHOR', 'en', 'Author');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_REPLIES', 'it', 'Numero di risposte');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_REPLIES', 'en', 'Replies');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_REPLIES', 'it', 'risposte');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_REPLIES', 'en', 'replies');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_VISITED', 'it', 'Numero di visite');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_VISITED', 'en', 'Views');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_VISITS', 'it', 'visite');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_VISITS', 'en', 'views');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_LAST_POST', 'it', 'Ultimo messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_LAST_POST', 'en', 'Last post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WRITTEN_BY', 'it', 'scritto da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WRITTEN_BY', 'en', 'written by');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_STATUS_OPEN', 'it', 'aperta');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_STATUS_OPEN', 'en', 'open');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_STATUS_CLOSED', 'it', 'chiusa');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD_STATUS_CLOSED', 'en', 'closed');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD', 'it', 'Discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD', 'en', 'Thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MESSAGES_IN_THIS', 'it', 'messaggi in questa discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MESSAGES_IN_THIS', 'en', 'for this thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_THE_LAST_POST', 'it', 'l''ultimo messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_THE_LAST_POST', 'en', 'the last post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_WAS_WRITTEN', 'it', 'è stato scritto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_WAS_WRITTEN', 'en', 'was written');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_MOD_TOOLS', 'it', 'Strumenti di Moderazione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_MOD_TOOLS', 'en', 'Moderation Tools');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_CLOSE_IT', 'it', 'Chiudi la discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_CLOSE_IT', 'en', 'Close thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_OPEN_IT', 'it', 'Apri la discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_OPEN_IT', 'en', 'Open thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_PIN_IT', 'it', 'Segna come importante');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_PIN_IT', 'en', 'Make it sticky');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_UNPIN_IT', 'it', 'Rimuovi dalle importanti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_UNPIN_IT', 'en', 'Remove from sticky list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_REMOVE_IT', 'it', 'Cancella discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_MOD_REMOVE_IT', 'en', 'Remove thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PERMALINK', 'it', 'Link permanente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PERMALINK', 'en', 'Permalink');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_NEW', 'it', 'Nuova Discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_NEW', 'en', 'New Thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_NEW', 'it', 'Nuovo Messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_NEW', 'en', 'New Post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEND_POST_TO', 'it', 'Invia un messaggio alla discussione:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEND_POST_TO', 'en', 'Send a new post to the thread:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_EDIT', 'it', 'Modifica Messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_EDIT', 'en', 'Edit Post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_EDIT_POST_FROM', 'it', 'Modifica il messaggio dalla discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_EDIT_POST_FROM', 'en', 'Edit post from thread:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_TEXT', 'it', 'Testo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_TEXT', 'en', 'Text');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_TITLE', 'it', 'Titolo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_TITLE', 'en', 'Title');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_BODY', 'it', 'Corpo del messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_BODY', 'en', 'Body Text');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACHMENTS', 'it', 'Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACHMENTS', 'en', 'Attachments');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACH_DELETE', 'it', 'Elimina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACH_DELETE', 'en', 'Delete');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_ATTACH', 'it', 'Allegato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_ATTACH', 'en', 'Attachment');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACH_SIZE_KB', 'it', 'dimensione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_ATTACH_SIZE_KB', 'en', 'size');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_ADD_ATTACH', 'it', 'Aggiungi Allegato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_POST_ADD_ATTACH', 'en', 'Add Attachment');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_POST_SAVE', 'it', 'Invia');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_POST_SAVE', 'en', 'Send');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_THREAD', 'it', 'Annulla e vai alla discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_THREAD', 'en', 'Cancel and go to the thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_SECTION', 'it', 'Annulla e vai alla sezione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_SECTION', 'en', 'Cancel and go to the section');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_REMOVE', 'it', 'Cancella Discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREAD_REMOVE', 'en', 'Remove Thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_SUMMARY', 'it', 'Dati della discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_SUMMARY', 'en', 'Thread summary');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_CONFIRM_REMOVE', 'it', 'Confermi la cancellazione?');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_CONFIRM_REMOVE', 'en', 'Do you really want to remove this thread?');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_CONFIRM_REMOVE', 'it', 'Si, confermo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_CONFIRM_REMOVE', 'en', 'Yes, I do want to remove it');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_SHORT', 'it', 'Msg.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_SHORT', 'en', 'Msg.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_LONG', 'it', 'Messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_LONG', 'en', 'Message');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_OF', 'it', 'di');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_OF', 'en', 'of');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ON_DATE', 'it', 'il');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ON_DATE', 'en', 'on');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_UNTITLED', 'it', 'Messaggio Senza Titolo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_UNTITLED', 'en', 'Untitled Post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_ATTACHMENTS', 'it', 'Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_ATTACHMENTS', 'en', 'Attachments');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_REPLY', 'it', 'Rispondi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_REPLY', 'en', 'Reply');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_QUOTE', 'it', 'Cita');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_QUOTE', 'en', 'Quote');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_EDIT', 'it', 'Modifica');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_EDIT', 'en', 'Edit');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_REMOVE', 'it', 'Cancella');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_REMOVE', 'en', 'Remove');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_NOT_FOUND', 'it', 'La sezione richiesta non è stata trovata.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_NOT_FOUND', 'en', 'Requested section was not found.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION_NOT_FOUND', 'it', 'Sezione Non Trovata');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION_NOT_FOUND', 'en', 'Section Not Found');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_INDEX', 'it', 'Vai all''indice del forum');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_INDEX', 'en', 'Go to forum index');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_TRASH', 'it', 'Elimina Messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_POST_TRASH', 'en', 'Remove Post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_CONFIRM_REMOVE', 'it', 'Vuoi davvero eliminare questo messaggio?');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_CONFIRM_REMOVE', 'en', 'Do you really want to remove this post?');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_USER_PROFILE', 'it', 'Profilo Utente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_USER_PROFILE', 'en', 'User Profile');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_YOUR_PROFILE', 'it', 'Il Tuo Profilo');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_YOUR_PROFILE', 'en', 'Your Profile');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USER_POSTS_NUMBER', 'it', 'Numero di messaggi scritti da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USER_POSTS_NUMBER', 'en', 'Posts written by');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USER_THREADS_NUMBER', 'it', 'Numero di discussioni aperte da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USER_THREADS_NUMBER', 'en', 'Threads opened by');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_USER_SHOW_POSTS', 'it', 'Mostra i Messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_USER_SHOW_POSTS', 'en', 'Show Posts');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_USER_SHOW_THREADS', 'it', 'Mostra le Discussioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_USER_SHOW_THREADS', 'en', 'Show Threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_ATTACH_MANAGEMENT', 'it', 'Gestione Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_ATTACH_MANAGEMENT', 'en', 'Attach Management');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_INSERTED', 'it', 'Numero di allegati inseriti nel forum');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_INSERTED', 'en', 'Attached files');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USED_SPACE', 'it', 'Spazio utilizzato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USED_SPACE', 'en', 'Used space');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MANAGE_ATTACHMENTS', 'it', 'Vai alla Gestione Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MANAGE_ATTACHMENTS', 'en', 'Go to Manage Attachments');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM', 'it', 'Messaggi Personali');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM', 'en', 'Personal Messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NEW_PMs', 'it', 'nuovi messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NEW_PMs', 'en', 'new messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_MESSAGES_NO_MESSAGE', 'it', 'Non hai messaggi da leggere.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_MESSAGES_NO_MESSAGE', 'en', 'No message to read.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MESSAGES_SHOW_ALL', 'it', 'Mostra tutti i messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MESSAGES_SHOW_ALL', 'en', 'Show all the messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NEW_WARNs', 'it', 'nuove ammonizioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NEW_WARNs', 'en', 'new warnings');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WARNs_NO_WARN', 'it', 'Nessuna ammonizione.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_WARNs_NO_WARN', 'en', 'No warning.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_WARNs_SHOW_ALL', 'it', 'Mostra tutte le ammonizioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_WARNs_SHOW_ALL', 'en', 'Show all the warnings');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TOTAL_COUNT', 'it', 'in totale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TOTAL_COUNT', 'en', 'total count');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MESSAGE_SEND_MSG_TO', 'it', 'Invia un messaggio a');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_MESSAGE_SEND_MSG_TO', 'en', 'Send a message to');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SANCTIONS', 'it', 'Sanzioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SANCTIONS', 'en', 'Sanctions');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SACTION_NO_SANTION_FOR_THIS_USER', 'it', 'Nessuna sanzione per');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SACTION_NO_SANTION_FOR_THIS_USER', 'en', 'No sanction for');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEND_WARN', 'it', 'Ammonisci');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEND_WARN', 'en', 'Send a Warning');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEND_SANCTION', 'it', 'Sanziona');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SEND_SANCTION', 'en', 'Sanction');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MESSAGES_WRITTEN_BY', 'it', 'Tutti I Messaggi Scritti da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MESSAGES_WRITTEN_BY', 'en', 'All The Messages Written by');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MESSAGES_ALL_YOUR_MSG', 'it', 'Tutti i messaggi scritti da te');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MESSAGES_ALL_YOUR_MSG', 'en', 'All the messages written by you');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_POST', 'it', 'Vai alla discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_POST', 'en', 'Go to thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_POSTS', 'it', 'Nessun messaggio.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_NO_POSTS', 'en', 'No posts.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_PROFILE_OF', 'it', 'Vai al Profilo:');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_PROFILE_OF', 'en', 'Go to Profile:');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_WRITTEN_BY', 'it', 'Discussioni iniziate da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_WRITTEN_BY', 'en', 'Threads started by');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_YOU_STARTED', 'it', 'Discussioni iniziate da te');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_THREADS_YOU_STARTED', 'en', 'Threads you started');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_NO_THREAD_FOUND', 'it', 'Nessuna discussione trovata.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_THREAD_NO_THREAD_FOUND', 'en', 'No thread found.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARNs', 'it', 'Ammonizioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARNs', 'en', 'Warnings');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_LIST', 'it', 'Lista dei Messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_LIST', 'en', 'Message List');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS', 'it', 'Stato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS', 'en', 'Status');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SENDER', 'it', 'Mittente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SENDER', 'en', 'Sender');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SUBJECT', 'it', 'Oggetto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SUBJECT', 'en', 'Subject');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DATE', 'it', 'Data');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DATE', 'en', 'Date');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_REMOVE', 'it', 'Elimina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_REMOVE', 'en', 'Remove');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS_TOREAD', 'it', 'da leggere');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS_TOREAD', 'en', 'to read');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS_READ', 'it', 'già letto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_STATUS_READ', 'en', 'already read');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DOREAD', 'it', 'Leggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DOREAD', 'en', 'Read');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DELETE', 'it', 'Elimina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_DELETE', 'en', 'Delete');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_NO_PM_FOR_YOU', 'it', 'Nessun messaggio per te.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_NO_PM_FOR_YOU', 'en', 'No messages for you.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM_DETAILS', 'it', 'Dettaglio Messaggio Personale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM_DETAILS', 'en', 'Personal Message Details');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARN_DETAILS', 'it', 'Dettaglio Ammonizione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARN_DETAILS', 'en', 'Warning Details');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_RECIPIENT', 'it', 'Destinatario');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_RECIPIENT', 'en', 'Recipient');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM_NEW', 'it', 'Nuovo Messaggio Personale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_PM_NEW', 'en', 'New Personal Message');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARN_NEW', 'it', 'Nuova Ammonizione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_WARN_NEW', 'en', 'New Warning');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_BODY', 'it', 'Corpo del messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_BODY', 'en', 'Body');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_SUBJECT', 'it', 'Oggetto');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_PM_SUBJECT', 'en', 'Subject');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_PM_SEND', 'it', 'Invia');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_PM_SEND', 'en', 'Send');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_WARNINGS_SHOW_ALL', 'it', 'Mostra tutte le ammonizioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_WARNINGS_SHOW_ALL', 'en', 'Show all the warnings');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SACTION_USER', 'it', 'Sanziona');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SACTION_USER', 'en', 'Apply Sanction');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DATE_HELP', 'it', 'Inserisci la data di inizio e la data di fine indicando giorno mese anno nel formato <em>gg/mm/aaaa</em>, ad esempio così: 17/03/2010.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DATE_HELP', 'en', 'Insert start date and end date in the format <em>gg/mm/aaaa</em>, for example: 17/03/2010.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_START_DATE', 'it', 'Data Inizio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_START_DATE', 'en', 'Start Date');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_END_DATE', 'it', 'Data Fine');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_END_DATE', 'en', 'End Date');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_HELP_OUTRO', 'it', 'Applicando la sanzione, l''utente non potrà più utilizzare il forum sino alla data di scadenza.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_HELP_OUTRO', 'en', 'Sending the sanction, the user can''t use the forum until the end date.''');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_ARE_U_SURE', 'it', 'Applicare la sanzione?');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_ARE_U_SURE', 'en', 'Are you sure?');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SANCTION_SEND', 'it', 'Si, applica la sanzione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_SANCTION_SEND', 'en', 'Yes, send it');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_USER', 'it', 'Annulla l''operazione, vai al profilo di');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_CANCEL_TO_USER', 'en', 'No, go to the profile of');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SENT_OK', 'it', 'Il tuo messaggio è stato spedito correttamente all''utente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PM_SENT_OK', 'en', 'Your message was sent successfully to');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_SENDER', 'it', 'Mittente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SANCTION_SENDER', 'en', 'Sender');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE', 'it', 'Elimina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE', 'en', 'Delete');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TIME_AT', 'it', 'alle ore');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TIME_AT', 'en', 'at');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_ARE_AT_PAGE', 'it', 'Sei alla pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_ARE_AT_PAGE', 'en', 'You are at page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_OF_THREAD_LIST', 'it', 'della lista delle discussioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_OF_THREAD_LIST', 'en', 'of thread list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_THERE_ARE', 'it', 'Ci sono');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_THERE_ARE', 'en', 'There are');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_TOTAL_THREADS', 'it', 'discussioni in totale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_TOTAL_THREADS', 'en', 'threads');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_OF_POST_LIST', 'it', 'della lista dei messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_OF_POST_LIST', 'en', 'of the list of messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_TOTAL_POSTS', 'it', 'messaggi in totale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_TOTAL_POSTS', 'en', 'messages');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_SEARCH_RETURNED', 'it', 'La ricerca ha restituito');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_SEARCH_RETURNED', 'en', 'Search returned');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_SEARCH_RESULTS', 'it', 'risultati in totale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_SEARCH_RESULTS', 'en', 'results');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_NO_PARAMETER_INSERTED', 'it', 'Inserisci almeno una parola chiave da cercare.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_NO_PARAMETER_INSERTED', 'en', 'Insert at least a keyword.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_YOU_SEARCHED', 'it', 'Hai cercato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_YOU_SEARCHED', 'en', 'You searched');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_TEXT', 'it', 'Testo da cercare nei messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SEARCH_TEXT', 'en', 'Text to find in posts');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_RESULT', 'it', 'Risultato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_RESULT', 'en', 'Result');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_USER_NO_PARAMETER_INSERTED', 'it', 'Inserisci almeno una parola da ricercare, altrimenti verranno mostrati tutti gli utenti.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_USER_NO_PARAMETER_INSERTED', 'en', 'Insert al least a keyword, otherwise all the users will be shown.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH_IN_USERS', 'it', 'Cerca Utenti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH_IN_USERS', 'en', 'Search Users');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_USERS_NO_RESULTS', 'it', 'Nessun utente trovato.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_USERS_NO_RESULTS', 'en', 'No user found.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH_POSTS', 'it', 'Cerca tra i messaggi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SEARCH_POSTS', 'en', 'Search posts');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_POSTS_NO_RESULTS', 'it', 'Nessun risultato trovato.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SEARCH_POSTS_NO_RESULTS', 'en', 'No result found.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_ATTACH_TRASH', 'it', 'Rimozione Allegato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_ATTACH_TRASH', 'en', 'Trash Attachment');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACH_TRASH_CONFIRM', 'it', 'Vuoi davvero eliminare l''allegato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACH_TRASH_CONFIRM', 'en', 'Do you really want to delete the attachment');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_FIRST', 'it', 'Vai alla prima pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_FIRST', 'en', 'Go to first page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_JUMP', 'it', 'Salta');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_JUMP', 'en', 'Jump');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_BACKWARD', 'it', 'indietro');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_BACKWARD', 'en', 'backward');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_FORWARD', 'it', 'avanti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_FORWARD', 'en', 'forward');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_PREV', 'it', 'Prec.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_PREV', 'en', 'Prev,');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_PREV_FULL', 'it', 'Pagina Precedente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_PREV_FULL', 'en', 'Previous Page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_NEXT', 'it', 'Succ.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_NEXT', 'en', 'Next');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_NEXT_FULL', 'it', 'Pagina Successiva');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_NEXT_FULL', 'en', 'Next Page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_LAST', 'it', 'Vai all''ultima pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_LAST', 'en', 'Go to the last page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_PAGE', 'it', 'Vai alla pagina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_PAGE', 'en', 'Go to page');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_YOU_CANNOT_CREATE_THREADS', 'it', 'Non puoi creare nuove discussioni.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_YOU_CANNOT_CREATE_THREADS', 'en', 'You can''t create new threads.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_ARE_BANNED', 'it', 'Non ti è consentito accedere alla pagina richiesta.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_YOU_ARE_BANNED', 'en', 'You are not allowd to access the requested page.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PLEASE_LOGIN', 'it', 'Per accedere alla funzionalità richiesta è necessario essere registrati ed effettuare l''accesso.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PLEASE_LOGIN', 'en', 'Please login in. You need to be registered.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USEFUL_LINKS', 'it', 'Collegamenti che ti potrebbero essere utili');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_USEFUL_LINKS', 'en', 'Useful links');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_FORUM_INDEX', 'it', 'Indice del Forum');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_GOTO_FORUM_INDEX', 'en', 'Forum Index');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION_PREVIOUS_SECTIONS', 'it', 'Sezioni Precedenti');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SECTION_PREVIOUS_SECTIONS', 'en', 'Previous Sections');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_FIRST_SHORT', 'it', 'Inizio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_FIRST_SHORT', 'en', 'Start');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_LAST_SHORT', 'it', 'Fine');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PAGER_GO_TO_LAST_SHORT', 'en', 'End');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_CANCEL_GOTO_MESSAGE', 'it', 'No, vai al messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_CANCEL_GOTO_MESSAGE', 'en', 'No, go to the post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SANCTION_DELETE', 'it', 'Rimuovi Sanzione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_SANCTION_DELETE', 'en', 'Remove Sanction');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MANAGE_ATTACHMENTS', 'it', 'Gestione Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_TITLE_MANAGE_ATTACHMENTS', 'en', 'Manage Attachments');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_COUNT', 'it', 'Totale Allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_COUNT', 'en', 'Attachments count');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_USED_SPACE', 'it', 'Spazio utilizzato');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_USED_SPACE', 'en', 'Used space');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_LIST', 'it', 'Lista dei file allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_LIST', 'en', 'Attached files list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_FILE', 'it', 'Nome del File');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_FILE', 'en', 'Filename');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_SIZE', 'it', 'Dimensione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_SIZE', 'en', 'Size');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_POST', 'it', 'Messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_POST', 'en', 'Post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_DELETE', 'it', 'Elimina');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_ATTACHMENT_DELETE', 'en', 'Delete');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_GO_TO_POST', 'it', 'Vai al messaggio');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_GO_TO_POST', 'en', 'Go to the post');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_OPERATION_NOT_AVAILABLE', 'it', 'Operazione non disponibile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_OPERATION_NOT_AVAILABLE', 'en', 'Not available');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE_MESSAGE', 'it', 'Vuoi davvero eliminare la sanzione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE_MESSAGE', 'en', 'Do you really want to delete the sanction');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE_FOR_THE_USER', 'it', 'per l''utente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_DELETE_FOR_THE_USER', 'en', 'for the user');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_SUBSECTION_LIST', 'it', 'Lista delle sottosezioni');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_SUBSECTION_LIST', 'en', 'Subsections list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_SUBSECTION_SUMMARY', 'it', 'La tabella mostra la lista delle sottosezioni. Nella prima colonna il titolo, nella seconda l''oggetto, nella terza lo stato della sezione, nella quarta il conteggio delle discussioni presenti e nella quinta il conteggio dei messaggi.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SECTION_SUBSECTION_SUMMARY', 'en', 'This table shows the subsections list. In the first column the title, in the second the subject, the third the status, in the fourth the threads count and in the fifth the messages count.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_LIST_SUMMARY', 'it', 'La tabella mostra la lista degli allegati. Nella prima colonna il collegamento per lo scaricamento con il nome del file, nella seconda la dimensione in kb, nella terza il collegamento al messaggio con l''identificativo del messaggio, nella quarta il collegamento per la rimozione dell''allegato.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_ATTACHMENTS_LIST_SUMMARY', 'en', 'This table shows the attachments list. In the first column the filename with the link for the download, in the second the size in kb, in the third the link to the post with its id and in the fourth the link for removing the attachment.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PIMs_LIST_SUMMARY', 'it', 'La tabella mostra la lista dei messaggi personali ricevuti. Nella prima colonna lo stato, nella seconda il mittente, nella terza l''oggetto il collegato alla pagina del dettaglio, nella quarta la data di ricezione e nella quinta il collegamento per effettuare la rimozione.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_PIMs_LIST_SUMMARY', 'en', 'This table shows the list of received personal messages. In the first column the status, in the second the sender, in the third the subject with the link to the page of the details, in the fourth the recived date and in the fifth the link for removing it.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SACTION_LIST', 'it', 'Lista delle sanzioni ');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_SACTION_LIST', 'en', 'Sanctions list');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_LIST_SUMMARY', 'it', 'La tabella mostra la lista delle sanzioni applicate. Nella prima colonna la data di inizio, nella seconda la data di fine, nella terza il nome di chi l''ha applicata, nella quarta il collegamento per la rimozione.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_SANCTION_LIST_SUMMARY', 'en', 'This table shows the sanctions list. In the first column the start date, in the second column the end date, in the third column the sender name, in the fourth the link for removing it.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_GO', 'it', 'Vai');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_GO', 'en', 'Go');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_GOTO_ATTACHMENTS', 'it', 'Vai alla lista degli allegati');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_BUTTON_GOTO_ATTACHMENTS', 'en', 'Go to the list of attachments');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_TOTAL_SPACE', 'it', 'totale');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_TOTAL_SPACE', 'en', 'total');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_FREE_SPACE', 'it', 'libero');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_FREE_SPACE', 'en', 'free');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_LOGIN_TO_POST', 'it', 'Per rispondere è necessario effetuare l''accesso.');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_POST_LOGIN_TO_POST', 'en', 'In order to reply in this thread please login.');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD', 'it', 'Discussione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_LABEL_THREAD', 'en', 'Thread');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_RSS_LONG', 'it', 'Aggiornamento in tempo reale in formato RSS/XML per: ');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpforum_RSS_LONG', 'en', 'RSS/XML Realtime Updates for: ');
