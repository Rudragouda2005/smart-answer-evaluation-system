package com.lcs.evaluation.controller;

import com.lcs.evaluation.model.CompareRequest;
import com.lcs.evaluation.model.LcsResult;
import com.lcs.evaluation.service.LcsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller exposing endpoints for answer comparison and health checks.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EvaluationController {

    private final LcsService lcsService;

    public EvaluationController(LcsService lcsService) {
        this.lcsService = lcsService;
    }

    /**
     * Compares teacher and student answers using the LCS algorithm.
     */
    @PostMapping("/compare")
    public ResponseEntity<LcsResult> compare(@Valid @RequestBody CompareRequest request) {
        LcsResult result = lcsService.compareAnswers(
                request.getTeacherAnswer(),
                request.getStudentAnswer());
        return ResponseEntity.ok(result);
    }

    /**
     * Health check endpoint to verify the backend is running.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> body = new HashMap<>();
        body.put("status", "UP");
        body.put("service", "Smart Answer Evaluation System");
        return ResponseEntity.ok(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
