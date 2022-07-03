# Json Database

JSON database is a single file database that stores information in the form of JSON. It is a remote database, 
so it's usually accessed through the Internet.

Socket connection is built with using of a multithreaded server that accepts requests from many clients simultaneously.

To use the app:
- run server Main
- run client Main with args as listed bellow

You can send requests by two ways: 
1. Pass to a client Main args: <br />
-t operation type ("get", "set", "delete", "exit")<br /> 
-k key (key you want to operate in DB)<br /> 
-v value (value you want to set)<br />
Args examples:<br /> 
-t get -k name<br />
-t set -k name -v John<br />
-t delete -k name<br />
-t exit<br />
2. Pass to a client args a file name that contains command as json object (example-set is in folder resources/data, 
also you can create your own files or correct current)<br />
Args example:<br /> 
-in testSet.json<br />
