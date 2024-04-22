.DEFAULT_GOAL := help
.PHONY: help run destroy format test

help:
	@echo he needs some milk

# Run containers
run:
	docker compose up

# Destroy containers for database and app - useful for freeing up ports
destroy:
	docker compose down

# Build the server
build:
	docker compose build

# Rebuild server and start it: IMPORTANT TO RESTART AFTER CHANGING SOURCE CODE
restart:
	make build
	make run

format:
	mvn spotless:apply

test:
	mvn clean test