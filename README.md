# Spring Batch SFTP to Database

A Spring Batch application that reads CSV and Excel files from an SFTP server and loads them into a database. The application supports scheduled execution and manual triggering via REST API.

## Overview

This application implements an ETL (Extract, Transform, Load) process that:
1. Connects to an SFTP server
2. Reads CSV and Excel files containing customer records
3. Processes and validates the data
4. Loads the records into a database
5. Supports automatic scheduling and manual triggering

## Project Structure

```
src/main/java/com/example/batchdemo/
├── config/
│   └── SftpConfig.java           # SFTP connection configuration
├── controller/
│   └── JobController.java        # REST endpoints for job control
├── job/
│   └── SftpToDatabaseJob.java    # Batch job configuration
├── listener/
│   └── SftpSessionJobListener.java # SFTP session lifecycle management
├── model/
│   └── Customer.java             # Domain model
├── processor/
│   └── CustomerProcessor.java    # Data transformation logic
├── reader/
│   ├── FileReader.java          # File reading interface
│   ├── CsvFileReader.java       # CSV file implementation
│   ├── ExcelFileReader.java     # Excel file implementation
│   ├── FileReaderFactory.java   # Factory for file readers
│   └── MultiFileReader.java     # Multi-file processing
├── repository/
│   └── CustomerRepository.java  # Data access layer
└── service/
    ├── SftpService.java         # SFTP operations interface
    └── impl/
        └── SftpServiceImpl.java # SFTP implementation
```

## Features
- Multi-format file support (CSV and Excel)
- SFTP integration with efficient session management
- Automatic job scheduling (three times daily)
- REST API for manual job triggering
- Chunk-based processing for optimal performance
- JPA-based database operations

#### Reader
- **MultiFileCsvReader**: Custom ItemReader that reads CSV files from SFTP server

#### Processor
- **CustomerProcessor**: Processes and validates loan records

#### Model
- **Customer**: Entity class representing loan data

#### Repository
- **CustomerRepository**: JPA repository for loan records

## Technical Stack

- Java 17+
- Spring Boot 3.x
- Spring Batch
- Spring Integration (SFTP)
- Spring Data JPA
- OpenCSV for CSV parsing
- Apache POI for Excel parsing
- H2 Database (configurable)
- Maven for dependency management

## Prerequisites

- JDK 17 or later
- Maven 3.x
- SFTP server access
- Database (MySQL/PostgreSQL)

## Configuration

Configure the following properties in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# SFTP Configuration
sftp.host=your-sftp-host
sftp.port=22
sftp.user=your-sftp-username
sftp.password=your-sftp-password
sftp.input.directory=/path/to/your/files

# Job Scheduling
job.cron.morning=0 0 6 * * *     # 6:00 AM
job.cron.afternoon=0 0 14 * * *   # 2:00 PM
job.cron.evening=0 0 22 * * *     # 10:00 PM

# Batch
spring.batch.jdbc.initialize-schema=always
```

## Usage

### Running the Application

```bash
./mvnw spring-boot:run
```

### Triggering Jobs Manually

Use the REST API to trigger the job manually:

```bash
curl -X POST http://localhost:8080/api/jobs/sftp-import
```

### Automatic Scheduling

The job runs automatically three times per day:
- Morning: 6:00 AM
- Afternoon: 2:00 PM
- Evening: 10:00 PM

You can modify these schedules in the `application.properties` file.

### File Processing

1. The application monitors the configured SFTP directory for CSV and Excel files
2. Files are processed in chunks of 10 records
3. Each record is transformed into a Customer entity
4. Processed records are saved to the database
5. SFTP sessions are managed efficiently:
   - Created only when needed (job start)
   - Reused throughout the job
   - Properly closed after job completion
