#AUTHOR: Pramod Badiger

terraform {
    backend "s3" {
        bucket = "pramod858tf"
        key    = "EKS/terraform.tfstate"
        region = "us-east-1"
    }
}
