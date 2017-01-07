<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<meta http-equiv="x-ua-compatible" content="IE=10">
	<script src="static/search.js"></script>
	<link rel="stylesheet" type="text/css" href="static/styles.css"> 
	<title>Datacenter</title>
</head>
<body>
<h1 class=title>Data center</h1>
<p class=navbar>
<a href="datasources">datasources</a>
</p>
<p class=searchbar>
<input id="searchInput" type="text" value="" size="50" onkeyup="searchFor(this.value)"></input>
</p>
<div class=results id="results" >
<p>
${message}
</p> 
<p>
store: ${store}
</p> 
</div>
</body>
</html>