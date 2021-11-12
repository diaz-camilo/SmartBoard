package com.smartboard;

import com.smartboard.Utils.DBManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Testing
//        extends javafx.application.Application
{
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainApplication.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("SmartBoard");
//        stage.setScene(scene);
////        stage.setResizable(false);
//        stage.show();
//    }


    public static void main(String[] args) throws IOException {

//        Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        System.out.println(date);
//        String dateString = date.toString();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM h:mm aa");
//        System.out.println(sdf.format(date));
//        launch();


        DBManager.addWorkspace("cam");
    }
}