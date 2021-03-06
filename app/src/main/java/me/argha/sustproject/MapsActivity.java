package me.argha.sustproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;

import me.argha.sustproject.utils.Util;

/**
 * Created by ASUS on 11/28/2015.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    int intensity;
    ArrayList<WeightedLatLng> weightedData;
    HeatmapTileProvider heatmapTileProvider;
    TileOverlay overlay;

    MapFragment mapFragment;
    GoogleMap googleMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        Toast.makeText(MapsActivity.this,"BLABLA",Toast.LENGTH_SHORT).show();
        Log.i("TAG", "On create");
        intensity=40;
        weightedData=new ArrayList<WeightedLatLng>();


        mapFragment= (MapFragment)getFragmentManager().findFragmentById(R.id
                .mapFragment);
        Log.i("TAG", "mapFragment is: " + mapFragment);
        mapFragment.getMapAsync(this);
        setData();
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        this.googleMap=mapFragment.getMap();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.60, 90.45), 8));
        googleMap.setMyLocationEnabled(false);
        Log.i("TAG", "Map is ready");
        Util.showToast(MapsActivity.this, "Map is ready");
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addHeatMap(latLng);
            }
        });
        addHeatMap(new LatLng(23.598840, 90.390541938938));
        addHeatMap(new LatLng(23.598841, 90.3905438778));
        addHeatMap(new LatLng(23.598842, 90.390541));
        addHeatMap(new LatLng(23.598843, 90.390541));
        addHeatMap(new LatLng(23.598844, 90.390540));

        addHeatMap(new LatLng(22.598840, 90.2390541938938));
        addHeatMap(new LatLng(22.598841, 90.23905438778));
    }

    private void addHeatMap(LatLng...locs) {
//        int intensity=10;

        for(LatLng i:locs){
            weightedData.add(new WeightedLatLng(i, intensity));
//            intensity--;
        }
        // Get the data: latitude/longitude positions of police stations.

        setData();
    }

    private void setData() {
        if(weightedData.size()<=0)
            return;
        if(heatmapTileProvider==null){

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            heatmapTileProvider = new HeatmapTileProvider.Builder()
                    .weightedData(weightedData)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            overlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider
                    (heatmapTileProvider));
//            overlay.clearTileCache();
        }
        else{
            heatmapTileProvider.setWeightedData(weightedData);
            heatmapTileProvider.setRadius(40);
            overlay.clearTileCache();
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        if (resultCode == ConnectionResult.SUCCESS){
//            Toast.makeText(getApplicationContext(),
//                    "isGooglePlayServicesAvailable SUCCESS",
//                    Toast.LENGTH_LONG).show();
//        }else{
//            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
//        }

    }
}
