# # Build the final image with the application JAR
# FROM openjdk:17

# WORKDIR /app

# COPY wait-for-it.sh wait-for-it.sh

# RUN chmod +x wait-for-it.sh

# CMD ["./wait-for-it.sh", "localhost:8080"]


# Use an official Node.js runtime as the base image
FROM node:17-alpine

# Set the working directory in the Docker image
WORKDIR /app

# Copy the application files to the Docker image
COPY package.json .

# Install dependencies
RUN yarn

# Copy the rest of the application files to the Docker image
COPY . .


# Start the application
CMD ["npm", "run", "dev"]