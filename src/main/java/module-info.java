module snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.sql;


    opens snakegame to javafx.fxml;
    exports snakegame;
    exports snakegame.controllers;
    opens snakegame.controllers to javafx.fxml;
}

