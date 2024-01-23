# currency-exchange-api

**REST API for currency exchange.** Provides capabilities for managing currencies, exchange rates, and calculating exchange amounts using direct, reverse, and cross rates.

## Installation

To build and run the project, use Maven and Java. Make sure you have Maven and Java installed (Tomcat 10 is recommended for deployment on the server).

```bash
git clone https://github.com/Jurdio/pet-project-05-currency-conversion.git
```
## Exchange Rates
### `GET /exchangeRates`
Retrieve the list of exchange rates. Example response:

```json
[
  {
    "id": 0,
    "baseCurrency": {
      "id": 0,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 1,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 0.99
  }
]

```
### `GET /exchangeRate/USDEUR`
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}

```
### `POST /exchangeRates`
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```
### `PATCH /exchangeRate/USDEUR`
```json
{
  "id": 0,
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 0.99
}
```
### `GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT`

```json
{
  "baseCurrency": {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Australian dollar",
    "code": "AUD",
    "sign": "A€"
  },
  "rate": 1.45,
  "amount": 10.00,
  "convertedAmount": 14.50
}

```
## Currencies
### `GET /currencies`
```json
[
  {
    "id": 0,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  }
]

```
### `GET /currency/EUR`
```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```
### `POST /currencies`

```json
{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

