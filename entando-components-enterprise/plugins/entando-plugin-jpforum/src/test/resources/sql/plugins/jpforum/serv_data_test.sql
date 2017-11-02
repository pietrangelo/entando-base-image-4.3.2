INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('forum', 'forum', -1, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">forum</property><property key="it">forum</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">forum</property><property key="it">forum</property></properties>', 1);
INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('test_1', 'forum', 1, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">test_1</property><property key="it">test_1</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">test_1</property><property key="it">test_1</property></properties>', 1);
INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('a', 'forum', 2, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">a</property><property key="it">a</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">a</property><property key="it">a</property></properties>', 1);
INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('b', 'forum', 3, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">b</property><property key="it">b</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">b</property><property key="it">b</property></properties>', 1);
INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('f', 'e', 1, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">f</property><property key="it">f</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">f</property><property key="it">f</property></properties>', 0);
INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) VALUES ('e', 'test_1', 1, 'true', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">e</property><property key="it">e</property></properties>', '<?xml version="1.0" encoding="UTF-8"?><properties><property key="en">e</property><property key="it">e</property></properties>', 1);

INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('e', 'administrators');
INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('e', 'customers');
INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('f', 'administrators');
INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('a', 'free');
INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('b', 'free');
INSERT INTO jpforum_sectiongroups (section, groupname) VALUES ('forum', 'free');

INSERT INTO jpforum_threads (code, sectionid, username, openthread, pin) VALUES (1, 'b', 'admin', 'true', 'false');

INSERT INTO jpforum_posts (code, username, title, text, postdate, threadid) VALUES (1, 'admin', 'title', '<?xml version="1.0" encoding="UTF-8"?><post>text</post>', '2009-12-21 00:00:00', 1);

INSERT INTO jpforum_messages (code, sender, recipient, title, body, messagetype, messagedate, toread) VALUES (1, 'admin', 'mainEditor', 'test1', 'test message body', 'MESSAGE', '2010-01-08 12:00:00', 'true');
INSERT INTO jpforum_messages (code, sender, recipient, title, body, messagetype, messagedate, toread) VALUES (2, 'mainEditor', 'admin', 'test2', 'test message body', 'MESSAGE', '2010-01-08 12:01:00', 'true');
INSERT INTO jpforum_messages (code, sender, recipient, title, body, messagetype, messagedate, toread) VALUES (3, 'admin', 'mainEditor', 'test3', 'test warn', 'WARN', '2010-01-08 12:02:00', 'true');
INSERT INTO jpforum_messages (code, sender, recipient, title, body, messagetype, messagedate, toread) VALUES (4, 'admin', 'mainEditor', 'test4', 'test warn', 'WARN', '2010-01-08 12:01:30', 'true');
