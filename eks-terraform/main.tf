#AUTHOR: Pramod Badiger

resource "aws_vpc" "EKS_VPC" {
    cidr_block           = var.vpc_cidr
    instance_tenancy     = "default"
    enable_dns_hostnames = true
    enable_dns_support   = true

    tags = {
        Name = "EKS-VPC"
    }
}

resource "aws_subnet" "public-subnet-1" {
    vpc_id                  = aws_vpc.EKS_VPC.id
    cidr_block              = var.public_sb1_cidr
    availability_zone       = "${var.region}a"
    map_public_ip_on_launch = true
    
    tags = {
        Name = "public-subnet-1"
        "kubernetes.io/role/elb" = "1"
        "kubernetes.io/cluster/demo-eks-cluster" = "shared"
    }
}

resource "aws_subnet" "public-subnet-2" {
    vpc_id                  = aws_vpc.EKS_VPC.id
    cidr_block              = var.public_sb2_cidr
    availability_zone       = "${var.region}b"
    map_public_ip_on_launch = true
    
    tags = {
        Name = "public-subnet-2"
        "kubernetes.io/role/elb" = "1"
        "kubernetes.io/cluster/demo-eks-cluster" = "shared"
    }
}

resource "aws_subnet" "private-subnet-1" {
    vpc_id            = aws_vpc.EKS_VPC.id
    cidr_block        = var.private_sb1_cidr
    availability_zone = "${var.region}a"
    
    tags = {
        Name = "private-subnet-1"
        "kubernetes.io/role/internal-elb" = "1"
        "kubernetes.io/cluster/demo-eks-cluster" = "shared"
    }
}

resource "aws_subnet" "private-subnet-2" {
    vpc_id            = aws_vpc.EKS_VPC.id
    cidr_block        = var.private_sb2_cidr
    availability_zone = "${var.region}b"
    
    tags = {
        Name = "private-subnet-2"
        "kubernetes.io/role/internal-elb" = "1"
        "kubernetes.io/cluster/demo-eks-cluster" = "shared"
    }
}

resource "aws_internet_gateway" "igw" {
    vpc_id = aws_vpc.EKS_VPC.id
    
    tags = {
        Name = "igw"
    }
} 

resource "aws_route_table" "public_route_table" {
    vpc_id = aws_vpc.EKS_VPC.id
    
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.igw.id
    }
    
    tags = {
        Name = "public-route-table"
    }
}

resource "aws_route_table_association" "rta_to_public1" {
    subnet_id      = aws_subnet.public-subnet-1.id
    route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "rta_to_public2" {
    subnet_id      = aws_subnet.public-subnet-2.id
    route_table_id = aws_route_table.public_route_table.id
}

resource "aws_eip" "eip" {
    vpc = true
    
    tags = {
        Name = "eip"
    }
}  

resource "aws_nat_gateway" "nat_gw" {
    allocation_id = aws_eip.eip.id
    subnet_id     = aws_subnet.public-subnet-1.id
    
    tags = {
        Name = "nat-gw"
    }
}

resource "aws_route_table" "private_route_table" {
    vpc_id = aws_vpc.EKS_VPC.id
    
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_nat_gateway.nat_gw.id
    }

    tags = {
        Name = "private-route-table"
    }
}

resource "aws_route_table_association" "rta_to_private1" {
    subnet_id      = aws_subnet.private-subnet-1.id
    route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "rta_to_private2" {
    subnet_id      = aws_subnet.private-subnet-2.id
    route_table_id = aws_route_table.private_route_table.id
}

resource "aws_security_group" "eks_cluster_sg" {
    vpc_id = aws_vpc.EKS_VPC.id
    name   = "eks-cluster-sg"

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "eks-cluster-sg"
    }
}

resource "aws_security_group" "eks_node_sg" {
    vpc_id = aws_vpc.EKS_VPC.id
    name   = "eks-node-sg"

    ingress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "eks-node-sg"
    }
}

resource "aws_iam_role" "eks_cluster_role" {
    name = "eks_cluster_role"

    assume_role_policy = jsonencode({
        Version = "2012-10-17",
        Statement = [
        {
            Action = "sts:AssumeRole",
            Effect = "Allow",
            Principal = {
            Service = "eks.amazonaws.com"
            }
        }
        ]
    })

    tags = {
        Name = "eks_cluster_role"
    }
}

resource "aws_iam_role_policy_attachment" "eks_cluster_role_policy_attachment" {
    role       = aws_iam_role.eks_cluster_role.name
    policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}

resource "aws_iam_role" "eks_node_role" {
    name = "eks_node_role"

    assume_role_policy = jsonencode({
        Version = "2012-10-17",
        Statement = [
        {
            Action = "sts:AssumeRole",
            Effect = "Allow",
            Principal = {
            Service = "ec2.amazonaws.com"
            }
        }
        ]
    })

    tags = {
        Name = "eks_node_role"
    }
}

resource "aws_iam_role_policy_attachment" "eks_node_role_policy_attachment" {
    role       = aws_iam_role.eks_node_role.name
    policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
}

resource "aws_iam_role_policy_attachment" "eks_cni_policy_attachment" {
    role       = aws_iam_role.eks_node_role.name
    policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
}

resource "aws_iam_role_policy_attachment" "eks_ec2_policy_attachment" {
    role       = aws_iam_role.eks_node_role.name
    policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

resource "aws_eks_cluster" "eks_cluster" {
    name     = "demo-eks-cluster"
    role_arn = aws_iam_role.eks_cluster_role.arn

    vpc_config {
        subnet_ids         = [aws_subnet.public-subnet-1.id, aws_subnet.public-subnet-2.id, aws_subnet.private-subnet-1.id, aws_subnet.private-subnet-2.id]
        security_group_ids = [aws_security_group.eks_cluster_sg.id]
    }

    tags = {
        Name = "demo-eks-cluster"
    }
}

resource "aws_eks_node_group" "node_group" {
    cluster_name    = aws_eks_cluster.eks_cluster.name
    node_group_name = "demo-eks-node-group"
    node_role_arn   = aws_iam_role.eks_node_role.arn
    subnet_ids      = [aws_subnet.public-subnet-1.id, aws_subnet.public-subnet-2.id]

    scaling_config {
        desired_size = 1
        max_size     = 3
        min_size     = 1
    }

    remote_access {
        ec2_ssh_key               = "${var.key_pair}"
        source_security_group_ids = [aws_security_group.eks_node_sg.id]
    }

    instance_types = ["t2.medium"]

    tags = {
        Name = "demo-eks-node-group"
    }
}
