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
<wp:headInfo type="CSS" info="widgets/form_list.css" />
--%>

<section class="form_list">

<h1><wp:i18n key="jpemailmarketing_FORM_SEARCH_FORM" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/search.action" />" method="post" >

		<fieldset>
			<label for="form_courseId"><wp:i18n key="jpemailmarketing_FORM_COURSEID" /></label>
			<input type="text" name="courseId" id="form_courseId" value="<s:property value="courseId" />" />
			<label for="form_fileCoverName"><wp:i18n key="jpemailmarketing_FORM_FILECOVERNAME" /></label>
			<input type="text" name="fileCoverName" id="form_fileCoverName" value="<s:property value="fileCoverName" />" />
			<label for="form_fileIncentiveName"><wp:i18n key="jpemailmarketing_FORM_FILEINCENTIVENAME" /></label>
			<input type="text" name="fileIncentiveName" id="form_fileIncentiveName" value="<s:property value="fileIncentiveName" />" />
			<label for="form_emailSubject"><wp:i18n key="jpemailmarketing_FORM_EMAILSUBJECT" /></label>
			<input type="text" name="emailSubject" id="form_emailSubject" value="<s:property value="emailSubject" />" />
			<label for="form_emailText"><wp:i18n key="jpemailmarketing_FORM_EMAILTEXT" /></label>
			<input type="text" name="emailText" id="form_emailText" value="<s:property value="emailText" />" />
			<label for="form_emailButton"><wp:i18n key="jpemailmarketing_FORM_EMAILBUTTON" /></label>
			<input type="text" name="emailButton" id="form_emailButton" value="<s:property value="emailButton" />" />
			<label for="form_emailMessageFriendly"><wp:i18n key="jpemailmarketing_FORM_EMAILMESSAGEFRIENDLY" /></label>
			<input type="text" name="emailMessageFriendly" id="form_emailMessageFriendly" value="<s:property value="emailMessageFriendly" />" />
			<label for="form_createdatStart_cal"><wp:i18n key="jpemailmarketing_FORM_CREATEDATSTART" /></label>
			<input type="text" name="createdatStart" id="form_createdatStart_cal" data-isdate="true" value="<s:property value="createdatStart" />" />
			<label for="form_createdatEnd_cal"><wp:i18n key="jpemailmarketing_FORM_CREATEDATEND" /></label>
			<input type="text" name="createdatEnd" id="form_createdatEnd_cal" data-isdate="true" value="<s:property value="createdatEnd" />" />
			<label for="form_updatedatStart_cal"><wp:i18n key="jpemailmarketing_FORM_UPDATEDATSTART" /></label>
			<input type="text" name="updatedatStart" id="form_updatedatStart_cal" data-isdate="true" value="<s:property value="updatedatStart" />" />
			<label for="form_updatedatEnd_cal"><wp:i18n key="jpemailmarketing_FORM_UPDATEDATEND" /></label>
			<input type="text" name="updatedatEnd" id="form_updatedatEnd_cal" data-isdate="true" value="<s:property value="updatedatEnd" />" />
		</fieldset>

  <button type="submit" class="btn btn-primary">
    <wp:i18n key="SEARCH" />
  </button>

<wpsa:subset source="formsId" count="10" objectName="groupForm" advanced="true" offset="5">
<s:set name="group" value="#groupForm" />

<div class="margin-medium-vertical text-center">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
  </a>
</p>

<table class="table table-bordered table-condensed table-striped">
<thead>
<tr>
	<th class="text-right">
	  <wp:i18n key="jpemailmarketing_FORM_CAMPAIGNID" />
	</th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_FILECOVERNAME" /></th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_FILEINCENTIVENAME" /></th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_EMAILSUBJECT" /></th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_EMAILTEXT" /></th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_EMAILBUTTON" /></th>
	<th class="text-left"><wp:i18n key="jpemailmarketing_FORM_EMAILMESSAGEFRIENDLY" /></th>
	<th class="text-center"><wp:i18n key="jpemailmarketing_FORM_CREATEDAT" /></th>
	<th class="text-center"><wp:i18n key="jpemailmarketing_FORM_UPDATEDAT" /></th>
	<th>
    <wp:i18n key="jpemailmarketing_FORM_ACTIONS" />
  </th>
</tr>
</thead>
<tbody>
<s:iterator id="formCampaignId">
<tr>
	<s:set var="form" value="%{getForm(#formCampaignId)}" />
	<td class="text-right">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/edit.action"><wp:parameter name="campaignId"><s:property value="#form.campaignId" /></wp:parameter></wp:action>"
      title="<wp:i18n key="EDIT" />: <s:property value="#form.campaignId" />"
      class="label label-info display-block">
    <s:property value="#form.campaignId" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <s:property value="#form.fileCoverName" />  </td>
	<td
            >
    <s:property value="#form.fileIncentiveName" />  </td>
	<td
            >
    <s:property value="#form.emailSubject" />  </td>
	<td
            >
    <s:property value="#form.emailText" />  </td>
	<td
            >
    <s:property value="#form.emailButton" />  </td>
	<td
            >
    <s:property value="#form.emailMessageFriendly" />  </td>
	<td
     class="text-center"        >
    <code><s:date name="#form.createdat" format="dd/MM/yyyy" /></code>  </td>
	<td
     class="text-center"        >
    <code><s:date name="#form.updatedat" format="dd/MM/yyyy" /></code>  </td>
	<td class="text-center">
    <a
      href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/trash.action"><wp:parameter name="campaignId"><s:property value="#form.campaignId" /></wp:parameter></wp:action>"
      title="<wp:i18n key="TRASH" />: <s:property value="#form.campaignId" />"
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