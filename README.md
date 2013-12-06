MongoDBSample
=============

This is sample project showing how to work with MongoDB driver API.

To run tests you need to bring MongoDB database up, populate it with initial tweets data and optional run it in sharded/replicated configuration. 

> NOTE: Pay attention to DB port you are using to connect to MongoDB using MongoDb driver! Also you need to use full domain name of your computer if you connected to network, otherwise you can use simply localhost.

### How to get MongoDB and how to get it up and running?  
* Download and unzip MongoDB distribution somewhere on your computer.  
* Run ./mongod from %MONGO_HOME%/bin directory to start MongoDB on default port 27017.  
* Run ./mongo from %MONGO_HOME%/bin to open admin shell connected to default MongoDB port.  

### How to prepare test data?
* Create new DB by running `use mongo_lecture` command where `mongo_lecture` - name of your db.
* Import data into your database by running  
`mongoimport --db mongo_lecture --collection twitter --file <path to out_parse.json file from project home>`  
command from %MONGO_HOME%/bin
* Check that everything is OK by checking count() of tweets in twitter collection after data import running `db.twitter.count()`  
command from admin shell

### How to transform single MongoDB instance into ReplicaSet?
* Create data directories for two other replicas somewhere on your file system.
* Run two other replicas by running  
`./mongod --dbpath <path to data directory created on 1st step> --port <free port> --replSet kpi/localhost:27017`  
command
* Connect by shell to main replica with data (e.g. `./mongo localhost:27017/admin`) and initialize replicaSet by running command like below with your replica ports.
```
rs.initiate(
{
	"_id" : "kpi",
	"version" : 1,
	"members" : [
		{
			"_id" : 0,
			"host" : "localhost:27017"
		},
		{
			"_id" : 1,
			"host" : "localhost:30001"
		},
		{
			"_id" : 2,
			"host" : "localhost:30002"
		}
	]
})
```
* Check that replica set initialized properly. Connect to any replica by shell and run `rs.status()` command which will show you whole replica set status. 
> NOTE: Use admin database for it.

### How to add simplest sharding?
* Create data directory for config server somewhere on your file system.
* Run config server by running  
`./mongod --dbpath <path to data directory created on 1st step> --port <free port>`  
command
* Run router process by running `./mongos --port <free port> --configdb <config server process url>` command
* Add "kpi" replicaSet as shard to router by running following command from shell connected to router instance:
`sh.addShard("kpi/localhost:27017")`
* Check your sharded configuration by running `sh.status()` command from shell connected to mongos router process
