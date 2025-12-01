package com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClient implements Runnable {

    private String hostname;
    private int port;
    private String username;
    private String picture;
    private ChatClientController controller;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ExecutorService executor;

    public ChatClient(String hostname, int port, String username, String picture, ChatClientController controller) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.picture = picture;
        this.controller = controller;
        this.executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Отправляем информацию о пользователе при подключении
            out.println("JOIN|" + username + "|" + picture);

            // Запускаем поток для получения сообщений
            executor.submit(this::receiveMessages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        System.out.println("Получено сообщение");
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                processMessage(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {

        String[] parts = message.split("\\|", 3);
        if (parts.length >= 3) {
            String type = parts[0];
            String sender = parts[1];
            String content = parts[2];

            if ("USER".equals(type)) {
                controller.addMessage(sender, content);
            } else if ("SYSTEM".equals(type)) {
                controller.addSystemMessage(content);
            }
        } else {
            controller.addMessage("Сервер", message);
        }
    }

    public void sendMessage(String message) {
        if (out != null && !message.trim().isEmpty()) {
            out.println("MESSAGE|" + username + "|" + message);
        }
    }

    public void disconnect() {
        try {
            if (out != null) {
                out.println("LEAVE|" + username);
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}