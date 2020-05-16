package com.torpedo;

// A Java program for a Server

import java.net.*;
import java.io.*;


public class Server extends Communication implements Runnable {
    private Game game;

    public Server(Game game) {
        super(game);

        this.game = game;
    }

    private ServerSocket server = null;

    public void run() {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();

            game.connected();

            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            line = "";
            // reads message from client until "Over" is sent
            while (true) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                    for (MsgHandler handler : msgListeners) {
                        handler.checkHit(line);
                    }

                } catch (IOException i) {
                    System.out.println(i);
                }
            }
//            System.out.println("Closing connection");
//
//            // close connection
//            socket.close();
//            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}