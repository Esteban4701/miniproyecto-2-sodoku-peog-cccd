package com.example.sudoku.controller;

import com.example.sudoku.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class WinController {

    @FXML
    private Label lblTime;

    @FXML
    private Button btnPlayAgain;

    public void setTime(String time) {
        lblTime.setText("time: " + time);
    }

    @FXML
    public void onPlayAgain() throws Exception {
        SceneNavigator.goToGame();
    }
}