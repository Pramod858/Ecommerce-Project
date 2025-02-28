# Create a DB subnet group for the RDS cluster
resource "aws_db_subnet_group" "rds_subnet" {
    name       = "${var.environment}-rds-subnet"
    subnet_ids = [var.private_subnet_3_id, var.private_subnet_4_id]

    tags = {
        Name = "${var.environment}-rds-subnet"
    }
}

# RDS Cluster
resource "aws_rds_cluster" "db_cluster" {
    cluster_identifier      = "${var.environment}-db-cluster"
    engine                  = "aurora-mysql"
    engine_version          = "8.0.mysql_aurora.3.08.0"  # Ensure this is a valid version for aurora-mysql
    port                    = 3306
    database_name           = var.db_name
    master_username         = var.db_username
    master_password         = var.db_password
    backtrack_window        = 0
    skip_final_snapshot     = true  # Be cautious in production environments
    db_subnet_group_name    = aws_db_subnet_group.rds_subnet.name
    availability_zones      = ["${var.region}a", "${var.region}b"]
    vpc_security_group_ids  = [var.db_security_group_id]

    backup_retention_period = 0

    lifecycle {
        ignore_changes = [backup_retention_period, availability_zones]
    }

    tags = {
        Name = "${var.environment}-db-cluster"
    }
}

# RDS Cluster Instance
resource "aws_rds_cluster_instance" "rds_cluster_instance" {
    identifier           = "rds-aurora-cluster-instance"
    cluster_identifier   = aws_rds_cluster.db_cluster.id
    engine               = "aurora-mysql"
    engine_version       = "8.0.mysql_aurora.3.08.0"
    instance_class       = "db.t3.medium"
    db_subnet_group_name = aws_db_subnet_group.rds_subnet.name
    depends_on           = [aws_rds_cluster.db_cluster]

    tags = {
        Name = "${var.environment}-db-cluster-instance"
    }
}
