package com.example.sudoku;

import com.example.sudoku.controller.SudokuController;
import com.example.sudoku.view.SceneNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point of the Sudoku application.
 * Initializes the JavaFX stage and loads the main game scene.
 */
public class Main extends Application {

    /**
     * Application entry point.
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     * Sets up the primary stage, loads the initial game scene,
     * and connects the keyboard event handler.
     *
     * @param primaryStage the primary stage of the application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneNavigator.setStage(primaryStage);

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