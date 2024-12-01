module sebron4.uppgift4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens sebron4 to javafx.fxml;
    exports sebron4;
}