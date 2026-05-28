module org.example.sodoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudoku to javafx.fxml;
    opens com.example.sudoku.controller to javafx.fxml;

    exports com.example.sudoku;
    exports com.example.sudoku.controller;
}