# Create a security group for the Load Balancer
resource "aws_security_group" "public_alb_sg" {
    name_prefix = "${var.environment}-public-alb-sg"
    vpc_id      = aws_vpc.vpc.id

    ingress {
        from_port   = 80
        to_port     = 80
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]  # Allow HTTP from anywhere
    }

    ingress {
        from_port   = 443
        to_port     = 443
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]  # Allow HTTPS from anywhere
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "${var.environment}-public-alb-sg"
    }
}


resource "aws_security_group" "internal_alb_sg" {
    name_prefix = "${var.environment}-internal-alb-sg"
    vpc_id      = aws_vpc.vpc.id

    ingress {
        from_port   = 80
        to_port     = 80
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]  # Allow HTTP from anywhere
    }

    ingress {
        from_port   = 443
        to_port     = 443
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]  # Allow HTTPS from anywhere
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "${var.environment}-internal-alb-sg"
    }
}

# Create a security group for ECS service
resource "aws_security_group" "ecs_sg" {
    name_prefix = "${var.environment}-ecs-security-group"
    vpc_id      = aws_vpc.vpc.id

    ingress {
        from_port   = var.spring_container_port
        to_port     = var.spring_container_port
        protocol    = "tcp"
        security_groups = [aws_security_group.internal_alb_sg.id]
    }

    ingress {
        from_port   = var.angular_container_port
        to_port     = var.angular_container_port
        protocol    = "tcp"
        security_groups = [aws_security_group.public_alb_sg.id]
    }

    ingress {
        from_port   = 22
        to_port     = 22
        protocol    = "tcp"
        security_groups = [aws_security_group.bastion_security_group.id]
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    depends_on = [ aws_security_group.internal_alb_sg, aws_security_group.bastion_security_group ]

    tags = {
        Name = "${var.environment}-ecs-security-group"
    }
}

# Create a security group for RDS (Database)
resource "aws_security_group" "database_sg" {
    name_prefix = "${var.environment}-database-sg"
    vpc_id      = aws_vpc.vpc.id

    ingress {
        from_port       = 3306
        to_port         = 3306
        protocol        = "tcp"
        security_groups = [aws_security_group.ecs_sg.id]
    }

    ingress {
        from_port       = 3306
        to_port         = 3306
        protocol        = "tcp"
        security_groups = [aws_security_group.bastion_security_group.id]  # Fixed typo here
    }

    egress {
        from_port        = 0
        to_port          = 0
        protocol         = "-1"
        cidr_blocks      = ["0.0.0.0/0"]
        ipv6_cidr_blocks = ["::/0"]
    }

    depends_on = [ aws_security_group.ecs_sg, aws_security_group.bastion_security_group ]

    tags = {
        Name = "${var.environment}-database-sg"
    }
}

# Create a security group for the Bastion Host
resource "aws_security_group" "bastion_security_group" {  # Fixed typo here
    name = "${var.environment}-bastion-security-group"
    vpc_id = aws_vpc.vpc.id

    ingress {
        from_port   = 22
        to_port     = 22
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]  # Allow SSH access from anywhere
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "${var.environment}-bastion-security-group"
    }
}
