package se.mobilapp.isacson.anna.assignment5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ViewModelController viewmodel;
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

    public static HomeFragment newInstance() {
        HomeFragment firstFragment = new HomeFragment();
        return firstFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy.
    // either dynamically or via XML Layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment.
        View view = inflater.inflate(R.layout.fragment_homefragment, parent, false);
        viewmodel = new ViewModelController();
        return view;

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here. E.g. view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here.

        setUpUI();
    }

    private void setUpUI() {
        nameEditText = getView().findViewById(R.id.username);
        passwordEditText = getView().findViewById(R.id.password);

        loginBtn = getView().findViewById(R.id.login);
        registerBtn = getView().findViewById(R.id.register);
        connectBtn = getView().findViewById(R.id.connect);
        disconnectBtn = getView().findViewById(R.id.disconnect);
        logoutBtn = getView().findViewById(R.id.logout);

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
            viewmodel.connectToServer();
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
            connectBtn.setEnabled(true);
            loginBtn.setEnabled(false);
            logoutBtn.setEnabled(false);
            registerBtn.setEnabled(false);
        }
    }

    private void disconnectToServer() {
        viewmodel.disconnectToServer();
    }

    void sendCommand(String input) {
        final String cmd = Integer.toString(++counter) + " " + input;
        new Thread(new Runnable() {
            @Override
            public void run() {
                viewmodel.sendToServer(cmd);
            }
        }).start();
    }

    /*void log(String msg) {
        final String line = msg + "\n";
        textviewText.post(new Runnable() {
            @Override
            public void run() {
                textviewText.setText(textviewText.getText() + line);
                textviewText.invalidate();
            }
        });
    }*/

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
}
