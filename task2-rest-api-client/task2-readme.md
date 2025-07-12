#### Task 2 README (task2-rest-api-client/README.md)

```markdown
# Task 2: REST API Client

## Objective
Write a Java application that consumes public REST APIs and displays data in structured format.

## Features
- HTTP GET requests
- JSON response parsing
- Structured data display
- Interactive menu system
- Error handling for network operations

## APIs Used
- **JSONPlaceholder**: https://jsonplaceholder.typicode.com/
  - Posts endpoint
  - Users endpoint
  - Todos endpoint

## Dependencies
- `org.json` library for JSON parsing

## How to Run

### Using Maven
```bash
mvn compile exec:java -Dexec.mainClass="RestApiClient"