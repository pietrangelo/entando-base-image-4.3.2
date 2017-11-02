<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">Constant Contact settings</span>
</h1>
<div id="main">
    <s:if test="%{_messageErrorDB!=null}">
        <div class="alert alert-danger  alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <s:property value="_messageErrorDB"/>
        </div>
    </s:if>
    <s:if test="%{_messageSuccessDB!=null}">
        <div class="alert alert-success  alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <s:property value="_messageSuccessDB"/>
        </div>
    </s:if>
    <s:form name="Configuration" namespace="/do/ConstantContact" method="post">
        <div class="form-group">
            <label for="jpconstantContact-apikey">APIKEY *</label>
            <wpsf:textfield name="_updateAPIKey" value="%{getAPI()}" id="jpconstantContact-apikey" cssClass="form-control" />
        </div>
        <div class="form-group">
            <label for="jpconstantContact-secret">Secret *</label>
            <wpsf:textfield name="_updateSecret" value="%{getSecret()}" id="jpconstantContact-secret" cssClass="form-control" />
        </div>
        <div class="form-group">
            <label for="jpconstantContact-token">Token</label>
            <wpsf:textfield name="_updateToken" value="%{getTokenSettings()}" id="jpconstantContact-token" cssClass="form-control" />
        </div>
        <fieldset class="margin-large-top col-xs-12">
            <legend class="margin-large-top cursor-pointer">
                <a href="#jpconstantcontactAdvancedSettings" data-toggle="collapse" data-target="#jpconstantcontactAdvancedSettings">
                    Pages Redirect&#32;
                    <span class="icon fa fa-chevron-down pull-right2"></span>
                </a>
            </legend>	
            <div id="jpconstantcontactAdvancedSettings" class="collapse">
                <div class="row">
                    <div class="form-group col-md-8">
                        <label for="jpconstantContact-Contactpage">Contact Page</label>
                        <wpsf:textfield name="_updateContactPage" value="%{getContactPage()}" id="jpconstantContact-Contactpage" cssClass="form-control" />
                    </div>
                    <div class="form-group col-md-4">
                        <label  for="jpconstantContact-Contactframe">Frame</label>
                        <wpsf:textfield name="_updateContactFrame" value="%{getContactFrame()}" id="jpconstantContact-Contactframe" cssClass="form-control" />
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-8">
                        <label for="jpconstantContact-CampaignPage">Campaign Page</label>
                        <wpsf:textfield name="_updateCampaignPage" value="%{getCampaignPage()}" id="jpconstantContact-CampaignPage" cssClass="form-control" />
                    </div>
                    <div class="form-group col-md-4">
                        <label  for="jpconstantContact-CampaignFrame">Frame</label>
                        <wpsf:textfield name="_updateCampaignFrame" value="%{getCampaignFrame()}" id="jpconstantContact-CampaignFrame" cssClass="form-control" />
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-8">
                        <label for="jpconstantContact-EventPage">Event Page</label>
                        <wpsf:textfield name="_updateEventPage" value="%{getEventPage()}" id="jpconstantContact-EventPage" cssClass="form-control" />
                    </div>
                    <div class="form-group col-md-4">
                        <label  for="jpconstantContact-EventFrame">Frame</label>
                        <wpsf:textfield name="_updateEventFrame" value="%{getEventFrame()}" id="jpconstantContact-EventFrame" cssClass="form-control" />
                    </div>
                </div>
            </div>
            <div class="form-group">
                <wpsf:submit type="button" cssClass="btn btn-primary" action="saveConfig">
                    <span class="icon fa fa-save"></span>&#32;
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </fieldset>
    </s:form>
</div>
