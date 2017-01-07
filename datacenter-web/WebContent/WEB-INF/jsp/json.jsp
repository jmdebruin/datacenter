<%@ page

	language="java"
	contentType="application/json;
	charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="com.fasterxml.jackson.databind.ObjectMapper"

%><%=

	new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request.getAttribute("message")) 

%>