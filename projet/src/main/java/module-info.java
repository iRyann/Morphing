module morphing {
    requires transitive java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    opens morphing to javafx.fxml;
    exports morphing;
}
