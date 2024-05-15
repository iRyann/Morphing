module morphing {
    requires transitive java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.squareup.gifencoder; 

    opens morphing to javafx.fxml;
    exports morphing;
}
