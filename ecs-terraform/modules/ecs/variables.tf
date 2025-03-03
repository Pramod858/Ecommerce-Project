variable "environment" {
    type = string
}

variable "region" {
    type = string
}

variable "vpc_id" {
    type = string
}

variable "public_subnet_1_id" {
    type = string
}

variable "public_subnet_2_id" {
    type = string
}

variable "private_subnet_1_id" {
    type = string
}

variable "private_subnet_2_id" {
    type = string
}

variable "public_alb_sg_id" {
    type = string
}

variable "internal_alb_sg_id" {
    type = string
}

variable "ecs_security_group_id" {
    type = string
}

variable "ecsTaskExecutionRole_arn" {
    type = string
}

variable "ecsTaskRole_arn" {
    type = string
}

variable "acm_certificate_arn" {
    type = string
}

variable "spring_container_port" {
    type = number
}

variable "angular_container_port" {
    type = number
}

variable "container_env_vars_config" {
    type = string
    default = ""
}

variable "task_count" {
    type = number
    default = 1
}

variable "min_capacity" {
    type = number
    default = 1
}

variable "max_capacity" {
    type = number
    default = 3
}

