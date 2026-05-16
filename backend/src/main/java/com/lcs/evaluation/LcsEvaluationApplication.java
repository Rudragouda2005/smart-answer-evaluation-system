package com.lcs.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Smart Answer Evaluation System backend.
 * Starts the Spring Boot REST API that performs LCS-based comparison.
 */
@SpringBootApplication
public class LcsEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LcsEvaluationApplication.class, args);
    }
}
