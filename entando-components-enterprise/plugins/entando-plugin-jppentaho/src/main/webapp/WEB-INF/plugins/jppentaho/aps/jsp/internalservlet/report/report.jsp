<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>
<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>
<div class="jppentaho">
<c:set var="validateFrame"><s:property value="validateFrame"/></c:set>
	<c:choose>
		<c:when test="${sessionScope.currentUser != 'guest'}">
			<s:if test="%{getReportParamUIInfo().reportTitle.length()>0}">
				<h2><s:property value="%{getReportParamUIInfo().reportTitle}" /></h2>
			</s:if>
			<s:else>
				<h2><wp:i18n key="jppentaho_GET_THE_REPORT" /></h2>
			</s:else>
			<form action="<wp:action path="/ExtStr2/do/jppentaho/Front/pentaho/generate.action" />" method="post">
				
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
					<wpsf:hidden name="name" />
					<wpsf:hidden name="solution" />
					<wpsf:hidden name="path" />
					<input name="submitted" value="true" type="hidden"/>
				</p>
				<div class="report_config_item">
					<p>
						<%-- <span class="bold"><wp:i18n key="jppentaho_LABEL_REPORT" /></span>: --%>
						<%--
							<wp:i18n key="jppentaho_GET_THE_REPORT" />
						--%>
						<%//TODO mettere descrizione report <s:property value="description" /> %>
						<span class="bold">(<s:property value="name" />)</span>
					</p>
				</div>
				
				<%-- output type --%>
				<div class="report_config_item">
				
					<p>
						<s:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 999) %><%= java.lang.Math.round(java.lang.Math.random() * 999) %></s:set>
						<label class="bold" for="jppentaho_outputtype_<s:property value="#rand" />"><wp:i18n key="jppentaho_LABEL_OUTPUTTYPE" /></label>:<br />
						<s:if test="%{getReportParamUIInfo().getOutputTypeLock()}">
							<input name="output" value="<s:property value="%{getReportParamUIInfo().getOutputType()}" />" type="hidden"/>
						</s:if>
						<select name="output" id="jppentaho_outputtype_<s:property value="#rand" />" <s:if test="%{getReportParamUIInfo().getOutputTypeLock()}"> disabled="disabled"</s:if>>
							<s:iterator value="%{outputTypes}" var="current">
								<option value="<s:property value="%{#current}" />" <s:if test="%{getReportParamUIInfo().getOutputType() == #current}"> selected="selected"</s:if>><s:property value="%{#current}" /></option>
							</s:iterator>
						</select>
					</p>			
				</div>
				 
				<s:if test="%{null != getReportParamUIInfo().getParam() && getReportParamUIInfo().getParam().size() >0 }">
					<s:iterator value="%{getReportParamUIInfo().getParam()}" var="current" status="status">
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
					&#32;
				</p>
				<p class="vai">
					<s:if test="%{getType() == 'single'}">
						<a href="<wp:action path="/ExtStr2/do/jppentaho/Front/pentahoConfig/configSingleReport.action"/>" ><wp:i18n key="jppentaho_CHANGEREPORT" /></a>									
					</s:if>
					<s:else>
						<a href="<wp:action path="/ExtStr2/do/jppentaho/Front/pentaho/list.action" />" ><wp:i18n key="jppentaho_MSG_BACK_TO_REPORTS" /></a>
					</s:else>
				</p>
			</form>
		</c:when>
		<c:otherwise>
			<p><wp:i18n key="jppentaho_MSG_PLEASE_LOGIN" /></p>
		</c:otherwise>
	</c:choose>

</div>