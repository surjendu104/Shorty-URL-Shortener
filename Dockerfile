FROM openjdk:17-jdk
EXPOSE 8080
ADD target/url-shortener-images.jar url-shortener-images.jar
ENTRYPOINT ["java","-jar","/url-shortener-images.jar"]