<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>
<div class="jppentaho">
	<s:set var="reportsParams" value="%{reportsParam}" />
	<h2><wp:i18n key="jppentaho_GET_THE_REPORT" /></h2>
	<s:if test="%{null != #reportsParams && #reportsParams.size() > 0}">
		<p>
			<s:set var="solution_name" value="%{#reportsParams.get(0).solution}" />
			<wp:i18n key="jppentaho_CHOOSE_A_REPORT" />&#32;<em><s:property value="%{#solution_name}" /></em>
		</p>

		<ul>
			<s:iterator value="#reportsParams" var="curr">
				<li>
				<%-- PRPT --%>
				<s:if test="%{#curr.type == 0}">
					<a
						href="<wp:action path="/ExtStr2/do/jppentaho/Front/pentaho/report.action"/>&amp;solution=<s:property value="%{#curr.solution}"/>&amp;path=<s:property value="%{#curr.path}" />&amp;name=<s:property value="%{#curr.name}" />" >
						<s:property value="%{#curr.description}" />
					</a>
				</s:if>
				<%-- XACTION --%>
				<s:elseif test="%{#curr.type == 1}">
					<a
						href="<wp:action path="/ExtStr2/do/jppentaho/Front/pentaho/xaction.action"/>&amp;solution=<s:property value="%{#curr.solution}"/>&amp;path=<s:property value="%{#curr.path}" />&amp;action=<s:property value="%{#curr.action}" />" >
						<s:property value="%{#curr.description}" />
					</a>
				</s:elseif>
				</li>
			</s:iterator>
		</ul>
	</s:if>
	<s:else>
		<p>
			<wp:i18n key="jppentaho_NOREPORT_FOR_SOLUTION" />
		</p>
	</s:else>

	<p class="vai">
		<a class="fakebutton" href="<wp:action path="/ExtStr2/do/jppentaho/Front/pentahoConfig/config.action"/>" ><wp:i18n key="jppentaho_CHANGE_PATH" /></a>
	</p>
	<s:set var="reportsParams" value="%{null}" />
</div>