# Local SFTP Test Server

This directory contains Docker configuration for running a local SFTP server for testing purposes.

## Configuration

- Server Host: localhost
- Port: 2222 (mapped from container port 22)
- Username: testuser
- Password: testpass
- Upload Directory: /upload

## Sample Data

The server comes pre-loaded with sample test files in the upload directory:
- customers.csv - Sample customer data in CSV format
- customers.xlsx - Sample customer data in Excel format

## Usage

1. Start the SFTP server:
```bash
cd infrastructure
docker-compose up -d
```

2. Update your application.properties for local testing:
```properties
sftp.host=localhost
sftp.port=2222
sftp.username=testuser
sftp.password=testpass
sftp.directory=/upload
```

3. Test SFTP connection:
```bash
# Using sftp command line client
sftp -P 2222 testuser@localhost

# Using curl
curl -k --insecure sftp://testuser:testpass@localhost:2222/upload/
```

4. Stop the server:
```bash
docker-compose down
```

## Notes

- The server is configured to restart automatically unless explicitly stopped
- Sample data is mounted as a volume, so changes persist between container restarts
- For security in production, always use environment variables or secrets for credentials
