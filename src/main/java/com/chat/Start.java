package com.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Start extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("styles/style.css").toExternalForm());
        stage.setTitle("Chat Application");
        //stage.initStyle(StageStyle.UNDECORATED);--убирает шапку
        stage.setScene(scene);
        Image image = new Image(getClass().getResourceAsStream("image/chat.png"));
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        }
    }




