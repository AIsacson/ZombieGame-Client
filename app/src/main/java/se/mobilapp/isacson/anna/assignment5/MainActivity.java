package se.mobilapp.isacson.anna.assignment5;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ServerConnection serverConnection;
    private Handler handler = new Handler();
    private TextView textView;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private Button registerBtn;
    private Button connectBtn;
    private Button disconnectBtn;

    private String username;
    // ! must be changed in the future, so the password is not in clear text !
    private String password;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
    }

    private void setUpUI() {
        textView = (TextView) findViewById(R.id.textView);
        nameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        loginBtn = (Button) findViewById(R.id.login);
        registerBtn = (Button) findViewById(R.id.register);
        connectBtn = (Button) findViewById(R.id.connect);
        disconnectBtn = (Button) findViewById(R.id.disconnect);

        registerBtn.setOnClickListener(this);
        registerBtn.setEnabled(false);

        loginBtn.setOnClickListener(this);
        loginBtn.setEnabled(false);

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
            sendRightCommand("REGISTER " + username + " " + password);
        }

        if(v == loginBtn) {
            sendRightCommand("LOGIN " + username + " " + password);
        }
    }

    public void disconnected() {
        serverConnection = null;
    }

    public void sendCommand(String input) {
        final String cmd = Integer.toString(counter+1) + " " + input;
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnection.sendToServer(cmd);
            }
        }).start();
    }

    void sendRightCommand(String cmd) {
        String command = "";
        String [] commandSplitter = cmd.split(" ");
        if(isValidField(username) && isValidField(password)){
            switch(commandSplitter[0]) {
                case "REGISTER":
                    command = "REGISTER " + commandSplitter[1] + " " + commandSplitter[2];
                    sendCommand(command);
                    break;
                case "LOGIN":
                    command = "LOGIN " + commandSplitter[1] + " " + commandSplitter[2];
                    sendCommand(command);
                    break;
                default:
                    break;
            }
        } else {
            System.err.println("Invalid input-fields.");
        }
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

    void log(String msg) {
        final String line = msg + "\n";
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText() + line);
                textView.invalidate();
            }
        });
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
