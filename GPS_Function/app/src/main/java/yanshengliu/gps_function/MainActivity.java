package yanshengliu.gps_function;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView, locationTextView, realTimeLocationTextView;
    private Button statusBtn, locationBtn, realTimeLocationBtn;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusBtn = (Button) findViewById(R.id.statusBtn);
        locationBtn = (Button) findViewById(R.id.locationBtn);
        realTimeLocationBtn = (Button) findViewById(R.id.realTimeLocationBtn);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        realTimeLocationTextView = (TextView) findViewById(R.id.realTimeLocationTextView);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//########################end real time location
    }
    @Override
    protected void onResume(){
        super.onResume();
        //#################################GPS Status###################################
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    statusTextView.setText("GPS online!");
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("GPS Setting")
                            .setPositiveButton("Setting",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            for (int j = 0; j < 10; j++) {
                                                System.out.println("Test!!!!!!!!");
                                            }
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, 0);
                                        }
                                    }).setNegativeButton("No", null).create()
                            .show();
                }
            }
        });
//#################################END GPS STATUS###############################
//#########################################location
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";
                Criteria criteria = new Criteria();
                criteria.setAltitudeRequired(true);
                criteria.setBearingRequired(true);
                criteria.setCostAllowed(false);
                criteria.setPowerRequirement(Criteria.POWER_LOW);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                while(location==null){
//                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                }
                if (location==null){
                    locationTextView.setText("There is no recorded last location!");
                }
                else{
                    String provider = mLocationManager.getBestProvider(criteria, true);
                    //Location location=mLocationManager.getLastKnownLocation(provider);
                    String latitude = "" + location.getLatitude();
                    String longitude = "" + location.getLongitude();
                    String time = "" + location.getTime();
                    String accuracy = "" + location.getAccuracy();
                    String speed = "" + location.getSpeed();
                    msg = "Date/Time: " + time + "\n" + "Provider: " + provider + "\n" + "Accuracy: " + accuracy + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Speed: " + speed + "\n";
                    locationTextView.setText(msg);
                }
            }
        });
//#########################################end location
//########################real time location
        realTimeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realTimeLocationTextView.setText("1111111111111111");

                List providerName = mLocationManager.getAllProviders();
                for (int j = 0; j < 10; j++) {
                    System.out.println("Test~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~real time location");
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        updateView(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        realTimeLocationTextView.setText("Searching``````````\n Please wait.\n on StatusChanged!!!!");
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        updateView(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        updateView(null);
                    }
                });


            }

            private void updateView(Location location) {
                if(location!=null){
                    String msg="";
                    String latitude = "" + location.getLatitude();
                    String longitude = "" + location.getLongitude();
                    String time = "" + location.getTime();
                    String accuracy = "" + location.getAccuracy();
                    String speed = "" + location.getSpeed();
                    msg = "Date/Time: " + time + "\n"  + "Accuracy: " + accuracy + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Speed: " + speed + "\n";
                    realTimeLocationTextView.setText(msg);
                }
                else{
                    realTimeLocationTextView.setText("No MSG");
                }
            }
        });
    }




}
