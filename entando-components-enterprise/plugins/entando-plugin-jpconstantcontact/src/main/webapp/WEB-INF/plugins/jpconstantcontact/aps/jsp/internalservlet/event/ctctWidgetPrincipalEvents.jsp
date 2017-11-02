<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:bean name="org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators.EventComparator" var="nameComparator" />
 
 <h2><wp:i18n key="jpconstantcontact_EVENTS"/></h2>

<div class=" margin-medium-vertical">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listEvents.action"></wp:action>" ><span class="icon-repeat"></span></a>
</div>
<s:if test="_messageErrors!=null"><div class="alert alert-error"><s:property value="_messageErrors"/></div></s:if>
<s:if test="_messageSuccess!=null"><div class="alert alert-success"><s:property value="_messageSuccess"/></div></s:if>
    <table class="table table-responsive table-bordered table-striped">
        <thead>
        <th class="text-center" title="Actions">A.</th>
        <th>Name</th>
        <th>Status</th>
         <th class="text-center" title="Number of Subscribers">N.S</th>
    </thead>
    <tbody>
    <s:sort comparator="#nameComparator" source="%{getUserEvents()}" var="eventVar">   
    <s:set var="eventPageVar" value="getEventPage()"/>
    <s:set var="eventFrameVar" value="getEventFrame()"/>
    <c:choose>
        <c:when test="${ empty eventPageVar ||  empty eventFrameVar }">  
            <s:iterator var="eventVar">    
                <tr>             
                    <td class="text-center" style="min-width: 40px">
                        <a target="blank" href="https://ui.constantcontact.com/rnavmap/evp/hub/details?id=<s:property value="%{#eventVar.getId()}"/>" title="Advanced UI"> 
                            <span class="icon-pencil "></span>
                        </a>
                        <a style="margin-left:5px" target="_blank" href="<s:property value="%{getAEvent(#eventVar.getId()).getRegistrationUrl()}"/>" title="Go to the event">
                          <span class="icon-globe"></span>
                        </a>
                    </td>  
                    <td><a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/viewEvent.action"><wp:parameter name="_eventId"><s:property value="#eventVar.getId()"/></wp:parameter></wp:action>" ><s:property value="%{#eventVar.getName()}"/></a></td>
                    <td>
                        <s:set var="currentStatusVar"><s:property value="#eventVar.getStatus()"/></s:set>
                        <s:if test="#currentStatusVar.equals('DRAFT')"><span  class="label label-warning"><i class="icon-pause icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('ACTIVE')"><span class="label label-info"><i class="icon-play icon-white"></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('CANCELLED')"><span class="label"><i class="icon-ban-circle icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                        <s:if test="#currentStatusVar.equals('COMPLETE')"><span class="label label-success"><i class="icon-ok icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                        </td>
                    <td><s:property value="%{getEventRegistrants(#eventVar.getId()).size()}"/></td>
                </tr>
            </s:iterator>
        </c:when>        
        <c:otherwise>
            <s:iterator var="eventVar">    
            <tr>             
                <td class="text-center" style="min-width: 40px">
                    <a target="blank" href="https://ui.constantcontact.com/rnavmap/evp/hub/details?id=<s:property value="%{#eventVar.getId()}"/>" title="Advanced UI"> 
                        <span class="icon-pencil "></span>
                    </a>
                    <a style="margin-left:5px" target="_blank" href="<s:property value="%{getAEvent(#eventVar.getId()).getRegistrationUrl()}"/>" title="Go to the event">
                        <span class="icon-globe"></span>
                    </a>
                </td>  
                <td><a href="<wp:url page="${eventPageVar}"><wp:parameter name="_eventId"><s:property value="#eventVar.getId()"/></wp:parameter><wp:parameter name="internalServletFrameDest" value="${eventFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/viewEvent.action"></wp:parameter></wp:url>" ><s:property value="%{#eventVar.getName()}"/></a></td>
                <td>
                   <s:set var="currentStatusVar"><s:property value="#eventVar.getStatus()"/></s:set>
                   <s:if test="#currentStatusVar.equals('DRAFT')"><span  class="label label-warning"><i class="icon-pause icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                   <s:if test="#currentStatusVar.equals('ACTIVE')"><span class="label label-info"><i class="icon-play icon-white"></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                   <s:if test="#currentStatusVar.equals('CANCELLED')"><span class="label"><i class="icon-ban-circle icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                   <s:if test="#currentStatusVar.equals('COMPLETE')"><span class="label label-success"><i class="icon-ok icon-white" ></i>&#32;<small><wp:i18n key="jpconstantcontact_${currentStatusVar}"/></small></span></s:if>
                </td>
               <td><s:property value="%{getEventRegistrants(#eventVar.getId()).size()}"/></td>
            </tr>
        </s:iterator>
        </c:otherwise>
    </c:choose>
   </s:sort>
</tbody>
</table>
<c:choose>
    <c:when test="${ empty eventPageVar ||  empty eventFrameVar }">  
        <div class="form-group">
            <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/newEvent.action"></wp:action>" class="btn btn-primary">New Event</a>
        </div>
    </c:when>        
    <c:otherwise>
        <div class="form-group">
        <a href="<wp:url page="${eventPageVar}"><wp:parameter name="internalServletFrameDest" value="${eventFrameVar}"/><wp:parameter name="internalServletActionPath" value="/ExtStr2/do/FrontEnd/Ctct/newEvent.action"></wp:parameter></wp:url>" class="btn btn-primary">New Event</a>
        </div>
    </c:otherwise>
</c:choose>
<div class="margin-medium-top">
    <wp:currentPage param="code" var="currentPageCodeVar" />
    <a href="<wp:action path="/ExtStr2/do/Ctct/logOut.action"><wp:parameter name="_pageCode"><s:property value="#attr.currentPageCodeVar" /></wp:parameter></wp:action>" class="btn btn-warning">Logout</a>
</div>
