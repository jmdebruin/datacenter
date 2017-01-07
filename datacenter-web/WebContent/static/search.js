/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
var dataSets="";

function showSearchResults(jsonResponse) {
	dataSets = JSON.parse(jsonResponse);
	if (dataSets.length == 1){
		showDataSet(dataSets[0]);
	} else {
		var body = document.getElementById("results");
		body.innerHTML="";		
		var list = document.createElement('ul');
		for (var i=0; i< dataSets.length; i++) {
			var dataSet=dataSets[i];
			var listItem = document.createElement('li');
			var bold = document.createElement('b');
			var text = document.createTextNode(dataSet.type+': ');
			bold.appendChild(text);			
			listItem.appendChild(bold);
			var href = document.createElement('a');
			linktext =  document.createTextNode(dataSet.id);
			href.id=dataSet.id;
			href.href="\#";
			href.setAttribute("onclick","showSelectedDataSet(this.id);return false;");
			href.appendChild(linktext);
			text = document.createTextNode(getDataSetSummary(dataSet));
			listItem.appendChild(href);
			listItem.appendChild(text);
			list.appendChild(listItem);
		}
		body.appendChild(list);
	}
}

function getDataSetSummary(dataSet){
	var properties = dataSet.properties
	var keys = Object.keys(properties);
	var str = ', '+properties['description'];
	for (var i=0; i< keys.length; i++) {
		var key = keys[i];
		var value = properties[key];
		if (!(key.charAt(0) === '.' || key === 'description' )){
			str += ', '+ key + ': ' + value;
		}
	}
	return str;
}

function showSelectedDataSet(id) {
	var n=dataSets.length;
	for (var i=0; i< dataSets.length; i++) {
		if (dataSets[i].id === id){
			var dataSet=dataSets[i];
			showDataSet(dataSet);
			break;
		}
	}	
}

function showDataSet(dataSet){
	var body = document.getElementById("results");
	body.innerHTML="";
	var table = document.createElement('TABLE');
	table.className = "dataSet";
	var properties = dataSet.properties
	var keys = Object.keys(properties);
	for (var i=0; i< keys.length; i++) {
		var row = table.insertRow(-1);
		var cell1 = row.insertCell(-1)
		var cell2 = row.insertCell(-1)
		cell1.innerHTML=keys[i];
		cell2.innerHTML=properties[keys[i]];		
	}
	body.appendChild(table)
}

// global
function searchFor(str){
	var xmlhttp;
	str = str.trim();
	if (str.length==0) return;
    if (window.XMLHttpRequest) {
       xmlhttp = new XMLHttpRequest();
    }
    xmlhttp.onreadystatechange = function () {
       if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
          showSearchResults(xmlhttp.responseText);
       }
    };
	xmlhttp.open("GET", "searchFor?text="+str,true);
    xmlhttp.send();
	
}
