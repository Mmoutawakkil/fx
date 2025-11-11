# ===============================
# VARIABLES
# ===============================
DC=docker compose
APP_SERVICE=fx-importer-app
DB_SERVICE=fx_postgres
VOLUME=postgres_data

# ===============================
# BUILD & RUN
# ===============================
up: build
	@$(DC) up -d

build:
	@$(DC) build

# ===============================
# STOP & CLEAN
# ===============================
down:
	@$(DC) down

down-volumes:
	@$(DC) down -v

clean: down-volumes
	@echo "All containers and volumes removed."

# ===============================
# LOGS
# ===============================
logs:
	@$(DC) logs -f

logs-app:make
	@$(DC) logs -f $(APP_SERVICE)

logs-db:
	@$(DC) logs -f $(DB_SERVICE)

# ===============================
# SHELL
# ===============================
shell-app:
	@$(DC) exec $(APP_SERVICE) sh

shell-db:
	@$(DC) exec $(DB_SERVICE) psql -U user -d deals_db

# ===============================
# RESTART
# ===============================
restart: down up