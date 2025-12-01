package com.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {

    private static Stage primaryStage;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("StartPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat Application");
        stage.setScene(scene);
        Image image = new Image(getClass().getResourceAsStream("images/chat.png"));
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}




