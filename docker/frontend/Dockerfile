# Use an official Node.js runtime as a parent image for the build stage
FROM node:20-alpine as build

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular application for production
RUN npm run build --prod

# Use a lightweight web server (Nginx) to serve the Angular app
FROM nginx:alpine

# Copy the built Angular app from the build stage to the Nginx web server directory
COPY --from=build /usr/src/app/dist/front-end-ecommerce /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start the nginx
# CMD ["nginx", "-g", "daemon off;"]
