<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib  prefix="wp" uri="/aps-core" %>
  
<wp:currentPage param="code" var="currentPageCodeVar" />

<s:bean name="org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators.ContactComparator" var="surnameComparator" />

<%--<s:property value="#attr.currentPageCodeVar" />--%>
<h2>All current Active Contacts </h2>
<div class="margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listContacts.action"></wp:action>" ><span class="icon-repeat "></span></a>
</div>
<table class="table table-bordered table-condensed table-hover " summary="" >
  <thead>
    <tr>
      <th class="text-center"title="Actions" >A.</th>
      <th class="text-left">Surname</th>
      <th class="text-left">Name</th>
      <th class="text-left" >Mail</th>
      <th class="text-left" title="Number of Lists">N.</th>
    </tr>
  </thead>
  <tbody>
    <s:sort comparator="#surnameComparator" source="%{getContactsList()}" var="itemVar">   
    <s:iterator var="itemVar">    
      <tr> 
        <td>
          <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeContactFromAllLists.action"><wp:parameter name="_contactId"><s:property value="#itemVar.getId()"/></wp:parameter></wp:action>" title="Remove"><span class="icon-trash"></span> </a>
        </td> 
        <%--<s:property value="#itemVar.getId()"/>--%>
        <td><ins> <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/viewContact.action"><wp:parameter name="_contactId"><s:property value="#itemVar.getId()"/></wp:parameter></wp:action>" ><s:property value="#itemVar.getLastName()" /></a></ins></td>
        <td> <s:property value="#itemVar.getFirstName()" /></td>
        <td> <s:set var="mailVar" value="%{#itemVar.getEmailAddresses()}"/>
            <s:iterator value="#mailVar" var="addresses" >
                          <s:property  value="%{#addresses.getEmailAddress()}"/>
            </s:iterator>
        </td>
        <td><s:property value="#itemVar.getLists().size()" /></td>
        </tr>
    </s:iterator>   
         </s:sort> 
  </tbody>
</table>
<div class="margin-medium-vertical"><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>" class="btn btn-default">Go back</a></div>
