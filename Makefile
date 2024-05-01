.DEFAULT_GOAL := help
.PHONY: help run destroy format test

help:
	@echo Available targets:
	@echo   build   : Build the application
	@echo   run     : Run the application
	@echo   restart : Stop and rerun the application
	@echo   format  : Format the source code of the application
	@echo   compile : Compile the source code
	@echo   test    : Run all unit and integration tests
	@echo   install : Install the application
	@echo   site    : Generate project site documentation
	@echo Usage: make 'target' e.g make build
# Run containers
# todo: change to "docker compose up" for final delivery
run:
	make destroy
	make build
	docker compose up

# Destroy containers for database and app - useful for freeing up ports
destroy:
	docker compose down

# Build the server
build:
	docker compose build

# Rebuild server and start it: IMPORTANT TO RESTART AFTER CHANGING SOURCE CODE
restart:
	make destroy
	make build
	make run

format:
	mvn spotless:apply

test:
	mvn clean test

compile:
	mvn clean compile

install:
	mvn clean install

site:
	mvn clean site