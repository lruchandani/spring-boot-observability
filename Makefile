ROOT_PASSWORD  ?= password
DATABASE ?= customers
DATABASE_USER ?=customer
DATABASE_PASSWORD ?=customer
APP_NAME ?=customer-service

.PHONY: help start-dep start-cust-svc start-order-svc start-product-svc stop-cust-svc \
stop-order-svc stop-product-svc start-all stop-all
.DEFAULT_GOAL := help


# GENERAL

help :		## Help
	@echo ""
	@echo "*** $(NAME) Makefile help ***"
	@echo ""
	@echo "Targets list:"
	@grep -E '^[a-zA-Z_-]+ :.*?## .*$$' $(MAKEFILE_LIST) | sort -k 1,1 | awk 'BEGIN {FS = ":.*?## "}; {printf "\t\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo ""

start-dep:
			docker-compose -f ./docker-compose.yml up -d

start-cust-svc: start-dep   ## start customer sevrice
				 ./gradlew customer-service:bootRun  & echo $$! > cust-svc.PID

start-order-svc: start-dep   ## start order sevrice
				 ./gradlew order-service:bootRun  & echo $$! > order-svc.PID

start-product-svc: start-dep   ## start product sevrice
				 ./gradlew product-service:bootRun  & echo $$! > product-svc.PID

stop-cust-svc: ## stop customer svc
	if [ -a cust-svc.PID ]; then \
             kill -TERM $$(cat cust-svc.PID) || true; \
    fi;

stop-order-svc: ## stop order svc
	if [ -a order-svc.PID ]; then \
             kill -TERM $$(cat order-svc.PID) || true; \
    fi;

stop-product-svc: ## stop product svc
	if [ -a product-svc.PID ]; then \
             kill -TERM $$(cat product-svc.PID) || true; \
    fi;

start-all: start-dep start-cust-svc start-order-svc start-product-svc ## start all
stop-all: stop-cust-svc stop-order-svc stop-product-svc  ## stop all
	 docker-compose -f ./docker-compose.yml down
