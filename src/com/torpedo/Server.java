package com.torpedo;

// A Java program for a Server

import java.net.*;
import java.io.*;


public class Server extends Communication implements Runnable {
    private Game game;

    public Server(Game game,String IP) throws Exception {
        super(game);
        this.game = game;
        this.IP =InetAddress.getByName(IP);
        this.server = new ServerSocket(port,50, this.IP);
        }

    private InetAddress IP = null;
    private ServerSocket server = null;

    public void run() {
        // starts server and waits for a connection
        try {

            System.out.println("Server started on: " + IP + " : " + port);

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

                    String[] message = line.split(":");
                    if(message[0].equals("hit")) {
                        game.checkHit(message[1]);
                    }else {
                        game.fireResult(message[1]);
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