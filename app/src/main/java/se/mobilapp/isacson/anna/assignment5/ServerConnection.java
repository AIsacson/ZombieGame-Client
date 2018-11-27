package se.mobilapp.isacson.anna.assignment5;

import android.arch.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private ViewModelController viewModel;
    private String host;
    private int port;
    private Socket clientSocket;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private boolean disconnected = false;
    private String lineFromServer;

    public ServerConnection(ViewModelController viewModel, String host, int port) {
        this.viewModel = viewModel;
        this.host = host;
        this.port = port;
    }

    public void connectToServer() {
        try {
            viewModel.log("Kopplar upp sig mot servern...");
            clientSocket = new Socket(host, port);
            viewModel.log("Uppkopplad.");

            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
        } catch (Exception e) {
            System.err.println("Failed to connect to Server.");
        }
    }

    void disconnectToServer() {
        disconnected = true;
        viewModel.disconnected();
    }

    void sendToServer(String msg) {
        toServer.println(msg);
    }

    @Override
    public void run() {
        try {
            while(!disconnected){
                lineFromServer = fromServer.readLine();
                viewModel.log("From server: " + lineFromServer);
            }
            toServer.close();
            fromServer.close();
            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Failed to read from Server.");
        }
    }
}