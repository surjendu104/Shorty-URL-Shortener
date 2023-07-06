FROM openjdk:8
EXPOSE 8080
ADD target/url-shortener-images.jar url-shortener-images.jar
ENTRYPOINT ["java","-jar","/url-shortener-images.jar"]