<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>
<div class="jppentaho">
<c:set var="validateFrame"><s:property value="validateFrame"/></c:set>
	<s:if test="%{title!=null && title.length()>0}"><h2><s:property value="%{title}" /></h2></s:if>
	<s:else><h2><wp:i18n key="jppentaho_GET_THE_REPORT" /></h2></s:else>
	<form action="<wp:action path="/ExtStr2/do/jppentaho/Front/pentaho/configure.action" />" method="post">
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
				            <li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="message message_error">	
			<h3><s:text name="message.title.ActionErrors" /></h3>
					<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</ul>
			</div>
		</s:if>
		
		<%-- hidden params --%>
		<p class="noscreen">
			<wpsf:hidden name="code" />
			<wpsf:hidden name="solution" />
			<wpsf:hidden name="path" />
			<wpsf:hidden name="name" />
			<wpsf:hidden name="queryString" />
			<wpsf:hidden name="profileParams" />
		</p>
		<div class="report_config_item">
			<p>
				<%//TODO mettere descrizione report <s:property value="description" /> %>
				<span class="bold">(<s:property value="report.name" />)</span>
			</p>
		</div>
		<s:if test="%{paramsToConfigure!=null && paramsToConfigure.size()>0}">
			<s:iterator value="%{paramsToConfigure}" var="current" status="status">
				<s:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></s:set>
				<s:set var="htmlId" value="%{'jppentaho_reportparams_'+#status.count+#rand}" />
				<div class="report_config_item">
					<s:if test="%{#current.parameterRenderType == 'dropdown'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/dropdown.jsp" />
					</s:if>
					<s:elseif test="%{#current.parameterRenderType == 'datepicker'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/datepicker.jsp" />
					</s:elseif>	
					<s:elseif test="%{#current.parameterRenderType == 'list'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/list.jsp" />
					</s:elseif>
					<s:elseif test="%{#current.parameterRenderType == 'radio'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/radio.jsp" />
					</s:elseif>	
					<s:elseif test="%{#current.parameterRenderType == 'checkbox'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/checkbox.jsp" />
					</s:elseif>	
					<s:elseif test="%{#current.parameterRenderType == 'togglebutton'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/togglebutton.jsp" />
					</s:elseif>	
					<s:elseif test="%{#current.parameterRenderType == 'textbox'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/textbox.jsp" />
					</s:elseif>
					<s:elseif test="%{#current.parameterRenderType == 'multi-line'}">
						<s:include value="/WEB-INF/plugins/jppentaho/aps/jsp/internalservlet/report/inc/multi-line.jsp" />
					</s:elseif>
				</div>
			</s:iterator>
		</s:if>
		
		<p class="vai">
			<input type="submit" value="<wp:i18n key="jppentaho_EXECUTE" />" class="button" />
		</p>
	</form>
</div>