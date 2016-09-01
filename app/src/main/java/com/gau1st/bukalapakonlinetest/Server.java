package com.gau1st.bukalapakonlinetest;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class Server extends Thread {
    public interface OnReadListener {
        void onRead(Server server, String response);
    }

    Socket socket;
    String address;
    int port;
    OnReadListener listener = null;

    public Server(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(InetAddress.getByName(address), port);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter writer = new PrintWriter(bw, true);
            writer.println("*99*1##");
            writer.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if (listener != null) {
                    listener.onRead(this, line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hhh", String.valueOf(e.getMessage()));
        }
    }

    public void setListener(OnReadListener listener) {
        this.listener = listener;
    }
}
