FROM atmoz/sftp:latest

# Create test user with password
# Username: testuser
# Password: testpass
# Directory: /upload

ENV TESTUSER=testuser:testpass:1001
ENV TESTDIR=/home/testuser/upload

# Create upload directory and set permissions
RUN mkdir -p /home/testuser/upload && \
    chown testuser:users /home/testuser/upload && \
    chmod 755 /home/testuser/upload

# Copy sample files for testing
COPY ./sample-data/ /home/testuser/upload/

# Expose SFTP port
EXPOSE 22
