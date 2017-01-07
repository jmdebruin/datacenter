<%@ page

	language="java"
	contentType="text/html;
	charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"

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
  <c:forEach items="${loglines}" var="logline">
    <tr>
      <td><c:out value="${logline}" /></td>
    </tr>
  </c:forEach>
</table>
</body>
</html>