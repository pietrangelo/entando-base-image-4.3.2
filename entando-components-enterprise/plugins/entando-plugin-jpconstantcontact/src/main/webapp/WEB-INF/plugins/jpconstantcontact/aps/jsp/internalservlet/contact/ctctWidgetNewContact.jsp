<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
  
<h2>New contact</h2>
<s:if test="_messageErrors!=null"><div class="alert alert-error"><s:property value="_messageErrors"/></div></s:if>
<s:if test="_message!=null"><div class="alert alert-success"><s:property value="_message"/></div></s:if></br>
<div class="form-group margin-medium-vertical">
    <form action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/saveContact.action"/>" method="post" >
	 <table> 
	 <tr><td><label class="control-label" for="Contact_Name" >*Name</label>
          <wpsf:textfield id="Contact_Name" name="_fName" value="" required="required" /></td></tr>
     <tr><td> <label class="control-label" for="Contact_Surname" >*Surname</label>
             <wpsf:textfield id="Contact_Surname" name="_surname" value="" required="required"/></td></tr>
     <tr><td><label class="control-label" for="ContactAddress" >*Address</label>
             <wpsf:textfield id="ContactAddress" name="_newAddress" value="" required="required"/></td></tr>
      <tr><td>
              <label class="control-label" for="ContactList" >List</label>
              <wpsf:select id="ContactList" name="_currentList" list="%{setMainContactsLists()}" listKey="Id" listValue="%{getName()}" >
        </wpsf:select></td></tr>
	 </table> 	
        <div>
            <wpsf:submit type="button" cssClass="btn btn-primary">Save</wpsf:submit>
        </div>
    </form>
</div> 
<div class="margin-base-top"> 
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>" class="btn btn-default">Go back</a>
</div>
    
    
    
    
    
    
    
    
    
    