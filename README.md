# Superchat Backend Challenge Moritz

## Run

```shell
./gradlew spotlessApply build && docker-compose up --build
```

## Api

### getConversations

GET `/api/v1/conversations`

Response:

```json
{
  "recipientId": "string",
  "senderId": "string",
  "messages": []
}
```

### createMessage

POST `/api/v1/conversations`

Request:

```json
{
  "recipientId": "string",
  "senderId": "string",
  "content": "string"
}
```

### getContacts

GET `/api/v1/contacts`

Response:

```json
[
  {
    "firstName": "string",
    "lastName": "string",
    "email": "string",
    "phoneNumber": "string"
  }
]
```

### createContact

POST `/api/v1/contacts`

Request:

```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string"
}
```

## Substitution

| Placeholder   | Replacement                                  |
|---------------|----------------------------------------------|
| {{recipient}} | First name and last name of recipient        |
| {{sender}}    | First name and last name of sender           |
| 0.1 BTC       | BTC value in USD, e.g. `0.1 BTC` -> `10 USD` |
