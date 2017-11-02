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
<wp:headInfo type="CSS" info="widgets/subscriber_list.css" />
--%>

<section class="subscriber_list">

<h1><wp:i18n key="jpemailmarketing_SUBSCRIBER_SEARCH_SUBSCRIBER" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/search.action" />" method="post" >

  <fieldset>
		<label for="subscriber_id"><wp:i18n key="jpemailmarketing_SUBSCRIBER_ID" /></label>
		<input type="text" name="id" id="subscriber_id" value="<s:property value="id" />" />
		<label for="subscriber_courseId"><wp:i18n key="jpemailmarketing_SUBSCRIBER_COURSEID" /></label>
		<input type="text" name="courseId" id="subscriber_courseId" value="<s:property value="courseId" />" />
		<label for="subscriber_name"><wp:i18n key="jpemailmarketing_SUBSCRIBER_NAME" /></label>
		<input type="text" name="name" id="subscriber_name" value="<s:property value="name" />" />
		<label for="subscriber_email"><wp:i18n key="jpemailmarketing_SUBSCRIBER_EMAIL" /></label>
		<input type="text" name="email" id="subscriber_email" value="<s:property value="email" />" />
		<label for="subscriber_hash"><wp:i18n key="jpemailmarketing_SUBSCRIBER_HASH" /></label>
		<input type="text" name="hash" id="subscriber_hash" value="<s:property value="hash" />" />
		<label for="subscriber_status"><wp:i18n key="jpemailmarketing_SUBSCRIBER_STATUS" /></label>
		<input type="text" name="status" id="subscriber_status" value="<s:property value="status" />" />
		<label for="subscriber_createdatStart_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_CREATEDATSTART" /></label>
		<input type="text" name="createdatStart" id="subscriber_createdatStart_cal" data-isdate="true" value="<s:property value="createdatStart" />" />
		<label for="subscriber_createdatEnd_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_CREATEDATEND" /></label>
		<input type="text" name="createdatEnd" id="subscriber_createdatEnd_cal" data-isdate="true" value="<s:property value="createdatEnd" />" />
		<label for="subscriber_updatedatStart_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_UPDATEDATSTART" /></label>
		<input type="text" name="updatedatStart" id="subscriber_updatedatStart_cal" data-isdate="true" value="<s:property value="updatedatStart" />" />
		<label for="subscriber_updatedatEnd_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_UPDATEDATEND" /></label>
		<input type="text" name="updatedatEnd" id="subscriber_updatedatEnd_cal" data-isdate="true" value="<s:property value="updatedatEnd" />" />
  </fieldset>

  <button type="submit" class="btn btn-primary">
    <wp:i18n key="SEARCH" />
  </button>

<wpsa:subset source="subscribersId" count="10" objectName="groupSubscriber" advanced="true" offset="5">
<s:set name="group" value="#groupSubscriber" />

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
  </a>
</p>

<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
  <th class="text-right">
    <wp:i18n key="jpemailmarketing_SUBSCRIBER_ID" />
  </th>
	<th
         class="text-right"        ><wp:i18n key="jpemailmarketing_SUBSCRIBER_COURSEID" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_SUBSCRIBER_NAME" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_SUBSCRIBER_EMAIL" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_SUBSCRIBER_HASH" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_SUBSCRIBER_STATUS" /></th>
	<th
     class="text-center"            ><wp:i18n key="jpemailmarketing_SUBSCRIBER_CREATEDAT" /></th>
	<th
     class="text-center"            ><wp:i18n key="jpemailmarketing_SUBSCRIBER_UPDATEDAT" /></th>
	<th>
    <wp:i18n key="jpemailmarketing_SUBSCRIBER_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<s:iterator id="subscriberId">
<tr>
	<s:set var="subscriber" value="%{getSubscriber(#subscriberId)}" />
	<td class="text-right">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/edit.action"><wp:parameter name="id"><s:property value="#subscriber.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="EDIT" />: <s:property value="#subscriber.id" />"
      class="label label-info display-block">
    <s:property value="#subscriber.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
         class="text-right"    >
    <s:property value="#subscriber.courseId" />  </td>
	<td
            >
    <s:property value="#subscriber.name" />  </td>
	<td
            >
    <s:property value="#subscriber.email" />  </td>
	<td
            >
    <s:property value="#subscriber.hash" />  </td>
	<td
            >
    <s:property value="#subscriber.status" />  </td>
	<td
     class="text-center"        >
    <code><s:date name="#subscriber.createdat" format="dd/MM/yyyy" /></code>  </td>
	<td
     class="text-center"        >
    <code><s:date name="#subscriber.updatedat" format="dd/MM/yyyy" /></code>  </td>
	<td class="text-center">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/trash.action"><wp:parameter name="id"><s:property value="#subscriber.id" /></wp:parameter></wp:action>"
      title="<wp:i18n key="TRASH" />: <s:property value="#subscriber.id" />"
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