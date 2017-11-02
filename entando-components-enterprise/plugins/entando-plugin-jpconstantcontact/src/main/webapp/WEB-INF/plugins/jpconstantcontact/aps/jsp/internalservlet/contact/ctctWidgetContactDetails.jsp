<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h2><wp:i18n key="jpconstantcontact_CONTACTDETAILS"/></h2>

 
<s:set value="%{getCurrentContact()}" var="itemVar"/>
<s:set var="_specificIdVar" value="_listId"/>

<div class="margin-medium-vertical">
   <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/viewContact.action"><wp:parameter name="_contactId"><s:property value="#itemVar.getId()"/></wp:parameter><wp:parameter name="_listId"><s:property value="#_specificIdVar" /></wp:parameter></wp:action>" ><span class="icon-repeat"></span></a>
</div>
   
<s:if test="_messageErrors!=null"><div class="alert alert-error"><s:property value="_messageErrors"/></div></s:if>
<s:if test="_message!=null"><div class="alert alert-success"><s:property value="_message"/></div></s:if>

    <div class="form-group">
        <form action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/updateAContact.action"/>" method="post" >
        <s:set value="#itemVar.getFirstName()" var="name" scope="page"/>
        <s:set value="#itemVar.getLastName()" var="surname" scope="page"/>
        <s:set value="#itemVar.getCompanyName()" var="company" scope="page"/>        
        <s:set value="#itemVar.getJobTitle()" var="jobTitle" scope="page"/>
        <s:set value="#itemVar.getCellPhone()" var="cellPhone" scope="page"/>
        <s:set value="#itemVar.getHomePhone()" var="homePhone" scope="page"/>
        <!--mail-->
        <s:set var="mailVar" value="%{#itemVar.getEmailAddresses()}"/>
        <s:iterator value="#mailVar" var="addresses" >
            <%--<s:property value="%{#addresses.getEmailAddress()}" />--%>
            <s:set value="#addresses.getEmailAddress()" var="email" scope="page"/>
        </s:iterator>
<!-- Notes
        <s:set var="noteVar" value="%{#itemVar.getNotes()}"/>
        <s:iterator value="#noteVar" var="notesVar" >
            <s:property value="%{#notesVar.getNode()}" />
            <s:set value="#notesVar.getNote()" var="notes" scope="page"/>
        </s:iterator>
      -->
        <table>  
            <s:set value="%{getContactLists()}" var="list" />
            <tr><td>Name</td><td><wpsf:textfield id="1" name="_fName" value="%{#attr.name}"> </wpsf:textfield></td></tr>
            <tr><td>Surname</td><td><wpsf:textfield id="2" name="_surname" value="%{#attr.surname}"> </wpsf:textfield></td></tr>
            <tr><td>Company</td><td><wpsf:textfield id="3" name="_company" value="%{#attr.company}"> </wpsf:textfield></td></tr>
            <tr><td>JobTitle</td><td><wpsf:textfield id="4" name="_jobTitle" value="%{#attr.jobTitle}"> </wpsf:textfield></td></tr>
            <tr><td>Email</td><td><wpsf:textfield id="5" value="%{#attr.email}" cssClass="uneditable-input"> </wpsf:textfield></td></tr>
            <tr><td>CellPhone</td><td><wpsf:textfield id="6" name="_cellPhone" value="%{#attr.cellPhone}"> </wpsf:textfield></td></tr>
            <tr><td>List</td><td>
            <div class="well">
            <s:iterator value="list" var="listVar" >   
                <s:set value="%{getNameContactListById(#listVar.getId())}" var="nameListVar"/>
                <%--<s:property  value="%{#nameListVar}"/>--%>
                    <ul class="unstyled">
                        <span  class="label">
                            <s:if test="#nameListVar.length()>21">
                            <abbr title="<s:property  value="%{#nameListVar}"/>"><s:property  value="%{#nameListVar.substring(0,21)}"/>..</abbr>
                            </s:if>
                            <s:else><s:property  value="%{#nameListVar}"/></s:else>
                      <span>    
                    </ul>
            </s:iterator> 
            </div>    
            </td></tr>
            <!-- old add list--<tr><td>Add List</td><td><wpsf:select name="_currentList" list="%{setMainContactsLists()}" listKey="Id" listValue="%{getName()}" /></td></tr>-->
            
            <tr><td>Add/Remove List</td><td><wpsf:select headerValue="No selection" headerKey="999" name="_currentList" list="%{setMainContactsLists()}" listKey="Id" listValue="%{getName()}" /></td></tr>
            <!--<tr><td>Note</td><td>
                    <wpsf:textarea id="8"  name="_noteContent"  value="%{#attr.notes}"></wpsf:textarea>
                    <%--<wpsf:textfield id="8" name="_noteContent"  value="%{#attr.notes}"></wpsf:textfield>--%>    
           </td></tr>-->
            </table>
            <s:if test="%{#list.size()==1}"><div class="alert alert-warning">If you remove the list of this contact, it will be removed</div></s:if>
            <div class="form-group margin-medium-top" >      
            <wpsf:submit type="button" cssClass="btn btn-primary">Update</wpsf:submit>
        </div>
        <!--hidden parameter posted-->
        <s:set value="#itemVar.Id" var="_contactId"/>
        <input type="hidden" value="${_contactId}" name="_contactId"/>
    </form>
    <div class="margin-medium-vertical"><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>" class="btn btn-default">Go back List</a></div>
</div>       
