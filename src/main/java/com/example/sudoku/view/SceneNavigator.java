package com.example.sudoku.view;

import com.example.sudoku.controller.SudokuController;
import com.example.sudoku.controller.WinController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

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
