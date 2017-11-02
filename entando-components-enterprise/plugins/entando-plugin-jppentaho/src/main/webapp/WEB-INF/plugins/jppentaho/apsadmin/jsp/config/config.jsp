<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="jppentaho.title.pentahoManagement" /></h1>
<div id="main">
	<s:form action="save" >
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
				<h2><s:text name="message.title.FieldErrors" /></h2>
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
					<h2><s:text name="message.title.ActionErrors" /></h2>
					<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
				<h2><s:text name="messages.confirm" /></h2>
				<ul>
					<s:iterator value="actionMessages">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<fieldset class="margin-more-top">
			<legend><s:text name="jppentaho.server" /></legend>
			<p>
				<label for="config.debug" class="basic-mint-label"><s:text name="jppentaho.config.label.debug" />:</label>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.debug" id="config.debug" cssClass="radiocheck" />
			</p>
			<p>
				<label for="config.serverUsername" class="basic-mint-label"><s:text name="jppentaho.config.label.serverUsername" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverUsername" id="config.serverUsername" cssClass="text" />
			</p>
			<p>
				<label for="config.serverPassword" class="basic-mint-label"><s:text name="jppentaho.config.label.serverPassword" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverPassword" id="config.serverPassword" cssClass="text" />
			</p>
			<p>
				<label for="config.serverLocalProtocol" class="basic-mint-label"><s:text name="jppentaho.config.label.serverLocalProtocol" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverLocalProtocol" id="config.serverLocalProtocol" cssClass="text" />
			</p>
			<p>
				<label for="config.serverLocalUrl" class="basic-mint-label"><s:text name="jppentaho.config.label.serverLocalUrl" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverLocalUrl" id="config.serverLocalUrl" cssClass="text" />
			</p>
			<p>
				<label for="config.serverLocalPort" class="basic-mint-label"><s:text name="jppentaho.config.label.serverLocalPort" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverLocalPort" id="config.serverLocalPort" cssClass="text" />
			</p>
			<p>
				<label for="config.serverProtocol" class="basic-mint-label"><s:text name="jppentaho.config.label.serverProtocol" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverProtocol" id="config.serverProtocol" cssClass="text" />
			</p>
			<p>
				<label for="config.serverUrl" class="basic-mint-label"><s:text name="jppentaho.config.label.serverUrl" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverUrl" id="config.serverUrl" cssClass="text" />
			</p>
			<p>
				<label for="config.serverPort" class="basic-mint-label"><s:text name="jppentaho.config.label.serverPort" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverPort" id="config.serverPort" cssClass="text" />
			</p>
                        
			<p>
				<label for="config.serverContextPath" class="basic-mint-label"><s:text name="jppentaho.config.label.serverContextPath" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverContextPath" id="config.serverContextPath" cssClass="text" />
			</p>
                        
			<p>
				<label for="config.reportDefBasePath" class="basic-mint-label"><s:text name="jppentaho.config.label.reportDefBasePath" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.reportDefBasePath" id="config.reportDefBasePath" cssClass="text" />
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.serverVisibleFromClient" id="debug" cssClass="radiocheck" />&#32;<label for="debug"><s:text name="jppentaho.config.label.serverVisibleFromClient" /></label>
			</p>
			<p>
				<label for="config.reportDetailPage" class="basic-mint-label"><s:text name="jppentaho.config.label.reportDetailPage" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.reportDetailPage" id="config.reportDetailPage" cssClass="text" />
			</p>
		</fieldset>
		<fieldset>
			<legend><s:text name="jppentaho.dataIntegration"></s:text></legend>
				<p>
					<label for="config.dataIntegrationRepositoryName" class="basic-mint-label"><s:text name="jppentaho.config.label.dataIntegrationRepositoryName" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="config.dataIntegrationRepositoryName" id="config.dataIntegrationRepositoryName" cssClass="text" />
				</p>
				<p>
					<label for="config.dataIntegrationUsername" class="basic-mint-label"><s:text name="jppentaho.config.label.dataIntegrationUsername" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="config.dataIntegrationUsername" id="config.dataIntegrationUsername" cssClass="text" />
				</p>
				<p>
					<label for="config.dataIntegrationPassword" class="basic-mint-label"><s:text name="jppentaho.config.label.dataIntegrationPassword" />:</label>
					<wpsf:password useTabindexAutoIncrement="true" name="config.dataIntegrationPassword" id="config.dataIntegrationPassword" cssClass="text" />
				</p>
		</fieldset>
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
	</s:form>
</div>