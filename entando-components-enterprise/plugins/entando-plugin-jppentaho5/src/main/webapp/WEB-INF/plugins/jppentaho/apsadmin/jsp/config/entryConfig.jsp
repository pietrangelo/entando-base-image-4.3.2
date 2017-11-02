<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="jppentaho5.title.config" /></span>
</h1>

<div id="main">
	<s:form action="save">

		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="actionErrors">
                        <li><s:property /></li>
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
						<li><s:property /></li>
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
							<li><s:property /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<fieldset class="col-xs-12">
			<legend><s:text name="jppentaho5.pentaho.instance" /></legend>

			<div class="form-group">
				<label for="jppentaho5.label.instance"><s:text name="jppentaho5.label.instance" /></label>
				<wpsf:textfield name="config.instance" id="instance" cssClass="form-control" />
			</div>

		</fieldset>

		<fieldset class="col-xs-12">
			<legend><s:text name="jppentaho5.pentaho.authentication.param" /></legend>

			<div class="checkbox">
				<label for="jppentaho5.label.resetUrl">
					<wpsf:checkbox name="config.casAuthentication" id="casAuthentication"   />
					<s:text name="jppentaho5.label.cas" />
				</label>
			</div>

			<div class="form-group">
				<label for="jppentaho5.label.username"><s:text name="jppentaho5.label.username" /></label>
				<wpsf:textfield name="config.username" id="username" cssClass="form-control" />
			</div>

			<div class="form-group">
				<label for="jppentaho5.label.password"><s:text name="jppentaho5.label.password" /></label>
				<wpsf:textfield name="config.password" id="password" cssClass="form-control" />
			</div>



			<div class="checkbox">
				<label for="jppentaho5.label.tapPlugin">
					<wpsf:checkbox name="config.tapPlugin" id="tapPlugin"   />
					<s:text name="jppentaho5.label.tapPlugin" />
				</label>
			</div>

		</fieldset>


		<div class="form-horizontal">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit action="test" type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="jppentaho5.label.test" />
				</wpsf:submit>
			</div>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
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