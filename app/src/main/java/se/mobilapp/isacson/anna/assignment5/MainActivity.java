package se.mobilapp.isacson.anna.assignment5;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SendData{

    private HomeFragment firstFragment;
    private GameFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            firstFragment = HomeFragment.newInstance();
            secondFragment = GameFragment.newInstance();
            ft.add(R.id.your_placeholder, firstFragment).commit();
        }
    }

    protected void displayFirstFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(firstFragment.isAdded()) {
            ft.show(firstFragment);
        } else {
            ft.add(R.id.your_placeholder, firstFragment);
        }
        if(secondFragment.isAdded()) {
            ft.hide(secondFragment);
        }
        ft.commit();
    }

    protected void displaySecondFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(secondFragment.isAdded()) {
            ft.show(secondFragment);
        } else {
            ft.add(R.id.your_placeholder, secondFragment);
        }
        if(firstFragment.isAdded()) {
            ft.hide(firstFragment);
        }
        ft.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_setup:
                    displayFirstFragment();
                    return true;
                case R.id.navigation_map:
                    displaySecondFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void sendData(String msg) {
        firstFragment.sendCommand(msg);
    }
}
