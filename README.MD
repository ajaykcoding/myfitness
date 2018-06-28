###Requirements

 ``Java 8``

###Running the Code

####Windows - 
SET JAVA_HOME=<PATH_TO_JAVA>

run `mvnw clean install`

run `java -jar target\chatserver-0.0.1-SNAPSHOT.jar`

####OSX - 
EXPORT JAVA_HOME=<PATH_TO_JAVA>

run `./mvnw clean install`

run `java -jar target\chatserver-0.0.1-SNAPSHOT.jar`

#### Limitations
Uses a local H2 

Can be easily switched to mysql with proper indexes 

### if i had more time 
Docker and DockerCompose 
and use redis 

