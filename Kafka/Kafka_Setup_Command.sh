#!/usr/bin/env bash
docker build -t hishailesh77/kafka-zookeeper:v1.1 . && docker build -t hishailesh77/kafka-zookeeper:latest . && docker push hishailesh77/kafka-zookeeper:v1.1 && docker push hishailesh77/kafka-zookeeper:latest && kubectl apply -f kafka-zookeeper-setup.yaml
