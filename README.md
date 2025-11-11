## Tech Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL 18+
- Docker & Docker Compose
- Maven

## Prerequisites

- Docker & Docker Compose installed
- Make installed for "make" commands

## Setup

1. Clone the repository:

```bash
git clone https://github.com/yourusername/fx-importer.git
cd fx-importer
```

2. Start the application
   1. Using make
   ```bash
    make up
    ```
   2. Using compose
   ```bash
    docker compose up -d
    ```

3. Stop the application
    1. Using make
   ```bash
    make down
    ```
    2. Using compose
   ```bash
    docker compose down -v
    ```
   
## API Usage
**POST** `/api/deals`

**Body:**
```json
{
  "dealUniqueId": "1",
  "fromCurrency": "USD",
  "toCurrency": "MAD",
  "dealTimestamp": "2025-11-11T20:00:00+01:00",
  "amount": 100.00
}
```

**POST** `/api/deals/bulk`

**Body:**
```json
[
  {
    "dealUniqueId": "2",
    "fromCurrency": "USD",
    "toCurrency": "EUR",
    "dealTimestamp": "2025-11-11T20:00:00+01:00",
    "amount": 10021.00
  },
  {
    "dealUniqueId": "3",
    "fromCurrency": "EUR",
    "toCurrency": "MAD",
    "dealTimestamp": "2025-11-11T20:00:00+01:00",
    "amount": 5400.00
  },{
    "dealUniqueId": "4",
    "fromCurrency": "MAD",
    "toCurrency": "EUR",
    "dealTimestamp": "2025-11-11T20:00:00+01:00",
    "amount": 8954.13
  }
]
```

## Testing
```bash
    mvn test
```

## Logging
```bash
    make logs-app
```