# Flash Sale Service

## 1.Overview

This project is a Flash Sale Service built with Spring Boot.

It supports:
- User authentication (email/phone with OTP verification)
- Flash sale with time-based campaigns
- Concurrency-safe purchase handling (no overselling)

---

## 2.Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JMeter (for load testing)
- Docker (optional)

---

## 3.How to Run

-Clone the repository

-Start PostgreSQL (locally or via Docker)

-Run the application:

```
./mvnw spring-boot:run
```

Application runs at:
```
http://localhost:8080
```

---

## 4.Main APIs

POST /auth
- Register or login using email or phone

POST /auth/verify
- Verify OTP

GET /flash-sale/current
- Get current flash sale items

POST /flash-sale/buy
- Purchase flash sale item

---

## 5.Key Design Decisions

### Concurrency Handling

- Use atomic database update:
  ```
  UPDATE ... WHERE stock > 0
  ```
  → Prevents overselling under concurrent requests

- Use unique constraint (user_id + date)  
  → Ensures each user can only purchase once per day

- Verified with JMeter concurrent testing

---

### Authentication

- Supports email or phone login
- OTP verification for user validation
- Password stored using BCrypt hashing

---

## 6.Assumptions

- Users already have sufficient balance
- No real payment integration
- OTP delivery is mocked (logged to console)

---

## 7.Limitations

- Inventory is updated synchronously in database
- No OTP rate limiting implemented
- No idempotency key for duplicate requests
- No caching layer (Redis)

---

## 8.Future Improvements

- Add Redis caching to reduce database load
- Use message queue (Kafka/RabbitMQ) for async processing
- Implement idempotency key to prevent duplicate requests
- Add rate limiting for OTP
- Introduce distributed locking for multi-instance scaling

---

## 9.Testing

- Load testing performed using JMeter

Verified:
- No overselling
- Stock never goes negative
- Number of successful orders equals available stock

---

## 10.Notes

This implementation prioritizes correctness and simplicity.

The system is designed to be easily extended for high scalability
using caching and event-driven architecture.