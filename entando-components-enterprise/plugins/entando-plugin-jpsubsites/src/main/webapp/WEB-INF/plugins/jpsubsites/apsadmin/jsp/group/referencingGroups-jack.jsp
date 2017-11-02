<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="margin-none"><s:text name="jpsubsites.title.group.referencedSubsites" /></h3>
    </div>
    <div class="panel-body">
        <s:if test="null != references['jpsubsitesSubsiteManagerUtilizers']">
            <s:form>
            <s:set var="referencingSubsitesVar" value="references['jpsubsitesSubsiteManagerUtilizers']" />
            <wpsa:subset source="#referencingSubsitesVar" count="10" objectName="subsitesReferencesGroup" advanced="true" offset="5" pagerId="referencingSurveysId">
                <s:set var="group" value="#subsitesReferencesGroup" />
                <div class="pager">
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
                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>
            </s:form>
        </s:if>
        <s:else>
            <div class="alert alert-info"><s:text name="note.referencedContent.empty" /></div>
        </s:else>
    </div>
</div>