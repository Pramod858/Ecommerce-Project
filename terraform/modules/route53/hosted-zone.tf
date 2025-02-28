# Create an A record
resource "aws_route53_record" "spring_www" {
  zone_id = var.route53_zone_id
  name    = var.spring_domain_name  # Replace with your subdomain or use "" for root domain
  type    = "A"

  alias {
    name                   = var.internal_alb_dns_name
    zone_id                = var.internal_alb_zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "angular_www" {
  zone_id = var.route53_zone_id
  name    = var.angular_domain_name  # Replace with your subdomain or use "" for root domain
  type    = "A"

  alias {
    name                   = var.public_alb_dns_name
    zone_id                = var.public_alb_zone_id
    evaluate_target_health = true
  }
}