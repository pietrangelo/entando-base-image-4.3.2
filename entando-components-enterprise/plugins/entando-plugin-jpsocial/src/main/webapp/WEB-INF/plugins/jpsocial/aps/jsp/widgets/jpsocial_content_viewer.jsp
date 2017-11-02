<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="jpsocial" uri="/jpsocial"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<jacms:contentInfo param="authToEdit" var="canEditThis" />
<jacms:contentInfo param="contentId" var="myContentId" />
<jpsocial:contentPermalink contentId="${myContentId}" var="permalink" />


<c:if test="${canEditThis}">
    <div class="bar-content-edit">
        <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/edit.action?contentId=<jacms:contentInfo param="contentId" />&amp;backend_client_gui=advanced" class="btn btn-info">
            <wp:i18n key="EDIT_THIS_CONTENT" /> <i class="icon-edit icon-white"></i></a>
    </div>
</c:if>

<wp:url var="currentUrl" paramRepeat="true" />
<c:if test="${param.social_status eq 'ok'}">
    <div class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
        <strong><c:out value="${'Success.'}"/></strong>
    </div>
</c:if>

<jacms:content publishExtraTitle="true" />

<c:set var="testFb"><wp:info key="systemParam" paramName="applicationBaseURL" />/do/jpsocial/Front/Share/intro.action</c:set>
<wp:pageWithWidget widgetTypeCode="jpsocial_entryPost" var="post_page" listResult="false"/>
<wp:url page="${post_page.code}" var="url_post" />
<c:url value="${url_post}" var="facebookTest">
    <c:param name="provider" value="facebook" />
    <c:param name="redirectUrl" value="${currentUrl}" />
    <c:param name="service" value="jacmsContentManager" />
    <c:param name="permalink" value="${permalink}" />
    <c:param name="textToShare" value="testText" />
    <c:param name="objectId" value="${myContentId}" />
</c:url>
<c:url value="${url_post}" var="twitterTest">
    <c:param name="provider" value="twitter" />
    <c:param name="textToShare" value="testText" />
    <c:param name="redirectUrl" value="${currentUrl}" />
    <c:param name="service" value="jacmsContentManager" />
    <c:param name="permalink" value="${permalink}" />
    <c:param name="objectId" value="${myContentId}" />
</c:url>
<div class="col-xs-12 margin-medium-vertical">
    <div class="btn-group">
        <a class="btn btn-default" href="${facebookTest}">
            <wp:i18n key="jpsocial_LOGIN_FACEBOOK"/>
<!--            <span class="icon icon-facebook"></span>-->
        </a>
        <a class="btn btn-default" href="${twitterTest}">
            <wp:i18n key="jpsocial_LOGIN_TWITTER"/>
<!--            <span class="icon icon-twitter"></span>    -->
        </a>
    </div>
</div>