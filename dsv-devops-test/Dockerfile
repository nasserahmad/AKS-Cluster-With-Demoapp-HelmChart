######Sample JAVA multistage docker file######

##Docker Best practices
#Dont use docker image based on a full operating system image,extra binaries,large image size & longer download,security vulnerabilities
#Use the smallest possible base image that fits your needs. No need full OS & eliminate extra binaries to improve performance.
#Be very specific about the images and tags, to be more precise we can use image SHA256. 
#Use explicit and deterministic Docker base image tags.
#Build & Install only what you need in production in the container image
#Find and fix security vulnerabilities in your container Docker image. Can use different tools like snyk
#Don’t run containers as root create a user
#Use multi-stage builds for your containers in rather than using larger image and defining multiple layers to install binaries and build your code.

# Java application build stage with Maven.
FROM maven:3.8.3-jdk-8-slim AS java-build
RUN mkdir /javaproject
COPY ./src /javaproject
COPY pom.xml /javaproject
WORKDIR /javaproject
##cache maven remote dependencies of .m2 repository to avoid downloading in future build to improve performance.
RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM adoptopenjdk/openjdk8:x86_64-alpine-jre8u345-b01 AS java-publish
ARG ORG
ENV ORG_NAME ${ORG}
RUN apk add dumb-init
RUN mkdir /java-app
#Don’t run containers as root create a user
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=java-build /javaproject/target/*.jar /java-app/application.jar
WORKDIR /java-app
RUN chown -R javauser:javauser /java-app
USER javauser
#Properly handle events to safely terminate a Java Docker web application
CMD "dumb-init" "java" "-jar" "application.jar"