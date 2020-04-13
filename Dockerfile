#base image
FROM java:8
COPY /target/myorange-0.0.1-SNAPSHOT.jar myorange-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch /myorange-0.0.1-SNAPSHOT.jar'
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myorange-0.0.1-SNAPSHOT.jar"]

