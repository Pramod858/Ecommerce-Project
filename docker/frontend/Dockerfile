# Use an official Node.js runtime as a parent image for the build stage
FROM node:20-alpine AS build

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Copy the rest of the application code
COPY . /usr/src/app/

# Install dependencies
RUN npm install

# Build the Angular application for production
RUN npm run build --configuration production

# Use a lightweight web server (Nginx) to serve the Angular app
FROM nginx:alpine

# Copy custom Nginx configuration
# COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built Angular app from the build stage to the Nginx web server directory
COPY --from=build /usr/src/app/dist/front-end-ecommerce/browser /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
