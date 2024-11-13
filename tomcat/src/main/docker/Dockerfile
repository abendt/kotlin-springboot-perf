# this builds a layered Docker image
# https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html

FROM amazoncorretto:21-alpine-jdk as builder

WORKDIR /builder
COPY *.jar backend.jar
RUN java -Djarmode=tools -jar backend.jar extract --layers --destination extracted

FROM amazoncorretto:21-alpine-jdk

WORKDIR /backend

RUN apk update && apk add --no-cache curl jq

USER nobody

ENV JAVA_OPTS=""
ENV OTEL_JAVAAGENT_ENABLED=false
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar backend.jar"]

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ARG COMMIT
LABEL commit_sha=${COMMIT}
ENV COMMIT_SHA=${COMMIT}
