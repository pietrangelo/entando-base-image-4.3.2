<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1>
	<c:if test="${provider == 'facebook' }" ><wp:i18n key="jpsocial_FB_POST_TITLE" /></c:if>
	<c:if test="${provider == 'twitter' }" ><wp:i18n key="jpsocial_TITLE_TWITTER" /></c:if>
</h1>
    <%-- <c:out value="${provider}"/>
    <c:out value="${permalink}" />  
    <c:out value="${idObject}" />
    <c:out value="${service}" />

    <c:out value="${description}" />
    <c:out value="" escapeXml="false"/> --%>

    <form action="<wp:action path="/ExtStr2/do/jpsocial/Front/Share/post.action" />" method="post" accept-charset="UTF-8" class="form-horizontal">
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
                <h4 class="margin-none"><wp:i18n key="ERRORS" /></h4>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
                <h4 class="margin-none"><wp:i18n key="MESSAGES" /></h4>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon icon-remove"></span></button>
                <h4 class="margin-none"><wp:i18n key="ERRORS" /></h4>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <p class="noscreen">
            <s:hidden name="service" />
            <s:hidden name="permalink" />
            <s:hidden name="objectId" />
            <s:hidden name="redirectUrl" />
            <s:hidden name="provider" />
            <s:hidden name="textToShare" />
        </p>

        <div class="form-group">
            <div class="col-xs-12">
            	<label for="postText"><wp:i18n key="jpsocial_POST_TEXT" /></label>
                <s:textarea name="postText" id="postText" value="%{postText}" cssClass="form-control" cols="40" rows="3" />
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-medium-vertical">
                    <s:submit  type="button" cssClass="btn btn-primary">
                        <wp:i18n key="jpsocial_SHARE"/>
                    </s:submit>
                    <s:submit type="button" action="cancel" cssClass="btn btn-warning"> 
                        <wp:i18n key="CANCEL"/>  
                    </s:submit>
            </div>
        </div>
    </form>