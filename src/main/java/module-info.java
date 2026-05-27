module org.example.sodoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires jama;


    opens org.example.sodoku to javafx.fxml;
    exports org.example.sodoku;
}