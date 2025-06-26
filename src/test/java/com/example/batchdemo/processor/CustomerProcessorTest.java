package com.example.batchdemo.processor;

import com.example.batchdemo.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerProcessorTest {

    private CustomerProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CustomerProcessor();
    }

    @Test
    void process_ValidData() throws Exception {
        // Arrange
        String[] input = {"John Doe", "30", "john@test.com", "123 Main St"};

        // Act
        Customer result = processor.process(input);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(30, result.getAge());
        assertEquals("john@test.com", result.getEmail());
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void process_InvalidAge() throws Exception {
        // Arrange
        String[] input = {"John Doe", "invalid", "john@test.com", "123 Main St"};

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> processor.process(input));
    }

    @Test
    void process_NullInput() throws Exception {
        // Act
        Customer result = processor.process(null);

        // Assert
        assertNull(result);
    }

    @Test
    void process_EmptyInput() throws Exception {
        // Arrange
        String[] input = {};

        // Act
        Customer result = processor.process(input);

        // Assert
        assertNull(result);
    }

    @Test
    void process_InsufficientData() throws Exception {
        // Arrange
        String[] input = {"John Doe", "30"}; // Missing email and address

        // Act
        Customer result = processor.process(input);

        // Assert
        assertNull(result);
    }

    @Test
    void process_IncompleteData() throws Exception {
        // Arrange
        String[] input = {"John Doe", "30"};

        // Act
        Customer result = processor.process(input);

        // Assert
        assertNull(result);
    }
}
