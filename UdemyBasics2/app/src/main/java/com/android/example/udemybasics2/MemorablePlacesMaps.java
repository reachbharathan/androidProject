package com.android.example.udemybasics2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MemorablePlacesMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationListener locationListener;
    LocationManager locationManager;
    int position;
    SharedPreferences memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorable_places_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent extras = getIntent();

        Bundle value = extras.getExtras();

        if (getIntent().hasExtra("Location")) {

            position = Integer.parseInt(value.getString("Location","0"));

        }

    }

    public void positionToPlace(LatLng place) {

            try {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> locationAddress = geocoder.getFromLocation(place.latitude, place.longitude, 1);
                String title = locationAddress.get(0).getAddressLine(0);
                mMap.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,10));

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void getUserLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                positionToPlace(currentLocation);

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);

        }


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

        memory = this.getSharedPreferences("com.android.example.udemybasics2",Context.MODE_PRIVATE);

        if ( position == 0 ) {

            getUserLocation();

        } else {


            LatLng goToPlace = new LatLng(Double.parseDouble(MemorablePlaces.latitude.get(position)), Double.parseDouble(MemorablePlaces.longitude.get(position)));
            positionToPlace(goToPlace);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    Toast.makeText(getApplicationContext(),"Location Saved",Toast.LENGTH_SHORT).show();

                    MemorablePlaces.latitude.add(String.valueOf(latLng.latitude));
                    MemorablePlaces.longitude.add(String.valueOf(latLng.longitude));

                    MemorablePlaces.locationAddress.add(String.valueOf(addresses.get(0).getAddressLine(0)));

                    memory.edit().putString("locationAddress", ObjectSerializer.serialize(MemorablePlaces.locationAddress)).apply();

                    memory.edit().putString("latitude",ObjectSerializer.serialize(MemorablePlaces.latitude)).apply();
                    memory.edit().putString("longitude",ObjectSerializer.serialize(MemorablePlaces.longitude)).apply();

                    MemorablePlaces.arrayAdapter.notifyDataSetChanged();

                    MemorablePlaces.list.setAdapter(MemorablePlaces.arrayAdapter);

                    positionToPlace(latLng);



                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);

        }

    }

}
