package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WinController {

    @FXML
    private Label lblTime;

    @FXML
    private Button btnPlayAgain;

    /**
     * Recibe el tiempo final desde el SudokuController
     */
    public void setTime(String time) {
        lblTime.setText("Tiempo: " + time);
    }

    /**
     * Botón jugar de nuevo, vuelve al juego
     */
    @FXML
    public void onPlayAgain() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudoku/fxml/sudoku-game-view.fxml"));
        Parent root = loader.load();

        SudokuController controller = loader.getController();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(controller::onKeyPressed);

        Stage stage = (Stage) btnPlayAgain.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}