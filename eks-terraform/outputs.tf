#AUTHOR: Pramod Badiger

output "vpc_id" {
    value = aws_vpc.EKS_VPC.id
}

output "public_subnet_ids" {
    value = [
        aws_subnet.public-subnet-1.id,
        aws_subnet.public-subnet-2.id
    ]
}

output "private_subnet_ids" {
    value = [
        aws_subnet.private-subnet-1.id,
        aws_subnet.private-subnet-2.id
    ]
}

output "eks_cluster_id" {
    value = aws_eks_cluster.eks_cluster.id
}

output "eks_node_group_id" {
    value = aws_eks_node_group.node_group.id
}