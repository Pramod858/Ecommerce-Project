variable "environment" {
    type = string
    default = "ecommerce-app"
}   
variable "region" {
    type = string
    default = "us-east-1"
}

variable "db_username" {
    type = string
    default = "admin"
}

variable "db_password" {
    type = string
    default = "admin123"
}

variable "db_name" {
    type = string
    default = "mydb"
}

variable "spring_container_port" {
    type = number
    default = 8080
}

variable "angular_container_port" {
    type = number
    default = 80
}

variable "key_name" {
    type = string
    default = "AWS"
}

# Your hosted zone domain name
variable "domain_name" {
    type = string
    default = "pramodpro.xyz"
}

# Your application domain name
variable "spring_domain_name" {
    type = string
    default = "api.pramodpro.xyz"
}

variable "angular_domain_name" {
    type = string
    default = "app.pramodpro.xyz"
}
