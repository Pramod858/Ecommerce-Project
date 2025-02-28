# Create an ECS cluster
resource "aws_ecs_cluster" "ecs_cluster" {
    name = "${var.environment}-ecs-cluster"
}


# Application Load Balancer
resource "aws_lb" "internal_alb" {
    name               = "${var.environment}-internal-alb"
    load_balancer_type = "application"
    security_groups    = [var.internal_alb_sg_id]
    subnets            = [var.public_subnet_1_id,var.public_subnet_2_id]
    ip_address_type    = "ipv4" 

    tags = {
        Name = "${var.environment}-internal-alb"
    }
}

# Target Group
resource "aws_lb_target_group" "api_target_group" {
    name        = "${var.environment}-api-target-grp"
    port        = var.spring_container_port
    protocol    = "HTTP"
    vpc_id      = var.vpc_id
    target_type = "ip"

    health_check {
        path                = "/actuator/health"  # Use if Actuator is enabled
        interval            = 30
        timeout             = 5
        healthy_threshold   = 2
        unhealthy_threshold = 2
    }

    depends_on = [ aws_lb.internal_alb ]
}

resource "aws_lb_listener" "ecs_internal_alb_listener_http" {
    load_balancer_arn = aws_lb.internal_alb.arn
    port = 80
    protocol = "HTTP"

    default_action {
        type = "redirect"

        redirect {
            port = 443
            protocol = "HTTPS"
            status_code = "HTTP_301"
        }
    }

    depends_on = [ aws_lb.internal_alb, aws_lb_target_group.api_target_group ]
}

resource "aws_lb_listener" "ecs_internal_alb_listener_https" {
    load_balancer_arn = aws_lb.internal_alb.arn
    port              = 443
    protocol          = "HTTPS"
    certificate_arn   = var.acm_certificate_arn

    default_action {
        type             = "forward"
        target_group_arn = aws_lb_target_group.api_target_group.arn
    }

    depends_on = [aws_lb.internal_alb, aws_lb_target_group.api_target_group]
}



resource "aws_cloudwatch_log_group" "ecs_spring" {
    name              = "/ecs/spring/${var.environment}"
    retention_in_days = 1
}

resource "aws_ecs_task_definition" "spring_task_definition" {
    family                   = "${var.environment}-spring-task-definition"
    network_mode             = "awsvpc"
    requires_compatibilities = ["FARGATE"]
    cpu                      = "512"
    memory                   = "1024"

    execution_role_arn       = var.ecsTaskExecutionRole_arn
    task_role_arn            = var.ecsTaskRole_arn
    
    container_definitions    = <<EOF
[
    {
    "name": "${var.environment}-spring",
    "image": "${data.aws_ecr_repository.spring_ecr_repo.repository_url}:<image_tag>",
    "essential": true,
    "portMappings": [
        {
            "containerPort": ${var.spring_container_port}
        }
    ],
    ${var.container_env_vars_config}
    "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
            "awslogs-region": "${var.region}",
            "awslogs-group": "/ecs/spring/${var.environment}",
            "awslogs-stream-prefix": "ecs"
            }
        }
    }
]
EOF
}

# Create an ECS service
resource "aws_ecs_service" "spring_ecs_service" {
    name            = "${var.environment}-spring-ecs-service"
    cluster         = aws_ecs_cluster.ecs_cluster.id
    task_definition = aws_ecs_task_definition.spring_task_definition.arn
    desired_count   = var.task_count
    launch_type     = "FARGATE"


    network_configuration {
        subnets          = [var.private_subnet_1_id,var.private_subnet_2_id]
        security_groups  = [var.ecs_security_group_id]
        assign_public_ip = false
    }

    load_balancer {
        target_group_arn = aws_lb_target_group.api_target_group.arn
        container_name   = "${var.environment}-spring"
        container_port   = var.spring_container_port
    }

    depends_on = [
        aws_ecs_cluster.ecs_cluster,
        aws_ecs_task_definition.spring_task_definition,
        aws_lb_listener.ecs_internal_alb_listener_http,
        aws_lb_listener.ecs_internal_alb_listener_https
    ]
}

resource "aws_lb" "public_alb" {
    name                = "${var.environment}-public-alb"
    load_balancer_type  = "application"
    security_groups     = [var.public_alb_sg_id]
    subnets             = [var.public_subnet_1_id, var.public_subnet_2_id]

    tags = {
        Name = "${var.environment}-public-alb"
    }
}

resource "aws_lb_target_group" "public_target_group" {
    name         = "${var.environment}-pub-target-group"
    port         = var.angular_container_port
    protocol     = "HTTP"
    vpc_id       = var.vpc_id
    target_type  = "ip"

    depends_on   = [aws_lb.public_alb]
}

resource "aws_lb_listener" "ecs_public_alb_listener_http" {
    load_balancer_arn = aws_lb.public_alb.arn
    port              = 80
    protocol          = "HTTP"

    default_action {
        type = "redirect"

        redirect {
            port        = 443
            protocol    = "HTTPS"
            status_code = "HTTP_301"
        }
    }

    depends_on = [ aws_lb.public_alb, aws_lb_target_group.public_target_group ]
}

resource "aws_lb_listener" "ecs_public_alb_listener_https" {
    load_balancer_arn = aws_lb.public_alb.arn
    port              = 443
    protocol          = "HTTPS"
    certificate_arn   = var.acm_certificate_arn

    default_action {
        type             = "forward"
        target_group_arn = aws_lb_target_group.public_target_group.arn
    }

    depends_on = [ aws_lb.public_alb, aws_lb_target_group.public_target_group ]
}

resource "aws_cloudwatch_log_group" "ecs_angular" {
    name              = "/ecs/angular/${var.environment}"
    retention_in_days = 1
}

resource "aws_ecs_task_definition" "angular_task_definition" {
    family                   = "${var.environment}-angular-task-definition"
    network_mode             = "awsvpc"
    requires_compatibilities = ["FARGATE"]
    cpu                      = "512"
    memory                   = "1024"

    execution_role_arn       = var.ecsTaskExecutionRole_arn
    task_role_arn            = var.ecsTaskRole_arn
    
    container_definitions    = <<EOF
[
    {
    "name": "${var.environment}-angular",
    "image": "${data.aws_ecr_repository.angular_ecr_repo.repository_url}:<image_tag>",
    "essential": true,
    "portMappings": [
        {
            "containerPort": ${var.angular_container_port}
        }
    ],
    ${var.container_env_vars_config}
    "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
            "awslogs-region": "${var.region}",
            "awslogs-group": "/ecs/angular/${var.environment}",
            "awslogs-stream-prefix": "ecs"
            }
        }
    }
]
EOF
}

# Create an ECS service
resource "aws_ecs_service" "angular_ecs_service" {
    name            = "${var.environment}-angular-ecs-service"
    cluster         = aws_ecs_cluster.ecs_cluster.id
    task_definition = aws_ecs_task_definition.angular_task_definition.arn
    desired_count   = var.task_count
    launch_type     = "FARGATE"


    network_configuration {
        subnets          = [var.private_subnet_1_id,var.private_subnet_2_id]
        security_groups  = [var.ecs_security_group_id]
        assign_public_ip = false
    }

    load_balancer {
        target_group_arn = aws_lb_target_group.public_target_group.arn
        container_name   = "${var.environment}-angular"
        container_port   = var.angular_container_port
    }

    depends_on = [
        aws_ecs_cluster.ecs_cluster,
        aws_ecs_task_definition.angular_task_definition,
        aws_lb_listener.ecs_public_alb_listener_http,
        aws_lb_listener.ecs_public_alb_listener_https
    ]
}
