output "vpc_id" {
    value = aws_vpc.vpc.id
}

output "public_alb_sg_id" {
    value = aws_security_group.public_alb_sg.id
}

output "internal_alb_sg_id" {
    value = aws_security_group.internal_alb_sg.id
}

output "ecs_security_group_id" {
    value = aws_security_group.ecs_sg.id
}

output "db_security_group_id" {
    value = aws_security_group.database_sg.id
}

output "bastion_security_group_id" {
    value = aws_security_group.bastion_security_group.id
}

output "public_subnet_1_id" {
    value = aws_subnet.public_subnet_1.id
}

output "public_subnet_2_id" {
    value = aws_subnet.public_subnet_2.id
}

output "private_subnet_1_id" {
    value = aws_subnet.private_subnet_1.id
}

output "private_subnet_2_id" {
    value = aws_subnet.private_subnet_2.id
}

output "private_subnet_3_id" {
    value = aws_subnet.private_subnet_3.id
}

output "private_subnet_4_id" {
    value = aws_subnet.private_subnet_4.id
}