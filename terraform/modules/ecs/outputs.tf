output "internal_alb_dns_addr" {
    value = aws_lb.internal_alb.dns_name
}

output "internal_alb_zone_id" {
    value = aws_lb.internal_alb.zone_id
}

output "public_alb_dns_addr" {
    value = aws_lb.public_alb.dns_name
}

output "public_alb_zone_id" {
    value = aws_lb.public_alb.zone_id
}