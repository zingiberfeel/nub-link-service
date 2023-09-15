#!/bin/bash

# Generate TailwindCSS
npm install # Install dependencies (if not already installed)
npx tailwindcss -i styles/input.css -o ./src/main/resources/static/css/main.css

# Build the Docker image
docker build -t nub-snippet-service-image .

# Run the Docker container
docker run -d -p 8080:8080 --name nub-snippet-service-container nub-snippet-service-image