# Messaging Service
Messaging service for creating, modifying, deleting and retrieving messages.


```
$ mvn clean install  
$ java -jar target\messaging-service-${project.version}.jar
```

## Swagger

``http://localhost:8080/ms/swagger-ui.html``


## Security 
Create/Update/Delete services are protected with basic authentication with in memory user credentials. 
Use following - 

``user : password``

``user2@test.com : password``

``user3@test.com : password``


## Notes 
Project lombok is used, Install it inside your editor (Eclipse). 
