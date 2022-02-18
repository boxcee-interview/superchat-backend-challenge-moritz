# Superchat Backend Challenge Moritz

--> [Main GitHub Profile](https://github.com/boxcee) <--

## Table Of Contents

1. [Tasks](#Tasks)
2. [Run](#Run)
3. [API](#API)
4. [Substitution](#Substitution)

## Tasks

### API Service

Your service should allow potential users to use these functionalities:

1. Create contacts given their personal information (Name, E-Mail, etc)
2. List all contacts
3. Send a message to a contact
4. List all previous conversations
5. Receive messages from an external service via a webhook

### Additional:

Please support at least substituting the following:

1. Name of contact
2. Current Bitcoin Price in $

## Run

```shell
./gradlew spotlessApply build && docker-compose up --build
```

## API

### getConversations

GET `/api/v1/conversations`

Response:

```json
{
  "recipientId": "string",
  "senderId": "string",
  "messages": [
    "Message"
  ]
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

### incomingWebhook

POST `/api/v1/webhooks`

Request:

```json
{
  "recipient": "Contact",
  "sender": "Contact",
  "message": "string"
}
```

## Substitution

When creating a new message, the content may be filled with placeholders. The placeholders will be replaced when the
message is read.

| Placeholder   | Replacement                                  |
|---------------|----------------------------------------------|
| {{recipient}} | First name and last name of recipient        |
| {{sender}}    | First name and last name of sender           |
| 0.1 BTC       | BTC value in USD, e.g. `0.1 BTC` -> `10 USD` |
