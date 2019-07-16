ROOT_PASSWORD  ?= password
DATABASE ?= customers
DATABASE_USER ?=customer
DATABASE_PASSWORD ?=customer
APP_NAME ?=customer-service

.PHONY: help start-dep start-cust-svc stop
.DEFAULT_GOAL := help


# GENERAL

help :		## Help
	@echo ""
	@echo "*** $(NAME) Makefile help ***"
	@echo ""
	@echo "Targets list:"
	@grep -E '^[a-zA-Z_-]+ :.*?## .*$$' $(MAKEFILE_LIST) | sort -k 1,1 | awk 'BEGIN {FS = ":.*?## "}; {printf "\t\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo ""

start-dep :
			docker-compose -f ./docker-compose.yml up -d

start-cust-svc : start-dep   ## customer sevrice
				 ./gradlew customer-service:bootRun  & echo $$! > cust-svc.PID

stop : ## stop
	 docker-compose -f ./docker-compose.yml down
	 if [ -a cust-svc.PID ]; then \
         kill -TERM $$(cat cust-svc.PID) || true; \
     fi;
