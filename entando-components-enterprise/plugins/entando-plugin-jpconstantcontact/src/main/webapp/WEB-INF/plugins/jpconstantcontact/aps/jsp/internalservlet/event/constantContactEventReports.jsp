<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
         
       ['Event', 'Registrants']
            <s:iterator value="getUserEvents()" var="eventVar"> 
                ,['<s:property value="%{#eventVar.getName()}"/>',   <s:property value="%{getEventRegistrants(#eventVar.getId()).size()}"/>]            
            </s:iterator> 
        ]);

        var options = {
          title: 'Events Reports',
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
        chart.draw(data, options);
      }
</script>

<div id="piechart_3d" style="height: 400px; width:100%"></div>

 
