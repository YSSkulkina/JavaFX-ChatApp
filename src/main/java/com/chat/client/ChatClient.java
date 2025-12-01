package com.chat.client;

import com.chat.StartController;
import com.chat.messages.Message;
import com.chat.messages.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.chat.messages.MessageType.CONNECTED;

public class ChatClient implements Runnable {

    private String hostname;
    private int port;
    private static String username;
    private static String picture;
    private ChatClientController controller;
    private Socket socket;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    private static final String HASCONNECTED = "has connected";

    public ChatClient(String hostname, int port, String username, String picture, ChatClientController controller) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.picture = picture;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostname, port);
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            StartController.getInstance().showAlert("Ошибка", "Невозможно подключиться к серверу");
        }

        try {
            connect();

            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    switch (message.getType()) {
                        case USER:
                            controller.addMessage(message);
                            break;
                        case NOTIFICATION:
                        case SERVER:
                            String notificationText = message.getName() + " " + message.getMsg();
                            controller.addNotification(notificationText);
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            controller.logoutScene();
        }
    }

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }
}