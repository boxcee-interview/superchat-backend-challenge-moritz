FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine

RUN apk update && apk upgrade && apk add curl \
  && rm -rf /var/cache/apk/* \
  && mkdir /app \
  && curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/master/contrib/install.sh | sh -s -- -b /usr/local/bin \
  && trivy filesystem --format=table --severity=MEDIUM,HIGH,CRITICAL --exit-code=1 --ignore-unfixed=true --skip-files=/usr/local/bin/trivy /

COPY build/libs/superchat-backend-challenge-moritz-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]
