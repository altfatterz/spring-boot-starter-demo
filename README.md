`common-starter` defines common functionality for microservices like
1. security configurations
2. logging 


`foo-service` uses the `common-starter`

```bash
http :8080/api/foo 

{
    "detail": "Full authentication is required to access this resource",
    "status": 401,
    "title": "Unauthorized"
}
```

```bash
$ http :8080/api/foo\?token=xyz
foo
```

```bash
$ http :8080/actuator/env

{
    "error": "Unauthorized",
    "message": "",
    "path": "/actuator/env",
    "status": 401,
    "timestamp": "2020-08-04T21:15:13.666+00:00"
}
```

```bash
$ http :8080/actuator/env -a admin:secret
HTTP/1.1 200
...
```

override in the foo-service for example:

```bash
$ java -jar foo-service/target/foo-service-0.0.1-SNAPSHOT.jar --spring.security.user.password=verysecre
```
