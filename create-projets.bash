#!/usr/bin/env bash

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=students-service \
--package-name=com.tutoringhub.studentservice \
--groupId=com.tutoringhub.studentservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
students-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=tutors-service \
--package-name=com.tutoringhub.tutorservice \
--groupId=com.tutoringhub.tutorservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
tutors-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=lessons-service \
--package-name=com.tutoringhub.lessonservice \
--groupId=com.tutoringhub.lessonservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
lessons-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=supervisorconfirmations-service \
--package-name=com.tutoringhub.supervisorconfirmationservice \
--groupId=com.tutoringhub.supervisorconfirmationservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
supervisorconfirmations-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.cardealership.apigateway \
--groupId=com.cardealership.apigateway \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
api-gateway

