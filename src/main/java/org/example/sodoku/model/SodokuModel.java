package org.example.sodoku.model;
import java.util.*;

/**
 * Model class for a 6x6 Sudoku game.
 * Handles matrix generation, validation, hollowing logic, and hint system.
 */
public class SodokuModel {

    /** The current game matrix (with holes). */
    private final ArrayList<ArrayList<Integer>> matrix;

    /** The solved matrix before hollowing. */
    private ArrayList<ArrayList<Integer>> solution;

    /** Random instance for shuffling and random selection. */
    private final Random random = new Random();

    /** Positions removed during hollowing, used for the hint system. */
    private final ArrayList<Pos> removed = new ArrayList<>();

    /**
     * Represents a position in the matrix.
     */
    static class Pos {
        int i, j;

        /**
         * @param i row index
         * @param j column index
         */
        Pos(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * Constructor. Initializes both matrices with zeros.
     */
    public SodokuModel() {
        matrix = buildEmptyMatrix();
        solution = buildEmptyMatrix();
    }

    /**
     * Builds a 6x6 matrix filled with zeros.
     *
     * @return empty 6x6 ArrayList matrix
     */
    private ArrayList<ArrayList<Integer>> buildEmptyMatrix() {
        ArrayList<ArrayList<Integer>> m = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                row.add(0);
            }
            m.add(row);
        }
        return m;
    }

    /**
     * Copies a 6x6 matrix into a new independent instance.
     *
     * @param source matrix to copy
     * @return new matrix with the same values
     */
    private ArrayList<ArrayList<Integer>> copyMatrix(ArrayList<ArrayList<Integer>> source) {
        ArrayList<ArrayList<Integer>> copy = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < source.get(i).size(); j++) {
                row.add(source.get(i).get(j));
            }
            copy.add(row);
        }
        return copy;
    }

    /**
     * Gets the value at position (i, j) in the game matrix.
     *
     * @param i row index
     * @param j column index
     * @return value at (i, j)
     */
    public int get(int i, int j) {
        return matrix.get(i).get(j);
    }

    /**
     * Sets a value at position (i, j) in the game matrix.
     *
     * @param i row index
     * @param j column index
     * @param val value to set
     */
    public void set(int i, int j, int val) {
        matrix.get(i).set(j, val);
    }

    /**
     * Gets the solution value at position (i, j).
     *
     * @param i row index
     * @param j column index
     * @return solution value at (i, j)
     */
    public int getSolution(int i, int j) {
        return solution.get(i).get(j);
    }

    /**
     * Checks whether the current game matrix matches the solution.
     *
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean isSolved() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (!matrix.get(i).get(j).equals(solution.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether a value placed by the player at (i, j) matches the solution.
     *
     * @param i row index
     * @param j column index
     * @param val value entered by the player
     * @return true if correct, false otherwise
     */
    public boolean isCorrect(int i, int j, int val) {
        return solution.get(i).get(j) == val;
    }

    /**
     * Reveals a hint by restoring the last removed position from the solution.
     *
     * @return the Pos that was revealed, or null if no hints are left
     */
    public Pos giveHint() {
        if (removed.isEmpty()) {
            return null;
        }
        Pos pos = removed.remove(removed.size() - 1);
        matrix.get(pos.i).set(pos.j, solution.get(pos.i).get(pos.j));
        return pos;
    }

    /**
     * Returns the number of hints remaining.
     *
     * @return size of the removed list
     */
    public int hintsLeft() {
        return removed.size();
    }

    /**
     * Validates whether a number can be placed at position (x, y).
     * Checks row, column, and 2x3 block constraints.
     *
     * @param x row index
     * @param y column index
     * @param n number to validate (1-6)
     * @return true if the number is valid at that position, false otherwise
     */
    public boolean validation(int x, int y, int n) {
        for (int i = 0; i < 6; i++) {
            if (matrix.get(x).get(i) == n) {
                return false;
            }
        }

        for (int i = 0; i < 6; i++) {
            if (matrix.get(i).get(y) == n) {
                return false;
            }
        }

        int x0 = (x / 3) * 3;
        int y0 = (y / 2) * 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (matrix.get(x0 + i).get(y0 + j) == n) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Recursively generates a valid completed Sudoku matrix using backtracking.
     * Traverses column by column, row by row.
     *
     * @param i row index
     * @param j column index
     * @return true if the matrix was successfully completed, false otherwise
     */
    public boolean generator(int i, int j) {
        if (j == 6) {
            return true;
        }

        int nextI;
        int nextJ;
        if (i == 5) {
            nextI = 0;
            nextJ = j + 1;
        } else {
            nextI = i + 1;
            nextJ = j;
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        Collections.shuffle(numbers);

        int idx = 0;
        while (idx < 6) {
            int num = numbers.get(idx);
            if (validation(i, j, num)) {
                matrix.get(i).set(j, num);
                if (generator(nextI, nextJ)) {
                    return true;
                }
                matrix.get(i).set(j, 0);
            }
            idx++;
        }
        return false;
    }

    /**
     * Returns the block index (0-5) for a given position.
     * Blocks are ordered left to right, top to bottom.
     *
     * @param i row index
     * @param j column index
     * @return block index between 0 and 5
     */
    private int getBlockIndex(int i, int j) {
        int blockRow = i / 2;
        int blockCol = j / 3;
        return blockRow * 2 + blockCol;
    }

    /**
     * Hollows out the completed matrix to create a playable puzzle.
     * Saves the solution before hollowing.
     * Guarantees a minimum of 2 hints per block and unique solutions.
     * Removed positions are stored for the hint system.
     */
    public void hollower() {
        solution = copyMatrix(matrix);

        ArrayList<ArrayList<Pos>> blocks = new ArrayList<>();
        int[][] blockStarts = {{0, 0}, {0, 3}, {2, 0}, {2, 3}, {4, 0}, {4, 3}};
        for (int b = 0; b < blockStarts.length; b++) {
            ArrayList<Pos> block = new ArrayList<>();
            for (int i = blockStarts[b][0]; i < blockStarts[b][0] + 2; i++) {
                for (int j = blockStarts[b][1]; j < blockStarts[b][1] + 3; j++) {
                    block.add(new Pos(i, j));
                }
            }
            Collections.shuffle(block);
            blocks.add(block);
        }

        ArrayList<Pos> available = new ArrayList<>();
        for (int b = 0; b < blocks.size(); b++) {
            for (int k = 0; k < blocks.get(b).size(); k++) {
                available.add(blocks.get(b).get(k));
            }
        }

        int[] hintsPerBlock = {6, 6, 6, 6, 6, 6};

        int gap = 0;
        int idx = 0;

        while (gap < 20 && idx < available.size()) {
            Pos pos = available.get(idx);
            idx++;

            int blockIdx = getBlockIndex(pos.i, pos.j);
            if (hintsPerBlock[blockIdx] <= 2) {
                continue;
            }

            int saveNum = matrix.get(pos.i).get(pos.j);
            int possible = 0;
            int num = 1;
            while (num <= 6) {
                if (num != saveNum && validation(pos.i, pos.j, num)) {
                    possible++;
                }
                num++;
            }

            if (possible <= 1) {
                matrix.get(pos.i).set(pos.j, 0);
                removed.add(pos);
                hintsPerBlock[blockIdx]--;
                gap++;
            }
        }
    }

    /**
     * Prints the current game matrix to the console.
     */
    public void matrixView() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}

