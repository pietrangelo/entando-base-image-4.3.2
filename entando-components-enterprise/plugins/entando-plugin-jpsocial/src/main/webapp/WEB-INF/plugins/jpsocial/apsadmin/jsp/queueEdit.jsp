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

	<s:form >
		<wpsf:hidden name="contentOnSessionMarker" />
		
		<s:if test="%{!#showShareButton}" >
			<div class="alert alert info">
				<s:text name="jpsocial.message.cannotPost" />
			</div>
		</s:if>
		<%-- decide whether to return to list or the edit page --%>
		<s:if test="%{#post.fast == 'true'}" >
			<s:set name="destination" value="2"/>
		</s:if>
		<s:else>
			<s:set name="destination" value="1" />
		</s:else>

		<s:set var="supportedProvidersVar" value="%{getSupportedProviders()}" />

		<s:iterator value="%{#supportedProvidersVar}" var="socialNetwork">

			<s:set value="%{getDeliveryQueue(#socialNetwork)}" var="attributesList" />

			<s:if test="%{#attributesList.size() > 0}" >

				<fieldset class="subsection-ligth margin-more-bottom">
					<legend><s:property value="%{#socialNetwork.substring(0,1).toUpperCase()+#socialNetwork.substring(1)}" /></legend>

					<s:if test="%{isLogged(#socialNetwork)}">
						<em><s:text name="jpsocial.message.acceddGranted" /></em>

						<%-- check whether the user is logged at least to one social network --%>
						<s:set var="showShareButton" value="true" />

					</s:if>
					<s:else>
						<p>
							<a href="<s:property value="%{getAuthenticationURL(#socialNetwork)}"/>">
								<s:text name="%{'jpsocial.connect.to.'+#socialNetwork+'.before.share'}" />
							</a>
						</p>
					</s:else>

					<s:iterator value="#attributesList" var="item" >
						<p>
							<s:if test="%{#post.fast}" >
								<wpsf:hidden name="%{#item.id+'_confirm'}" value="%{'activation_'+#item.id}" />
							</s:if>
							<s:else>
								<wpsf:checkbox name="%{#item.id+'_confirm'}" id="%{'activation_'+#item.id}"/>
							</s:else>
							<label for="<s:property value="%{'activation_'+#item.id}" />">
								<s:property value="#item.name" />
								(<s:property value="#item.lang" />)
							</label>
							<br />
							<wpsf:textarea name="%{#item.id}" value="%{#item.text}" cols="30" rows="3" />
						</p>

						<p>
							<%-- Show select for youtube  --%>
							<s:if test="%{#item.queue == 'youtube' && #item.type == 2}">
								<label for="youtubeCategoryType"><s:text name="jpsocial.label.category"/>:</label><br />
								<wpsf:select name="%{#item.id+'_ytCategory'}" id="%{#item.id+'_ytCategory'}" cssClass="text" list="youtubeCategories" listKey="id" listValue="description" />
							</s:if>
						</p>

					</s:iterator> <%-- Attributes list --%>
				</s:if>

			</fieldset>
		</s:iterator> <%-- Social network --%>

		<s:set name="actualContentId" value="%{#post.getContent().getId()}" />

		<%-- Cancel button --%>
		<wpsa:actionParam action="cancelPost" var="cancelPostAction">
			<wpsa:actionSubParam name="contentId" value="%{#actualContentId}" />
			<wpsa:actionSubParam name="origin" value="%{#destination}" />
		</wpsa:actionParam>
		<wpsf:submit
			action="%{#cancelPostAction}"
			cssClass="button"
			value="%{getText('jpsocial.label.cancel')}" />

		<%-- Rovoke credentials --%>
		<wpsa:actionParam action="revokeCredentials" var="revokePostAction">
			<wpsa:actionSubParam name="contentId" value="%{#actualContentId}" />
			<wpsa:actionSubParam name="origin" value="%{#destination}" />
		</wpsa:actionParam>
		<wpsf:submit
			action="%{#revokePostAction}"
			cssClass="button"
			value="%{getText('jpsocial.label.revoke')}" />

		<%-- ShareButton  --%>
		<p class="centerText">
			<wpsa:actionParam action="postQueue" var="postQueueAction" >
				<wpsa:actionSubParam name="origin" value="%{'1'}" />
			</wpsa:actionParam>
			<wpsf:submit
				action="%{#postQueueAction}"
				cssClass="button"
				value="%{getText('jpsocial.hookpoint.attribute.share')}" />
		</p>

	</s:form>

</div>
