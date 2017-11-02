<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

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

<section>
<s:if test="strutsAction==1">
	<h1><wp:i18n key="jpemailmarketing_SUBSCRIBER_NEW" /></h1>
</s:if>
<s:elseif test="strutsAction==2">
	<h1><wp:i18n key="jpemailmarketing_SUBSCRIBER_EDIT" /></h1>
</s:elseif>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Subscriber/save.action" />" method="post">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-error">
			<h2><s:text name="message.title.FieldErrors" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
				<li><s:property/></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="alert alert-error">
			<h2><s:text name="message.title.ActionErrors" /></h2>
			<ul>
				<s:iterator value="actionErrors">
				<li><s:property/></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="id" />
	</p>

	<fieldset>
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
		<label for="subscriber_createdat_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_CREATEDAT" /></label>
		<input type="text" name="createdat" id="subscriber_createdat_cal" data-isdate="true" value="<s:property value="createdat" />" />
		<label for="subscriber_updatedat_cal"><wp:i18n key="jpemailmarketing_SUBSCRIBER_UPDATEDAT" /></label>
		<input type="text" name="updatedat" id="subscriber_updatedat_cal" data-isdate="true" value="<s:property value="updatedat" />" />
	</fieldset>

	<wpsf:submit type="button" cssClass="btn btn-primary">
		<wp:i18n key="SAVE" />
	</wpsf:submit>

</form>
</section>