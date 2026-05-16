package com.lcs.evaluation.model;

import java.util.List;

/**
 * Complete evaluation result returned to the frontend after LCS comparison.
 */
public class LcsResult {

    private int lcsLength;
    private String lcsSequence;
    private double similarityPercentage;
    private String grade;
    private String evaluationResult;
    private List<String> matchingWords;
    private List<String> teacherTokens;
    private List<String> studentTokens;
    private int[][] dpTable;
    private List<AlgorithmStep> algorithmSteps;
    private int teacherWordCount;
    private int studentWordCount;

    public int getLcsLength() {
        return lcsLength;
    }

    public void setLcsLength(int lcsLength) {
        this.lcsLength = lcsLength;
    }

    public String getLcsSequence() {
        return lcsSequence;
    }

    public void setLcsSequence(String lcsSequence) {
        this.lcsSequence = lcsSequence;
    }

    public double getSimilarityPercentage() {
        return similarityPercentage;
    }

    public void setSimilarityPercentage(double similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(String evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    public List<String> getMatchingWords() {
        return matchingWords;
    }

    public void setMatchingWords(List<String> matchingWords) {
        this.matchingWords = matchingWords;
    }

    public List<String> getTeacherTokens() {
        return teacherTokens;
    }

    public void setTeacherTokens(List<String> teacherTokens) {
        this.teacherTokens = teacherTokens;
    }

    public List<String> getStudentTokens() {
        return studentTokens;
    }

    public void setStudentTokens(List<String> studentTokens) {
        this.studentTokens = studentTokens;
    }

    public int[][] getDpTable() {
        return dpTable;
    }

    public void setDpTable(int[][] dpTable) {
        this.dpTable = dpTable;
    }

    public List<AlgorithmStep> getAlgorithmSteps() {
        return algorithmSteps;
    }

    public void setAlgorithmSteps(List<AlgorithmStep> algorithmSteps) {
        this.algorithmSteps = algorithmSteps;
    }

    public int getTeacherWordCount() {
        return teacherWordCount;
    }

    public void setTeacherWordCount(int teacherWordCount) {
        this.teacherWordCount = teacherWordCount;
    }

    public int getStudentWordCount() {
        return studentWordCount;
    }

    public void setStudentWordCount(int studentWordCount) {
        this.studentWordCount = studentWordCount;
    }
}
