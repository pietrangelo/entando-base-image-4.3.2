<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="pth" uri="/jppentaho5-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<%--<wp:headInfo type="JS_EXT" info="http://code.jquery.com/jquery-2.1.0.min.js" />--%>
<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.11.2/jquery-ui.js" />
<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.min.css" />
<link href="<wp:resourceURL />plugins/jppentaho5/administration/css/jppentaho5.css" rel="stylesheet" type="text/css">

<c:set var="pathVar"><wp:currentWidget param="config" configParam="pathParam"/></c:set>
<c:set var="csvArgsVar"><wp:currentWidget param="config" configParam="argsParam"/></c:set>

<p>
	<wp:i18n key="jppentaho5_PUBLISH_REPORT" />
</p>

<%--
<c:out value="${pathVar}"/><br/>
<c:out value="${csvArgsVar}"/><br/>
--%>

<wp:info key="currentLang" var="currentLang" />

<c:set var="js_for_datepicker">
/* Italian initialization for the jQuery UI date picker plugin. */
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
	$.datepicker.setDefaults( $.datepicker.regional[ "<c:out value="${currentLang}" />" ] );
	$("input[data-isdate=true]").datepicker({
				changeMonth: true,
				changeYear: true,
				dateFormat: "yy-mm-dd'T'00:00:00.000"
		});
});
</c:set>
<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />

<pth:displayReport paramVar="parametersVar" urlVar="urlVar" path="${pathVar}" csvArgs="${csvArgsVar}" customPrefix="title" />

<form action="<wp:url/>" method="post">

	<c:forEach var="paramVar" items="${parametersVar}">
	
	<div>
		<%-- display parameter name only if not hidden --%>
		<c:if test="${paramVar.attribute['parameter-render-type'].value != 'hidden'}">
			<p><c:out value="${paramVar.name}"/></p>
		</c:if>
		<!-- 
			name: <c:out value="${paramVar.name}"/>, type: <c:out value="${paramVar.type}"/>
		 -->
		<c:choose>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'hidden'}">
			<!-- default configuration parameter -->
			<input type="hidden" name="${paramVar.formName}" value="${paramVar.values['hidden'].value }"/>
			</c:when>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'dropdown'}">
				<!-- DROPDOWN -->
				
				<select name="${paramVar.formName}">
				<c:forEach var="valueVar" items="${paramVar.values}">
					<!-- 
						value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
					 -->
					<option value="${valueVar.key}" <c:if test="${valueVar.value.selected}">selected="selected"</c:if> ><c:out value="${valueVar.value.label}" /></option>
				</c:forEach>
				</select>
				
			</c:when>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'list'}">
				<!-- LIST -->
				
				<select name="${paramVar.formName}" multiple="multiple">
				<c:forEach var="valueVar" items="${paramVar.values}">
					<!--
						value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
					-->
					<option value="${valueVar.key}" <c:if test="${valueVar.value.selected}">selected="selected"</c:if> ><c:out value="${valueVar.value.label}" /></option>
				</c:forEach>
				</select>
				
			</c:when>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'radio'}">
				<!-- RADIO  -->
				
				<c:forEach var="valueVar" items="${paramVar.values}">
				<input type="radio" name="${paramVar.formName}" value="${valueVar.key}" <c:if test="${valueVar.value.selected}">checked="checked"</c:if> >
					<c:out value="${valueVar.value.label}" /><br/>
				</c:forEach>
				
			</c:when>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'checkbox'}">
				<!-- CHECKBOX -->
				
				<c:forEach var="valueVar" items="${paramVar.values}">
				<input type="checkbox" name="${paramVar.formName}" value="${valueVar.key}" <c:if test="${valueVar.value.selected}">checked="checked"</c:if> >
					<c:out value="${valueVar.value.label}" /><br/>
				</c:forEach>
				
			</c:when>
<%--
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'togglebutton'}">
				<h4>togglebutton</h4>
			</c:when>
--%>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'textbox'}">
				<!--TEXTBOX -->
				
				<c:if test="${paramVar.isList}" >
					<c:forEach var="valueVar" items="${paramVar.values}">
						<!-- 
							value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
						-->
						<c:if test="${valueVar.value.selected}"> <c:set var="defaultTextVar" value="${valueVar.value.label}"/> </c:if>
					</c:forEach>
				</c:if>
				<input name="${paramVar.formName}" type="text"  value="${defaultTextVar}">
				
			</c:when>
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'datepicker'}">
				<!-- DATEPICKER -->
				
				<c:forEach var="valueVar" items="${paramVar.values}">
					<c:if test="${not empty startInterval}">
						<c:set var="endInterval" value="${valueVar.key}"/>
					</c:if>
					<c:if test="${empty startInterval}">
						<c:set var="startInterval" value="${valueVar.key}"/>
					</c:if>
					<!-- 
					value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
					-->
				</c:forEach>
				
				<c:if test="${not empty endInterval}">
				<p>
					from <c:out value="${startInterval}"/> to <c:out value="${endInterval}"/>
				</p>
				</c:if>
				<input id="<c:out value="${paramVar.formName}" />" name="<c:out value="${paramVar.formName}" />" value="${startInterval}" type="text" data-isdate="true" class="input-xlarge" />
				
			</c:when>
			<%--
			<c:when test="${paramVar.attribute['parameter-render-type'].value == 'multi-line'}">
				<h4>multi-line</h4>
				
				<c:forEach var="valueVar" items="${paramVar.values}">
					value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
				</c:forEach>
				
			</c:when>
			--%>
			<c:otherwise>
				<p>
					expected type: <c:out value="${paramVar.type}"/>
				</p>
				
				<h4>unknown <c:out value="${paramVar.attribute['parameter-render-type'].value}"/> </h4>
				
				<c:if test="${paramVar.isList}" >
					<c:forEach var="valueVar" items="${paramVar.values}">
						<!-- 
						-->
							value: <c:out value="${valueVar.key}" />, type: <c:out value="${valueVar.value.type}" />, selected: <c:out value="${valueVar.value.selected}" /><br/>
						<c:if test="${valueVar.value.selected}"> <c:set var="defaultTextVar" value="${valueVar.value.label}"/> </c:if>
					</c:forEach>
				</c:if>
				<input name="${paramVar.formName}" type="text"  value="${defaultTextVar}">
				
			</c:otherwise>
		</c:choose>
		<br/>
	</div>
	
	<c:set var="startInterval" value=""/>
	<c:set var="endInterval" value=""/>
	</c:forEach>
	
	<button value="submit"><wp:i18n key="jppentaho5_RENDER_REPORT" /></button>
	
</form>

<%--
    dropdown
    list
    radio
    checkbox
    togglebutton
    textbox
    datepicker
    multi-line
--%>