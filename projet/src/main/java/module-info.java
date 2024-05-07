module morphing {
    requires javafx.controls;
    requires javafx.fxml;

    opens morphing to javafx.fxml;
    exports morphing;
}
