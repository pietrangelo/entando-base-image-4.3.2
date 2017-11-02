<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block"><s:text name="jpsubsite.title.settings" /></span>
</h1>

<s:if test="hasActionMessages()">
    <div class="alert alert-success alert-dismissable fade in">
        <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
        <ul class="margin-base-top">
            <s:iterator value="actionMessages">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>
<s:if test="hasActionErrors()">
    <div class="alert alert-danger alert-dismissable fade in">
        <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
        <ul class="margin-base-top">
            <s:iterator value="actionErrors">
                <li><s:property escapeHtml="false" /></li>
            </s:iterator>
        </ul>
    </div>
</s:if>

<s:form action="save" >
    <fieldset class="col-xs-12"><legend><s:text name="jpsubsite.name.generalSettings" /></legend>
        <div class="form-group">
            <label for="rootPageCode"><s:text name="jpsubsite.config.rootPageCode" /></label>
            <select name="subsiteConfig.rootPageCode" id="rootPageCode" class="form-control">
                <s:iterator var="pageVar" value="pageList">
                    <option <s:if test="subsiteConfig.rootPageCode == #pageVar.code">selected="selected"</s:if> 
                                                                                     value="<s:property value="#pageVar.code"/>"><s:if test="!#pageVar.showable"> [i]</s:if><s:property value="#pageVar.getShortFullTitle(currentLang.code)"/></option>
                </s:iterator>
            </select>
        </div>
        <div class="form-group">
            <label for="pageModel"><s:text name="jpsubsite.config.pageModel" /></label>
            <select name="subsiteConfig.pageModel" id="pageModel" class="form-control">
                <s:iterator var="pageModelVar" value="pageModels">
                    <option <s:if test="subsiteConfig.pageModel == #pageModelVar.code">selected="selected"</s:if> 
                                                                                       value="<s:property value="#pageModelVar.code"/>"><s:property value="#pageModelVar.descr"/></option>
                </s:iterator>
            </select>
        </div>
        <div class="form-group">
            <label for="categoriesRoot"><s:text name="jpsubsite.config.categoriesRoot" /></label>
            <select name="subsiteConfig.categoriesRoot" id="categoriesRoot" class="form-control">
                <s:iterator var="categoryVar" value="categories">
                    <option <s:if test="subsiteConfig.categoriesRoot == #categoryVar.code">selected="selected"</s:if> 
                                                                                           value="<s:property value="#categoryVar.code"/>"><s:property value="#categoryVar.getShortFullTitle(currentLang.code)"/></option>
                </s:iterator>
            </select>
        </div>
    </fieldset>
    <fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="jpsubsite.name.contentTypes.models" /></legend>
        <div class="table-responsive margin-base-vertical">
            <table class="table table-bordered" summary="<s:text name="jpsubsite.title.contentTypes.list" />">
                <tr>
                    <th><s:text name="jpsubsite.contentType" /></th>
                    <th><s:text name="jpsubsite.contentModel" /></th>
                </tr> 
                <s:iterator value="contentTypes" var="contentType" >
                    <tr>
                        <td><s:property value="#contentType.description"  /></td> 
                        <td>
                            <wpsf:select name="%{'modelId_' + #contentType.code}" id="%{'modelId_' + #contentType.code}" 
                                         value="%{subsiteConfig.contentModels[#contentType.code]}" list="%{getModelsByType(#contentType.code)}" 
                                         headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" class="form-control" />
                        </td> 
                    </tr>
                </s:iterator>
            </table>
        </div>
    </fieldset>
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                <span class="icon fa fa-floppy-o"></span>&#32;
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
</s:form>
