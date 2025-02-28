resource "aws_iam_role" "ecsTaskExecutionRole" {
    name               = "${var.environment}-ecsTaskExecutionRole"
    assume_role_policy = jsonencode({
        Version        = "2012-10-17"
        
        Statement = [
        {
            Effect    = "Allow"
            Principal = {
                Service   = "ecs-tasks.amazonaws.com"
            }
            Action    = "sts:AssumeRole"
        }
        ]
    })
}

resource "aws_iam_role_policy_attachments_exclusive" "example" {
    role_name   = aws_iam_role.ecsTaskExecutionRole.name
    policy_arns = ["arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"]
}


resource "aws_iam_role" "ecsTaskRole" {
    name               = "${var.environment}-ecsTaskRole"
    assume_role_policy = jsonencode({
        Version        = "2012-10-17"
        
        Statement = [
        {
            Effect    = "Allow"
            Principal = {
                Service   = "ecs-tasks.amazonaws.com"
            }
            Action    = "sts:AssumeRole"
        }
        ]
    })
}
