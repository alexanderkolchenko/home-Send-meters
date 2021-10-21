package com.example.enumerators;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Приложение с графическим интерфейсом для отправки показаний бытовых счетчиков в УК
 * и сохранения их значений в базе данных
 *
 * @author Alexander Kolchenko
 * @version 1.00 2021-20-17
 */
public class MetersApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MetersApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Счетчики");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}