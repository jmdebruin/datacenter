<%@ page

	language="java"
	contentType="text/html;
	charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="nl.slack4all.datacenter.lib.DataSet"

%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Test_01</title>
</head>
<body>
<table>
  <c:forEach items="${list}" var="dataSet">
    <tr>
      <td><c:out value="${dataSet.getId()}" /></td>
      <td><c:out value="${dataSet.getMap()}" /></td>
    </tr>
  </c:forEach>
</table>
</body>
</html>