package se.mobilapp.isacson.anna.assignment5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private MainActivity mainActivity;
    private String host;
    private int port;
    private Socket clientSocket;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private boolean disconnected = false;
    private String lineFromServer;

    public ServerConnection(MainActivity mainActivity, String host, int port) {
        this.mainActivity = mainActivity;
        this.host = host;
        this.port = port;
    }

    public void connectToServer() {
        try {
            clientSocket = new Socket(host, port);

            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        } catch (Exception e) {
            System.err.println("Failed to connect to Server.");
        }
    }

    private void disconnectToServer() {
        disconnected = true;
        mainActivity.disconnected();
    }

    private void sendToServer(String msg) {
        toServer.println(msg);
    }

    @Override
    public void run() {
        try {
            while(!disconnected){
                lineFromServer = fromServer.readLine();
            }
        } catch (Exception e) {
            System.err.println("Failed to read from Server.");
        }
    }
}
