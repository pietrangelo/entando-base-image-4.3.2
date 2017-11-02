<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="margin-none"><s:text name="jpsubsites.title.category.referencingSubsites" /></h3>
    </div>
    <div class="panel-body">
        <s:if test="null != references['jpsubsitesSubsiteManagerUtilizers']">
            <s:form>
            <wpsa:subset source="references['jpsubsitesSubsiteManagerUtilizers']" count="10" objectName="subsitesReferencesVar" advanced="true" offset="5" pagerId="contentManagerReferences">
                <s:set var="group" value="#subsitesReferencesVar" />
                <div class="text-center">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
                <table class="table table-bordered" id="subsitesListTable">
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
                <div class="text-center">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>
            </s:form>
        </s:if>
        <s:else>
            <p class="margin-none"><s:text name="jpsubsites.note.category.referencingSubsites.empty" /></p>
        </s:else>
    </div>
</div>