-- create keyspace datacenter

CREATE KEYSPACE datacenter
   WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};
           
-- create datasets table

USE datacenter;

CREATE TABLE datasets (
   datasource  text,
   type        text,
   version     timestamp,
   id          text,
   dataset     map<text,text>,
   primary key ((datasource,type,version),id)
);

CREATE TABLE datasources (
	datasource text,
	type       text,
	version    timestamp,
    primary key ((datasource,type),version)
);


 
