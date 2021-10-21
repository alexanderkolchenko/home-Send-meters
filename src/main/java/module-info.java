module com.example.enumerators {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.mail;
    requires java.sql;
    requires mysql.connector.java;
    requires ojdbc10;


    opens com.example.enumerators to javafx.fxml;
    exports com.example.enumerators;
}