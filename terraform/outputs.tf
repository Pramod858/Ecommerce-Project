output "vpc_id" {
    value = module.vpc.vpc_id
}

output "internal_alb_dns_addr" {
    value = module.ecs.internal_alb_dns_addr
}

output "db_endpoint" {
    value = module.rds.db_endpoint
}
