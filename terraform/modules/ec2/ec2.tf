data "aws_ami" "latest_ubuntu" {
    most_recent = true
    owners      = ["amazon"] # Canonical
    filter {
        name   = "name"
        values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
    }

    filter {
        name   = "virtualization-type"
        values = ["hvm"]
    }

    filter {
        name   = "root-device-type"
        values = ["ebs"]
    }
}

resource "aws_instance" "bastion_instance" {
    ami                    = data.aws_ami.latest_ubuntu.id
    instance_type          = "t2.small"
    key_name               = var.key_name
    vpc_security_group_ids = [var.bastion_security_group_id]
    subnet_id              = var.public_subnet_1_id

    tags = {
        Name = "${var.environment}-bastion-instance"
    }
}