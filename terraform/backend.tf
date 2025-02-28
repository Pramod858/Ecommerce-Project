terraform {
    backend "s3" {
        bucket = "pramod858tf"
        key    = "terraform/ecommerce-app/terraform.tfstate"
        region = "us-east-1"
    }
}