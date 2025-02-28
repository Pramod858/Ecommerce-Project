variable "environment" {
    type = string
}

variable "region" {
    type = string
}

variable "vpc_cidr" {
    type    = string
    default = "10.0.0.0/16"
}

variable "public_sb1_cidr" {
    type    = string
    default = "10.0.1.0/24"
}

variable "public_sb2_cidr" {
    type    = string
    default = "10.0.2.0/24"
}

variable "private_sb1_cidr" {
    type    = string
    default = "10.0.3.0/24"
}

variable "private_sb2_cidr" {
    type    = string
    default = "10.0.4.0/24"
}

variable "private_sb3_cidr" {
    type    = string
    default = "10.0.5.0/24"
}

variable "private_sb4_cidr" {
    type    = string
    default = "10.0.6.0/24"
}

variable "spring_container_port" {
    type = number
}

variable "angular_container_port" {
    type = number
}