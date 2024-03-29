FROM gradle:7.6-jdk19-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean bootJar --no-daemon

FROM openjdk:19-alpine
COPY --from=build /home/gradle/src/build/libs/*.jar zombietracker.jar
CMD java -jar zombietracker.jar
EXPOSE 8080