<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
  
<s:if test="%{(getAPI()=='') || (getSecret()=='')}">
    <h2 class="title-page">Login</h2>
    <div class="alert alert-error">Insert correct Constant Contact
        <a href="<s:url action="controlPanel" namespace="/do/ConstantContact" />">Settings</a>
    </div>
</s:if>
<s:else>  
    <h2 class="title-page">Login</h2>
    <div>
        <wp:currentPage param="code" var="currentPageCodeVar" />
        <s:form action="loginConstantContact"  namespace="/do/Ctct" method="post">
            you are not logged on Constant Contact
            <input type="hidden" name="_pageCode" value="<s:property value="#attr.currentPageCodeVar" />">
            <s:submit value="Login" cssClass="btn btn-primary"/>
        </s:form>
    </div>
</s:else>