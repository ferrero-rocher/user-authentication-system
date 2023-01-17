FROM maven:3.5.4-jdk-8
WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests
EXPOSE 9001
CMD ["mvn", "spring-boot:run"]