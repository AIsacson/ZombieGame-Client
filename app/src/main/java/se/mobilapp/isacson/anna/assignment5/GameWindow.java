package se.mobilapp.isacson.anna.assignment5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameWindow extends Commands implements View.OnClickListener {
    private Button logoutBtn;
    private TextView textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamewindow);
        setupUI();
    }

    private void setupUI() {
        logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(this);
        textview = findViewById(R.id.textView2);
    }

    @Override
    public void onClick(View v) {
        if(v == logoutBtn) {
        }
    }
}
