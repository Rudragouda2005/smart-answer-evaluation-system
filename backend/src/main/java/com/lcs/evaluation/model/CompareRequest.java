package com.lcs.evaluation.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body sent from the frontend with teacher and student answers.
 */
public class CompareRequest {

    @NotBlank(message = "Teacher answer cannot be empty")
    private String teacherAnswer;

    @NotBlank(message = "Student answer cannot be empty")
    private String studentAnswer;

    public CompareRequest() {
    }

    public CompareRequest(String teacherAnswer, String studentAnswer) {
        this.teacherAnswer = teacherAnswer;
        this.studentAnswer = studentAnswer;
    }

    public String getTeacherAnswer() {
        return teacherAnswer;
    }

    public void setTeacherAnswer(String teacherAnswer) {
        this.teacherAnswer = teacherAnswer;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
}
