package com.lcs.evaluation.model;

/**
 * Represents one step in the LCS dynamic programming process for visualization.
 */
public class AlgorithmStep {

    private int stepNumber;
    private String description;
    private int row;
    private int col;
    private int dpValue;

    public AlgorithmStep() {
    }

    public AlgorithmStep(int stepNumber, String description, int row, int col, int dpValue) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.row = row;
        this.col = col;
        this.dpValue = dpValue;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getDpValue() {
        return dpValue;
    }

    public void setDpValue(int dpValue) {
        this.dpValue = dpValue;
    }
}
