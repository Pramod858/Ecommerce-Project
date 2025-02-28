data "aws_ecr_repository" "spring_ecr_repo" {
    name = "${var.environment}-spring-ecr"
}

data "aws_ecr_repository" "angular_ecr_repo" {
    name = "${var.environment}-angular-ecr"
}