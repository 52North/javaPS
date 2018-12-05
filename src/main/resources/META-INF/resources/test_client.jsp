<%--

    Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
    Software GmbH

    This program is free software; you can redistribute it and/or modify it
    under the terms of the GNU General Public License version 2 as published
    by the Free Software Foundation.

    If the program is linked with libraries which are licensed under one of
    the following licenses, the combination of the program with the linked
    library is not considered a "derivative work" of the program:

        - Apache License, version 2.0
        - Apache Software License, version 1.0
        - GNU Lesser General Public License, version 3
        - Mozilla Public License, versions 1.0, 1.1 and 2.0
        - Common Development and Distribution License (CDDL), version 1.0

    Therefore the distribution of the program linked with libraries licensed
    under the aforementioned licenses, is permitted by the copyright holders
    if the distribution is compliant with both the GNU General Public
    License version 2 and the aforementioned licenses.

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
    Public License for more details.

--%>

<%@page import="java.util.Set"%>
<% ServletContext sc = request.getServletContext();%>
<% String processid = sc.getAttribute("processId").toString();%>
<% Set<?> jobSet = (Set<?>)sc.getAttribute("jobSet");%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>

<h2>Jobs</h2>
<ul>
<c:forEach var="job" items="${jobSet}">
<li>${job}</li>
</c:forEach>
</ul>
</div>

<h2>Submit new job</h2>

<div>

<textarea name="request" id="requestTextarea" style="width : 800px; height : 485px">
{
	"inputs": [{
		"id": "data",
		"input": {
			"format": {
				"mimeType": "text/xml",
				"schema": "http://schemas.opengis.net/gml/3.1.1/base/feature.xsd"
			},
			"value": {
				"href": "http://geoprocessing.demo.52north.org:8080/geoserver/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=topp:tasmania_roads&SRS=EPSG:4326&OUTPUTFORMAT=GML3"
			}
		}
	},
	{
		"id": "width",
		"input": {
			"format": {
				"mimeType": "text/plain"
			},
			"value": {
				"inlineValue": "0.05"
			}
		}
	}],
	"outputs": [{
		"id": "result",
		"format": {
			"mimeType": "text/xml",
			"schema": "http://schemas.opengis.net/gml/3.1.1/base/feature.xsd"
		},
		"transmissionMode": "reference"
	}]
}
</textarea>
<p></p>
<button id="sendButton">Create job</button>

</div>
<p></p>
<div>
<h2>Response location</h2>
<input type="text" id="locationText" ></input>
</div>

<!-- <form id="requestForm" method="post" class="form-horizontal" action=""> -->
<!-- 	<div class="form-group"> -->
<!-- 		<label class="col-lg-2 control-label">Service URL</label> -->
<!-- 		<div class="col-lg-10"> -->
<%-- 			<input class="form-control" name="url" id="serviceUrlField" value="<%= processid %>" type="text" /> --%>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	<div class="form-group"> -->
<!-- 		<label class="col-lg-2 control-label">Request Examples</label> -->
<!-- 		<div class="col-lg-10"> -->
<!-- 			<select id="selRequest" class="form-control"> -->
<!-- 				<option value=" "></option> -->
<!-- 			</select> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	<div class="form-group"> -->
<!-- 		<pre class="editor"><textarea name="request" id="requestTextarea" class="form-control"></textarea></pre> -->
<!-- 	</div> -->
<!-- 	<div class="form-group"> -->
<!-- 		<button type="submit" class="btn btn-primary">Send</button> -->
<!-- 		<button id="clearBtn" type="reset" class="btn btn-primary">Clear</button> -->
<!-- 	</div> -->
<!-- </form> -->
<!-- <div class="form-horizontal"> -->
<!-- 	<div class="form-group"> -->
<!-- 		<pre><textarea name="request" id="responseTextarea"></textarea></pre> -->
<!-- 	</div> -->
<!-- </div> -->

<script src="<c:url value="/js/vendor/jquery-1.11.3.min.js" />"></script>
<script type="text/javascript">
$(document).ready(
		function() {
				
				$('#sendButton').click(function(event){
					xhttp = new XMLHttpRequest();
					xhttp.open("POST", "./jobs", true);
					xhttp.setRequestHeader("Content-type", "application/json");
					xhttp.send($('#requestTextarea').val());
					
					  $.ajax({
						   type: 'POST',
						   contentType: "application/json",
						   url:'./jobs',
						   data: $('#requestTextarea').val(),
						   success: function(data, textStatus, request){
							   $('#locationText').val(request.getResponseHeader('location'));
						   },
						   error: function (request, textStatus, errorThrown) {
						        alert("Error");
						   }
					  });
					
				});
			});
</script>