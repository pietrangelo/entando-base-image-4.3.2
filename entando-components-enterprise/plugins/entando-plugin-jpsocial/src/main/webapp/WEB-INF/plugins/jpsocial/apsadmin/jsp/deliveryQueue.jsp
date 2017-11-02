<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpcocial.title.editQueue" />
    </span>
</h1>
<div id="main">

	<s:set var="post" value="pendingPost"/>
	
	<s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<ul class="margin-base-vertical">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property/></li>
						</s:iterator>
					</s:iterator>
			</ul>
		</div>
	</s:if>
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

	<s:form>
		<wpsf:hidden name="contentOnSessionMarker" />
		<s:set var="destination" value="origin" />
		<s:set var="supportedProvidersVar" value="%{getSupportedProviders()}" />
		<s:iterator value="%{#supportedProvidersVar}" var="socialNetwork">
			<s:set value="%{getDeliveryQueue(#socialNetwork)}" var="attributesList" />
			<s:if test="%{#attributesList.size() > 0}" >
				<fieldset class="col-xs-12">
					<legend><s:property value="%{#socialNetwork.substring(0,1).toUpperCase()+#socialNetwork.substring(1)}" /></legend>
					<s:if test="%{isLogged(#socialNetwork)}">
                                            <div class="panel panel-default">
                                                <div class="panel-body">
						<em><s:text name="jpsocial.message.acceddGranted" /></em>
                                                    
                                                </div>
                                            </div>
					</s:if>
					<s:else>
						<div class="form-group">
							<a href="<s:property value="%{getAuthenticationURL(#socialNetwork)}"/>">
								<s:text name="%{'jpsocial.connect.to.'+#socialNetwork+'.before.share'}" />
							</a>
						</div>
					</s:else>
					<s:iterator value="#attributesList" var="item" >
						<div class="form-group">
							<label class="control-label" for="<s:property value="%{'activation_'+#item.id}" />">
                                                                <code class="label label-info"><s:property value="#item.lang" /></code>
								<s:property value="#item.name" />
							<s:if test="%{#post.fast}" >
								<wpsf:hidden name="%{#item.id+'_confirm'}" value="%{'activation_'+#item.id}" />
							</s:if>
							<s:else>
                                                            <span class="margin-small-left">
								<wpsf:checkbox name="%{#item.id+'_confirm'}" id="%{'activation_'+#item.id}"/>
                                                            </span>
							</s:else>
							</label>
							<wpsf:textarea cssClass="form-control" name="%{#item.id}" value="%{#item.text}" cols="30" rows="3" />
						</div>
                                                <div class="form-group">
                                                    
							<%-- Show select for youtube  --%>
							<s:if test="%{#item.queue == 'youtube' && #item.type == 2}">
								<label for="youtubeCategoryType"><s:text name="jpsocial.label.category"/></label>
								<wpsf:select name="%{#item.id+'_ytCategory'}" id="%{#item.id+'_ytCategory'}" cssClass="form-control" list="youtubeCategories" listKey="id" listValue="description" />
							</s:if>
                                                </div>
					</s:iterator> <%-- Attributes list --%>
				</s:if>
			</fieldset>
		</s:iterator> <%-- Social network --%>

		<s:set name="actualContentId" value="%{#post.getContent().getId()}" />

                <div class="form-group">
                    <div class="btn-group">
                        
                    
		<%-- Cancel button --%>

		<wpsa:actionParam action="cancelPost" var="cancelPostAction">
			<wpsa:actionSubParam name="contentId" value="%{#actualContentId}" />
			<wpsa:actionSubParam name="origin" value="%{#destination}" />
		</wpsa:actionParam>
		<wpsf:submit
			action="%{#cancelPostAction}"
			cssClass="btn btn-default"
			value="%{getText('jpsocial.label.cancel')}" />

		<%-- ShareButton  --%>
			<wpsa:actionParam action="postQueue" var="postQueueAction" >
				<wpsa:actionSubParam name="origin" value="%{'1'}" />
			</wpsa:actionParam>
			<wpsf:submit
				action="%{#postQueueAction}"
				cssClass="btn btn-default"
				value="%{getText('jpsocial.hookpoint.attribute.share')}" />
                    </div>
		</div>

		<s:hidden name="permalink"/>
		<s:hidden name="idObject"/>
		<s:hidden name="service"/>

	</s:form>

</div>
