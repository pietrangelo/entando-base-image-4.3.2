<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 
	DATEPICKER
--%>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/calendar.css"/>
<wp:headInfo type="JS" info="lib/calendar_wiz.js"/>
<c:set var="datepicker_code">
window.addEvent("domready", function(){
	var obj = {};
	obj['<s:property value="#htmlId" />'] = "d/m/Y";
	var myCal = new Calendar(obj);
	myCal.options.navigation = 1; 
	myCal.options.months = ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'];
	myCal.options.days = ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />'];
	myCal.options.prevText = "<s:text name="calendar.label.prevText" />";	//Mese precedente 
	myCal.options.nextText = "<s:text name="calendar.label.nextText" />";	//Mese successivo
	myCal.options.introText = "<s:text name="calendar.label.introText" />";	//Benvenuto nel calendario		
});
</c:set>
<wp:headInfo type="JS_RAW" info="${datepicker_code}"/>
<p>
	<label class="bold" for="<s:property value="#htmlId" />"><s:property value="#current.name" /></label>:<br />
	<input id="<s:property value="#htmlId" />" name="<s:property value="#current.name" />" value="<c:out value="${param[current.name]}" />" type="text" class="text pentahoDatePicker"/>
	<span class="inlineNote">dd/MM/yyyy</span>
</p>