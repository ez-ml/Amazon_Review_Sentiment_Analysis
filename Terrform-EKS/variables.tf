variable "region" {
  default     = "us-west-2"
  description = "AWS region"
}

provider "aws" {
  region = "us-west-2"
}

data "aws_availability_zones" "available" {}

locals {
  cluster_name = "wss-reporting-${random_string.suffix.result}"
}

resource "random_string" "suffix" {
  length  = 8
  special = false
}