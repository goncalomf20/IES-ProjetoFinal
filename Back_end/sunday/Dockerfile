# Use a base image with OpenJDK and redis-tools
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy the application files
COPY . .

# Set the entry point
ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "./wait-for-it.sh", "rabbitmq:15672", "--", "./mvnw", "spring-boot:run"]

