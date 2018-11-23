package se.mobilapp.isacson.anna.assignment5;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ServerConnection serverConnection;
    private Handler handler = new Handler();
    private ListView listViewText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private Button registerBtn;
    private Button connectBtn;
    private Button disconnectBtn;
    private Button logoutBtn;

    private String username;
    // ! must be changed in the future, so the password is not in clear text !
    private String password;
    private String input;

    private int counter = 0;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
    }

    private void setUpUI() {
        listViewText = findViewById(R.id.listView);
        nameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listViewText.setAdapter(adapter);

        loginBtn = findViewById(R.id.login);
        registerBtn = findViewById(R.id.register);
        connectBtn = findViewById(R.id.connect);
        disconnectBtn = findViewById(R.id.disconnect);
        logoutBtn = findViewById(R.id.logout);

        registerBtn.setOnClickListener(this);
        registerBtn.setEnabled(false);

        loginBtn.setOnClickListener(this);
        loginBtn.setEnabled(false);

        logoutBtn.setOnClickListener(this);
        logoutBtn.setEnabled(false);

        connectBtn.setOnClickListener(this);

        disconnectBtn.setOnClickListener(this);
        disconnectBtn.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        username = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(v == connectBtn) {
            new ServerConnectionTask().execute();
            connectBtn.setEnabled(false);
            disconnectBtn.setEnabled(true);
            registerBtn.setEnabled(true);
            loginBtn.setEnabled(true);
        }
        if(v == registerBtn) {
            if(!(isValidField(username) || isValidField(password))) {
                //change this to a pop up later (or something similar).
                System.err.println("The fields needs to be a single word.");
            } else {
                input = "REGISTER " + username + " " + password;
                sendCommand(input);
            }
        }
        if(v == loginBtn) {
            if(!(isValidField(username) || isValidField(password))) {
                //change this to a pop up later (or something similar).
                System.err.println("The fields needs to be a single word.");
            } else {
                input = "LOGIN " + username + " " + password;
                sendCommand(input);
            }
            logoutBtn.setEnabled(true);
        }
        if(v == logoutBtn) {
            sendCommand("LOGOUT");
            logoutBtn.setEnabled(false);
        }

        if(v == disconnectBtn) {
            disconnectBtn.setEnabled(false);
            disconnectToServer();
        }
    }

    private void disconnectToServer() {
        serverConnection.disconnectToServer();
    }

    public void disconnected() {
        serverConnection = null;
        connectBtn.setEnabled(true);
        loginBtn.setEnabled(false);
        logoutBtn.setEnabled(false);
        registerBtn.setEnabled(false);
    }

    void sendCommand(String input) {
        final String cmd = Integer.toString(++counter) + " " + input;
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnection.sendToServer(cmd);
            }
        }).start();
    }

    void log(String msg) {
        final String line = msg + "\n";
        handler.post(new Runnable() {
            @Override
            public void run() {
                listItems.add(line);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private boolean isValidField(String field) {
        if(field.length() == 0) {
            System.err.println("The input-field cannot be empty.");
            return false;
        } else {
            String [] testString = field.split(" ");
            if(testString.length > 1) {
                System.err.println("The input-field must be one single line.");
                return false;
            }
        }
        return true;
    }

    private class ServerConnectionTask extends AsyncTask<Void, Void, ServerConnection>{

        @Override
        protected ServerConnection doInBackground(Void... _) {
            String host = getString(R.string.HOST);
            int port = Integer.valueOf(getString(R.string.PORT));
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