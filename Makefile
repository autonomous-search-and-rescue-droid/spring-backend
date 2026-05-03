SHELL := /bin/bash

SPRING_DOCKER_COMPOSE_ENABLED ?= false
SPRING_DATA_MONGODB_URI ?= mongodb://root:secret@localhost:27017/search_droid_db?authSource=admin
JAVA_VERSION ?= 17
JAVAC ?= /usr/bin/javac

.PHONY: backend mongodb-up

mongodb-up:
	docker-compose up -d mongodb

backend: mongodb-up
	env SPRING_DOCKER_COMPOSE_ENABLED=$(SPRING_DOCKER_COMPOSE_ENABLED) SPRING_DATA_MONGODB_URI='$(SPRING_DATA_MONGODB_URI)' ./mvnw -Dmaven.test.skip=true -Djava.version=$(JAVA_VERSION) -Dmaven.compiler.release=$(JAVA_VERSION) -Dmaven.compiler.fork=true -Dmaven.compiler.executable=$(JAVAC) spring-boot:run
