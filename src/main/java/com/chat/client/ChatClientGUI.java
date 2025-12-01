package com.chat.client;

import com.chat.Start;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatClientGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("ChatClientGUI.fxml"));
        Scene clientScene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat Application");
        stage.setScene(clientScene);
        Image image = new Image(Start.class.getResourceAsStream("images/chat.png"));
        stage.getIcons().add(image);

        stage.show();
    }

    public static void main(String[] args) {
         launch();

    }
}

