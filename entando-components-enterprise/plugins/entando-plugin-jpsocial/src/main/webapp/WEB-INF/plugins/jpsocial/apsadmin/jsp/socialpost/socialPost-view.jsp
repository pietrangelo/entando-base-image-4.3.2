<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpsocial" uri="/jpsocial" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:set var="targetNS" value="%{'/do/jpsocial/SocialPost'}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />">
            <s:text name="jpsocial.title.socialPostManagement" />
        </a>&#32;/&#32;
        <s:text name="jpsocial.title.socialPost.view" />&#32;<s:property value="id" />
    </span>
</h1>

<div id="main">

    <table class="table table-bordered">
        <tr>
            <th class="text-right"><s:text name="label.id" /></th>
            <td><code><s:property value="id" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.date" /></th>
            <td><code><s:date name="date" format="dd/MM/yyyy HH:mm"/></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.username" /></th>
            <td><code><s:property value="username" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.permalink" /></th>
            <td><a href="<s:property value="permalink" />"><s:property value="permalink" /></a></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.service" /></th>
            <td><s:property value="service" /></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.objectid" /></th>
            <td><code><s:property value="objectid" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.provider" /></th>
            <td><code><s:property value="provider" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.socialid" /></th>
            <td><code><s:property value="socialid" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.socialurl" /></th>
            <td>
                <s:set var="idSocialPost"><s:property value="id" /></s:set> 
        <jpsocial:socialLink idSocialPost="${idSocialPost}" var="socialLink" />
        <a href="<c:out value="${socialLink}" />"> <c:out value="${socialLink}" /> </a>
        </td>
        </tr>
        <tr>          
            <th class="text-right"><s:text name="label.text" /></th>
            <td>
                <s:set var="text"><s:property value="text" /></s:set>
                <s:property value='#text.replaceAll("\n", "<br />")' escape="false" escapeHtml="false"  />
            </td>
        </tr>
    </table>

</div>
