<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  

<h2>New list</h2>
<s:if test="_message!=null"><div class="alert alert-error"><s:property value="_message"/></div></s:if>
  
<div class="margin-medium-top form-group">
  <form action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/saveList.action"/>" method="post" >
     <label class="control-label" for="ListName" >*Name</label>
    <wpsf:textfield id="ListName" name="_listName" value="" required="required" />  
    <div >
    <wpsf:submit type="button" cssClass="btn btn-primary">
      Save
    </wpsf:submit>
      </div> 
  </form>
</div>  
<div class="margin-base-top"> 
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/list.action"></wp:action>" class="btn btn-default">Go back</a>
</div>
