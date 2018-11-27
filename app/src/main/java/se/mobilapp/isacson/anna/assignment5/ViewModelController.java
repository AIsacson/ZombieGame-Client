package se.mobilapp.isacson.anna.assignment5;

import android.os.AsyncTask;

public class ViewModelController {

    private ServerConnection serverConnection;
    private HomeFragment fragment = new HomeFragment();

    void disconnectToServer() {
        serverConnection.disconnectToServer();
    }

    void disconnected() {
        serverConnection = null;
    }

    void connectToServer() {
        new ServerConnectionTask().execute();
    }

    void sendToServer(String msg) {
        serverConnection.sendToServer(msg);
    }

    void log(String msg) {
        System.out.println("-->log: " + msg);
        //fragment.log(msg);
    }


    private class ServerConnectionTask extends AsyncTask<Void, Void, ServerConnection> {
        private static final String HOST = "localhost";
        private static final int PORT = 2002;

        @Override
        protected ServerConnection doInBackground(Void[] Objects) {
            serverConnection = new ServerConnection(ViewModelController.this, HOST, PORT);
            serverConnection.connectToServer();
            return serverConnection;
        }

        @Override
        protected void onPostExecute(ServerConnection serverConn) {
            serverConnection = serverConn;

            new Thread(serverConnection).start();
        }
    }


}
