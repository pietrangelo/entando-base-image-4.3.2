<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.jpsalesforce.config" /></span>
</h1>

<div id="main">
	<s:form action="save">

		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
			<div class="alert alert-info alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
				<ul class="margin-base-vertical">
					<s:iterator value="actionMessages">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none">
					<s:text name="messages.error" />
					&ensp;
					<span
						class=" text-muted icon fa fa-question-circle cursor-pointer"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-field-messages"></span>
				</h2>
				<ul class="collapse margin-small-top" id="content-field-messages">
					<s:iterator value="fieldErrors" var="e">
						<s:iterator value="#e.value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<fieldset class="col-xs-12">
			<legend><s:text name="jpsalesforce.legend.authentication.param" /></legend>
			
			<div class="form-group">
				<label for="jpsalesforce.label.oid"><s:text name="jpsalesforce.label.oid" /></label>
				<wpsf:textfield name="config.oid" id="oid" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.appId"><s:text name="jpsalesforce.label.appId" /></label>
				<wpsf:textfield name="config.appId" id="appId" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.appSecret"><s:text name="jpsalesforce.label.appSecret" /></label>
				<wpsf:textfield name="config.appSecret" id="appSecret" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.username"><s:text name="jpsalesforce.label.userId" /></label>
				<wpsf:textfield name="config.userId" id="userId" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.password"><s:text name="jpsalesforce.label.userPwd" /></label>
				<wpsf:textfield name="config.userPwd" id="userPwd" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.securityToken"><s:text name="jpsalesforce.label.securityToken" /></label>
				<wpsf:textfield name="config.securityToken" id="password" cssClass="form-control" />
			</div>
			
		</fieldset>
		
		<fieldset class="col-xs-12">
			<legend><s:text name="jpsalesforce.legend.authentication.url" /></legend>
			
			<div class="alert alert-info">
				<s:text name="jpsalesforce.legend.note" />
			</div>
			
			<div class="checkbox">
					<label for="jpsalesforce.label.authorizationNewUrl">
						<wpsf:checkbox name="resetEndopoint" id="authorizationNewUrl" />
						<s:text name="jpsalesforce.label.reset" />
					</label>
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.loginEndpoint"><s:text name="jpsalesforce.label.loginEndpoint" /></label>
				<wpsf:textfield name="config.loginEndpoint" id="loginEndpoint" cssClass="form-control" />
			</div>

			<div class="form-group">
				<label for="jpsalesforce.label.authorizationTokenUrl"><s:text name="jpsalesforce.label.tokenEndpoint" /></label>
				<wpsf:textfield name="config.tokenEndpoint" id="tokenEndpoint" cssClass="form-control" />
			</div>
			
			<div class="form-group">
				<label for="jpsalesforce.label.preferredVersion"><s:text name="jpsalesforce.label.preferredVersion" /></label>
				<wpsf:textfield name="config.preferredVersion" id="preferredVersion" cssClass="form-control" />
			</div>
			
		</fieldset>
	
		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit action="test" type="button" cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="jpsalesforce.label.test" />
					</wpsf:submit>
				</div>
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
			</div>
		</div>
	
	</s:form>
</div>
