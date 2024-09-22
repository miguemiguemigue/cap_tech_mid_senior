# Price API

## Description

* The Price API allows querying the price of a product for a specific brand and date.
* Prices are stored in an H2 database
* Several prices could apply to a date, product and brand
    * Price API will find the price with top priority
        * Priority logic is implemented in the Domain Price model


## Main features
* Spring boot 3
* Java 17
* JPA/Hibernate and H2
* OpenAPI yml integration to define API endpoint
* Hexagonal architecture
* Tests
    * Unit tests
    * Persistence and integration tests using H2 database
      * Integration tests (PricesApiIntegrationTest) contains the requested Test 1-5, and more.
* Flyway for evolution control
  * Script that populate database with the prices of the documentation
* Postman collection ready to launch
  * Contains the requested tests in the documentation

## Requirements

- JDK 17
- Maven 3.8.X

## Steps to test it
### 1. Clone the Repository

First, clone the repository using the following command:

```bash
git clone https://github.com/miguemiguemigue/cap_tech_mid_senior.git
cd cap_tech_mid_senior/prices-api
```

### 2. Compile the project

Make sure you are in the project directory and run the following command to compile it:

```bash
mvn clean install
```

### 3. Run tests

To run the tests for the project, use the following command:

```bash
mvn test
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

### 5. Flyway populates the database with the following prices


| ID | PRODUCT_ID | BRAND_ID | PRICE_LIST_ID | START_DATE             | END_DATE               | PRIORITY | PRICE  | CURRENCY |
|----|------------|----------|----------------|-------------------------|------------------------|----------|--------|----------|
| 1  | 35455      | 1        | 1              | 2020-06-14T00:00:00     | 2020-12-31T23:59:59    | 0        | 35.50  | EUR      |
| 2  | 35455      | 1        | 2              | 2020-06-14T15:00:00     | 2020-06-14T18:30:00    | 1        | 25.45  | EUR      |
| 3  | 35455      | 1        | 3              | 2020-06-15T00:00:00     | 2020-06-15T11:00:00    | 1        | 30.50  | EUR      |
| 4  | 35455      | 1        | 4              | 2020-06-15T16:00:00     | 2020-12-31T23:59:59    | 1        | 38.95  | EUR      |


### 6. Postman collection test

Integration and persistence tests are already providing confidence about the API behaviour, but you can import
a postman collection that contains tests, and reuse it to call the API the way you need.

Import /postman/Price API.postman_collection.json to Postman.

Run collection to pass tests. The collection contains the requested Test 1-5.

Create new request to test whatever you need.

## Endpoints

### `GET /prices`

**Description**: Get price details for a product and brand on a specific date.

**Parameters**:
- `date` (required): Date and time (`date-time`). Example: "2018-06-16T00:00:00"
- `product_id` (required): Product identifier (integer).
- `brand_id` (required): Brand identifier (integer).

**Responses**:
- `200 OK`: Price details.
- `400 Bad Request`: Invalid request.
- `404 Not Found`: No price information found.
