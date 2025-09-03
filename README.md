# Preclinical Data Platform

A comprehensive Spring Boot application designed for managing preclinical research data, built to understand and demonstrate Spring Boot concepts and best practices.

## 🚀 Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.5
- **Framework**: Spring Framework with comprehensive annotation-driven configuration

## 📋 Project Overview

This application serves as a learning platform for Spring Boot concepts while providing a robust system for managing preclinical research data including studies, patients, efficacy measurements, and adverse events.

## 🏗️ Architecture & Design Patterns

The application follows modern Spring Boot best practices with a layered architecture:

### Core Layers
- **Controllers**: REST API endpoints for external communication
- **Services**: Business logic and transaction management
- **Repositories**: Data access layer with Spring Data JPA
- **Entities**: JPA entities representing data models
- **DTOs**: Data transfer objects for API communication
- **Configuration**: Application configuration and cross-cutting concerns

### Key Features Implemented
- **Caching**: Redis/in-memory caching with Spring Cache abstraction
- **Async Processing**: Non-blocking operations using `@Async`
- **Scheduling**: Automated tasks with `@Scheduled`
- **Event-Driven Architecture**: Application events for loose coupling
- **Retry Mechanism**: Fault tolerance with `@Retryable`
- **Security**: Spring Security configuration
- **Validation**: Bean validation with comprehensive annotations
- **Exception Handling**: Global exception handling with `@ControllerAdvice`

## 📁 Project Structure

```
src/main/java/com/preclinical/platform/preclinicaldataplatform/
├── PreclinicaldataplatformApplication.java    # Main application class
├── configuration/                             # Spring configuration classes
│   ├── AsyncConfig.java                      # Async processing config
│   ├── CacheConfig.java                      # Caching configuration
│   ├── PreclinicalPlatformProperties.java    # Custom properties binding
│   ├── PreclinicalScheduledTasks.java        # Scheduled task definitions
│   ├── RetryConfig.java                      # Retry mechanism setup
│   ├── SchedulingConfig.java                 # Scheduling configuration
│   ├── SecurityConfig.java                   # Security configuration
│   └── WebConfig.java                        # Web MVC configuration
├── controller/                                # REST API controllers
│   ├── AnalysisController.java               # Analysis endpoints
│   ├── PatientController.java                # Patient management
│   └── StudyController.java                  # Study management
├── dto/                                       # Data Transfer Objects
│   ├── AnalysisReport.java
│   ├── CreateAdverseEventRequest.java
│   ├── CreateEfficacyMeasurementRequest.java
│   ├── CreatePatientRequest.java
│   ├── CreateStudyRequest.java
│   ├── EfficacyAnalysisRequest.java
│   ├── SafetyAnalysisReport.java
│   ├── StudyStatisticsReport.java
│   ├── UpdatePatientRequest.java
│   └── UpdateStudyRequest.java
├── entity/                                    # JPA Entities
│   ├── AdverseEvent.java                     # Adverse event tracking
│   ├── EfficacyMeasurement.java              # Efficacy data
│   ├── Patient.java                          # Patient information
│   └── Study.java                            # Clinical study data
├── event/                                     # Application Events
│   ├── EfficacyMeasurementRecordedEvent.java
│   ├── PatientEnrolledEvent.java
│   ├── PatientStatusChangedEvent.java
│   ├── PatientUpdatedEvent.java
│   ├── SeriousAdverseEventAlert.java
│   ├── StudyCreatedEvent.java
│   └── StudyStatusChangedEvent.java
├── exception/                                 # Exception handling
│   ├── DuplicateStudyCodeException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── IllegalStudyStatusException.java
│   ├── PatientEnrollmentException.java
│   ├── PatientNotFoundException.java
│   └── StudyNotFoundException.java
├── repository/                                # Data Access Layer
│   ├── AdverseEventRepository.java
│   ├── EfficacyMeasurementRepository.java
│   ├── PatientRepository.java
│   └── StudyRepository.java
└── service/                                   # Business Logic Layer
    ├── AuditService.java
    ├── DataAnalysisService.java
    ├── EfficacyMeasurementService.java
    ├── EmailNotificationService.java
    └── StudyManagementService.java
```

## 🔧 Spring Boot Annotations Used

This project extensively uses Spring Boot annotations to demonstrate various concepts:

### Core Spring Annotations
- `@SpringBootApplication` - Main application class
- `@Component`, `@Service`, `@Repository`, `@Controller` - Stereotype annotations
- `@Configuration` - Configuration classes
- `@Bean` - Bean definition
- `@Profile` - Environment-specific beans

### Web Layer
- `@RestController` - REST API controllers
- `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` - HTTP mapping
- `@PathVariable`, `@RequestParam`, `@RequestBody` - Request binding
- `@ControllerAdvice`, `@ExceptionHandler` - Exception handling

### Data Layer
- `@Entity`, `@Table` - JPA entities
- `@Id`, `@GeneratedValue` - Primary key configuration
- `@Column`, `@JoinColumn` - Column mapping
- `@OneToMany`, `@ManyToOne` - Relationships
- `@Enumerated` - Enum mapping
- `@CreationTimestamp`, `@UpdateTimestamp` - Audit timestamps

### Validation
- `@Valid`, `@Validated` - Validation trigger
- `@NotNull`, `@NotBlank`, `@Size` - Field validation
- `@Min`, `@Max`, `@Positive` - Numeric validation
- `@DecimalMin`, `@DecimalMax` - Decimal validation
- `@Pattern`, `@Future`, `@PastOrPresent` - Format validation
- `@AssertTrue` - Custom validation

### Caching
- `@EnableCaching` - Enable caching support
- `@Cacheable`, `@CacheEvict`, `@CachePut` - Cache operations

### Async & Scheduling
- `@EnableAsync`, `@Async` - Asynchronous processing
- `@EnableScheduling`, `@Scheduled` - Task scheduling

### Other Features
- `@EnableRetry`, `@Retryable` - Retry mechanism
- `@Transactional` - Transaction management
- `@ConfigurationProperties` - External configuration binding
- `@EnableJpaRepositories` - JPA repositories
- `@EnableWebSecurity` - Security configuration

### Data & Utility
- `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor` - Lombok annotations
- `@Slf4j` - Logging
- `@JsonFormat` - JSON serialization
- `@Query`, `@Modifying`, `@Param` - Custom queries
- `@Override` - Method override

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven or Gradle
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)

### Running the Application

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/preclinicaldataplatform.git

# Navigate to project directory
cd preclinicaldataplatform

# Run the application (if using Maven)
./mvnw spring-boot:run

# Or if using Gradle
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## 📚 Learning Objectives

This project demonstrates:

1. **Spring Boot Fundamentals**: Auto-configuration, starter dependencies, application properties
2. **Dependency Injection**: Constructor, field, and setter injection patterns
3. **Data Access**: Spring Data JPA, custom queries, transaction management
4. **Web Development**: REST APIs, request/response handling, validation
5. **Caching**: Spring Cache abstraction with multiple cache providers
6. **Async Processing**: Non-blocking operations and event-driven architecture
7. **Scheduling**: Automated background tasks
8. **Security**: Authentication and authorization
9. **Error Handling**: Global exception handling and custom exceptions
10. **Testing**: Unit and integration testing strategies
11. **Configuration**: Externalized configuration and profiles

## 🤝 Contributing

This is a learning project. Feel free to fork, experiment, and submit pull requests with improvements or additional Spring Boot concepts.

## 📄 License

This project is created for educational purposes.

---

**Note**: This project is built as a learning exercise to understand Spring Boot concepts and best practices. It demonstrates a comprehensive use of Spring Boot features in a real-world scenario context.