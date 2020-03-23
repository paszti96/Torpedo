package com.torpedo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public abstract class Communication implements Runnable{
    protected Socket socket		 = null;
    protected DataInputStream in = null;
    protected DataOutputStream out	 = null;
    protected int port = 5000;

    public void send(String msg) throws Exception{
        out.writeUTF(msg);
    }
}
