# Web Bán Xe - Deployment & Setup Guide

## Installation Requirements

### 1. MySQL Database Setup

**Install MySQL Server**

- Download from: https://dev.mysql.com/downloads/mysql/
- Windows: Use MySQL Installer
- Linux: `sudo apt-get install mysql-server`
- macOS: `brew install mysql`

**Create Database**

```bash
mysql -u root -p
```

```sql
CREATE DATABASE IF NOT EXISTS web_ban_xe
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE web_ban_xe;
-- Import schema.sql file
source /path/to/database/schema.sql;
```

**Verify Connection**

```bash
mysql -u root -p -e "USE web_ban_xe; SHOW TABLES;"
```

### 2. Java & Maven Setup

**Install Java 25**

```bash
# Windows
choco install jdk25

# Linux
sudo apt-get install openjdk-25-jdk

# macOS
brew install openjdk@25
```

**Install Maven**

```bash
# Windows
choco install maven

# Linux
sudo apt-get install maven

# macOS
brew install maven
```

**Verify Installation**

```bash
java -version
mvn -version
```

### 3. Project Setup

```bash
# Clone repository
git clone <your-repo-url> web_ban_xe
cd web_ban_xe

# Install dependencies
./mvnw clean install

# Update database configuration
# Edit: src/main/resources/application.properties
# Set: quarkus.datasource.username, quarkus.datasource.password, quarkus.datasource.jdbc.url
```

## Running the Application

### Development Mode

```bash
./mvnw quarkus:dev
```

- API URL: http://localhost:8080/api
- Dev UI: http://localhost:8080/q/dev

### Production Build

```bash
./mvnw clean package -DskipTests
java -jar target/quarkus-app/quarkus-run.jar
```

### Native Binary (GraalVM)

```bash
./mvnw clean package -Dnative -DskipTests
./target/web_ban_xe-1.0.0-SNAPSHOT-runner
```

## Docker Deployment

### Build Docker Image

```bash
docker build -f src/main/docker/Dockerfile.jvm -t web-ban-xe:latest .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

### Access Application

- API: http://localhost:8080/api
- Health Check: http://localhost:8080/api/health

## Testing

### Run Unit Tests

```bash
./mvnw test
```

### Run Integration Tests

```bash
./mvnw verify
```

## Common Issues & Solutions

### Issue 1: MySQL Connection Error

```
SQLException: Unable to load authentication plugin
```

**Solution:**

```properties
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/web_ban_xe?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

### Issue 2: Port 8080 Already in Use

```bash
# Linux/macOS
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Issue 3: Maven Build Failure

```bash
# Clean cache and rebuild
./mvnw clean install -U -X

# Check Java version
java -version  # Must be 25+
```

## Performance Optimization

### Database Optimization

```sql
-- Add indexes for common queries
CREATE INDEX idx_listings_user ON listings(nguoi_dung_id);
CREATE INDEX idx_listings_status ON listings(trang_thai);
CREATE INDEX idx_users_email ON users(email);

-- Enable query cache
SET GLOBAL query_cache_type = ON;
SET GLOBAL query_cache_size = 268435456;
```

### Application Tuning

```properties
# src/main/resources/application.properties

# JVM Memory Settings
quarkus.native.java-heap-percentage=70

# Connection Pool
quarkus.datasource.max-size=20
quarkus.datasource.min-size=5

# Cache
quarkus.rest.response-buffering=enabled
```

## Monitoring & Logging

### View Logs

```bash
# Development mode
tail -f logs/application.log

# Production
docker logs web-ban-xe
```

### Health Checks

```bash
# API Health
curl http://localhost:8080/api/health

# Quarkus Health Endpoint (if enabled)
curl http://localhost:8080/q/health
```

## Database Backup & Restore

### Backup Database

```bash
mysqldump -u root -p web_ban_xe > database/backup_$(date +%Y%m%d).sql
```

### Restore Database

```bash
mysql -u root -p web_ban_xe < database/backup_20260519.sql
```

## Security Checklist

- ✅ Change default MySQL password
- ✅ Use environment variables for sensitive data
- ✅ Enable HTTPS in production
- ✅ Implement API authentication/JWT
- ✅ Add input validation on all endpoints
- ✅ Enable SQL parameterized queries (done via Hibernate)
- ✅ Regular security updates
- ✅ Database encryption for sensitive fields
- ✅ API rate limiting
- ✅ Log security events

## Production Checklist

- ✅ Use native binary build for better performance
- ✅ Configure external database (not local)
- ✅ Enable CORS only for trusted domains
- ✅ Add API versioning
- ✅ Implement comprehensive logging
- ✅ Setup CI/CD pipeline
- ✅ Configure automatic backups
- ✅ Setup monitoring and alerts
- ✅ Load balancing setup
- ✅ SSL/TLS certificates

## Support & Resources

- **Quarkus Docs**: https://quarkus.io/guides/
- **Hibernate Guide**: https://hibernate.org/orm/documentation/
- **MySQL Documentation**: https://dev.mysql.com/doc/
- **REST Architecture**: https://restfulapi.net/
