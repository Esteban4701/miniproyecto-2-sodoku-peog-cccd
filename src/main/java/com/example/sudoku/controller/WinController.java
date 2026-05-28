package com.example.sudoku.controller;

import com.example.sudoku.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

/**
 * Controller for the win screen view.
 * Handles the display of the final time and navigation after winning.
 */
public class WinController implements IWinController {

    @FXML
    private Label lblTime;

    @FXML
    private Button btnPlayAgain;

    /**
     * Sets the final time to display on the win screen.
     *
     * @param time the final time string in MM:SS format
     */
    @Override
    public void setTime(String time) {
        lblTime.setText("⏱" + time);
    }

    /**
     * Handles the play again button action.
     * Navigates back to the game screen.
     *
     * @throws Exception if the scene cannot be loaded
     */
    @Override
    @FXML
    public void onPlayAgain() throws Exception {
        SceneNavigator.goToGame();
    }
}