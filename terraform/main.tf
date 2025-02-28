module "vpc" {
    source                 = "./modules/vpc"

    environment            = var.environment
    region                 = var.region
    spring_container_port  = var.spring_container_port
    angular_container_port = var.angular_container_port
}

module "iam" {
    source      = "./modules/iam"

    environment = var.environment
}

module "rds" {
    source               = "./modules/rds"

    depends_on           = [ module.vpc ]
    environment          = var.environment
    region               = var.region 
    private_subnet_3_id  = module.vpc.private_subnet_3_id
    private_subnet_4_id  = module.vpc.private_subnet_4_id
    db_security_group_id = module.vpc.db_security_group_id

    db_name              = var.db_name
    db_username          = var.db_username
    db_password          = var.db_password
}

module "acm" {
    source             = "./modules/acm"
    depends_on         = [ module.vpc ]
    domain_name        = var.domain_name
}

module "ecs" {
    source                    = "./modules/ecs"

    depends_on                = [ module.vpc, module.rds, module.acm] 
    region                    = var.region
    environment               = var.environment
    vpc_id                    = module.vpc.vpc_id
    public_subnet_1_id        = module.vpc.public_subnet_1_id
    public_subnet_2_id        = module.vpc.public_subnet_2_id
    private_subnet_1_id       = module.vpc.private_subnet_1_id
    private_subnet_2_id       = module.vpc.private_subnet_2_id
    public_alb_sg_id          = module.vpc.public_alb_sg_id
    internal_alb_sg_id        = module.vpc.internal_alb_sg_id
    ecs_security_group_id     = module.vpc.ecs_security_group_id

    ecsTaskExecutionRole_arn  = module.iam.ecsTaskExecutionRole_arn
    ecsTaskRole_arn           = module.iam.ecsTaskRole_arn
    acm_certificate_arn       = module.acm.acm_certificate_arn
    spring_container_port     = var.spring_container_port
    angular_container_port    = var.angular_container_port

    container_env_vars_config = <<EOF
        "environment" : [
            {"name": "SPRING_DATASOURCE_URL", "value": "jdbc:mysql://${module.rds.db_endpoint}:3306/${var.db_name}"},
            {"name": "SPRING_DATASOURCE_USERNAME", "value": "${var.db_username}"},
            {"name": "SPRING_DATASOURCE_PASSWORD", "value": "${var.db_password}"},
            {"name": "ANGULAR_URL", "value": "https://${var.angular_domain_name}"}
        ],
    EOF
}

# module "ec2" {
#     source                     = "./modules/ec2"

#     depends_on                 = [ module.vpc ]
#     environment                = var.environment
#     vpc_id                     = module.vpc.vpc_id
#     public_subnet_1_id         = module.vpc.public_subnet_1_id
#     public_subnet_2_id         = module.vpc.public_subnet_2_id
#     bastion_security_group_id  = module.vpc.bastion_security_group_id
#     key_name                   = var.key_name
# }

module "route53" {
    source          = "./modules/route53"

    depends_on            = [ module.ecs, module.acm ]
    environment           = var.environment
    domain_name           = var.domain_name
    spring_domain_name    = var.spring_domain_name
    angular_domain_name   = var.angular_domain_name
    route53_zone_id       = module.acm.route53_zone_id
    internal_alb_dns_name = module.ecs.internal_alb_dns_addr
    internal_alb_zone_id  = module.ecs.internal_alb_zone_id
    public_alb_dns_name   = module.ecs.public_alb_dns_addr
    public_alb_zone_id    = module.ecs.public_alb_zone_id
}