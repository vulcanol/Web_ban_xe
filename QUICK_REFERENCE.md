# Web Bán Xe - Quick Reference Guide

## Project Structure

```
web_ban_xe/
├── src/main/
│   ├── java/org/acme/
│   │   ├── domain/              # JPA Entity Classes
│   │   │   ├── User.java
│   │   │   ├── Brand.java
│   │   │   ├── Category.java
│   │   │   ├── Car.java
│   │   │   ├── Listing.java
│   │   │   ├── ListingImage.java
│   │   │   ├── Order.java
│   │   │   ├── Payment.java
│   │   │   ├── Review.java
│   │   │   └── Favorite.java
│   │   ├── repository/          # Data Access Layer (Panache)
│   │   │   ├── UserRepository.java
│   │   │   ├── BrandRepository.java
│   │   │   ├── CategoryRepository.java
│   │   │   ├── CarRepository.java
│   │   │   ├── ListingRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   ├── PaymentRepository.java
│   │   │   ├── ReviewRepository.java
│   │   │   └── FavoriteRepository.java
│   │   ├── service/             # Business Logic
│   │   │   ├── UserService.java
│   │   │   ├── BrandService.java
│   │   │   ├── CategoryService.java
│   │   │   ├── CarService.java
│   │   │   ├── ListingService.java
│   │   │   └── OrderService.java
│   │   ├── resource/            # REST API Endpoints
│   │   │   ├── UserResource.java
│   │   │   ├── BrandResource.java
│   │   │   ├── CategoryResource.java
│   │   │   ├── CarResource.java
│   │   │   ├── ListingResource.java
│   │   │   └── OrderResource.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── UserDTO.java
│   │   │   ├── BrandDTO.java
│   │   │   ├── CategoryDTO.java
│   │   │   ├── CarDTO.java
│   │   │   ├── ListingDTO.java
│   │   │   ├── OrderDTO.java
│   │   │   ├── PaymentDTO.java
│   │   │   ├── ReviewDTO.java
│   │   │   └── FavoriteDTO.java
│   │   └── GreetingResource.java
│   └── resources/
│       └── application.properties
├── database/
│   ├── schema.sql               # Database schema & sample data
│   ├── migrations/
│   └── scripts/
├── docs/
│   ├── api/
│   └── database/
├── pom.xml
├── README.md
├── API_DOCUMENTATION.md
└── DEPLOYMENT_GUIDE.md
```

## Key Technologies

| Component | Technology             |
| --------- | ---------------------- |
| Framework | Quarkus 3.35.3         |
| Language  | Java 25                |
| ORM       | Hibernate              |
| Database  | MySQL 8.0              |
| REST      | Quarkus REST           |
| JSON      | Jackson                |
| Security  | BCrypt                 |
| Build     | Maven                  |
| Testing   | JUnit 5 + REST Assured |

## Adding New Features

### 1. Create Entity Class

```java
@Entity
@Table(name = "table_name")
public class YourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "column_name", nullable = false)
    private String field;

    // getters and setters
}
```

### 2. Create Repository

```java
@ApplicationScoped
public class YourEntityRepository implements PanacheRepository<YourEntity> {

    public List<YourEntity> findByField(String value) {
        return list("field", value);
    }
}
```

### 3. Create Service

```java
@ApplicationScoped
public class YourEntityService {

    @Inject
    YourEntityRepository repository;

    public List<YourEntityDTO> getAll() {
        return repository.listAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}
```

### 4. Create REST Resource

```java
@Path("/entities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class YourEntityResource {

    @Inject
    YourEntityService service;

    @GET
    public Response getAll() {
        return Response.ok(service.getAll()).build();
    }
}
```

### 5. Create DTO

```java
public class YourEntityDTO {
    public Integer id;
    public String field;
}
```

## Common Database Queries

### Create User

```
POST /api/users/register
{
  "fullName": "Nguyễn Văn A",
  "email": "user@example.com",
  "password": "password",
  "phoneNumber": "0901234567"
}
```

### Search Listings

```
GET /api/listings/price?minPrice=500000000&maxPrice=1000000000
GET /api/listings/province/Hà Nội
GET /api/listings/user/1
```

### Create Listing

```
POST /api/listings
{
  "userId": 1,
  "carId": 1,
  "title": "Car Title",
  "price": 1050000000,
  "mileage": 18000,
  "condition": "CU",
  "color": "Trắng",
  "province": "Hà Nội",
  "district": "Ba Đình"
}
```

## Hibernate Mappings

### One-to-Many

```java
@OneToMany(mappedBy = "parent")
private List<Child> children;
```

### Many-to-One

```java
@ManyToOne
@JoinColumn(name = "parent_id")
private Parent parent;
```

### Many-to-Many

```java
@ManyToMany
@JoinTable(
    name = "entity1_entity2",
    joinColumns = @JoinColumn(name = "entity1_id"),
    inverseJoinColumns = @JoinColumn(name = "entity2_id")
)
private List<Entity2> entities;
```

## Panache Query Examples

```java
// Find all
List<Entity> all = Entity.listAll();

// Find by condition
Entity single = Entity.find("name", "value").firstResult();
Optional<Entity> optional = Entity.find("id", id).firstResultOptional();

// Find with query
List<Entity> results = Entity.list("name = ?1 and age > ?2", "John", 18);

// Delete
Entity.deleteAll();
Entity.delete("name", "John");

// Count
long count = Entity.count();
```

## Quarkus Dev Mode Features

- **Live Reload**: Automatic hot reload on code changes
- **Dev UI**: Available at http://localhost:8080/q/dev/
- **Dev Console**: Inspect database, logs, metrics
- **Debug Mode**: Easy debugging support

## Testing Commands

```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=YourTest

# Run with coverage
./mvnw test jacoco:report

# Integration tests
./mvnw verify

# Skip tests
./mvnw package -DskipTests
```

## Useful Maven Commands

```bash
# Clean and build
./mvnw clean install

# Skip tests
./mvnw clean install -DskipTests

# Update dependencies
./mvnw dependency:tree

# Check for vulnerabilities
./mvnw dependency-check:check

# Format code
./mvnw format:format
```

## Debugging Tips

### Enable Debug Logging

```properties
quarkus.log.category."org.acme".level=DEBUG
quarkus.log.category."org.hibernate".level=DEBUG
```

### Print SQL Queries

```properties
quarkus.hibernate-orm.log.sql=true
quarkus.jpa.properties.hibernate.format_sql=true
```

### Database Connection Issues

```bash
# Test connection
mysql -h localhost -u root -p web_ban_xe -e "SELECT 1;"
```

## Performance Tips

1. **Use Indexes** on frequently searched columns
2. **Lazy Loading** for large relationships
3. **Query Projections** to fetch only needed fields
4. **Connection Pooling** configuration
5. **Caching** strategies for read-heavy data
6. **Pagination** for large result sets
7. **Batch Operations** for bulk inserts/updates

## Security Best Practices

✅ Hash passwords with BCrypt  
✅ Use parameterized queries (automatic via Hibernate)  
✅ Validate input data  
✅ Use HTTPS in production  
✅ Implement authentication/authorization  
✅ Limit API rates  
✅ Log security events  
✅ Encrypt sensitive data  
✅ Keep dependencies updated  
✅ Use environment variables for secrets

## Useful Links

- API Docs: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- Deployment: [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)
- Quarkus: https://quarkus.io/guides/
- Hibernate: https://hibernate.org/orm/documentation/
- MySQL: https://dev.mysql.com/doc/
