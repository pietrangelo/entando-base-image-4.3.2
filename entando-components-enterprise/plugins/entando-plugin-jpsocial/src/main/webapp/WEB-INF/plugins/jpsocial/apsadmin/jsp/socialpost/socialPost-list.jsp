<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="targetNS" value="%{'/do/jpsocial/SocialPost'}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpsocial.title.socialPostManagement" />
    </span>
</h1>
<div id="main">
    <s:form action="list" cssClass="form-horizontal">
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>


        <div class="form-group">
            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <span class="input-group-addon">
                    <span class="icon fa fa-file-text-o fa-lg" 
                          title="<s:text name="label.search.by"/>&#32;<s:text name="label.text" />">
                    </span>
                </span>        
                <wpsf:textfield name="text" id="social-hystory-search-text" cssClass="form-control input-lg" />
                <span class="input-group-btn">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
                        <span class="sr-only"><s:text name="%{getText('label.search')}" /></span>
                        <span class="icon fa fa-search"></span>
                    </wpsf:submit>

                    <button type="button" class="btn btn-primary btn-lg dropdown-toggle" 
                            data-toggle="collapse" 
                            data-target="#search-advanced" title="Refine your search">
                        <span class="sr-only"><s:text name="title.searchFilters" /></span>
                        <span class="caret"></span>
                    </button>    
                </span>
            </div>

            <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div id="search-advanced" class="collapse well collapse-input-group">
                    <div class="form-group">
                        <label for="social-hystory-search-username" class="control-label col-sm-2 text-right"><s:text name="label.username" /></label>
                        <div class="col-sm-5">                   
                            <wpsf:textfield name="username" id="social-hystory-search-username" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="social-hystory-search-provider" class="control-label col-sm-2 text-right"><s:text name="label.provider" /></label>
                        <div class="col-sm-5">                   
                            <wpsf:textfield name="provider" id="social-hystory-search-provider" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="social-hystory-search-dateStart_cal" class="control-label col-sm-2 text-right"><s:text name="label.dateStart" /></label>
                        <div class="col-sm-5">                   
                            <wpsf:textfield name="dateStart" id="social-hystory-search-dateStart_cal" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="social-hystory-search-dateEnd_cal" class="control-label col-sm-2 text-right"><s:text name="label.dateEnd" /></label>
                        <div class="col-sm-5">                   
                            <wpsf:textfield name="dateEnd" id="social-hystory-search-dateEnd_cal" cssClass="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 col-sm-offset-2">  
                            <wpsf:submit type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.search')}" />
                                <span class="icon fa fa-search"></span>
                            </wpsf:submit>
                        </div>
                    </div> 
                </div>
            </div>
        </div>
        <%--
                also available these search fields
                <p>
                        <label for="social-hystory-search-id" class="basic-mint-label"><s:text name="label.id" />:</label>
                        <wpsf:textfield useTabindexAutoIncrement="true" name="id" id="social-hystory-search-id" cssClass="text" />
                </p>
                <p>
                        <label for="social-hystory-search-service" class="basic-mint-label"><s:text name="label.service" />:</label>
                        <wpsf:textfield useTabindexAutoIncrement="true" name="service" id="social-hystory-search-service" cssClass="text" />
                </p>
                <p>
                        <label for="social-hystory-search-permalink" class="basic-mint-label"><s:text name="label.permalink" />:</label>
                        <wpsf:textfield useTabindexAutoIncrement="true" name="permalink" id="social-hystory-search-permalink" cssClass="text" />
                </p>
                <p>
                        <label for="social-hystory-search-socialid" class="basic-mint-label"><s:text name="label.socialid" />:</label>
                        <wpsf:textfield useTabindexAutoIncrement="true" name="socialid" id="social-hystory-search-socialid" cssClass="text" />
                </p>
                <p>
                        <label for="social-hystory-search-objectid" class="basic-mint-label"><s:text name="label.objectid" />:</label>
                        <wpsf:textfield useTabindexAutoIncrement="true" name="objectid" id="social-hystory-search-objectid" cssClass="text" />
                </p>
        --%>
    </s:form>
    <s:form action="search">	
        <div class="subsection-light">
            <p class="noscreen">
                <%-- disabled, see above 
                        <wpsf:hidden name="id" />
                        <wpsf:hidden name="service" />
                        <wpsf:hidden name="permalink" />
                        <wpsf:hidden name="socialid" />
                        <wpsf:hidden name="objectid" />
                --%>
                <wpsf:hidden name="text" />
                <wpsf:hidden name="dateStart" />
                <wpsf:hidden name="dateEnd" />
                <wpsf:hidden name="provider" />
                <wpsf:hidden name="username" />
            </p>

            <wpsa:subset source="socialPostsId" count="25" objectName="groupSocialPosts" advanced="true" offset="5">
                <s:set name="group" value="#groupSocialPosts" />

                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

                <table class="table table-bordered">
                    <tr>
                        <%-- disabled see above 
                                <th scope="col"><s:text name="label.service" /></th>
                                <th scope="col"><s:text name="label.permalink" /></th>
                                <th scope="col"><s:text name="label.socialid" /></th>
                                <th scope="col"><s:text name="label.objectid" /></th>
                        --%>
                        <th scope="col" class="text-center"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
                        <th scope="col" class="text-right"><s:text name="label.id" /></th>
                        <th scope="col"><s:text name="label.text" /></th>
                        <th scope="col" class="text-center"><s:text name="label.date" /></th>
                        <th scope="col"><s:text name="label.username" /></th>
                        <th scope="col"><s:text name="label.provider" /></th>
                    </tr>
                    <s:iterator var="id">
                        <s:set var="currentPostVar" value="%{getSocialPost(#id)}" />
                        <tr>
                            <td class="text-center">
                                <div class="btn-group btn-group-xs">
                                        <a class="btn btn-default btn-xs"
                                            href="<s:url action="view"><s:param name="id" value="#currentPostVar.id"></s:param></s:url>" 
                                    title="<s:text name="label.view" />: <s:property value="#currentPostVar.id"/>">
                                            <span class="sr-only"></span>
                                            <span class="icon fa fa-info"></span>
                                        </a>
                                    </div>
                                   <div class="btn-group btn-group-xs margin-xs-left">             
                                    <a class="btn btn-warning btn-xs" href="<s:url action="trash"><s:param name="id" value="#currentPostVar.id"></s:param></s:url>" 
                                       title="<s:text name="label.remove" />: <s:property value="#currentPostVar.id"/>">
                                        <span class="sr-only"></span>
                                        <span class="icon fa fa-times-circle-o"></span>
                                    </a>
                                   </div>

                                </td>
                                <td class="text-right"><code><s:property value="#currentPostVar.id"/></code></td>
                                    <td>
                                    <s:if test="%{ (#currentPostVar.text.length() > 70) }">
                                        <span title="<s:property value="%{ #currentPostVar.text}"/>">
                                            <s:property value="%{ #currentPostVar.text.substring(0, 70) }"/>
                                            &hellip;
                                        </span>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#currentPostVar.text"/>
                                    </s:else>
                            </td>
                            <td class="text-center"><code><s:date name="#currentPostVar.date" format="dd/MM/yyyy HH:mm"/></code></td>
                            <td><code><s:property value="#currentPostVar.username"/></code></td>
                            <td><code><s:property value="#currentPostVar.provider"/></code></td>

                            <%-- disabled, see above 
                                    <td><s:property value="#currentPostVar.permalink"/></td>
                                    <td><s:property value="#currentPostVar.service"/></td>
                                    <td><s:property value="#currentPostVar.socialid"/></td>
                                    <td><s:property value="#currentPostVar.objectid"/></td>
                            --%>
                        </tr>
                    </s:iterator>
                </table>
                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>

            </wpsa:subset>
        </div>
    </s:form>
</div>
