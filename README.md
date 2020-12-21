# Site checker

Backend for the OhMyDown.com project

### Swagger
http://localhost:8080/swagger-ui.html

### Running from a different profile
```
gradle bootRun --args='--spring.profiles.active=fast'
```
```
java -jar -Dspring.profiles.active=fast *.jar
```