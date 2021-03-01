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
<% String originalRequestURL = sc.getAttribute("originalRequestURL").toString();%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>

<h2>Jobs</h2>
<a href="${originalRequestURL}.json" target="_blank">Show this page as JSON document</a>
</div>

<h2>Submit new job</h2>

<div>

<button id="loadButton">Load example request</button>
<p></p>
<textarea name="request" id="requestTextarea" style="width : 800px; height : 485px"></textarea>
<p></p>
<button id="sendButton">Create job</button>
</div>
<p></p>
<div id="responseLocationDiv" style="display: none">
<h2>Response location</h2>
<input type="text" id="locationText" style="width : 800px"></input><button id="openButton">Open</button>
</div>
<div id="responseDiv" style="display: none">
<h2>Response</h2>
<textarea name="request" id="responseTextarea" style="width : 800px; height : 485px"></textarea>
</div>
<script src="<c:url value="/js/vendor/jquery-1.11.3.min.js" />"></script>
<script type="text/javascript">
$(document).ready(
		function() {
				
				$('#sendButton').click(function(event){
					$('#responseDiv').hide();
					$('#responseLocationDiv').hide();
					
					var data = $('#requestTextarea').val();
					
					var isSyncExecute = isSyncExecutionMode(data);
					
					if(isSyncExecute){
						//sync execute
						
						  $.ajax({
							   type: 'POST',
							   contentType: "application/json",
							   url:'./jobs?sync-execute=true',
							   data: data,
							   success: function(data, textStatus, request){
								   $('#responseTextarea').val(JSON.stringify(data,null,4));
								   $('#responseDiv').show();
							   },
							   error: function (request, textStatus, errorThrown) {
							        alert("Error");
							   }
						  });
						
					} else {
						
						  $.ajax({
							   type: 'POST',
							   contentType: "application/json",
							   url:'./jobs',
							   data: data,
							   success: function(data, textStatus, request){
								   $('#locationText').val(request.getResponseHeader('location'));
								   $('#responseLocationDiv').show();
							   },
							   error: function (request, textStatus, errorThrown) {
							        alert("Error");
							   }
						  });
						
					}
				});
				$('#openButton').click(function(event){
					window.open($('#locationText').val(), '_blank');			
				});
			});
			
			function isSyncExecutionMode(data)  {
				const obj = JSON.parse(data)
				return obj.mode == "sync";
			}
</script>