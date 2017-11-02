<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:if test="!#referencingSubsites.empty">
    <s:form>
    <wpsa:subset source="#referencingSubsites" count="10" objectName="referencingSubsitesGroupVar" advanced="true" offset="5" pagerId="referencingContentsId">
        <s:set var="group" value="#referencingSubsitesGroupVar" />
        <div class="text-center">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
        </div>
        <div class="table-responsive">
            <table class="table table-bordered" id="subsitesListTable" summary="<s:text name="jpsubsites.note.page.referencingSubsites.summary" />" >
                <caption class="text-strong margin-base-vertical"><s:text name="jpsubsites.title.page.referencingSubsites" /></caption>
                <tr>
                <th><s:text name="label.code" /></th>
                <th><s:text name="label.title" /></th>
                <th><s:text name="label.description" /></th>
                </tr>
                <s:iterator var="subsiteVar">
                    <tr>
                    <td><span class="monospace"><s:property value="#subsiteVar.code" /></span></td>
                    <td><s:property value="getTitle(#subsiteVar.code, #subsiteVar.titles)" /></td>
                    <td><s:property value="getTitle(#subsiteVar.code, #subsiteVar.descriptions)" /></td>
                    </tr>
                </s:iterator>
            </table>
        </div>
        <div class="text-center">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
        </div>
    </wpsa:subset>
    </s:form>
</s:if>
<s:else>
    <p class="alert alert-info"><s:text name="jpsubsites.note.page.referencingSubsites.empty" /></p>
</s:else>