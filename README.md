# Card Management System (CMS)

A microservices-based financial system for managing accounts, cards, and transactions with integrated fraud detection.

## Architecture Overview

### 1. **Card Management Service (CMS)**
- **Purpose**: Core business logic for accounts, cards, and transaction processing
- **Port**: 8080
- **Key Features**:
  - Account lifecycle management
  - Card issuance and status management
  - Transaction processing with business rule validation
  - Integration with fraud detection service

### 2. **Fraud Detection Service (FDS)**
- **Purpose**: Real-time fraud detection and risk assessment
- **Port**: 8081
- **Engine**: Rule-based fraud detection using Drools
- **Key Features**:
  - Transaction amount-based risk scoring
  - Configurable fraud detection rules
  - RESTful API for fraud checks
 
## API Documentation
![CleanShot 2025-06-30 at 05 00 17@2x](https://github.com/user-attachments/assets/f627a61c-5369-4ae6-bdba-74b0e4c27e2a)


## Points Could Be Improved

### Performance
- **Caching Strategy**: Implement Redis caching for frequently accessed account and card data to reduce database queries
- **Database Optimization**: Add proper indexing on frequently queried fields (account_id, card_number, transaction_date)
- **Connection Pooling**: Optimize database connection pool settings for better resource utilization
- **Async Processing**: Move non-critical operations (logging, notifications) to async processing with message queues
- **Pagination Optimization**: Implement cursor-based pagination for large datasets instead of offset-based

### Security
- **Authentication & Authorization**: Implement token authentication to secure communication between microservices
- **Input Validation**: Add comprehensive input sanitization to prevent SQL injection and XSS attacks
- **Rate Limiting**: Implement API rate limiting to prevent abuse and DDoS attacks
- **Audit Logging**: Add comprehensive audit trails for all financial operations

### Code Quality
- **Circuit Breaker Pattern**: Implement circuit breakers for external service calls (FDS integration)
- **Retry Mechanism**: Add configurable retry logic with exponential backoff for transient failures
- **Monitoring & Observability**: Integrate application metrics with Prometheus and distributed tracing
- **Configuration Management**: Externalize configuration using Spring Cloud Config or similar
- **Error Handling**: Enhance error responses with correlation IDs for better debugging
- **API Versioning**: Implement proper API versioning strategy for backward compatibility

### Architecture
- **Event-Driven Architecture**: Use event sourcing for transaction processing to improve auditability
- **Service Mesh**: Implement service mesh (Istio) for better service-to-service communication
- **Database per Service**: Separate databases for each microservice to ensure true independence
- **CQRS Pattern**: Implement Command Query Responsibility Segregation for better read/write optimization

## Getting Started

### Prerequisites
- Docker and Docker Compose

### How To Run
```bash
    docker-compose up -d
```


## Best Practices Implemented

### **Clean Architecture**
- **Controller Layer**: HTTP requests/responses and validation
- **Service Layer**: Business logic and orchestration
- **Repository Layer**: Data access abstraction
- **Entity Layer**: Domain models with JPA annotations

### **Error Handling & Validation**
- Global exception handler with consistent error responses
- Bean validation with custom annotations
- Request/response DTOs for clean API contracts

### **Testing Strategy**
- Unit tests with Mockito for service layer
- Integration tests with MockMvc for controllers
- Comprehensive test coverage including edge cases

### **Code Quality**
- Interface segregation for better testability
- Lombok for boilerplate reduction
- Clean mapping between DTOs and entities
- Structured logging with Logback

## Production Enhancements

For a real production system, I would add:

### **Security**
- token based security to secure communication between microservices 
- API rate limiting
- Audit logging

### **Scalability**
- Service discovery (Eureka/Consul)
- Load balancing and circuit breakers
- Message queues for async processing
- Caching with Redis

### **DevOps**
- CI/CD pipelines (Jenkins/GitHub Actions)
- Monitoring with Prometheus/Grafana

### **Compliance**
- PCI DSS compliance for card data
- Data encryption at rest and in transit
