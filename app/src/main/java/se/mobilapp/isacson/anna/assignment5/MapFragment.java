package se.mobilapp.isacson.anna.assignment5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

    private ViewModelController viewmodel;

    public static MapFragment newInstance() {
        MapFragment secondFragment = new MapFragment();
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment.
        View view = inflater.inflate(R.layout.fragment_mapfragment, parent, false);
        viewmodel = new ViewModelController();
        return view;

    }

}
