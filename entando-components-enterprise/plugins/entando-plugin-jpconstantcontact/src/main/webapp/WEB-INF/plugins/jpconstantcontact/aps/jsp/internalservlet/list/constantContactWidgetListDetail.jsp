<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:bean name="org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators.ContactComparator" var="surnameComparator" />

<s:set var="_specificIdVar" value="%{_listId}"/>
<s:set var="_nameVar" value="%{_listName}"/>
<h2 class="title-page">Contacts of&#32;<s:property value="#_nameVar"/>&#32;list</h2>
<div class="margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/view.action"><wp:parameter name="_listId"><s:property value="#_specificIdVar" /></wp:parameter><wp:parameter name="_listName"><s:property value="_nameVar" /></wp:parameter></wp:action>"><span class="icon-repeat"></span></a>
</div>
<s:if test="setSingleContactList().size > 0">
 <div class="col-md-6">
    <table class="table table-hover" summary="" >
        <tr>
            <th> SurName</th>
            <th> Name</th>
            <th>Email Address</th>
        </tr>
        <!--Set IdList to pass value to another action--> 
         <s:sort comparator="#surnameComparator" source="%{setSingleContactList()}" var="itemsVar">   
    <s:iterator var="itemsVar">    
            <tr>
               <td><ins>
                       <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/viewContact.action"><wp:parameter name="_contactId"><s:property value="#itemsVar.getId()"/></wp:parameter><wp:parameter name="_listId"><s:property value="#_specificIdVar" /></wp:parameter></wp:action>" > <s:property value="#itemsVar.getLastName()"/></a> 
                  </ins></td>
              <td><s:property value="#itemsVar.getFirstName()"/></td>
                <td>
                    <s:set var="mailVar" value="%{#itemsVar.getEmailAddresses()}"/>
                    <s:iterator value="#mailVar" var="addresses" >
                        <s:property value="%{#addresses.getEmailAddress()}" />
                    </s:iterator>
                </td>
            <!--<td>
                    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeContactFromAList.action"><wp:parameter name="_listId"><s:property value="#_specificIdVar" /></wp:parameter><wp:parameter name="_contactId"><s:property value="%{#itemsVar.getId()}" /></wp:parameter></wp:action>" >R </a>    
                </td>  -->
            </tr>
        </s:iterator>
    </s:sort>
    </table>
 </div>
</s:if>
<s:else>
<div class="margin-medium-vertical">
    There are no elements in this list
</div >
</s:else>
<div class="margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>" class="btn btn-default">Go back</a>
</div >