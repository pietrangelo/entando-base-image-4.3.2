<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:set value="getEmailCampaing()" var="itemVar"/>
<s:set var="sent" value="%{#itemVar.getStatus()}"/>
<s:set value="#itemVar.getMessageFooter()" var="footerVar" scope="page"/>
<s:set value="%{#attr.footerVar.getOrganizationName()}" var="organizationVar" scope="page"/>
<s:set value="%{#attr.footerVar.getAddressLine1()}" var="addr" scope="page"/>
<s:set value="%{#attr.footerVar.getPostalCode()}" var="postalCodeVar" scope="page"/>
<s:set value="%{#attr.footerVar.getCity()}" var="cityVar" scope="page"/>
<s:set value="%{#attr.footerVar.getCountry()}" var="countryVar"/>
<s:set value="#itemVar.getName()" var="nameVar" scope="page"/>
<s:set value="#itemVar.getModifiedDate()" var="modifiedDateVar" scope="page"/>
<s:set var="_campaignId" value="#itemVar.getId()"/>

<s:if test="%{!#sent.equals('DRAFT')}">
    <h2><wp:i18n key="jpconstantcontact_CAMPAIGNDETAILS"/></h2>

    <div class="alert alert-warning">Only draft campaigns can be updated, to <b>copy</b> or <b>unschedule</b> campaign go to <u><a href="https://ui.constantcontact.com/rnavmap/emcf/email/edit?flow=edit&camefrom=view&agent.uid=<s:property value="%{#itemVar.getId()}"/>" target="_blank">Advanced UI</a></u>
    </div>
    <div class="form" style="max-width: 220px; -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;">
    <div class="form-group">
        <label class="control-label" for="CampaignName" >&nbspCampaign Name</label>
        <div class="controls">
            <wpsf:textfield id="CampaignName" name="_campaignName" value="%{#attr.nameVar}" disabled="true"></wpsf:textfield>
        </div>
    </div>
    <div class="form-group" style=" -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;">
        <label class="control-label" for="List" >*&nbspList</label>
        <div class="well ">
            <s:iterator id="List" value="%{getCampaignToContactLists()}" var="listVar" >         
                <s:set value="%{getNameContactListById(#listVar.getId())}" var="nameListVar"/>
                <%--<s:property  value="%{#nameListVar}"/>--%>
                <ul class="unstyled">
                    <span  class="label">
                        <s:if test="#nameListVar.length()>21">
                        <abbr title="<s:property  value="%{#nameListVar}"/>"><s:property  value="%{#nameListVar.substring(0,21)}"/>..</abbr>
                        </s:if>
                        <s:else><s:property  value="%{#nameListVar}"/></s:else>
                    </span>    
                </ul>
            </s:iterator> 
        </div>
    </div>   
    <div class="form-group">
        <label class="control-label" for="OrganizationName" >&nbspOrganization</label>
        <div class="controls">
            <wpsf:textfield id="OrganizationName" name="_organizationName" value="%{#attr.organizationVar}" disabled="true" > </wpsf:textfield>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label" for="Address" >&nbspAddress</label>
        <div class="controls">
            <wpsf:textfield id="Address" name="_address"value="%{#attr.addr}" disabled="true" > </wpsf:textfield>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label" for="PostalCode" >&nbspPostal Code</label>
        <div class="controls">
            <wpsf:textfield id="PostalCode" name="_postalCode" value="%{#attr.postalCodeVar}" disabled="true"> </wpsf:textfield>
        </div>
    </div>    
    <div class="form-group">
        <label class="control-label" for="City" >&nbspCity</label>
        <div class="controls">
            <wpsf:textfield id="City" name="_citta" value="%{#attr.cityVar}" disabled="true"> </wpsf:textfield>
        </div>
    </div>      
    <div class="form-group">
        <label class="control-label" for="Status" >&nbspStatus:</label>
        <div class="controls"> <wpsf:textfield id="Status" value="%{#sent}" disabled="true"></wpsf:textfield>
        </div>
    </div> 
</div>         
        
</s:if>

<s:else>   
<h2><wp:i18n key="jpconstantcontact_CAMPAIGNDETAILS"/></h2>
<s:if test="_messageErrors!=null"><div class="alert alert-error"><s:property value="_messageErrors"/></div></s:if>
<s:if test="_messageSuccess!=null"><div class="alert alert-success"><s:property value="_messageSuccess"/></div></s:if>
<div class="form " style="  max-width: 220px; -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;">
        <form id="1" action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/updateComplexCampaign.action"/>" method="post" >
            <div class="form-group " style="webkit-box-sizing: border-box">
                <label class="control-label" for="CampaignName" >*&nbspCampaign Name</label>
                <div class="controls">
                    <wpsf:textfield id="CampaignName" name="_campaignName" value="%{#attr.nameVar}" required="required" ></wpsf:textfield>
                    </div>
            </div>
            <div class="form-group">
                <label class="control-label" for=AddRemoveList >*&nbspAdd/Remove List</label>
                <div class="controls">
                <wpsf:select  id="AddRemoveList" headerValue="No selection" headerKey="999" name="_currentList" list="%{setMainContactsLists()}" listKey="Id" listValue="%{getName()}" />
            </div>
            </div>
            <div class="form-group" style=" -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;">
                <label class="control-label" for="List" >*&nbspList</label>
                <div class="well ">
                    <s:iterator id="List" value="%{getCampaignToContactLists()}" var="listVar" >         
                        <s:set value="%{getNameContactListById(#listVar.getId())}" var="nameListVar"/>
                        <%--<s:property  value="%{#nameListVar}"/>--%>
                        <ul class="unstyled">
                            <span  class="label">
                                <s:if test="#nameListVar.length()>21">
                                <abbr title="<s:property  value="%{#nameListVar}"/>"><s:property  value="%{#nameListVar.substring(0,21)}"/>..</abbr>
                                </s:if>
                                <s:else><s:property  value="%{#nameListVar}"/></s:else>
                            </span>    
                        </ul>
                    </s:iterator> 
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="Organization" >*&nbspOrganization</label>
                <div class="controls">
                    <wpsf:textfield id="Organization" name="_organizationName" value="%{#attr.organizationVar}" required="required"> </wpsf:textfield>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="Address" >*&nbspAddress</label>
                <div class="controls">
                <wpsf:textfield id="Address" name="_address"value="%{#attr.addr}" required="required" > </wpsf:textfield>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label" for="Postal Code" >*&nbspPostal Code</label>
                <div class="controls">
                <wpsf:textfield id="Postal Code" name="_postalCode" value="%{#attr.postalCodeVar}" required="required"> </wpsf:textfield>
                </div>
            </div>    
            <div class="form-group">
                <label class="control-label" for="City" >*&nbspCity</label>
                <div class="controls">
                <wpsf:textfield id="City" name="_citta" value="%{#attr.cityVar}" required="required"> </wpsf:textfield>
                </div>
            </div>      
           <%-- Hidden because this field is not modifiable in complex campaigns --%>    
            <input type="hidden" value="<s:property value="%{#attr.countryVar}"/>" name="_country"/>
            <input type="hidden" value="${_campaignId}" name="_campaignId"/>
            <div class="form-group">
                <label class="control-label" for="Status" >&nbspStatus:</label>
                <div class="controls"> <wpsf:textfield id="Status" value="%{#sent}" disabled="true"></wpsf:textfield>
                </div>
            </div>
            <div class="form-group">
                 <wpsf:submit type="button" cssClass="btn btn-primary">Update</wpsf:submit>
                <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/send.action"><wp:parameter name="_campaignId">${_campaignId}</wp:parameter></wp:action>" title="Send" class="btn btn-default">Send Now</a> 
            </div>
        </form> 
        
        <form id="2" action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/schedulateCampaign.action"/>" method="post" >
                            
           <%-- Collapse Schedule--%>     
            <legend class="margin-large-top cursor-pointer">
                <a href="#constantContact-schedule" data-toggle="collapse" data-target="#constantContact-schedule">
                 Schedule&#32;
                 <span class="icon fa fa-chevron-down pull-right"></span>
                </a>
            </legend> 
            <div id="constantContact-schedule" class="collapse">
                <div class="form-group">
                    <label class="control-label" for="Start_Day" >*&nbspSchedule Date (UTC):</label>
                    <div class="controls controls-row">
                    <wpsf:textfield id="Start_Day" name="_eventStartDate" value=""  cssClass="input-small" placeholder="yyyy-MM-dd" />
                    HH
                    <select  name="_eventStartTimeHours" id="Start_TimeHours" class="input-mini">
                        <option value="1" selected />1</option>
                        <option value="2" >2</option>
                        <option value="3" >3</option>
                        <option value="4"  >4</option>
                        <option value="5"  >5</option>
                        <option value="6"  >6</option>
                        <option value="7"  >7</option>
                        <option value="8"  >8</option>
                        <option value="9"  >9</option>
                        <option value="10"  >10</option>
                        <option value="11"  >11</option>
                        <option value="12"  >12</option>
                        <option value="13"  />13</option>
                        <option value="14"  />14</option>
                        <option value="15"  />15</option>
                        <option value="16"  />16</option>
                        <option value="17"  />17</option>
                        <option value="18"  />18</option>
                        <option value="19"  />19</option>
                        <option value="20"  />20</option>
                        <option value="21"  />21</option>
                        <option value="22"  />22</option>
                        <option value="23"  />23</option>
                        <option value="24"  />24</option>
                    </select>
                    mm  
                    <select name="_eventStartTimeMinutes" id="Start_TimeMinutes" class="input-mini" >
                        <option value="0" selected />00</option>
                        <option value="15"  />15</option>
                        <option value="30"  />30</option>
                        <option value="45"  />45</option>
                    </select>
                     <div class="form-group margin-base-top margin-medium-bottom">
                       <wpsf:submit action="schedulateCampaign" type="button" cssClass="btn btn-default">Shedulate</wpsf:submit>
                     </div>
                </div> 
            </div> 
        </div> 
      <input type="hidden" value="${_campaignId}" name="_campaignId"/>
        </form>  
         </div>            
</s:else>
    
<div class="margin-medium-top">
    <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listCampaigns.action"/>"class="btn btn-default" >Go back</a>
</div> 
