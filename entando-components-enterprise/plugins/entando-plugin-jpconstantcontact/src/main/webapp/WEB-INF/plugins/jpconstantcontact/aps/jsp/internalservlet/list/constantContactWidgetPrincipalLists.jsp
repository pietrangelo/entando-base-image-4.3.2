<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
    
<s:bean name="org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators.ContactListComparator" var="surnameComparator" />
    
<h2><wp:i18n key="jpconstantcontact_CONTACTLISTS"/></h2>
    
<div class="margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>"><span class="icon-repeat"></span></a>  
    </div>
    <table class="table table-hover" >
        <thead>
            <tr>
                <th title="Actions" >A</th>
                <th class="text-left" title="Name of the List">List Name</th>
                <th title="Number of elements">Elements</th>     
            </tr>
        </thead>
        <tbody>
        <s:sort comparator="#surnameComparator" source="%{setMainContactsLists()}" var="itemVar"> 
            <s:set var="contactPageVar" value="getContactPage()"/>
            <s:set var="contactFrameVar" value="getContactFrame()"/>
            <c:choose>
                <c:when test="${empty contactPageVar || empty contactFrameVar}">
                   <s:iterator var="itemVar"> 
                    <tr>   
                        <td  title="Remove List" class="text-center"><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeList.action"><wp:parameter name="_listId"><s:property value="#itemVar.getId()" /></wp:parameter></wp:action>" title="Remove"><span class="icon-trash"></span> </a></td>  
                        <td class="text-left"><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/view.action" ><wp:parameter name="_listId"><s:property value="#itemVar.getId()" /></wp:parameter><wp:parameter name="_listName"><s:property value="#itemVar.getName()" /></wp:parameter></wp:action>"><s:property value="%{#itemVar.name}"/></a></td>         
                        <td><s:property value="%{#itemVar.getContactCount()}"/></td>
                    </tr>
                </s:iterator>   
                </c:when>
                <c:otherwise>
                <s:iterator var="itemVar">    
                    <tr>   
                        <td  title="Remove List" class="text-center"><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeList.action"><wp:parameter name="_listId"><s:property value="#itemVar.getId()" /></wp:parameter></wp:action>" title="Remove"><span class="icon-trash"></span> </a></td>  
                        <td class="text-left"><a href="<wp:url page="${contactPageVar}"><wp:parameter name="_listId"><s:property value="#itemVar.getId()" /></wp:parameter><wp:parameter name="internalServletFrameDest" value="${contactFrameVar}"/><wp:parameter name="_listName"><s:property value="#itemVar.getName()" /></wp:parameter><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/view.action"></wp:parameter></wp:url>"><s:property value="%{#itemVar.name}"/> </a></td>      
                        <td><s:property value="%{#itemVar.getContactCount()}"/></td>
                    </tr>
                </s:iterator>
                </c:otherwise>
              </c:choose>
        </s:sort>
    </tbody>
</table>
<s:if test="(!(#contactPageVar=='')&& !(#contactFrameVar==''))">     
    <div>
        <a href="<wp:url page="${contactPageVar}"><wp:parameter name="internalServletFrameDest" value="${contactFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/newList.action"></wp:parameter></wp:url>" class="btn btn-primary">New List</a>
        <a href="<wp:url page="${contactPageVar}"><wp:parameter name="internalServletFrameDest" value="${contactFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/newContact.action"></wp:parameter></wp:url>" class="btn btn-primary">New Contact</a>
        <a href="<wp:url page="${contactPageVar}"><wp:parameter name="internalServletFrameDest" value="${contactFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/listContacts.action"></wp:parameter></wp:url>" class="btn btn-default">All Contacts</a>
    </div>
</s:if>
<s:else>
    <div>
        <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/newList.action"></wp:action>" class="btn btn-primary">New List</a>
        <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/newContact.action"></wp:action>" class="btn btn-primary">New Contact</a>
        <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listContacts.action"></wp:action>" class="btn btn-default">All Contacts</a>
    </div>   
</s:else>  
<div class="margin-medium-vertical">
    <wp:currentPage param="code" var="currentPageCodeVar" />
    <a href="<wp:action path="/ExtStr2/do/Ctct/logOut.action"><wp:parameter name="_pageCode"><s:property value="#attr.currentPageCodeVar" /></wp:parameter></wp:action>" class="btn btn-warning">Logout</a>
</div>