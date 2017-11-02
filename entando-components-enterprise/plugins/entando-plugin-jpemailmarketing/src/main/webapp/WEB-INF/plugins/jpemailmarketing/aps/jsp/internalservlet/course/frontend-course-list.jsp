<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>

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
<wp:headInfo type="CSS" info="widgets/course_list.css" />
--%>

<section class="course_list">

<h1><wp:i18n key="jpemailmarketing_COURSE_SEARCH_COURSE" /></h1>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Course/search.action" />" method="post" >

		<fieldset>
		<label for="course_id"><wp:i18n key="jpemailmarketing_COURSE_ID" /></label>
		<input type="text" name="id" id="course_id" value="<s:property value="id" />" />
		<label for="course_name"><wp:i18n key="jpemailmarketing_COURSE_NAME" /></label>
		<input type="text" name="name" id="course_name" value="<s:property value="name" />" />
		<label for="course_sender"><wp:i18n key="jpemailmarketing_COURSE_SENDER" /></label>
		<input type="text" name="sender" id="course_sender" value="<s:property value="sender" />" />
		<label for="course_active"><wp:i18n key="jpemailmarketing_COURSE_ACTIVE" /></label>
		<input type="text" name="active" id="course_active" value="<s:property value="active" />" />
		<label for="course_cronexp"><wp:i18n key="jpemailmarketing_COURSE_CRONEXP" /></label>
		<input type="text" name="cronexp" id="course_cronexp" value="<s:property value="cronexp" />" />
		<label for="course_crontimezoneid"><wp:i18n key="jpemailmarketing_COURSE_CRONTIMEZONEID" /></label>
		<input type="text" name="crontimezoneid" id="course_crontimezoneid" value="<s:property value="crontimezoneid" />" />
		<label for="course_createdatStart_cal"><wp:i18n key="jpemailmarketing_COURSE_CREATEDATSTART" /></label>
		<input type="text" name="createdatStart" id="course_createdatStart_cal" data-isdate="true" value="<s:property value="createdatStart" />" />
		<label for="course_createdatEnd_cal"><wp:i18n key="jpemailmarketing_COURSE_CREATEDATEND" /></label>
		<input type="text" name="createdatEnd" id="course_createdatEnd_cal" data-isdate="true" value="<s:property value="createdatEnd" />" />
		<label for="course_updatedatStart_cal"><wp:i18n key="jpemailmarketing_COURSE_UPDATEDATSTART" /></label>
		<input type="text" name="updatedatStart" id="course_updatedatStart_cal" data-isdate="true" value="<s:property value="updatedatStart" />" />
		<label for="course_updatedatEnd_cal"><wp:i18n key="jpemailmarketing_COURSE_UPDATEDATEND" /></label>
		<input type="text" name="updatedatEnd" id="course_updatedatEnd_cal" data-isdate="true" value="<s:property value="updatedatEnd" />" />
		</fieldset>

		<button type="submit" class="btn btn-primary">
			<wp:i18n key="SEARCH" />
		</button>

<wpsa:subset source="coursesId" count="10" objectName="groupCourse" advanced="true" offset="5">
			<s:set name="group" value="#groupCourse" />

			<div class="margin-medium-vertical text-center">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>

			<p>
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Course/new.action"></wp:action>" title="<wp:i18n key="NEW" />" class="btn btn-info">
    <span class="icon-plus-sign icon-white"></span>&#32;
    <wp:i18n key="NEW" />
				</a>
			</p>

			<table class="table table-bordered table-condensed table-striped">
				<thead>
					<tr>
  <th class="text-right">
    <wp:i18n key="jpemailmarketing_COURSE_ID" />
  </th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_COURSE_NAME" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_COURSE_SENDER" /></th>
	<th
         class="text-right"        ><wp:i18n key="jpemailmarketing_COURSE_ACTIVE" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_COURSE_CRONEXP" /></th>
	<th
                 class="text-left"><wp:i18n key="jpemailmarketing_COURSE_CRONTIMEZONEID" /></th>
	<th
     class="text-center"            ><wp:i18n key="jpemailmarketing_COURSE_CREATEDAT" /></th>
	<th
     class="text-center"            ><wp:i18n key="jpemailmarketing_COURSE_UPDATEDAT" /></th>
	<th>
    <wp:i18n key="jpemailmarketing_COURSE_ACTIONS" />
  </th>
					</tr>
				</thead>
				<tbody>
					<s:iterator id="courseId">
						<tr>
							<s:set var="course" value="%{getCourse(#courseId)}" />
	<td class="text-right">
    <a
								href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Course/edit.action"><wp:parameter name="id"><s:property value="#course.id" /></wp:parameter></wp:action>"
								title="<wp:i18n key="EDIT" />: <s:property value="#course.id" />"
      class="label label-info display-block">
    <s:property value="#course.id" />&#32;
    <span class="icon-edit icon-white"></span>
    </a>
  </td>
	<td
            >
    <s:property value="#course.name" />  </td>
	<td
            >
    <s:property value="#course.sender" />  </td>
	<td
         class="text-right"    >
    <s:property value="#course.active" />  </td>
	<td
            >
    <s:property value="#course.cronexp" />  </td>
	<td
            >
    <s:property value="#course.crontimezoneid" />  </td>
	<td
     class="text-center"        >
    <code><s:date name="#course.createdat" format="dd/MM/yyyy" /></code>  </td>
	<td
     class="text-center"        >
    <code><s:date name="#course.updatedat" format="dd/MM/yyyy" /></code>  </td>
	<td class="text-center">
    <a
								href="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Course/trash.action"><wp:parameter name="id"><s:property value="#course.id" /></wp:parameter></wp:action>"
								title="<wp:i18n key="TRASH" />: <s:property value="#course.id" />"
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