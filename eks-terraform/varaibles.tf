#AUTHOR: Pramod Badiger

variable "vpc_cidr" {
    description = "VPC CIDR block"
    default     = "10.0.0.0/16"
}

variable "public_sb1_cidr" {
    description = "Public subnet 1 CIDR block"
    default     = "10.0.1.0/24"
}

variable "public_sb2_cidr" {
    description = "Public subnet 2 CIDR block"
    default     = "10.0.2.0/24"
}

variable "private_sb1_cidr" {
    description = "Private subnet 1 CIDR block"
    default     = "10.0.3.0/24"
}

variable "private_sb2_cidr" {
    description = "Private subnet 2 CIDR block"
    default     = "10.0.4.0/24"
}

variable "region" {
    description = "AWS region"
    default     = "us-east-1"
}

variable "key_pair" {
    description = "Key-Pair"
    type        = string
}
