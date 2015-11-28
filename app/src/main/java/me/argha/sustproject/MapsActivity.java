package me.argha.sustproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import me.argha.sustproject.utils.Util;

/**
 * Created by ASUS on 11/28/2015.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapFragment mapFragment;
    GoogleMap googleMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toast.makeText(MapsActivity.this,"BLABLA",Toast.LENGTH_SHORT).show();
        Log.i("TAG", "On create");
        mapFragment= (MapFragment)getFragmentManager().findFragmentById(R.id
                .mapFragment);
        Log.i("TAG","mapFragment is: "+mapFragment);
        mapFragment.getMapAsync(this);
        mapFragment.getMap();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap=mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        Log.i("TAG", "Map is ready");
        Util.showToast(MapsActivity.this,"Map is ready");
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
