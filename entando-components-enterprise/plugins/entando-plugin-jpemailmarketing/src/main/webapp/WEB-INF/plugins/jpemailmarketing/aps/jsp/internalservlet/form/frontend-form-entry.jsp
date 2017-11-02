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
	<h1><wp:i18n key="jpemailmarketing_FORM_NEW" /></h1>
</s:if>
<s:elseif test="strutsAction==2">
	<h1><wp:i18n key="jpemailmarketing_FORM_EDIT" /></h1>
</s:elseif>

<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpemailmarketing/Form/save.action" />" method="post">
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
			<wpsf:hidden name="courseId" />
	</p>

		<fieldset>
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
			<label for="form_createdat_cal"><wp:i18n key="jpemailmarketing_FORM_CREATEDAT" /></label>
			<input type="text" name="createdat" id="form_createdat_cal" data-isdate="true" value="<s:property value="createdat" />" />
			<label for="form_updatedat_cal"><wp:i18n key="jpemailmarketing_FORM_UPDATEDAT" /></label>
			<input type="text" name="updatedat" id="form_updatedat_cal" data-isdate="true" value="<s:property value="updatedat" />" />
		</fieldset>

	<wpsf:submit type="button" cssClass="btn btn-primary">
		<wp:i18n key="SAVE" />
	</wpsf:submit>

</form>
</section>