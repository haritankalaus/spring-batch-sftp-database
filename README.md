# Spring Batch SFTP ETL Pipeline 🚀

A robust Spring Boot application demonstrating automated ETL (Extract, Transform, Load) processing using Spring Batch with SFTP integration. Perfect for processing customer data from CSV and Excel files into a database with scheduled execution.

## 🌟 Key Features

- **SFTP Integration**: Secure file transfer with robust session management
- **Multi-Format Support**: Process both CSV and Excel files
- **Batch Processing**: Efficient large dataset handling with Spring Batch
- **Scheduled Jobs**: Configurable cron-based job scheduling
- **Error Handling**: Comprehensive error handling and logging
- **Database Integration**: Automated data loading into H2 database
- **Docker Support**: Local SFTP testing environment using Docker

## 🛠️ Technical Stack

- **Framework**: Spring Boot 3.x
- **Core Features**: Spring Batch, Spring Integration (SFTP)
- **Database**: H2 (embedded)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **File Processing**: Apache POI (Excel), OpenCSV (CSV)

## 📋 Prerequisites

- Java 21
- Maven
- Docker (for local SFTP testing)

## 🚀 Quick Start

1. Clone the repository:
```bash
git clone https://github.com/yourusername/spring-batch-sftp-database.git
cd spring-batch-sftp-database
```

2. Start the local SFTP server:
```bash
cd infrastructure
docker-compose up -d
```

3. Configure application.properties:
```properties
# SFTP Configuration
sftp.host=localhost
sftp.port=2222
sftp.user=testuser
sftp.password=testpass
sftp.input.directory=/upload
sftp.output.directory=/download

# Job Scheduling
job.cron.morning=0 0 6 * * *
job.cron.afternoon=0 0 14 * * *
job.cron.evening=0 0 22 * * *
```

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

## 📁 Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/batchdemo/
│   │   │       ├── config/        # Spring and Batch configurations
│   │   │       ├── controller/    # REST endpoints
│   │   │       ├── listener/      # Job and step listeners
│   │   │       ├── model/         # Domain models
│   │   │       ├── processor/     # Item processors
│   │   │       ├── reader/        # File readers (CSV, Excel)
│   │   │       └── service/       # SFTP and business services
│   │   └── resources/
│   └── test/                      # Unit and integration tests
└── infrastructure/                 # Docker SFTP setup
```

## 🔍 Features in Detail

### SFTP Integration
- Secure file transfer with session management
- Automatic connection handling and cleanup
- Support for multiple file formats

### Batch Processing
- Chunk-based processing for memory efficiency
- Transaction management
- Skip and retry policies
- Job execution listeners

### File Processing
- CSV files with header validation
- Excel files with multi-sheet support
- Decimal and integer number handling
- Error handling for malformed data

### Scheduling
- Configurable cron schedules
- Multiple daily job runs
- Job execution tracking

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
Requires Docker for SFTP testing:
```bash
mvn verify
```

## 📝 File Format Requirements

### CSV Format
```csv
name,age,email,address
John Doe,30,john@example.com,123 Main St
```

### Excel Format
- First row must be header: name, age, email, address
- Subsequent rows contain data
- Age can be integer or decimal (will be converted to integer)

## 🐳 Docker SFTP Server

### Configuration
- Host: localhost
- Port: 2222
- Username: testuser
- Password: testpass
- Upload Directory: /upload
- Download Directory: /download

### Commands
```bash
# Start SFTP server
docker-compose up -d

# Check logs
docker-compose logs

# Stop server
docker-compose down
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🔍 Keywords

`spring-batch`, `spring-boot`, `sftp`, `etl`, `batch-processing`, `file-processing`, `csv`, `excel`, `java`, `docker`, `automation`, `data-integration`, `spring-integration`, `enterprise-integration`, `data-pipeline`, `batch-jobs`, `scheduled-jobs`, `file-transfer`, `data-processing`, `spring-framework`

---

⭐ Found this helpful? Please star the repository!

For questions or issues, please open a GitHub issue or contact the maintainers.
