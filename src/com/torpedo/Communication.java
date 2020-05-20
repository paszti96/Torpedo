package com.torpedo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Communication implements Runnable {
    protected Socket socket = null;
    protected DataInputStream in = null;
    protected DataOutputStream out = null;
    protected int port = 5000;
    protected List<MsgHandler> msgListeners = new ArrayList<>();
    public String line = "";
    private Game game;

    public Communication(Game game) {
        this.game = game;
    }

    public void send(String msg){
        try {
            out.writeUTF(msg);
        }catch (Exception e)
        {
            System.out.println("Game Disconnected");
        }
    }

    public void addListener(MsgHandler addThis) {
        msgListeners.add(addThis);
    }

    public void fire(int x, int y) {
        String message = "hit:"+Integer.toString(x) + ',' + Integer.toString(y);
        send(message);


    }

    public void fireResult(int x, int y, boolean hit, boolean destroyed, boolean allDestroyed) {
        String message =
                String.format("result:%d,%d,%b,%b,%b",x,y,hit,destroyed,allDestroyed);
        send(message);
        //game.fireResult(x,y,hit,destroyed,allDestroyed);
    }

    interface MsgHandler {
        void checkHit(String msgCome);
    }
};
