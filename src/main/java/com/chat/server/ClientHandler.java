package com.chat.server;

import com.chat.messages.Message;
import com.chat.messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String username;
    private String picture;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());


            Message connectionMessage = (Message) ois.readObject();

            if (connectionMessage.getType() == MessageType.CONNECTED) {
                this.username = connectionMessage.getName();
                this.picture = connectionMessage.getPicture();
                System.out.println(username + " подключился к чату.");

                Message notification = new Message();
                notification.setName(this.username);
                notification.setType(MessageType.NOTIFICATION);
                notification.setMsg("присоединился к чату");
                ChatServer.broadcastMessage(notification, this);
            } else {
                return;
            }


             while (socket.isConnected()) {
                Message clientMessage = (Message) ois.readObject();
                if (clientMessage != null) {
                    ChatServer.broadcastMessage(clientMessage, this);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Клиент " + (username != null ? username : "") + " отключился.");
        } finally {
            ChatServer.removeClient(this);

            if (username != null) {
                Message leaveMessage = new Message();
                leaveMessage.setName(username);
                leaveMessage.setType(MessageType.SERVER);
                leaveMessage.setMsg("покинул чат.");
                ChatServer.broadcastMessage(leaveMessage, this);
            }

            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (socket != null) socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
