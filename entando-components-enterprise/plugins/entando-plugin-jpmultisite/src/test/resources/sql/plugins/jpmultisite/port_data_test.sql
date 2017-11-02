INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('jpmultisite_home', 'Multisite', '<frames>
	<frame pos="0">
		<descr>Main Navigation</descr>
	</frame>
	<frame pos="1">
		<descr>Small Column I</descr>
	</frame>
	<frame pos="2">
		<descr>Small Column II</descr>
	</frame>
	<frame pos="3">
		<descr>Small Column III</descr>
	</frame>
	<frame pos="4">
		<descr>Small Column IV</descr>
	</frame>
	<frame pos="5" main="true">
		<descr>Main Column I</descr>
	</frame>
	<frame pos="6">
		<descr>Main Column II</descr>
	</frame>
	<frame pos="7">
		<descr>Main Column III</descr>
	</frame>
	<frame pos="8">
		<descr>Main Column IV</descr>
	</frame>
	<frame pos="9">
		<descr>Main Column V</descr>
	</frame>
	<frame pos="10">
		<descr>Footer</descr>
	</frame>
</frames>', 'jpmultisite');




INSERT INTO contents (contentid, contenttype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART187@asd', 'ART', 'una descrizione particolare', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187@asd" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20051012164415', '20060622194219', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187@asd" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');

INSERT INTO contentrelations (contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ('ART187@asd', NULL, NULL, NULL, NULL, 'free');
INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ('ART187@asd', 'Titolo', 'jacms:title');
INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ('ART187@asd', 'Titolo', 'jacms:title');