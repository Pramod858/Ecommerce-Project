output "acm_certificate_arn" {
    value = aws_acm_certificate.cert.arn
}

output "route53_zone_id" {
    value = data.aws_route53_zone.selected.zone_id
}