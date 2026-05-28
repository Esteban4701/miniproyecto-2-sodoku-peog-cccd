package com.example.sudoku.controller;

import javafx.scene.input.KeyEvent;

/**
 * Interface defining the contract for the Sudoku game controller.
 * Handles cell interaction, number selection, hints, and game state.
 */
public interface ISudokuController {

    /**
     * Initializes the game board and all components.
     */
    void initialize();

    /**
     * Handles a cell click event on the board.
     *
     * @param row row index of the clicked cell
     * @param col column index of the clicked cell
     */
    void onCellClicked(int row, int col);

    /**
     * Places a number in a cell and validates it.
     *
     * @param row    row index
     * @param col    column index
     * @param number number to place (1-6)
     */
    void placeNumber(int row, int col, int number);

    /**
     * Erases the value of an editable cell.
     *
     * @param row row index
     * @param col column index
     */
    void eraseCell(int row, int col);

    /**
     * Selects a number to place or activates the eraser.
     *
     * @param number number to select (1-6), or -1 for eraser
     */
    void selectNumber(int number);

    /**
     * Handles keyboard input for number placement and erasing.
     *
     * @param event the key event
     */
    void onKeyPressed(KeyEvent event);

    /**
     * Handles the hint button action.
     */
    void onHintRequested();

    /**
     * Handles the puzzle solved event.
     */
    void onPuzzleSolved();
}
