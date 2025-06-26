# Spring Batch ETL Pipeline

A robust ETL (Extract, Transform, Load) pipeline built with Spring Batch for handling data movement between multiple systems.

## Overview

This application manages data transfer between:
- Database
- SFTP Server
- Shared Folder

## Key Components

### Jobs
1. **DatabaseToSftpCsvJob**: Exports database records to CSV files on SFTP server
2. **SftpToSharedJob**: Transfers files from SFTP server to shared folder
3. **SharedToSftpJob**: Moves files from shared folder to SFTP server

### Data Model
- **LoanRecord**: Contains loan information (id, name, amount)

### Services
- **SftpService**: Handles SFTP server operations
- **DatabaseReader**: Reads records from database
- **CsvFileWriter**: Writes data to CSV format
- **SharedFolderWriter**: Manages shared folder operations

## Technical Stack

- Java
- Spring Boot
- Spring Batch
- Spring Data JPA
- SFTP Client
- Maven

## Prerequisites

- JDK 17 or later
- Maven
- Access to SFTP server
- Database (configured in application.properties)
- Shared folder access

## Configuration

Configure the following in `application.properties`:
- Database connection
- SFTP server credentials
- Shared folder path
- Batch job parameters

## Building

```bash
mvn clean install
```

## Running

```bash
mvn spring-boot:run
```

## Job Execution

Jobs can be triggered via REST endpoints or scheduled tasks. Each job processes data in chunks for optimal performance and memory usage.

## Monitoring

Spring Batch provides built-in monitoring capabilities through:
- Job execution history
- Step execution statistics
- Chunk processing metrics
