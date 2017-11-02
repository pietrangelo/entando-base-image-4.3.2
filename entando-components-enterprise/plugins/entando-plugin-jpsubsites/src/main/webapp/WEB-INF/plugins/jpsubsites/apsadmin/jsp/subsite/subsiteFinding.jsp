<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.subsiteManagement" />
    </span>
</h1>

<s:if test="hasActionMessages()">
    <div class="message message_confirm">
        <s:text name="message.clonedSubsite" />
        <s:actionmessage />
    </div>
</s:if>
<a class="btn btn-default margin-base-bottom" href="<s:url action="new" />">
    <span class="icon fa fa-plus-circle">&#32;<s:text name="menu.subsites.new" /></span>
</a>
<s:set var="subsitesVar" value="subsites" />
<s:if test="%{#subsitesVar.size() > 0}">
    <s:set var="groupVar" value="subsites.groupName"/>
    <s:if test="%{#groupVar!='free'}">
        <div class="table-responsive">
            <table class="table table-bordered" summary="<s:text name="note.subsites.list.summary" />">
                <caption class="group-visibility"><s:text name="note.subsites.list" /></caption>
                <tr>
                    <th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 ">
                        <s:text name="label.actions" />
                    </th>
                    <th><s:text name="label.code" /></th>
                    <th><s:text name="label.title" /></th>
                    <th><s:text name="label.categoryCode" /></th>
                    <th><s:text name="label.public" /></th>
                </tr>
                <s:iterator value="#subsitesVar" var="subsiteVar">
                    <tr>
                        <td class="text-center text-nowrap">

                            <div class="btn-group btn-group-xs">
                                <a class="btn btn-default"
                                   href="<s:url action="edit"><s:param name="code" value="#subsiteVar.code"/></s:url>"
                                   title="<s:text name="label.edit" />:&#32;<s:property value="#subsiteVar.code" />" >
                                    <span class="icon fa fa-pencil-square-o"></span>
                                    <span class="sr-only"><s:text name="label.edit" />:&#32;<s:property value="#subsiteVar.code" /></span>
                                </a>
                            </div>

                            <div class="btn-group btn-group-xs">
                                <a class="btn btn-info"
                                   href="<s:url action="cloneSubsite"><s:param name="code" value="#subsiteVar.code"/></s:url>"
                                   title="<s:text name="label.clone" />: <s:property value="#subsiteVar.code" />">
                                    <span class="sr-only"><s:text name="label.clone" />: <s:property value="#subsiteVar.code" /></span>
                                    <span class="icon fa fa-files-o"></span>
                                </a>
                            </div>
                            <div class="btn-group btn-group-xs">
                                <a class="btn btn-warning"
                                   href="<s:url action="trash"><s:param name="code" value="#subsiteVar.code"/></s:url>"
                                   title="<s:text name="label.remove" />: <s:property value="#subsiteVar.code" />">
                                    <span class="sr-only"><s:text name="label.remove" />: <s:property value="#subsiteVar.code" /></span>
                                    <span class="icon fa fa-times-circle-o"></span>
                                </a>
                            </div>
                        </td>
                        <td>
                            <code><s:property value="#subsiteVar.code"/></code>
                        </td>
                        <td>
                            <s:property value="getTitle(#subsiteVar.code, #subsiteVar.titles)" />
                        </td>
                        <td>
                            <s:property value="#subsiteVar.categoryCode"/>
                        </td>
                        <td class="text-center">
                            <s:if test="#subsiteVar.visibility">
                                <span class="icon fa fa-check text-success" title="Sì"></span>
                            </s:if>
                            <s:else>
                                <span class="icon fa fa-pause text-warning" title="No"></span>
                            </s:else>
                        </td>
                    </tr>
                </s:iterator>
            </table>
        </div>
    </s:if>
</s:if>
<s:else>
    <p><s:text name="note.subsites.empty" /></p>
</s:else>

<style>
    .group-visibility{
        color: #fff;
        background-color: #5bc0de;
        border-color: #5bc0de;
        padding: 0.5em;
        text-align: start;
    }
</style>
