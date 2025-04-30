package com.amir.elk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller designed to generate sample log messages at various levels
 * for testing the ELK stack integration.
 */
@RestController
@RequestMapping("/api/logs") // Base path for all endpoints in this controller
public class LogGeneratorController {

    // Standard SLF4J Logger for this class
    private static final Logger LOGGER = LoggerFactory.getLogger(LogGeneratorController.class);

    /**
     * Handles GET requests to /api/logs/info.
     * Generates an INFO level log message.
     *
     * @return ResponseEntity containing a success message.
     */
    @GetMapping("/info")
    public ResponseEntity<String> generateInfoLog() {
        // Log an INFO message - this will be sent to Logstash if configured correctly
        LOGGER.info("Generating INFO log via /api/logs/info endpoint request.");

        String responseMessage = "INFO log generated successfully for ELK monitoring.";
        return ResponseEntity.ok(responseMessage); // Return HTTP 200 OK with the message
    }

    /**
     * Handles GET requests to /api/logs/warn.
     * Generates a WARN level log message.
     *
     * @return ResponseEntity containing a success message.
     */
    @GetMapping("/warn")
    public ResponseEntity<String> generateWarnLog() {
        // Log a WARN message
        LOGGER.warn("Generating WARN log via /api/logs/warn endpoint request. This could indicate a potential issue.");

        String responseMessage = "WARN log generated successfully for ELK monitoring.";
        return ResponseEntity.ok(responseMessage); // Return HTTP 200 OK
    }

    /**
     * Handles GET requests to /api/logs/error.
     * Generates an ERROR level log message, including a sample exception.
     *
     * @return ResponseEntity containing a success message.
     */
    @GetMapping("/error")
    public ResponseEntity<String> generateErrorLog() {
        // Create a sample exception to include in the log
        Exception sampleException = new RuntimeException("Simulated exception for ERROR log testing!");

        // Log an ERROR message with the exception stack trace
        LOGGER.error("Generating ERROR log via /api/logs/error endpoint request. An error condition is being simulated.", sampleException);

        String responseMessage = "ERROR log generated successfully for ELK monitoring.";
        return ResponseEntity.ok(responseMessage); // Return HTTP 200 OK
    }
}