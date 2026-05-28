package com.example.sudoku.view;

import com.example.sudoku.controller.SudokuController;
import com.example.sudoku.controller.WinController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Handles navigation between the different scenes of the Sudoku application.
 * Maintains a reference to the primary stage and loads FXML views on demand.
 */
public class SceneNavigator {

    /** The primary stage of the application. */
    private static Stage stage;

    /**
     * Sets the primary stage to be used for scene transitions.
     *
     * @param s the primary stage of the application
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Navigates to the main game scene.
     * Loads the sudoku-game-view.fxml and connects the keyboard handler.
     *
     * @throws Exception if the FXML file cannot be loaded
     */
    public static void goToGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                SceneNavigator.class.getResource("/com/example/sudoku/fxml/sudoku-game-view.fxml")
        );
        Parent root = loader.load();

        SudokuController controller = loader.getController();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(controller::onKeyPressed);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the win screen scene.
     * Loads the win-view.fxml and passes the final time to the controller.
     *
     * @param time the final time string in MM:SS format to display on the win screen
     * @throws Exception if the FXML file cannot be loaded
     */
    public static void goToWin(String time) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                SceneNavigator.class.getResource("/com/example/sudoku/fxml/win-view.fxml")
        );
        Parent root = loader.load();

        WinController controller = loader.getController();
        controller.setTime(time);

        stage.setScene(new Scene(root));
        stage.show();
    }
}
