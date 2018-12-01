package com.example.chqns022.androidassignment;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chqns022.androidassignment.ui.marker.MarkerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final LatLng SDYNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static ArrayList<com.example.chqns022.androidassignment.Marker> markerList = new ArrayList<>();
    private FirebaseDB db = new FirebaseDB();

    private Marker mPerth;
    private Marker mSdyney;
    private Marker mBrisbane;
    private DrawerLayout mDrawerLayout;

    public static void setMarkerList(ArrayList<com.example.chqns022.androidassignment.Marker> markerListCopy){
        markerList.clear();
        markerList.addAll(markerListCopy);
    }

    public static ArrayList<com.example.chqns022.androidassignment.Marker> getMarkerList(){
        return markerList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        for(int i = 0; i < markerList.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(markerList.get(i).getLocation().getLatitude(), markerList.get(i).getLocation().getLongitude()))
                    .title(markerList.get(i).getTitle())
                    .snippet("" + i)
            );
        }

        if(markerList.size() == 0) NotificationsControl.sendNotifications(this, "Getting markers error", "Oops, something went wrong. Please try again later.");

       // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        int i = Integer.parseInt(marker.getSnippet());

        MarkerFragment.setPreviousPage(1);
        MarkerFragment.setCurPos(i);
        MarkerFragment.setCurMarker(markerList.get(i));
        MarkerFragment.setIsFromMarker(true);
        MarkerFragment.setIsFromFavourites(false);

        FirebaseDB db = new FirebaseDB();
        Log.d("checkEmail", FirebaseDB.getCurrentUser().getEmail());
        db.checkIfMarkerisFavourite(MarkerFragment.getCurMarker(), FirebaseDB.getCurrentUser().getEmail(), MarkerFragment.getIsMarkerFavourite(), getApplicationContext());

        marker.showInfoWindow();

        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //int pos = Integer.parseInt(marker.getSnippet());
        marker.setSnippet("" + marker.getPosition());

        Intent intent = new Intent(MapsFragment.this, MarkerActivity.class);
        startActivity(intent);
        finish();

    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View contentsView;

        CustomInfoWindowAdapter(){
            contentsView = getLayoutInflater().inflate(R.layout.marker_fragment, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            /*TextView textView = (TextView) contentsView.findViewById(R.id.title);
            textView.setText("Hello World");

            TextView snippetView = contentsView.findViewById(R.id.snippet);
            snippetView.setText(marker.getSnippet());*/
            return contentsView;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapsFragment.this, HomeMenu.class);
        startActivity(intent);
        finish();
    }
}
