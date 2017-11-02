<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpsocial.title.contentQueue" />
    </span>
</h1>
<s:set var="post" value="pendingPost"/>
<s:set name="actualContentId" value="%{#post.getContent().getId()}" />
<s:set name="content" value="%{#post.getContent()}" />
<div class="panel panel-default">
<s:set var="supportedProvidersVar" value="%{getSupportedProviders()}" />
    <div class="panel-body">
	<s:text name="jpsocial.label.queues" />:&nbsp;<code><s:property value="#actualContentId" /></code> &ndash; <s:property value="#content.descr" /> (<s:property value="%{getSmallContentType(#content.typeCode).descr}" />)
<ul>
	<s:iterator value="%{#supportedProvidersVar}" var="socialNetwork">
		<s:if test="%{isLogged(#socialNetwork)}">
			<li><span class="monospace"><s:property value="%{#socialNetwork.substring(0,1).toUpperCase()+#socialNetwork.substring(1)}" /></li>

		</s:if>
	</s:iterator>
</ul>
    </div>
</div>
<s:form cssClass="form-horizontal">
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
	<wpsf:hidden name="contentOnSessionMarker" />
	
	<%-- back to content button --%>
	<wpsa:actionParam action="backToContentEdit" var="backAction">
		<wpsa:actionSubParam name="contentId" value="%{#actualContentId}" />
		<wpsa:actionSubParam name="origin" value="%{'1'}" />
	</wpsa:actionParam>
	<wpsf:submit
		action="%{#backAction}"
		cssClass="btn btn-default btn-block"
		value="%{getText('jpsocial.label.continue')}" />
        </div>
    </div>
</s:form>