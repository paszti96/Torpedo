package com.torpedo;

// A Java program for a Client

import java.net.*;
import java.io.*;

public class Client extends Communication implements Runnable {
    private Game game;
    private String address;

    public Client(Game game, String address) throws Exception {
        super(game);
        this.game = game;
        this.address = address;
        this.socket = new Socket(address, port);
    }

    public void run() {
        // establish a connection
        try {

            System.out.println("Connected on "+ address + " : "+port );

            game.connected();

            // takes input from terminal
            in = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
         } catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from input
        line = "";

        // keep reading until "Over" is input
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
    }
}