package com.chat;

import com.chat.client.ChatClient;
import com.chat.client.ChatClientController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML    private ImageView DefaultView;
    @FXML    private ImageView ManView;
    @FXML    private ImageView WomanView;
    @FXML    private ChoiceBox<String> imagePicker;
    @FXML    private Label selectedPicture;
    @FXML    private TextField hostField;
    @FXML    private TextField portField;
    @FXML    private Button signInBt;
    @FXML    private TextField usernameField;
    @FXML    private Label usernameLabel;

    private Scene scene;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String username;
    public static ChatClientController controller;
    private static StartController instance;

    public StartController() {
        instance = this;
    }

    public static StartController getInstance() {
        return instance;
    }

    @FXML
    void onSignInBtClick(ActionEvent event) throws IOException {
        if (usernameField.getText().equals("") || usernameField.getText().equals("Введите имя")) {
            usernameField.getStyleClass().add("error");
            showAlert("Ошибка", "Введите имя пользователя");
        } else {
            usernameField.getStyleClass().remove("error");
            String hostname = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            username = usernameField.getText();
            String picture = selectedPicture.getText();

            signInBt.getScene().getWindow().hide();

            FXMLLoader fmxlLoader = new FXMLLoader(Start.class.getResource("ChatClientGUI.fxml"));
            Parent window = fmxlLoader.load();
            controller = fmxlLoader.<ChatClientController>getController();

            ChatClient listener = new ChatClient(hostname, port, username, picture, controller);
            Thread x = new Thread(listener);
            x.setDaemon(true);
            x.start();

            controller.setClient(listener);
            controller.setMyUsername(username);
            controller.setUsernameImage(picture);

            this.scene = new Scene(window);

            Platform.runLater(() -> {
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setWidth(790);
                stage.setHeight(550);
                stage.setTitle("Chat Application");
                Image image = new Image(Start.class.getResourceAsStream("images/chat.png"));
                stage.getIcons().add(image);
                stage.setScene(this.scene);
                stage.setMinWidth(800);
                stage.setMinHeight(300);
                stage.centerOnScreen();
                stage.show();
            });
        }
    }

    @FXML
    void onTextFieldUserName(MouseEvent event) {
        usernameField.clear();
    }

    public void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagePicker.getSelectionModel().selectFirst();
        selectedPicture.textProperty().bind(imagePicker.getSelectionModel().selectedItemProperty());
        selectedPicture.setVisible(false);

        imagePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldPicture, String newPicture) {
                if (oldPicture != null) {
                    switch (oldPicture) {
                        case "Default":
                            DefaultView.setVisible(false);
                            break;
                        case "Man":
                            ManView.setVisible(false);
                            break;
                        case "Woman":
                            WomanView.setVisible(false);
                            break;
                    }
                }
                if (newPicture != null) {
                    switch (newPicture) {
                        case "Default":
                            DefaultView.setVisible(true);
                            break;
                        case "Man":
                            ManView.setVisible(true);
                            break;
                        case "Woman":
                            WomanView.setVisible(true);
                            break;
                    }
                }
            }
        });
    }
}