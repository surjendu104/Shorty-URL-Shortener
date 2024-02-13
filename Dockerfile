FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y mongodb
RUN apt-get install -y openjdk-11-jre
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*
RUN service mongodb start
RUN sleep 5
EXPOSE 8080
ADD target/url-shortener-images.jar url-shortener-images.jar
ENTRYPOINT ["java","-jar","/url-shortener-images.jar"]