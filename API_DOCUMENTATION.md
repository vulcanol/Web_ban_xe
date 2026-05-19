# Web Bán Xe - Car Selling Platform API

## Overview

RESTful API for a Vietnamese car selling platform built with Quarkus, Hibernate, and MySQL.

## API Endpoints

### Base URL

```
http://localhost:8080/api
```

### 1. Users Endpoints

#### Register User

```
POST /api/users/register
Content-Type: application/json

{
  "fullName": "Nguyễn Văn An",
  "email": "an.nguyen@email.com",
  "password": "password123",
  "phoneNumber": "0901234567"
}
```

#### Login

```
POST /api/users/login
Content-Type: application/json

{
  "email": "an.nguyen@email.com",
  "password": "password123"
}
```

#### Get User by ID

```
GET /api/users/{id}
```

#### Get All Users

```
GET /api/users
```

#### Update User

```
PUT /api/users/{id}
Content-Type: application/json

{
  "fullName": "New Name",
  "phoneNumber": "0912345678",
  "address": "Hà Nội",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

### 2. Brands Endpoints

#### Get All Brands

```
GET /api/brands
```

#### Get Active Brands

```
GET /api/brands/active
```

#### Create Brand

```
POST /api/brands
Content-Type: application/json

{
  "name": "Toyota",
  "country": "Nhật Bản",
  "logoUrl": "https://example.com/toyota-logo.jpg"
}
```

### 3. Categories Endpoints

#### Get All Categories

```
GET /api/categories
```

#### Create Category

```
POST /api/categories
Content-Type: application/json

{
  "name": "Sedan",
  "description": "Xe con 4 cửa",
  "iconUrl": "https://example.com/sedan-icon.jpg"
}
```

### 4. Cars Endpoints

#### Get All Cars

```
GET /api/cars
```

#### Get Cars by Brand

```
GET /api/cars/brand/{brandId}
```

#### Get Cars by Category

```
GET /api/cars/category/{categoryId}
```

#### Create Car

```
POST /api/cars
Content-Type: application/json

{
  "brandId": 1,
  "categoryId": 1,
  "name": "Toyota Camry 2.5Q",
  "yearManufactured": 2022,
  "fuelType": "XANG",
  "transmission": "SO_TU_DONG",
  "seats": 5,
  "engine": "2AR-FE 2.5L",
  "horsePower": 181,
  "displacement": 2.5,
  "design": "Sedan"
}
```

### 5. Listings Endpoints

#### Get Active Listings

```
GET /api/listings
```

#### Get Listings by User

```
GET /api/listings/user/{userId}
```

#### Get Listings by Province

```
GET /api/listings/province/{province}
```

#### Get Listings by Price Range

```
GET /api/listings/price?minPrice=500000000&maxPrice=1000000000
```

#### Create Listing

```
POST /api/listings
Content-Type: application/json

{
  "userId": 1,
  "carId": 1,
  "title": "Bán Toyota Camry 2.5Q 2022 - Xe gia đình ít đi",
  "price": 1050000000,
  "mileage": 18000,
  "condition": "CU",
  "color": "Trắng ngọc trai",
  "licensePlate": "29A-12345",
  "description": "Xe cá nhân, đầy đủ đồ chơi, bảo dưỡng đúng hạn",
  "province": "Hà Nội",
  "district": "Ba Đình"
}
```

#### Increment View Count

```
POST /api/listings/{id}/view
```

### 6. Orders Endpoints

#### Get Orders by Buyer

```
GET /api/orders/buyer/{buyerId}
```

#### Create Order

```
POST /api/orders
Content-Type: application/json

{
  "buyerId": 2,
  "listingId": 1,
  "transactionPrice": 1000000000,
  "notes": "Interested in this car"
}
```

#### Update Order Status

```
PUT /api/orders/{id}/status
Content-Type: application/json

{
  "status": "DANG_XU_LY"
}
```

## Database Setup

### 1. Create MySQL Database

```sql
mysql -u root -p < database/schema.sql
```

### 2. Verify Connection

Update `application.properties` with your MySQL credentials:

```properties
quarkus.datasource.username=root
quarkus.datasource.password=your_password
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/web_ban_xe
```

## Running the Application

### Development Mode

```bash
./mvnw quarkus:dev
```

### Build and Run

```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Build Native Image

```bash
./mvnw clean package -Dnative
```

## Project Structure

```
web_ban_xe/
├── src/main/java/org/acme/
│   ├── domain/          # JPA Entity classes
│   ├── repository/      # Data access layer
│   ├── service/         # Business logic
│   ├── resource/        # REST API endpoints
│   └── dto/             # Data Transfer Objects
├── database/            # Database scripts
│   ├── schema.sql       # Database schema and sample data
│   └── migrations/      # Migration scripts
└── docs/                # Documentation
```

## Key Features

- **User Management**: Registration, login, profile updates
- **Car Catalog**: Browse cars by brand, category, fuel type, transmission
- **Listings**: Create, search, view car listings
- **Orders**: Create and manage car purchase orders
- **Reviews**: Rate sellers and comment on transactions
- **Favorites**: Save favorite listings
- **Payments**: Record payment transactions

## Enums

### UserRole

- KHACH_HANG (Customer)
- NGUOI_BAN (Seller)
- ADMIN (Administrator)

### FuelType

- XANG (Gasoline)
- DAU (Diesel)
- DIEN (Electric)
- HYBRID
- KHAC (Other)

### TransmissionType

- SO_TU_DONG (Automatic)
- SO_SAN (Manual)
- CVT
- BAN_TU_DONG (Semi-Automatic)

### ListingStatus

- CHO_DUYET (Pending Approval)
- DANG_DANG (Active)
- DA_BAN (Sold)
- HET_HAN (Expired)
- BI_AN (Hidden)

### OrderStatus

- CHO_XAC_NHAN (Awaiting Confirmation)
- DANG_XU_LY (Processing)
- HOAN_THANH (Completed)
- HUY_BO (Cancelled)

### PaymentStatus

- CHO_XU_LY (Processing)
- THANH_CONG (Success)
- THAT_BAI (Failed)
- HOAN_TIEN (Refunded)

## Technologies Used

- **Framework**: Quarkus 3.35.3
- **Language**: Java 25
- **ORM**: Hibernate
- **Database**: MySQL 8.0
- **REST**: Quarkus REST
- **JSON**: Jackson
- **Security**: BCrypt
- **Build**: Maven
