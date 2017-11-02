<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:set var="state" value="#report_input_control_v2.state" />
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<div class="control-group <s:if test="#report_input_control_v2.type =='singleValueDate'"> jpjasper-input-block-2_ic_type_single_value-date </s:if><s:elseif test="#report_input_control_v2.type =='singleValueNumber'"> jpjasper-input-block-2_ic_type_single_value-number </s:elseif>">
	<%-- singleValueDate --%>
		<s:if test="#report_input_control_v2.type =='singleValueDate'">
			<label
				class="control-label"
				for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />">
					<s:property value="#report_input_control_v2.label" />
					<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
			</label>
			<div class="controls">
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#control.listValue" />" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#current_ICC_config.frontEndOverride"/>" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.dataType" value="3" />
				<input
					data-isdate="true"
					type="text"
					id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />"
					name="formInputControlConfigMap.<s:property value="#state.id" />.value"
					value="<s:property value="#state.value" />"
					/>
			</div>
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
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
});

jQuery(function($){
	if (Modernizr.touch && Modernizr.inputtypes.date) {
		$.each(	$("input[data-isdate=true]"), function(index, item) {
			item.type = 'date';
		});
	} else {
		$.datepicker.setDefaults( $.datepicker.regional[ "<c:out value="${currentLang}" />" ] );
		$("input[data-isdate=true]").datepicker({
      			changeMonth: true,
      			changeYear: true,
      			dateFormat: "yy-mm-dd"
    		});
	}
});
</c:set>
			<wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
			<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js" />
			<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.min.css" />
			<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />

		</s:if>
	<%-- singleValueNumber --%>
		<s:elseif test="#report_input_control_v2.type =='singleValueNumber'">
			<label
				class="control-label"
				for="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />">
				<s:property value="#report_input_control_v2.label" />
				<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
			</label>
			<div class="controls">
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.id" value="<s:property value="#state.id" />" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.listValue" value="<s:property value="#control.listValue" />" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.frontEndOverride" value="<s:property value="#report_input_control_v2.frontEndOverride" />" />
				<input type="hidden" name="formInputControlConfigMap.<s:property value="#state.id" />.dataType" value="2" />
				<input
					type="text"
					id="jpjasper-input-<s:property value="#state.id" />-<s:property value="#attr.random" />"
					name="formInputControlConfigMap.<s:property value="#state.id" />.value"
					value="<s:property value="#state.value" />"
					/>
			</div>
		</s:elseif>
	<%-- other? --%>
		<s:else>
			<label class="control-label">
				<s:property value="#report_input_control_v2.label" />
				<s:include value="/WEB-INF/plugins/jpjasper/aps/jsp/internalservlet/report/modules/inc/mandatory-mark.jsp" />
			</label>
			<div class="controls">
				<span class="text-warning">
					<s:property value="#report_input_control_v2.type"/> NOT IMPLEMENTED
				</span>
			</div>
		</s:else>
</div>