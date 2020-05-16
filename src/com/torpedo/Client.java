package com.torpedo;

// A Java program for a Client

import java.net.*;
import java.io.*;

public class Client extends Communication implements Runnable {
    private Game game;
    private String address;

    public Client(Game game, String address) {
        super(game);
        this.game = game;
        this.address = address;
    }

    public void run() {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            game.connected();

            // takes input from terminal
            in = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
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
                for (MsgHandler handler : msgListeners) {
                    handler.checkHit(line);
                }
            } catch (IOException i) {
                System.out.println(i);
            }
        }
        // close the connection
//        try
//        {
//            input.close();
//            out.close();
//            socket.close();
//        }
//        catch(IOException i)
//        {
//            System.out.println(i);
//        }
    }
}