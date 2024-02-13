FROM ubuntu:latest

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y gnupg curl

RUN curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | gpg --dearmor -o /usr/share/keyrings/mongodb-server-7.0.gpg
RUN echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-7.0.list

RUN apt-get update && \
    apt-get install -y mongodb-org

RUN apt-get update && \
    apt-get install -y openjdk-11-jre


EXPOSE 27017 8080
ADD target/url-shortener-images.jar url-shortener-images.jar
ENTRYPOINT ["mongod", "--fork", "--logpath", "/var/log/mongodb/mongod.log", "&&", "java", "-jar", "/url-shortener-images.jar"]