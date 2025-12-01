package com.chat.client;


import com.chat.Start;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatClientController {

    @FXML    private TextArea chatArea;
    @FXML    private TextField messageField;
    @FXML    private Button sendButton;
    @FXML    private ImageView usernameImage;
    @FXML    private Label usernameLabel;

    private ChatClient client;

    @FXML
    void onSendMessageButtonClick(ActionEvent event) {
        sendMessage();
    }
    @FXML
    private void sendMessage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && client != null) {
            client.sendMessage(message);
            messageField.clear();
        }
    }
    public void setClient(ChatClient client) {
        this.client = client;
    }

    public void addMessage(String sender, String content) {
        Platform.runLater(() -> {
                String message = sender + ": " + content;
                chatArea.appendText(message + "\n");
        });
    }

    public void addSystemMessage(String content) {
        Platform.runLater(() -> {
            if (chatArea != null) {
                chatArea.appendText("[Система] " + content + "\n");
            }
        });
    }

    public void setUsername(String username) {
        Platform.runLater(() -> {
            usernameLabel.setText(username);
        });

    }

    public void setUsernameImage(String selectedPicture) {
        switch (selectedPicture) {
            case "Default":
                this.usernameImage.setImage(new Image(Start.class.getResource("images/default.png").toString()));
                break;
            case "Man":
                this.usernameImage.setImage(new Image(Start.class.getResource("images/man.png").toString()));
                break;
            case "Woman":
                this.usernameImage.setImage(new Image(String.valueOf(Start.class.getResource("images/woman.png"))));
                break;
        }
    }

    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }
}