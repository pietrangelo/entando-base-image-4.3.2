INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpnotifyNotify', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Publish Notify</property>
<property key="it">Pubblica Notify</property>
</properties>', '<config>
	<parameter name="id">id</parameter>
	<action name="jpnotifyNotifyConfig"/>
</config>','jpnotify', NULL, NULL, 1, 'free');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpnotifyNotify_list_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Notify List and Form</property>
<property key="it">Lista e Form Notify</property>
</properties>', NULL, 'jpnotify', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/jpnotify/Notify/list.action</property>
</properties>', 1, 'free');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ID', 'en', 'id');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ID', 'it', 'id');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_PAYLOAD', 'en', 'payload');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_PAYLOAD', 'it', 'payload');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATESTART', 'en', 'senddate start');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATESTART', 'it', 'senddate start');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATEEND', 'en', 'senddate end');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATEEND', 'it', 'senddate end');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATE', 'en', 'senddate');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDDATE', 'it', 'senddate');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ATTEMPTS', 'en', 'attempts');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ATTEMPTS', 'it', 'attempts');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENT', 'en', 'sent');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENT', 'it', 'sent');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDER', 'en', 'sender');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SENDER', 'it', 'sender');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_RECIPIENT', 'en', 'recipient');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_RECIPIENT', 'it', 'recipient');


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ACTIONS', 'it', 'Actions');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_ACTIONS', 'en', 'Actions');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_NEW', 'it', 'Notify New');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_NEW', 'en', 'Notify New');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_EDIT', 'it', 'Notify Edit');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_EDIT', 'en', 'Notify Edit');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_TRASH', 'it', 'Notify Trash');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_TRASH', 'en', 'Notify Trash');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_TRASH_CONFIRM', 'it', 'Notify Trash confirm');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_TRASH_CONFIRM', 'en', 'Notify Trash confirm');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SEARCH_NOTIFY', 'it', 'Notify search');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_SEARCH_NOTIFY', 'en', 'Notify search');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_NOT_FOUND', 'it', 'Notify not found');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ('jpnotify_NOTIFY_NOT_FOUND', 'en', 'Notify not found');


INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnotify_is_front_Notify_entry', 'jpnotifyNotify_list_form', 'jpnotify', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<@wp.info key="currentLang" var="currentLangVar" />

<#assign js_for_datepicker="jQuery(function($){
$.datepicker.regional[''''it''''] = {
closeText: ''''Chiudi'''',
prevText: ''''&#x3c;Prec'''',
nextText: ''''Succ&#x3e;'''',
currentText: ''''Oggi'''',
monthNames: [''''Gennaio'''',''''Febbraio'''',''''Marzo'''',''''Aprile'''',''''Maggio'''',''''Giugno'''',
''''Luglio'''',''''Agosto'''',''''Settembre'''',''''Ottobre'''',''''Novembre'''',''''Dicembre''''],
monthNamesShort:  [''''Gen'''',''''Feb'''',''''Mar'''',''''Apr'''',''''Mag'''',''''Giu'''',
''''Lug'''',''''Ago'''',''''Set'''',''''Ott'''',''''Nov'''',''''Dic''''],
dayNames: [''''Domenica'''',''''Luned&#236'''',''''Marted&#236'''',''''Mercoled&#236'''',''''Gioved&#236'''',''''Venerd&#236'''',''''Sabato''''],
dayNamesShort: [''''Dom'''',''''Lun'''',''''Mar'''',''''Mer'''',''''Gio'''',''''Ven'''',''''Sab''''],
dayNamesMin: [''''Do'''',''''Lu'''',''''Ma'''',''''Me'''',''''Gi'''',''''Ve'''',''''Sa''''],
weekHeader: ''''Sm'''',
dateFormat: ''''dd/mm/yy'''',
firstDay: 1,
isRTL: false,
showMonthAfterYear: false,
yearSuffix: ''''''''};
});

jQuery(function($) {
 if (Modernizr.touch && Modernizr.inputtypes.date) {
  $.each( $(\"input[data-isdate=true]\"), function(index, item) {
   item.type = ''''date'''';
  });
 } else {
  $.datepicker.setDefaults( $.datepicker.regional[ \"${currentLangVar}\" ] );
  $(\"input[data-isdate=true]\").datepicker({
	changeMonth: true,
	changeYear: true,
	dateFormat: \"dd/mm/yy\"
   });
 }
});
">
<@wp.headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<@wp.headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.1/jquery-ui.js" />
<@wp.headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<@wp.headInfo type="JS_RAW" info="${js_for_datepicker}" />
<section>
<@s.if test="strutsAction==1">
	<h1><@wp.i18n key="jpnotify_NOTIFY_NEW" /></h1>
</@s.if>
<@s.elseif test="strutsAction==2">
	<h1><@wp.i18n key="jpnotify_NOTIFY_EDIT" /></h1>
</@s.elseif>
<form action="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/save.action" />" method="post">
	<@s.if test="hasFieldErrors()">
		<div class="alert alert-error">
			<h2><@s.text name="message.title.FieldErrors" /></h2>
			<ul>
				<@s.iterator value="fieldErrors">
					<@s.iterator value="value">
				<li><@s.property/></li>
					</@s.iterator>
				</@s.iterator>
			</ul>
		</div>
	</@s.if>
	<@s.if test="hasActionErrors()">
		<div class="alert alert-error">
			<h2><@s.text name="message.title.ActionErrors" /></h2>
			<ul>
				<@s.iterator value="actionErrors">
				<li><@s.property/></li>
				</@s.iterator>
			</ul>
		</div>
	</@s.if>
	<p class="sr-only">
		<@wpsf.hidden name="strutsAction" />
		<@wpsf.hidden name="id" />
	</p>
	<fieldset>
		<label for="notify_payload"><@wp.i18n key="jpnotify_NOTIFY_PAYLOAD" /></label>
		<input type="text" name="payload" id="notify_payload" value="<@s.property value="payload" />" />
		<label for="notify_senddate_cal"><@wp.i18n key="jpnotify_NOTIFY_SENDDATE" /></label>
		<input type="text" name="senddate" id="notify_senddate_cal" data-isdate="true" value="<@s.property value="senddate" />" />
		<label for="notify_attempts"><@wp.i18n key="jpnotify_NOTIFY_ATTEMPTS" /></label>
		<input type="text" name="attempts" id="notify_attempts" value="<@s.property value="attempts" />" />
		<label for="notify_sent"><@wp.i18n key="jpnotify_NOTIFY_SENT" /></label>
		<input type="text" name="sent" id="notify_sent" value="<@s.property value="sent" />" />
		<label for="notify_sender"><@wp.i18n key="jpnotify_NOTIFY_SENDER" /></label>
		<input type="text" name="sender" id="notify_sender" value="<@s.property value="sender" />" />
		<label for="notify_recipient"><@wp.i18n key="jpnotify_NOTIFY_RECIPIENT" /></label>
		<input type="text" name="recipient" id="notify_recipient" value="<@s.property value="recipient" />" />
	</fieldset>
	<@wpsf.submit type="button" cssClass="btn btn-primary">
		<@wp.i18n key="SAVE" />
	</@wpsf.submit>
</form>
</section>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnotify_is_front_Notify_error', 'jpnotifyNotify_list_form', 'jpnotify', NULL, '<pre>
   __   _ __   _ __   ___   _ __  
 /''__`\/\`''__\/\`''__\/ __`\/\`''__\
/\  __/\ \ \/ \ \ \//\ \L\ \ \ \/ 
\ \____\\ \_\  \ \_\\ \____/\ \_\ 
 \/____/ \/_/   \/_/ \/___/  \/_/ 
                                  
</pre>', 1);




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnotify_is_front_Notify_list', 'jpnotifyNotify_list_form', 'jpnotify', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>

<@wp.info key="currentLang" var="currentLangVar" />

<#assign js_for_datepicker="jQuery(function($){
$.datepicker.regional[''''it''''] = {
closeText: ''''Chiudi'''',
prevText: ''''&#x3c;Prec'''',
nextText: ''''Succ&#x3e;'''',
currentText: ''''Oggi'''',
monthNames: [''''Gennaio'''',''''Febbraio'''',''''Marzo'''',''''Aprile'''',''''Maggio'''',''''Giugno'''',
''''Luglio'''',''''Agosto'''',''''Settembre'''',''''Ottobre'''',''''Novembre'''',''''Dicembre''''],
monthNamesShort:  [''''Gen'''',''''Feb'''',''''Mar'''',''''Apr'''',''''Mag'''',''''Giu'''',
''''Lug'''',''''Ago'''',''''Set'''',''''Ott'''',''''Nov'''',''''Dic''''],
dayNames: [''''Domenica'''',''''Luned&#236'''',''''Marted&#236'''',''''Mercoled&#236'''',''''Gioved&#236'''',''''Venerd&#236'''',''''Sabato''''],
dayNamesShort: [''''Dom'''',''''Lun'''',''''Mar'''',''''Mer'''',''''Gio'''',''''Ven'''',''''Sab''''],
dayNamesMin: [''''Do'''',''''Lu'''',''''Ma'''',''''Me'''',''''Gi'''',''''Ve'''',''''Sa''''],
weekHeader: ''''Sm'''',
dateFormat: ''''dd/mm/yy'''',
firstDay: 1,
isRTL: false,
showMonthAfterYear: false,
yearSuffix: ''''''''};
});

jQuery(function($) {
 if (Modernizr.touch && Modernizr.inputtypes.date) {
  $.each( $(\"input[data-isdate=true]\"), function(index, item) {
   item.type = ''''date'''';
  });
 } else {
  $.datepicker.setDefaults( $.datepicker.regional[ \"${currentLangVar}\" ] );
  $(\"input[data-isdate=true]\").datepicker({
	changeMonth: true,
	changeYear: true,
	dateFormat: \"dd/mm/yy\"
   });
 }
});
">
<@wp.headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<@wp.headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.1/jquery-ui.js" />
<@wp.headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<@wp.headInfo type="JS_RAW" info="${js_for_datepicker}" />

<section class="notify_list">

<h1><@wp.i18n key="jpnotify_NOTIFY_SEARCH_NOTIFY" /></h1>

<form action="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/search.action" />" method="post" >

  <fieldset>
		<label for="notify_id"><@wp.i18n key="jpnotify_NOTIFY_ID" /></label>
		<input type="text" name="id" id="notify_id" value="<@s.property value="id" />" />
		<label for="notify_payload"><@wp.i18n key="jpnotify_NOTIFY_PAYLOAD" /></label>
		<input type="text" name="payload" id="notify_payload" value="<@s.property value="payload" />" />
		<label for="notify_senddateStart_cal"><@wp.i18n key="jpnotify_NOTIFY_SENDDATESTART" /></label>
		<input type="text" name="senddateStart" id="notify_senddateStart_cal" data-isdate="true" value="<@s.property value="senddateStart" />" />
		<label for="notify_senddateEnd_cal"><@wp.i18n key="jpnotify_NOTIFY_SENDDATEEND" /></label>
		<input type="text" name="senddateEnd" id="notify_senddateEnd_cal" data-isdate="true" value="<@s.property value="senddateEnd" />" />
		<label for="notify_attempts"><@wp.i18n key="jpnotify_NOTIFY_ATTEMPTS" /></label>
		<input type="text" name="attempts" id="notify_attempts" value="<@s.property value="attempts" />" />
		<label for="notify_sent"><@wp.i18n key="jpnotify_NOTIFY_SENT" /></label>
		<input type="text" name="sent" id="notify_sent" value="<@s.property value="sent" />" />
		<label for="notify_sender"><@wp.i18n key="jpnotify_NOTIFY_SENDER" /></label>
		<input type="text" name="sender" id="notify_sender" value="<@s.property value="sender" />" />
		<label for="notify_recipient"><@wp.i18n key="jpnotify_NOTIFY_RECIPIENT" /></label>
		<input type="text" name="recipient" id="notify_recipient" value="<@s.property value="recipient" />" />
  </fieldset>

  <button type="submit" class="btn btn-primary">
    <@wp.i18n key="SEARCH" />
  </button>

<@wpsa.subset source="notifysId" count=10 objectName="groupNotify" advanced=true offset=5>
<@s.set name="group" value="#groupNotify" />
<@wp.freemarkerTemplateParameter var="group" valueName="groupNotify" removeOnEndTag=true >
<div class="margin-medium-vertical text-center">
	<@s.include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<@s.include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	<#--
	<@wp.fragment code="default_pagerInfo_is" escapeXml=false />
	<@wp.fragment code="default_pagerFormBlock_is" escapeXml=false />
	-->
</div>
<p>
  <a href="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/new.action"></@wp.action>" title="<@wp.i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <@wp.i18n key="NEW" />
  </a>
</p>
<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
  <th class="text-right">
    <@wp.i18n key="jpnotify_NOTIFY_ID" />
  </th>
	<th
                 class="text-left"><@wp.i18n key="jpnotify_NOTIFY_PAYLOAD" /></th>
	<th
     class="text-center"            ><@wp.i18n key="jpnotify_NOTIFY_SENDDATE" /></th>
	<th
         class="text-right"        ><@wp.i18n key="jpnotify_NOTIFY_ATTEMPTS" /></th>
	<th
         class="text-right"        ><@wp.i18n key="jpnotify_NOTIFY_SENT" /></th>
	<th
                 class="text-left"><@wp.i18n key="jpnotify_NOTIFY_SENDER" /></th>
	<th
                 class="text-left"><@wp.i18n key="jpnotify_NOTIFY_RECIPIENT" /></th>
	<th>
    <@wp.i18n key="jpnotify_NOTIFY_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<@s.iterator id="notifyId">
<tr>
	<@s.set var="notify" value="%{getNotify(#notifyId)}" />
	<td class="text-right">
    <a
      href="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/edit.action"><@wp.parameter name="id"><@s.property value="#notify.id" /></@wp.parameter></@wp.action>"
      title="<@wp.i18n key="EDIT" />: <@s.property value="#notify.id" />"
      class="label label-info display-block">
    <@s.property value="#notify.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <@s.property value="#notify.payload" />  </td>
	<td
     class="text-center"        >
    <code><@s.date name="#notify.senddate" format="dd/MM/yyyy" /></code>  </td>
	<td
         class="text-right"    >
    <@s.property value="#notify.attempts" />  </td>
	<td
         class="text-right"    >
    <@s.property value="#notify.sent" />  </td>
	<td
            >
    <@s.property value="#notify.sender" />  </td>
	<td
            >
    <@s.property value="#notify.recipient" />  </td>
	<td class="text-center">
    <a
      href="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/trash.action"><@wp.parameter name="id"><@s.property value="#notify.id" /></@wp.parameter></@wp.action>"
      title="<@wp.i18n key="TRASH" />: <@s.property value="#notify.id" />"
      class="btn btn-warning btn-small">
      <span class="icon-trash icon-white"></span>&#32;
      <@wp.i18n key="TRASH" />
    </a>
  </td>
</tr>
</@s.iterator>
</tbody>
</table>
<div class="margin-medium-vertical text-center">
	<@s.include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	<#--
	<@wp.fragment code="default_pagerFormBlock_is" escapeXml=false />
	-->
</div>
</@wp.freemarkerTemplateParameter>
</@wpsa.subset>
</form>
</section>', 1);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpnotify_is_front_Notify_trash', 'jpnotifyNotify_list_form', 'jpnotify', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>

<section>
	<h1><wp:i18n key="jpnotify_NOTIFY_TRASH" /></h1>
	<form action="<@wp.action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/delete.action" />" method="post">
		<@s.if test="hasFieldErrors()">
			<div class="alert alert-error">
				<h2><@s.text name="message.title.FieldErrors" /></h2>
				<ul>
					<@s.iterator value="fieldErrors">
						<@s.iterator value="value">
						<li><@s.property /></li>
						</@s.iterator>
					</@s.iterator>
				</ul>
			</div>
		</@s.if>
		<@s.if test="hasActionErrors()">
			<div class="alert alert-error">
				<h2><@s.text name="message.title.ActionErrors" /></h2>
				<ul>
					<@s.iterator value="actionErrors">
					<li><@s.property /></li>
					</@s.iterator>
				</ul>
			</div>
		</@s.if>
		<p class="sr-only">
			<@wpsf.hidden name="strutsAction" />
			<@wpsf.hidden name="id" />
		</p>
		<div class="alert alert-warning">
			<p>
				<@wp.i18n key="jpnotify_NOTIFY_TRASH_CONFIRM" />&#32;<@wp.i18n key="jpnotify_NOTIFY_ID" />&#32;<@s.property value="id" />?
			</p>
			<p>
				<@wpsf.submit type="button" cssClass="btn btn-warning">
					<span class="icon-trash icon-white"></span>&#32;
					<@wp.i18n key="TRASH" />
				</@wpsf.submit>
			</p>
		</div>
	</form>
</section>', 1);

