package com.lcs.evaluation.service;

import com.lcs.evaluation.model.LcsResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LcsServiceTest {

    @Autowired
    private LcsService lcsService;

    @Test
    void identicalAnswersHaveHighSimilarity() {
        String answer = "photosynthesis converts light energy into chemical energy";
        LcsResult result = lcsService.compareAnswers(answer, answer);
        assertEquals(100.0, result.getSimilarityPercentage(), 0.01);
        assertEquals("Excellent", result.getGrade());
    }

    @Test
    void partialMatchProducesLcs() {
        LcsResult result = lcsService.compareAnswers(
                "java is object oriented programming language",
                "java is a popular programming language");
        assertTrue(result.getLcsLength() >= 3);
        assertTrue(result.getSimilarityPercentage() > 0);
    }
}
