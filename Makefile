FRONTEND_DIR=lunatica-fe
BACKEND_DIR=lunatica-be
DIST_DIR=$(FRONTEND_DIR)/dist
PUBLIC_DIR=$(BACKEND_DIR)/app/src/main/resources/public

.PHONY: build build-frontend copy-frontend run-api run-bot clean-docker help

# default
all: help

# full build
build: build-frontend copy-frontend

# building the frontend
build-frontend:
	cd $(FRONTEND_DIR) && yarn install && yarn build

# copying frontend assets to the backend pub dir
copy-frontend:
	mkdir -p $(PUBLIC_DIR)
	cp -R $(DIST_DIR)/* $(PUBLIC_DIR)

# API only
run-api:
	cd $(BACKEND_DIR) && docker-compose up --build -d

# running API + Telegram bot
run-bot:
	cd $(BACKEND_DIR) && YABEDA_BOT_TOKEN=<bot-token> docker-compose up --build -d

clean-docker:
	cd $(BACKEND_DIR) && docker-compose down

help:
	@echo "Доступные цели:"
	@echo "  build-frontend   - Сборка фронта."
	@echo "  copy-frontend    - Копирование собранных файлов фронта в бэк."
	@echo "  build            - Полная сборка проекта (фронтенд + бэкенд)."
	@echo "  run-api          - Запуск только API."
	@echo "  run-bot          - Запуск API вместе с Telegram ботом."
	@echo "  help             - Показать это сообщение с подсказками."
	@echo "  clean-docker     - Остановка и удаление докеров."
