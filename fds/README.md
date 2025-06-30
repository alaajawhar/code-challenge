# Fraud Detection System

A standalone fraud detection microservice using Java Spring Boot and Drools for detecting potentially fraudulent transactions.

## Features

- **Transaction Amount Validation**: Flags transactions exceeding $10,000 as fraudulent (Drools-based)
- **Transaction Frequency Validation**: Flags transactions when more than 8 transactions are made with the same card within 1 hour (SQL-based)
- **Split Architecture**: Historical checks (SQL) and non-historical checks (Drools)
- **Data Persistence**: Transaction history storage for frequency analysis
- **Global Exception Handling**: Comprehensive error handling with standardized error responses

## Architecture

The fraud detection is split into two distinct phases:

### 1. Historical Checks (SQL-based)
- **Transaction Frequency**: Checks if the card has exceeded the frequency limit in the time window
- **Data Query**: Uses SQL to count transactions in the specified time interval
- **Performance**: Optimized for historical data analysis

### 2. Non-Historical Checks (Drools-based)
- **Transaction Amount**: Validates if the transaction amount exceeds the configured limit
- **Business Rules**: Uses Drools rules engine for configurable business logic
- **Extensibility**: Easy to add new rules without code changes

## Points Could Be Improved

### Performance
- **Rule Engine Optimization**: Cache compiled Drools rules to avoid recompilation on each request
- **Database Indexing**: Add indexes on card_number and transaction_timestamp for faster historical queries
- **Connection Pooling**: Optimize database connection pool for better concurrent request handling
- **Batch Processing**: Implement batch processing for bulk fraud checks to reduce individual request overhead
- **Query Optimization**: Use database views or materialized views for complex frequency calculations

### Security
- **Input Validation**: Add comprehensive validation for card number format and amount ranges
- **Data Encryption**: Encrypt card numbers in database storage and logs
- **Security**: Implement API key to secure this microservice
- **Rate Limiting**: Add rate limiting per card number to prevent abuse
- **Audit Logging**: Log all fraud check requests and results for compliance and monitoring

### Architecture
- **Event-Driven Processing**: Publish fraud detection results to message queues for downstream processing
- **Rule Versioning**: Implement versioning for fraud rules to track changes over time
- **Machine Learning Integration**: Prepare architecture for ML-based fraud detection models
- **Historical Data Archiving**: Implement data archiving strategy for old transaction history

## Getting Started

1. Start the Fraud Detection Service:
   ```bash
   cd fraud-detection-service
   mvn spring-boot:run
   ```

2. Test the API:
   ```bash
   curl -X POST http://localhost:8081/api/fraud/check \
     -H "Content-Type: application/json" \
     -d '{
       "cardNumber": "1234567890123456",
       "amount": 5000.00
     }'
   ```

## API Endpoints

### Fraud Detection Service (Port 8081)
- `POST /api/fraud/check` - Check transaction for fraud

### Request Format
```json
{
  "cardNumber": "1234567890123456",
  "amount": 5000.00
}
```

### Response Format
```json
{
  "isFraud": false,
  "reason": "Transaction passed all fraud checks"
}
```

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation failed",
  "message": "{cardNumber=Card number is required, amount=Amount must be positive}"
}
```

## Configuration

The fraud detection limits can be configured in `application.yml`:

```yaml
fraud:
  detection:
    amount-limit: 10000.00
    frequency-limit: 8
    time-interval-hours: 1
```