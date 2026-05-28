package com.example.sudoku;

import com.example.sudoku.controller.SudokuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/sudoku-game-view.fxml"));
        Parent root = fxmlLoader.load();

        SudokuController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(controller::onKeyPressed);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }
}
