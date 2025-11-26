package com.chat;

import com.chat.animation.Shake;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML    private ImageView DefaultView;
    @FXML    private ImageView ManView;
    @FXML    private ImageView WomanView;
    @FXML private ChoiceBox imagePicker;
    @FXML private Label selectedPicture;
    @FXML    private TextField hostField;
    @FXML    private TextField portField;
    @FXML    private Button signInBt;
    @FXML    private TextField usernameField;
    @FXML    private Label usernameLabel;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String username;

    @FXML
    void onSignInBtClick(ActionEvent event) {
    if(usernameField.getText().equals("")||usernameField.getText().equals("Введите имя")) {
        usernameField.getStyleClass().add("error");
        showAlert("Ошибка", "Введите имя пользователя");
    }else{
        usernameField.getStyleClass().remove("error");
        signInBt.getScene().getWindow().hide();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("ChatClientGUI.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root=loader.getRoot();
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    }

    @FXML
    void onTextFieldUserName(MouseEvent event) {
        usernameField.clear();

    }
    private void connectToServer() {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        username = usernameField.getText();

        if (username.isEmpty()) {
            showAlert("Ошибка", "Введите имя пользователя");
            return;
        }

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Отправляем имя пользователя
            out.println("/login " + username);

            showAlert("Успешно","Подключение к серверу " + host + ":" + port + " успешно\n");
        } catch (IOException e) {
            showAlert("Ошибка подключения", "Не удалось подключиться к серверу: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
