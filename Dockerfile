FROM ubuntu:latest

RUN apt-get install gnupg curl
RUN curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | \
       sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg \
       --dearmor
RUN echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

RUN apt-get update
RUN apt-get install -y mongodb-org
RUN apt-get install -y openjdk-11-jre
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*
RUN service mongodb start
RUN sleep 5
EXPOSE 8080
ADD target/url-shortener-images.jar url-shortener-images.jar
ENTRYPOINT ["java","-jar","/url-shortener-images.jar"]