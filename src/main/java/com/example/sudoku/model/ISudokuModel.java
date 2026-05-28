package com.example.sudoku.model;

/**
 * Interface that defines the contract for the Sudoku game model.
 * Covers matrix access, validation, puzzle generation, hollowing, and the hint system.
 */
public interface ISudokuModel {

    /**
     * Gets the value at position (i, j) in the game matrix.
     *
     * @param i row index
     * @param j column index
     * @return value at (i, j)
     */
    int get(int i, int j);

    /**
     * Sets a value at position (i, j) in the game matrix.
     *
     * @param i row index
     * @param j column index
     * @param val value to set
     */
    void set(int i, int j, int val);

    /**
     * Gets the solution value at position (i, j).
     *
     * @param i row index
     * @param j column index
     * @return solution value at (i, j)
     */
    int getSolution(int i, int j);

    /**
     * Checks whether the current game matrix matches the solution.
     *
     * @return true if the puzzle is solved, false otherwise
     */
    boolean isSolved();

    /**
     * Checks whether a value placed by the player at (i, j) matches the solution.
     *
     * @param i row index
     * @param j column index
     * @param val value entered by the player
     * @return true if correct, false otherwise
     */
    boolean isCorrect(int i, int j, int val);

    /**
     * Reveals a hint by restoring the last removed position from the solution.
     *
     * @return the Pos that was revealed, or null if no hints are left
     */
    SudokuModel.Pos giveHint();

    /**
     * Returns the number of hints remaining.
     *
     * @return size of the removed list
     */
    int hintsLeft();

    /**
     * Validates whether a number can be placed at position (x, y).
     * Checks row, column, and 2x3 block constraints.
     *
     * @param x row index
     * @param y column index
     * @param n number to validate (1-6)
     * @return true if the number is valid at that position, false otherwise
     */
    boolean validation(int x, int y, int n);

    /**
     * Recursively generates a valid completed Sudoku matrix using backtracking.
     *
     * @param i row index
     * @param j column index
     * @return true if the matrix was successfully completed, false otherwise
     */
    boolean generator(int i, int j);

    /**
     * Hollows out the completed matrix to create a playable puzzle.
     * Saves the solution before hollowing.
     */
    void hollower();

    /**
     * Prints the current game matrix to the console.
     */
    void matrixView();

    /**
     * Removes a position from the removed list when correctly placed by the player.
     *
     * @param i row index
     * @param j column index
     */
    void removeFromRemoved(int i, int j);
}
