# Getting Started

event-generator-consumer microservice consumes the employee card swipe events and save in cassandra db 

### Build & run container app 

* Run this command from root of the project to build the docker container
```` 
docker build --rm -t consumer .
docker-compose up -d --build
````
* Run this command to run the docker container & connect to the kafka broker inside net_kafka network
```` 
docker run -it --rm --network net_kafka consumer
````

* View the kafka consumed events
```` 
docker inspect consumer_cid >> Get the container ip address
http://<ip address>:8080/events/{batchSize} 
````
