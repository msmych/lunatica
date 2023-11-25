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
docker-compose up --build
```

To run locally (API and Telegram bot):
```bash
cd lunatica-be
YABEDA_BOT_TOKEN=<bot-token> docker-compose up --build
```

## Endpoints

### Create account
Request: `POST /api/accounts`
```json
{
    "email": "string",
    "pass": "string"
}
```
Response: `201`
```json
{
    "id": "uuid"
}
```

### Login
Request: `POST /api/login`
```json
{
    "email": "string",
    "pass": "string"
}
```
Response: `200` + set cookie `auth`

### Logout
Request: `POST /api/logout`
Response: `200` + Expire cookie `auth`

### Create complaint
Request: `POST /api/complaints`
```json
{
    "problemCountry": "DE",
    "problemDate": "2023-10-10",
    "type": "BANK_ACCOUNT_OPENING_REJECTED",
    "content": "string"
}
```
Response: `200`
```json
{
    "id": "uuid"
}
```

### Get complaints
Request: `GET /api/complaints?limit=20`

### Get complaint by id
Request: `GET /api/complaints/:id`

### Get messages by complaint
Request: `GET /api/messages?complaintId=uuid`
