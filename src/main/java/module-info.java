module com.smartboard.smartboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jetbrains.annotations;
    requires java.sql;
//    requires org.junit.jupiter.api;

    opens com.smartboard to javafx.fxml;
    exports com.smartboard.controllers;
    opens com.smartboard.controllers to javafx.fxml;

    exports com.smartboard to javafx.graphics;
}