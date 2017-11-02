<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

Customize your campaign
<div class="text-right margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/newComplexCampaign.action"></wp:action>" class="btn btn-xs"><span class="icon-repeat"></span></a>
</div>

<s:set var="token" value="%{getToken()}"/>
<div class="row">
    <iframe contenteditable="true"  scrolling="yes" height="800px" width="1000px" id="frame2" src="https://ui.constantcontact.com/rnavmap/emcf/email/create #cc_main_content" ></iframe>
</div>


<div class="text-right">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listCampaigns.action"></wp:action>"class="btn btn-default" >Go back</a>
</div>

