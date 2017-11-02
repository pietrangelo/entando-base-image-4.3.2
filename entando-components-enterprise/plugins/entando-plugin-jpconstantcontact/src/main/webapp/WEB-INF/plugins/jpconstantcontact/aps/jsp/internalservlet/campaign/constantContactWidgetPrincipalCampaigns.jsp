<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:bean name="org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators.CampaignComparator" var="nameComparator" />
<h2><wp:i18n key="jpconstantcontact_CAMPAIGNS"/></h2>

<div class=" margin-medium-vertical">
  <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listCampaigns.action"></wp:action>"><span class="icon-repeat"></span></a>
</div>
<div class="table-responsive">
  <table class="table table-bordered table-striped"> 
    <thead>
      <tr>
        <th class="text-center" title="Actions">A</th>
        <th>Campaign Name</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
    <s:sort comparator="#nameComparator" source="%{setUserCampaigns()}" var="campaignVar"> 
    <s:set var="campaignPageVar" value="getCampaignPage()"/>
    <s:set var="campaignFrameVar" value="getCampaignFrame()"/>
    <c:choose>
        <c:when test="${ empty campaignPageVar ||  empty campaignFrameVar }">    
            <s:iterator var="campaignVar">     
                <s:set var="sent" value="%{#campaignVar.getStatus()}"/>
                <tr>
                    <td style="min-width: 40px">
                        <a href="https://ui.constantcontact.com/rnavmap/emcf/email/edit?flow=edit&camefrom=view&agent.uid=<s:property value="%{#campaignVar.getId()}"/>" target="_blank">
                        <span class="icon-pencil"/></a>
                        <s:if test="%{!#sent.equals('SCHEDULED')&&!#sent.equals('RUNNING')}"><a style="margin-left:5px"  href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeCampaign.action"><wp:parameter name="_campaignId"><s:property value="%{#campaignVar.getId()}" /></wp:parameter></wp:action>" title="Remove"><span class="icon-trash"/> </a>  </s:if>
                    </td>
                    <td><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/viewComplexCampaign.action"><wp:parameter name="_campaignId"><s:property value="%{#campaignVar.getId()}"/></wp:parameter></wp:action>" ><s:property value="%{#campaignVar.getName()}"/></a>
                    </td>   
                    <td>
                        <s:set var="currentStatusVar"><s:property value="%{#campaignVar.getStatus()}"/></s:set>
                        <s:if test="#currentStatusVar.equals('DRAFT')"><span  class="label label-warning"><i class="icon-pause icon-white" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('SENT')"><span class="label label-success"><small><i class="icon-ok icon-white"></i></small></small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('SCHEDULED')"><span class="label"><i class="icon-time icon-time" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('RUNNING')"><span class="label label-info"><i class="icon-play icon-white" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                    </td>
                </tr>
            </s:iterator>
        </c:when>        
        <c:otherwise>   
            <s:iterator var="campaignVar">     
               <s:set var="sent" value="%{#campaignVar.getStatus()}"/>
               <tr>
                   <td style="min-width: 40px">
                       <a href="https://ui.constantcontact.com/rnavmap/emcf/email/edit?flow=edit&camefrom=view&agent.uid=<s:property value="%{#campaignVar.getId()}"/>" target="_blank">
                       <span class="icon-pencil"/></a>
                       <s:if test="%{!#sent.equals('SCHEDULED')&&!#sent.equals('RUNNING')}"><a style="margin-left:5px"  href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/removeCampaign.action"><wp:parameter name="_campaignId"><s:property value="%{#campaignVar.getId()}" /></wp:parameter></wp:action>" title="Remove"><span class="icon-trash"/> </a>  </s:if>
                   </td>
                   <td><a href="<wp:url page="${campaignPageVar}"><wp:parameter name="_campaignId"><s:property value="#campaignVar.getId()"/></wp:parameter><wp:parameter name="internalServletFrameDest" value="${campaignFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/viewComplexCampaign.action"></wp:parameter></wp:url>" ><s:property value="%{#campaignVar.getName()}"/></a></td>
                   <td>
                       <s:set var="currentStatusVar"><s:property value="%{#campaignVar.getStatus()}"/></s:set>
                       <s:if test="#currentStatusVar.equals('DRAFT')"><span  class="label label-warning"><i class="icon-pause icon-white" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                       <s:if test="#currentStatusVar.equals('SENT')"><span class="label label-success"><small><i class="icon-ok icon-white"></i></small></small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                       <s:if test="#currentStatusVar.equals('SCHEDULED')"><span class="label"><i class="icon-time icon-time" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                       <s:if test="#currentStatusVar.equals('RUNNING')"><span class="label label-info"><i class="icon-play icon-white" ></i>&#32;<small><s:property value="%{#campaignVar.getStatus()}"/></small></span></s:if>
                   </td>
               </tr>
             </s:iterator>
        </c:otherwise>
    </c:choose>
    </s:sort> 
    </tbody>
  </table>
</div>
<div class="margin-medium-top">   
  <a href="https://ui.constantcontact.com/rnavmap/emcf/email/create #cc_main_content" class="btn btn-primary" target="_blank">New Complex Campaign</a>    
</div> 
<div class="margin-medium-top">
     <wp:currentPage param="code" var="currentPageCodeVar" />
      <a href="<wp:action path="/ExtStr2/do/Ctct/logOut.action"><wp:parameter name="_pageCode"><s:property value="#attr.currentPageCodeVar" /></wp:parameter></wp:action>" class="btn btn-warning">Logout</a>
</div>



