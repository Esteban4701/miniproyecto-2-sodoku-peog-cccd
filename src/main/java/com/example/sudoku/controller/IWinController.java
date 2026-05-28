package com.example.sudoku.controller;

/**
 * Interface defining the contract for the win screen controller.
 * Handles the display of the final time and navigation after winning.
 */
public interface IWinController {

    /**
     * Sets the final time to display on the win screen.
     *
     * @param time the final time string in MM:SS format
     */
    void setTime(String time);

    /**
     * Handles the play again button action.
     * Navigates back to the game screen.
     *
     * @throws Exception if the scene cannot be loaded
     */
    void onPlayAgain() throws Exception;
}