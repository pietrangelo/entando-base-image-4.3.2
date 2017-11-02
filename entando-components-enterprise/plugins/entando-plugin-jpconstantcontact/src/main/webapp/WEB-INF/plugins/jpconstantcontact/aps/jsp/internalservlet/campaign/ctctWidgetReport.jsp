<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
    
<h2>Campaign Reports</h2>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<div class="table-responsive">
    <table class="table table-condensed table-striped table-bordered">
      <head>
        <th> Campaign name</th>
        <th> Sent</th>
        <th> Open</th>
      </head>
        <body>
            <script>
                var variables = [
                    ['Campaign name', 'sent', 'open'],
                ];       
            </script>
            <!--add status-->
            <s:iterator  value="setUserCampaigns()" var="campaignVar">
                <s:set var="sent" value="%{#campaignVar.getStatus()}"/>
                <script>
                        <s:if test="%{#sent.equals('SENT')}">
               variables.push(['<s:property escapeJavaScript="true" value="%{#campaignVar.getName()}"/>', <s:property value="%{getEmailsSent(#campaignVar.getId())}"/> , <s:property value="%{getEmailsOpenedDistinct(#campaignVar.getId())}"/>]);
                        <s:if test="!#status.last">,</s:if>
                    </s:if>   
                </script>
            <tr> 
                <td class="text-center"><s:property value="%{#campaignVar.getName()}"/></td>
                <td  class="text-center"><s:property value="%{getEmailsSent(#campaignVar.getId())}"/></td>
                <td  class="text-center"><s:property value="%{getEmailsOpenedDistinct(#campaignVar.getId())}"/></td>
            </tr>
        </s:iterator>
        </body>
    </table>
</div>
<script>
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        var data = google.visualization.arrayToDataTable(variables);
        
        var options = {
            title: 'Campaigns Report',
            colors:['#e74c3c','#33CCFF'],
        };
        
        var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }
    
</script>
<div id="chart_div" style="height:500px; width:90%"></div>
<div class="margin-medium-top">
    <a href="<wp:action path="/ExtStr2/do/Ctct/logOut.action"></wp:action>" class="btn btn-warning">Logout</a>
</div>