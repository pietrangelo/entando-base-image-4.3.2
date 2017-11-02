<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<%--CAL START --%>

<wp:info key="currentLang" var="currentLang" />

<c:set var="js_for_datepicker">
/* Italian initialisation for the jQuery UI date picker plugin. */
/* Written by Antonello Pasella (antonello.pasella@gmail.com). */
jQuery(function($){
$.datepicker.regional['it'] = {
closeText: 'Chiudi',
prevText: '&#x3c;Prec',
nextText: 'Succ&#x3e;',
currentText: 'Oggi',
monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
'Lug','Ago','Set','Ott','Nov','Dic'],
dayNames: ['Domenica','Luned&#236','Marted&#236','Mercoled&#236','Gioved&#236','Venerd&#236','Sabato'],
dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
dayNamesMin: ['Do','Lu','Ma','Me','Gi','Ve','Sa'],
weekHeader: 'Sm',
dateFormat: 'dd/mm/yy',
firstDay: 1,
isRTL: false,
showMonthAfterYear: false,
yearSuffix: ''};
});

jQuery(function($){
if (Modernizr.touch && Modernizr.inputtypes.date) {
$.each( $("input[data-isdate=true]"), function(index, item) {
item.type = 'date';
});
} else {
$.datepicker.setDefaults( $.datepicker.regional[ "<c:out value="${currentLang}" />" ] );
$("input[data-isdate=true]").datepicker({
       changeMonth: true,
       changeYear: true,
       dateFormat: "dd/mm/yy"
     });
}
});
</c:set>
<wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.1/jquery-ui.js" />
<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />

<%--CAL END --%>

<%--
optional CSS
<wp:headInfo type="CSS" info="widgets/notify_list.css" />
--%>

<section class="notify_list">

<h1><wp:i18n key="jpnotify_NOTIFY_SEARCH_NOTIFY" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/search.action" />" method="post" >

  <fieldset>
		<label for="notify_id"><wp:i18n key="jpnotify_NOTIFY_ID" /></label>
		<input type="text" name="id" id="notify_id" value="<s:property value="id" />" />
		<label for="notify_payload"><wp:i18n key="jpnotify_NOTIFY_PAYLOAD" /></label>
		<input type="text" name="payload" id="notify_payload" value="<s:property value="payload" />" />
		<label for="notify_senddateStart_cal"><wp:i18n key="jpnotify_NOTIFY_SENDDATESTART" /></label>
		<input type="text" name="senddateStart" id="notify_senddateStart_cal" data-isdate="true" value="<s:property value="senddateStart" />" />
		<label for="notify_senddateEnd_cal"><wp:i18n key="jpnotify_NOTIFY_SENDDATEEND" /></label>
		<input type="text" name="senddateEnd" id="notify_senddateEnd_cal" data-isdate="true" value="<s:property value="senddateEnd" />" />
		<label for="notify_attempts"><wp:i18n key="jpnotify_NOTIFY_ATTEMPTS" /></label>
		<input type="text" name="attempts" id="notify_attempts" value="<s:property value="attempts" />" />
		<label for="notify_sent"><wp:i18n key="jpnotify_NOTIFY_SENT" /></label>
		<input type="text" name="sent" id="notify_sent" value="<s:property value="sent" />" />
		<label for="notify_sender"><wp:i18n key="jpnotify_NOTIFY_SENDER" /></label>
		<input type="text" name="sender" id="notify_sender" value="<s:property value="sender" />" />
		<label for="notify_recipient"><wp:i18n key="jpnotify_NOTIFY_RECIPIENT" /></label>
		<input type="text" name="recipient" id="notify_recipient" value="<s:property value="recipient" />" />
  </fieldset>

  <button type="submit" class="btn btn-primary">
    <wp:i18n key="SEARCH" />
  </button>

<wpsa:subset source="notifysId" count="10" objectName="groupNotify" advanced="true" offset="5">
<s:set name="group" value="#groupNotify" />

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
  </a>
</p>

<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
  <th class="text-right">
    <wp:i18n key="jpnotify_NOTIFY_ID" />
  </th>
	<th
                 class="text-left"><wp:i18n key="jpnotify_NOTIFY_PAYLOAD" /></th>
	<th
     class="text-center"            ><wp:i18n key="jpnotify_NOTIFY_SENDDATE" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpnotify_NOTIFY_ATTEMPTS" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpnotify_NOTIFY_SENT" /></th>
	<th
                 class="text-left"><wp:i18n key="jpnotify_NOTIFY_SENDER" /></th>
	<th
                 class="text-left"><wp:i18n key="jpnotify_NOTIFY_RECIPIENT" /></th>
	<th>
    <wp:i18n key="jpnotify_NOTIFY_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<s:iterator id="notifyId">
<tr>
	<s:set var="notify" value="%{getNotify(#notifyId)}" />
	<td class="text-right">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/edit.action"><wp:parameter name="id"><s:property value="#notify.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="EDIT" />: <s:property value="#notify.id" />"
      class="label label-info display-block">
    <s:property value="#notify.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <s:property value="#notify.payload" />  </td>
	<td
     class="text-center"        >
    <code><s:date name="#notify.senddate" format="dd/MM/yyyy" /></code>  </td>
	<td
         class="text-right"    >
    <s:property value="#notify.attempts" />  </td>
	<td
         class="text-right"    >
    <s:property value="#notify.sent" />  </td>
	<td
            >
    <s:property value="#notify.sender" />  </td>
	<td
            >
    <s:property value="#notify.recipient" />  </td>
	<td class="text-center">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpnotify/Notify/trash.action"><wp:parameter name="id"><s:property value="#notify.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="TRASH" />: <s:property value="#notify.id" />"
      class="btn btn-warning btn-small">
      <span class="icon-trash icon-white"></span>&#32;
      <wp:i18n key="TRASH" />
    </a>
  </td>
</tr>
</s:iterator>
</tbody>
</table>

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</form>
</section>