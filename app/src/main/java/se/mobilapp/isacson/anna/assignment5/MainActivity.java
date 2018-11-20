package se.mobilapp.isacson.anna.assignment5;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ServerConnection serverConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void disconnected() {
        serverConnection = null;
    }

    private class ServerConnectionTask extends AsyncTask<Void, Void, ServerConnection> {

        @Override
        protected ServerConnection doInBackground(Void... voids) {
            String host = getString(R.string.host);
            int port = Integer.valueOf(getString(R.string.port));

            serverConnection = new ServerConnection(MainActivity.this, host, port);
            serverConnection.connectToServer();

            return serverConnection;
        }

        @Override
        protected void onPostExecute(ServerConnection serverConnection) {
            MainActivity.this.serverConnection = serverConnection;

            new Thread(serverConnection).start();
        }
    }
}
