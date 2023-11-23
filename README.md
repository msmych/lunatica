# Peredelano November hackathon app

To build locally:
```bash
cd lunatica-fe
yarn install
yarn build
cd ..
mkdir lunatica-be/app/src/main/resources/public
cp -R lunatica-fe/dist/* lunatica-be/app/src/main/resources/public
```

To run locally (just API):
```bash
cd lunatica-be
docker-compose up
```

To run locally (API and Telegram bot):
```bash
cd lunatica-be
YABEDA_BOT_TOKEN=<bot-token> docker-compose up
```

