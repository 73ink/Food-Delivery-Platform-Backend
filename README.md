# Food Delivery Platform Backend

## Project Overview

This project is a Spring Boot backend system for a Food Delivery Platform. The system allows customers to register, add addresses, browse restaurants, place orders, track deliveries, process payments, leave reviews, and view reports.

The project was developed as part of the TRA Java Training Program Sprint Edition. It follows a layered backend architecture and includes entities, DTOs, repositories, services, controllers, validation, global exception handling, soft delete rules, and a simple bonus front-end client.

## Main Idea

The main idea of the project is to build a backend system similar to a real food delivery application. Customers can order food from restaurants, restaurants can manage their menus, drivers can deliver orders, and the platform can generate useful reports such as revenue, top customers, and driver leaderboard.

## Technologies Used

* Java 17
* Spring Boot 3
* Spring Web
* Spring Data JPA
* MySQL
* Maven
* Lombok
* Spring Boot Starter Validation
* HTML
* CSS
* Vanilla JavaScript
* Postman
* GitHub
* ClickUp

## Project Structure

```text
com.fooddelivery
├── controllers
├── DTO
│   ├── request
│   └── response
├── Entities
├── Exceptions
├── Repositories
├── Services
└── Utils
```

## Architecture

The project follows layered architecture:

* **Controller Layer**: Receives HTTP requests and returns API responses.
* **Service Layer**: Contains the business logic of the system.
* **Repository Layer**: Communicates with the MySQL database using JPA queries.
* **Entity Layer**: Represents the database tables.
* **DTO Layer**: Controls the data sent to and returned from the API.
* **Exception Layer**: Handles errors globally using a single exception handler.
* **Utils Layer**: Contains reusable helper methods.

## Main Features

### Customer and Address Management

* Create new customers
* Get all customers
* Get customer by ID
* Get customer by email
* Add customer addresses
* Set default address
* Add loyalty points
* Deduct loyalty points
* Soft delete customer
* Soft delete address

### Restaurant and Menu Management

* Create restaurant owners
* Create restaurants
* Get all restaurants
* Get restaurant details
* Search restaurants by cuisine
* Search restaurants by name keyword
* Update delivery fee
* Toggle accepting orders
* Add menu items
* Update menu item availability
* Create combo meals
* Bulk update menu item prices

### Order Management

* Create empty order
* Add menu items to order
* Remove items from order using soft delete
* Apply discount
* Confirm order
* Update order status
* Cancel order only if it is still pending
* Calculate order totals
* Create corporate orders

### Delivery and Driver Management

* Register delivery drivers
* Get all drivers
* Get online drivers
* Update driver online status
* Update driver location
* Assign driver manually
* Auto assign first available online driver
* Mark delivery as picked up
* Mark delivery as delivered
* Get driver delivery history
* Get active delivery for driver
* Find nearby online drivers

### Payment Management

* Create payment for an order
* Complete payment
* Refund payment
* Get payment by order
* Filter payments
* View payment analytics by method

### Review Management

* Add restaurant review
* Add driver review
* Get restaurant reviews
* Get driver reviews
* Get average restaurant rating
* Get average driver rating
* Soft delete review

### Reporting and Analytics

* Restaurant revenue by date
* Completed orders count for a restaurant
* Top loyalty customers
* Driver leaderboard
* Platform daily summary
* Restaurant revenue by date range
* Driver earnings by date range
* Cancellation rate
* Busiest order hours
* Driver performance report

## Important Project Rules Applied

### DTOs

Entities are not exposed directly through the API. Request DTOs are used for incoming data, and Response DTOs are used for outgoing data.

### Manual Mapping

DTO classes include manual mapper methods such as:

```java
toEntity()
fromEntity()
applyTo()
```

No MapStruct or ModelMapper was used.

### Validation

Request DTOs use validation annotations such as:

```java
@NotBlank
@Email
@NotNull
@NotEmpty
@Min
@Max
@Pattern
@DecimalMin
```

Controllers use `@Valid` to validate request bodies before reaching the service layer.

### Global Exception Handling

The project uses a global exception handler with `@RestControllerAdvice`.

Handled exceptions include:

* `ResourceNotFoundException`
* `DuplicateResourceException`
* `InvalidOrderStateException`
* `MethodArgumentNotValidException`
* General server errors

The error response includes:

* timestamp
* status code
* error reason
* message
* path
* field errors for validation problems

### Soft Delete

The project does not permanently delete records from the database. Instead, records are soft deleted by setting:

```java
isActive = false
```

All repository queries filter active records using:

```java
isActive = true
```

This means inactive records are hidden from the API.

### Repository Queries

All repository methods are written using explicit `@Query`. Derived query methods are not used alone.

Example:

```java
@Query("SELECT c FROM Customer c WHERE c.email = :email AND c.isActive = true")
Optional<Customer> findByEmail(@Param("email") String email);
```

## Database

The project uses MySQL. The database name used during development is:

```text
fooddeliveryDB
```

Example database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fooddeliveryDB?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## How to Run the Backend

### 1. Clone the repository

```bash
git clone https://github.com/73ink/Food-Delivery-Platform-Backend.git
```

### 2. Open the project

Open the project in IntelliJ IDEA or any Java IDE.

### 3. Create MySQL database

Run this command in MySQL Workbench:

```sql
CREATE DATABASE fooddeliveryDB;
```

### 4. Update database password

Open:

```text
src/main/resources/application.properties
```

Update the MySQL username and password based on your local MySQL setup.

### 5. Run the application

Using Windows terminal:

```bash
.\mvnw.cmd spring-boot:run
```

Or using Mac/Linux/Git Bash:

```bash
./mvnw spring-boot:run
```

The backend will run on:

```text
http://localhost:8080
```

## Front-End Client

A simple bonus front-end client is included using:

```text
index.html
style.css
app.js
```

Location:

```text
src/main/resources/static
```

After running the backend, open:

```text
http://localhost:8080/index.html
```

The front-end includes:

* Restaurant Browse
* Menu and Cart
* Order Tracking
* Admin Reporting Dashboard

The front-end uses plain JavaScript `fetch()` calls to communicate with the Spring Boot REST API.

## Main API Endpoints

### Customers

```text
POST   /api/customers
GET    /api/customers
GET    /api/customers/{id}
GET    /api/customers/email/{email}
PATCH  /api/customers/{id}
PUT    /api/customers/{id}/deactivate
PUT    /api/customers/{id}/loyalty/add/{points}
PUT    /api/customers/{id}/loyalty/deduct/{points}
POST   /api/customers/{id}/addresses
GET    /api/customers/{id}/addresses
PUT    /api/customers/addresses/{addressId}/default
DELETE /api/customers/addresses/{addressId}
GET    /api/customers/{id}/orders
```

### Restaurant Owners

```text
POST   /api/owners
GET    /api/owners
GET    /api/owners/{id}
PUT    /api/owners/{id}/deactivate
```

### Restaurants

```text
POST   /api/restaurants/owner/{ownerId}
GET    /api/restaurants
GET    /api/restaurants/{id}
GET    /api/restaurants/cuisine/{cuisine}
GET    /api/restaurants/search?keyword=
GET    /api/restaurants/fee?maxFee=
PUT    /api/restaurants/{id}/toggle-orders?accepting=
PUT    /api/restaurants/{id}/fee/{newFee}
GET    /api/restaurants/{id}/menu
POST   /api/restaurants/{id}/menu
PUT    /api/restaurants/menu/{itemId}/available?status=
GET    /api/restaurants/menu/search?keyword=&minCalories=&maxCalories=
GET    /api/restaurants/{id}/combos
POST   /api/restaurants/{id}/combos
PUT    /api/restaurants/{id}/bulk-price-increase?percentage=
```

### Orders

```text
POST   /api/orders/customer/{customerId}/restaurant/{restaurantId}
POST   /api/orders/{id}/items
DELETE /api/orders/{id}/items/{itemId}
PUT    /api/orders/{id}/discount/{amount}
PUT    /api/orders/{id}/confirm
PUT    /api/orders/{id}/status/{status}
PUT    /api/orders/{id}/cancel
GET    /api/orders/{id}
GET    /api/orders/restaurant/{restaurantId}?status=
GET    /api/orders/customer/{customerId}
POST   /api/orders/corporate
```

### Drivers

```text
POST /api/drivers
GET  /api/drivers
GET  /api/drivers/online
PUT  /api/drivers/{id}/status?isOnline=
PUT  /api/drivers/{id}/location?lat=&lng=
GET  /api/drivers/{id}/deliveries
GET  /api/drivers/{id}/deliveries/active
```

### Deliveries

```text
POST /api/deliveries/order/{orderId}/assign-manual/{driverId}
POST /api/deliveries/order/{orderId}/assign-auto
GET  /api/deliveries/{id}
PUT  /api/deliveries/{id}/pickup
PUT  /api/deliveries/{id}/complete
GET  /api/deliveries/status/{status}
GET  /api/deliveries/drivers/nearby?lat=&lng=&radiusKm=
GET  /api/deliveries/drivers/{driverId}/performance
```

### Payments

```text
POST /api/payments/order/{orderId}?method=
PUT  /api/payments/{paymentId}/complete
PUT  /api/payments/{paymentId}/refund
GET  /api/payments/order/{orderId}
GET  /api/payments?method=&status=&from=&to=&page=&size=
GET  /api/payments/analytics/by-method
```

### Reviews

```text
POST   /api/reviews/restaurant/{restaurantId}/customer/{customerId}
POST   /api/reviews/driver/{driverId}/customer/{customerId}
GET    /api/reviews/restaurant/{restaurantId}
GET    /api/reviews/driver/{driverId}
GET    /api/reviews/restaurant/{restaurantId}/average
GET    /api/reviews/driver/{driverId}/average
DELETE /api/reviews/{reviewId}
```

### Reports

```text
GET /api/reports/revenue/restaurant/{restaurantId}?date=
GET /api/reports/orders/count/restaurant/{restaurantId}
GET /api/reports/customers/top-loyalty
GET /api/reports/drivers/leaderboard
GET /api/reports/platform/daily-summary?date=
GET /api/reports/revenue/restaurant/{restaurantId}?from=&to=
GET /api/reports/drivers/{driverId}/earnings?from=&to=
GET /api/reports/orders/cancellation-rate?from=&to=
GET /api/reports/platform/busiest-hours
```

## Example Testing Flow

A recommended testing order in Postman is:

```text
1. Create restaurant owner
2. Create restaurant
3. Add menu items
4. Create customer
5. Add customer address
6. Create order
7. Add items to order
8. Confirm order
9. Register driver
10. Assign delivery
11. Mark delivery picked up
12. Mark delivery delivered
13. Create payment
14. Complete payment
15. Add reviews
16. Test reports
17. Test validation errors
18. Test duplicate errors
19. Test soft delete
```

## GitHub Repository

```text
https://github.com/73ink/Food-Delivery-Platform-Backend
```

## Project Management

The project was planned using ClickUp as a Sprint 1 board. The tasks were divided by epics such as:

* Platform Foundations
* Customer and Address Management
* Restaurant and Menu Management
* Order Management
* Delivery and Driver Management
* Payment and Review
* Reporting and Analytics
* Front-End Bonus

Story points were added in the task descriptions because sprint points were not enabled in the ClickUp list.

## Final Deliverables

The final submission includes:

* Spring Boot backend source code
* MySQL database screenshots
* Postman collection for API testing
* ClickUp board screenshots or shared link
* Bonus front-end files
* GitHub repository link

## Author

Developed as part of the TRA Java Training Program Sprint Edition.
