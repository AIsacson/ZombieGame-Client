package se.mobilapp.isacson.anna.assignment5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GameFragment extends Fragment implements OnMapReadyCallback{

    private ViewModelController viewmodel;
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleMap mMap;

    public static GameFragment newInstance() {
        GameFragment secondFragment = new GameFragment();
        return secondFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment.
        View view = inflater.inflate(R.layout.fragment_gamefragment, parent, false);
        viewmodel = new ViewModelController();
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))).setTitle("MyLocation");
                CameraPosition userLocation = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(16).bearing(0).tilt(45).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(userLocation));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for persmission
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);

            Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude()))).setTitle("MyLocation");
            CameraPosition userLocation = CameraPosition.builder().target(new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude())).zoom(16).bearing(0).tilt(45).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(userLocation));
        }
        MapsInitializer.initialize(getContext());

    }
}