package com.torpedo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Communication implements Runnable{
    protected Socket socket		 = null;
    protected DataInputStream in = null;
    protected DataOutputStream out	 = null;
    protected int port = 5000;
    protected List<MsgHandler> msgListeners = new ArrayList<>();
    public String line = "";

    public void send(String msg) throws Exception{
        out.writeUTF(msg);
    }

    public void addListener(MsgHandler addThis){
        msgListeners.add(addThis);
    }

    interface MsgHandler{
        void checkHit(String msgCome);
    }
};
