package com.chat.client;


import com.chat.Start;
import com.chat.messages.Message;
import com.chat.messages.MessageType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatClientController implements Initializable {

    @FXML    private ListView<Message> chatListView;
    @FXML    private TextField messageField;
    @FXML    private Button sendButton;
    @FXML    private ImageView usernameImage;
    @FXML    private Label usernameLabel;

    private ChatClient client;
    private String myUsername;

    @FXML
    void onSendMessageButtonClick(ActionEvent event) throws IOException {
        sendMessage();
    }

    @FXML
    private void sendMessage(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    private void sendMessage() throws IOException {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && client != null) {
            client.send(message);
            messageField.clear();
        }
    }

    public void setClient(ChatClient client) {
        this.client = client;
    }

    public void addMessage(Message message) {
        Platform.runLater(() -> {
            chatListView.getItems().add(message);
            chatListView.scrollTo(chatListView.getItems().size() - 1);
        });
    }

    public void addNotification(String text) {
        Message notificationMessage = new Message();
        notificationMessage.setName("System");
        notificationMessage.setMsg(text);
        notificationMessage.setType(MessageType.SERVER);

        Platform.runLater(() -> {
            chatListView.getItems().add(notificationMessage);
            chatListView.scrollTo(chatListView.getItems().size() - 1);
        });
    }

    public void setMyUsername(String username) {
        this.myUsername = username;
        usernameLabel.setText(username);

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
                this.usernameImage.setImage(new Image(Start.class.getResource("images/woman.png").toString()));
                break;
        }
    }

    public void logoutScene() {
        Platform.runLater(() -> {
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("StartPage.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = Start.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new MessageCell();
            }
        });
    }

    private class MessageCell extends ListCell<Message> {

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox root = new HBox();
                root.setPadding(new Insets(5, 5, 5, 10));

                Text name = new Text(message.getName() + ": ");
                Text msg = new Text(message.getMsg());
                TextFlow textFlow = new TextFlow(name, msg);
                textFlow.setPadding(new Insets(8, 12, 8, 12));

                // СИСТЕМНОЕ СООБЩЕНИЕ
                if (message.getType() != MessageType.USER) {
                    root.setAlignment(Pos.CENTER);
                    name.setText(message.getMsg());
                    name.setFill(Color.GRAY);
                    textFlow.getChildren().setAll(name);
                    textFlow.setStyle("-fx-background-color: #e1e1e1; -fx-background-radius: 10;");

                    // МОЕ СООБЩЕНИЕ
                } else if (message.getName().equals(myUsername)) {
                    root.setAlignment(Pos.CENTER_RIGHT);
                    name.setFill(Color.WHITE);
                    msg.setFill(Color.WHITE);
                    textFlow.setStyle("-fx-background-color: #3D7F44; -fx-background-radius: 10;");

                    // ЧУЖОЕ СООБЩЕНИЕ
                } else {
                    root.setAlignment(Pos.CENTER_LEFT);
                    name.setFill(Color.BLACK);
                    msg.setFill(Color.BLACK);
                    textFlow.setStyle("-fx-background-color: #dcdcdc; -fx-background-radius: 10;");
                }

                root.getChildren().add(textFlow);
                setGraphic(root);
            }
        }
    }
}