package com.vokasi.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1 ;
    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment)
        getSupportFragmentManager().findFragmentById(R.id.map);

//        SupportMapFragment.newInstance();
//        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

//        FragmentManager :
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, mapFragment).commit();


        mapFragment.getMapAsync(this);


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


        LatLng jogja = new LatLng(-7.797, 110.370);
        mMap.addMarker(new MarkerOptions().position(jogja).title("Marker in Yogyakarta").snippet("Iam here!"));
        float zoom=13;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jogja,zoom));


        GroundOverlayOptions homeOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
                .position(jogja,100);
        mMap.addGroundOverlay(homeOverlay);


        setMapLongClick(mMap);
        setMapStyle(mMap);
        setPoiClick(mMap);
//        enableMyLocation(mMap);
//        setInfoWindowClickToPanorama(mMap);
    }

    private void setMapStyle(GoogleMap map) {
        try {

            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

//    private void enableMyLocation(GoogleMap mMap) {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]
//                            {Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION_PERMISSION);
//        }
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
//
//        switch (requestCode) {
//            case REQUEST_LOCATION_PERMISSION:
//                if (grantResults.length > 0
//                        && grantResults[0]
//                        == PackageManager.PERMISSION_GRANTED) {
//                    enableMyLocation(mMap);
//                    break;
//                }
//        }
//    }


    private void setMapLongClick (final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat :%1$.5f, Long : %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Dropped Pin")
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        });
    }

    private void setPoiClick (final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                .position(pointOfInterest.latLng)
                .title(pointOfInterest.name));

                poiMarker.showInfoWindow();
//                poiMarker.setTag("poi");

            }
        });
    }

//    private void setInfoWindowClickToPanorama(GoogleMap map) {
//        map.setOnInfoWindowClickListener(
//                new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker marker) {
//                        if (marker.getTag() == "poi") {
//                            StreetViewPanoramaOptions options =
//                                    new StreetViewPanoramaOptions().position(
//                                            marker.getPosition());
//
//                            SupportStreetViewPanoramaFragment streetViewFragment
//                                    = SupportStreetViewPanoramaFragment
//                                    .newInstance(options);
//
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.fragment_container,
//                                            streetViewFragment)
//                                    .addToBackStack(null).commit();
//
//                        }
//                    }
//                });
    }





