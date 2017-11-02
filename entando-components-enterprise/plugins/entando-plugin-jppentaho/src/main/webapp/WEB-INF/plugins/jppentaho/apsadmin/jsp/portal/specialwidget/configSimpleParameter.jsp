<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>
<h2><s:text name="title.configPage" /></h2>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<h3>Showlet: <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>

<s:form action="saveConfigSimpleParameter">
	<p class="noscreen">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
	</p>
	
	<s:iterator value="showlet.type.typeParameters" id="showletParam" >
		<p>
			<label for="showletparam_<s:property value="#showletParam.name" />"><s:property value="#showletParam.descr" /></label>:<br />
		 	<wpsf:textfield cssClass="text" id="%{'showletparam_'+#showletParam.name}" name="%{#showletParam.name}" value="%{showlet.config[#showletParam.name]}" />
		</p>
	</s:iterator>

	<p>
		<wpsf:submit cssClass="button"  value="%{getText('label.save')}" />
	</p>
</s:form>