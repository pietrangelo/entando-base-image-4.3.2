<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<table class="table table-bordered">
    <tr>
        <th class="text-right"><s:text name="jpjasper.label.report.label" /></th>
        <td><s:property value="#currentReport.label"/></td> 
    </tr>
    <tr>
        <th class="text-right"><s:text name="jpjasper.label.report.description" /></th>
        <td><s:property value="#currentReport.description"/></td>
    </tr>            
    <tr>
        <th class="text-right"><s:text name="jpjasper.label.report.uri" /></th>
        <td><s:property value="#currentReport.uriString"/></td>
    </tr>           
</table>