package com.comp2042;

public final class ClearRow {

    // this is a data transfer object (DTO) that holds the results after clearing complete rows
    // code here packages 3 pieces of information after rows are cleared :

    private final int linesRemoved; // no. of lines removed (1-4)
    private final int[][] newMatrix; // updated game board after the removal of rows and everything dropping down
    private final int scoreBonus; // points earned from clearing rows

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}
