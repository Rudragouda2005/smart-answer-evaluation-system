package com.lcs.evaluation.service;

import com.lcs.evaluation.model.AlgorithmStep;
import com.lcs.evaluation.model.LcsResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Core service implementing the Longest Common Subsequence (LCS) algorithm
 * using Dynamic Programming at the word level for answer comparison.
 */
@Service
public class LcsService {

    /**
     * Compares teacher and student answers and returns full LCS evaluation metrics.
     */
    public LcsResult compareAnswers(String teacherAnswer, String studentAnswer) {
        List<String> teacherTokens = tokenize(teacherAnswer);
        List<String> studentTokens = tokenize(studentAnswer);

        String[] teacher = teacherTokens.toArray(new String[0]);
        String[] student = studentTokens.toArray(new String[0]);

        int m = teacher.length;
        int n = student.length;

        int[][] dp = buildDpTable(teacher, student);
        List<AlgorithmStep> steps = buildAlgorithmSteps(teacher, student, dp);
        List<String> lcsWords = backtrackLcs(teacher, student, dp);
        int lcsLength = dp[m][n];

        double similarity = calculateSimilarity(lcsLength, m, n);
        String grade = assignGrade(similarity);
        String evaluation = buildEvaluationMessage(similarity, grade, lcsLength);

        LcsResult result = new LcsResult();
        result.setLcsLength(lcsLength);
        result.setLcsSequence(String.join(" ", lcsWords));
        result.setSimilarityPercentage(round(similarity, 2));
        result.setGrade(grade);
        result.setEvaluationResult(evaluation);
        result.setMatchingWords(lcsWords);
        result.setTeacherTokens(teacherTokens);
        result.setStudentTokens(studentTokens);
        result.setDpTable(dp);
        result.setAlgorithmSteps(steps);
        result.setTeacherWordCount(m);
        result.setStudentWordCount(n);

        return result;
    }

    /**
     * Splits text into normalized words (lowercase, punctuation removed).
     */
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(text.trim().split("\\s+"))
                .map(this::normalizeWord)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }

    private String normalizeWord(String word) {
        return word.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    /**
     * Builds the DP table for LCS.
     * dp[i][j] = length of LCS between teacher[0..i-1] and student[0..j-1]
     */
    public int[][] buildDpTable(String[] teacher, String[] student) {
        int m = teacher.length;
        int n = student.length;
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (teacher[i - 1].equals(student[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp;
    }

    /**
     * Records step-by-step explanations while filling the DP table.
     */
    private List<AlgorithmStep> buildAlgorithmSteps(String[] teacher, String[] student, int[][] dp) {
        List<AlgorithmStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(new AlgorithmStep(
                stepNum++,
                "Initialize DP table with dimensions (" + teacher.length + "+1) x (" + student.length + "+1). Row 0 and column 0 are zeros.",
                0, 0, 0));

        for (int i = 1; i <= teacher.length; i++) {
            for (int j = 1; j <= student.length; j++) {
                String desc;
                if (teacher[i - 1].equals(student[j - 1])) {
                    desc = String.format(
                            "Words match: '%s' == '%s'. dp[%d][%d] = dp[%d][%d] + 1 = %d",
                            teacher[i - 1], student[j - 1], i, j, i - 1, j - 1, dp[i][j]);
                } else {
                    desc = String.format(
                            "No match: '%s' vs '%s'. dp[%d][%d] = max(dp[%d][%d], dp[%d][%d]) = %d",
                            teacher[i - 1], student[j - 1], i, j, i - 1, j, i, j - 1, dp[i][j]);
                }
                steps.add(new AlgorithmStep(stepNum++, desc, i, j, dp[i][j]));
            }
        }

        steps.add(new AlgorithmStep(
                stepNum,
                "LCS length = dp[" + teacher.length + "][" + student.length + "] = " + dp[teacher.length][student.length],
                teacher.length, student.length, dp[teacher.length][student.length]));

        return steps;
    }

    /**
     * Backtracks through the DP table to reconstruct the LCS word sequence.
     */
    public List<String> backtrackLcs(String[] teacher, String[] student, int[][] dp) {
        List<String> lcs = new ArrayList<>();
        int i = teacher.length;
        int j = student.length;

        while (i > 0 && j > 0) {
            if (teacher[i - 1].equals(student[j - 1])) {
                lcs.add(teacher[i - 1]);
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        Collections.reverse(lcs);
        return lcs;
    }

    /**
     * Similarity = (2 * LCS length) / (teacher words + student words) * 100
     * This is a symmetric measure suitable for answer comparison.
     */
    public double calculateSimilarity(int lcsLength, int teacherCount, int studentCount) {
        if (teacherCount == 0 && studentCount == 0) {
            return 100.0;
        }
        if (teacherCount == 0 || studentCount == 0) {
            return 0.0;
        }
        return (2.0 * lcsLength / (teacherCount + studentCount)) * 100.0;
    }

    /**
     * Assigns a letter-grade label based on similarity percentage.
     */
    public String assignGrade(double similarity) {
        if (similarity >= 90) {
            return "Excellent";
        } else if (similarity >= 70) {
            return "Good";
        } else if (similarity >= 50) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    private String buildEvaluationMessage(double similarity, String grade, int lcsLength) {
        return String.format(
                Locale.ROOT,
                "The student answer matches %d key words in sequence with the teacher answer. " +
                        "Similarity is %.2f%% — graded as %s.",
                lcsLength, similarity, grade);
    }

    private double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
