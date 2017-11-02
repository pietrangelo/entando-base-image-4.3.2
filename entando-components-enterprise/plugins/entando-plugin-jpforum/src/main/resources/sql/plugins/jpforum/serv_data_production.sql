INSERT INTO authpermissions(permissionname, descr) VALUES ('jpforum_forumUser', 'utilizzo del forum');
INSERT INTO authpermissions(permissionname, descr) VALUES ('jpforum_sectionModerator', 'moderazione sezioni forum');
INSERT INTO authpermissions(permissionname, descr) VALUES ('jpforum_superuser', 'moderazione globale forum');

INSERT INTO authroles(rolename, descr) VALUES ('jpforum_roleUser', 'utente base del forum');
INSERT INTO authroles(rolename, descr) VALUES ('jpforum_moderator', 'moderatore sezione');
INSERT INTO authroles(rolename, descr) VALUES ('jpforum_superuser', 'moderatore globale forum');

INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_roleUser', 'jpforum_forumUser');
INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_moderator', 'jpforum_forumUser');
INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_moderator', 'jpforum_sectionModerator');

INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_superuser', 'jpforum_forumUser');
INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_superuser', 'jpforum_sectionModerator');
INSERT INTO authrolepermissions(rolename, permissionname) VALUES ('jpforum_superuser', 'jpforum_superuser');
