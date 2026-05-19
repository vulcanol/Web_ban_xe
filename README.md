# Web Bán Xe - Car Selling Platform

A modern Vietnamese car selling platform built with Quarkus, providing a RESTful API for managing car listings, user accounts, orders, and transactions.

## Features

✅ **User Management** - Register, login, manage user profiles  
✅ **Car Catalog** - Browse cars by brand, category, fuel type  
✅ **Listings** - Create and search car listings  
✅ **Orders** - Manage purchase orders  
✅ **Reviews** - Rate sellers and transactions  
✅ **Favorites** - Save favorite listings  
✅ **Payments** - Handle payment transactions

## Prerequisites

- Java 25+
- Maven 3.8.1+
- MySQL 8.0+
- Git

## Quick Start

### 1. Setup Database

```bash
# Create database and tables
mysql -u root -p < database/schema.sql
```

### 2. Configure Database Connection

Update `src/main/resources/application.properties`:

```properties
quarkus.datasource.username=root
quarkus.datasource.password=your_password
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/web_ban_xe
```

### 3. Run in Development Mode

```shell script
./mvnw quarkus:dev
```

The API will be available at: `http://localhost:8080/api`  
Dev UI: `http://localhost:8080/q/dev/`

## API Endpoints

### Users

- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Authenticate user
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile

### Listings

- `GET /api/listings` - Get active listings
- `GET /api/listings/user/{userId}` - Get user's listings
- `GET /api/listings/province/{province}` - Search by province
- `GET /api/listings/price?minPrice=X&maxPrice=Y` - Search by price
- `POST /api/listings` - Create new listing

### Cars

- `GET /api/cars` - Get all cars
- `GET /api/cars/brand/{brandId}` - Get cars by brand
- `GET /api/cars/category/{categoryId}` - Get cars by category
- `POST /api/cars` - Create new car

### Brands & Categories

- `GET /api/brands` - List all brands
- `GET /api/categories` - List all categories

### Orders

- `GET /api/orders/buyer/{buyerId}` - Get buyer's orders
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}/status` - Update order status

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for complete API reference.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/web_ban_xe-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
