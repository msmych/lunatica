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

### Create complaint
Request: `POST /api/complaints`
```json
{
    "problemCountry": "DE",
    "problemDate": "2023-10-10",
    "type": "BANK",
    "content": "string"
}
```

Response: `201`
```json
{
    "id": "uuid"
}
```

### Get complaint
Request: `GET /api/complaints/:id`
Response: `200`
```json
{
    "id": "uuid",
    "accountId": "uuid",
    "state": "NEW",
    "problemCountry": "DE",
    "problemDate": "2023-10-10",
    "type": "BANK",
    "createdAt": "2023-10-10T12:13:14.567Z",
    "updatedAt": "2023-10-10T12:13:14.567Z"
}
```

### Update complaint
Request: `PATCH /api/complaints/:id`
```json
{
    "state": "RESOLVED"
}
```
Response: `204`

### Get messages by complaint
Request: `GET /api/messages?complaintId=uuid`
Response: `200`
```json
[
    {
        "id": "uuid",
        "complaintId": "uuid",
        "content": "string",
        "createdAt": "2023-10-10T12:13:14.567Z",
        "updatedAt": "2023-10-10T12:13:14.567Z"
    }
]
```

