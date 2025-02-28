resource "aws_appautoscaling_target" "spring_ecs_target" {
    max_capacity       = var.max_capacity
    min_capacity       = var.min_capacity
    resource_id        = "service/${var.environment}-ecs-cluster/${aws_ecs_service.spring_ecs_service.name}"
    scalable_dimension = "ecs:service:DesiredCount"
    service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "spring_ecs_policy" {
    name               = "${var.environment}-spring-scaling-policy"
    policy_type        = "TargetTrackingScaling"
    resource_id        = aws_appautoscaling_target.spring_ecs_target.resource_id
    scalable_dimension = aws_appautoscaling_target.spring_ecs_target.scalable_dimension
    service_namespace  = aws_appautoscaling_target.spring_ecs_target.service_namespace

    target_tracking_scaling_policy_configuration {
        predefined_metric_specification {
            predefined_metric_type = "ECSServiceAverageCPUUtilization"
        }
        target_value = 70
    }
}

resource "aws_appautoscaling_target" "angular_ecs_target" {
    max_capacity       = var.max_capacity
    min_capacity       = var.min_capacity
    resource_id        = "service/${var.environment}-ecs-cluster/${aws_ecs_service.angular_ecs_service.name}"
    scalable_dimension = "ecs:service:DesiredCount"
    service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "angular_ecs_policy" {
    name               = "${var.environment}-angular-scaling-policy"
    policy_type        = "TargetTrackingScaling"
    resource_id        = aws_appautoscaling_target.angular_ecs_target.resource_id
    scalable_dimension = aws_appautoscaling_target.angular_ecs_target.scalable_dimension
    service_namespace  = aws_appautoscaling_target.angular_ecs_target.service_namespace

    target_tracking_scaling_policy_configuration {
        predefined_metric_specification {
            predefined_metric_type = "ECSServiceAverageCPUUtilization"
        }
        target_value = 70
    }
}