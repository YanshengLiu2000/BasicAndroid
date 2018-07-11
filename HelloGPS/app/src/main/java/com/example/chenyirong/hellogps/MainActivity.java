package com.example.chenyirong.hellogps;

import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 4000; // 4 second
    Location location; // location
    TextView location_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button status_btn = (Button) findViewById(R.id.status);
        final Button location_btn = (Button) findViewById(R.id.location);
        final TextView active = (TextView) findViewById(R.id.active);
        location_btn.setEnabled(false);
        location_text = (TextView) findViewById(R.id.locationdetail);
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);

        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting GPS status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled) {
                    active.setText("GPS is enabled");
                    location_btn.setEnabled(true);
                    location_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            LocationListener locationListener= new LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {
                                    updateView(location);
                                }

                                @Override
                                public void onStatusChanged(String s, int i, Bundle bundle) {

                                }

                                @Override
                                public void onProviderEnabled(String s) {

                                }

                                @Override
                                public void onProviderDisabled(String s) {

                                }
                            };


                            checkPermission("android.permission.ACCESS_FINE_LOCATION", Binder.getCallingPid(), Binder.getCallingUid());
                            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                            long time = new Date().getTime();
                            Date date = new Date(time);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String textofDate = sdf.format(date);

                            //System.out.println(textofDate);

                            //System.out.println("经度："+location.getLongitude());
                            String provider = location.getProvider();
                            float accuracy = location.getAccuracy();
                            double altitude = location.getAltitude();
                            double latitude = location.getLatitude();
                            float speed = location.getSpeed();

                            location_text.setText("Date/Time: "+textofDate+" "+"\n" +
                            "Provider: "+provider+"\n"+
                            "Accuracy: "+Float.toString(accuracy)+"\n"+
                            "Altitude: "+Double.toString(altitude)+"\n"+
                            "Latitude: "+Double.toString(latitude)+"\n"+
                            "Speed: "+Float.toString(speed)+"\n");

                            updateView(location);
                            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        }
                    });
                }
                else {
                    active.setText("GPS is not enabled");
                    AlertDialog.Builder  setAD= new AlertDialog.Builder(MainActivity.this);
                    setAD.setTitle("Setting the GPS");
                    setAD.setMessage("GPS is not enabled. Do you want to go to settings menu?");
                    setAD.setCancelable(false);
                    setAD.setPositiveButton("Setting",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent1);
                                }
                            });
                    setAD.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    setAD.create().show();
                }
            }
        });



    }

    private void updateView(Location location){
        if(location!=null){
            long time = new Date().getTime();
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String textofDate = sdf.format(date);
            Toast.makeText(MainActivity.this,"At time: "+textofDate+ "\nProvider: "+location.getProvider()+"\nLongitude：\n" +
                            String.valueOf(location.getLongitude())+"\nLatitude：\n"+
                                            String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
            //location_text.setText("Set location\n\nLongitude：");
            //location_text.append(String.valueOf(location.getLongitude()));
            //location_text.append("\nLatitude：");
            //location_text.append(String.valueOf(location.getLatitude()));
        }
        else{
            //清空EditText对象
            //location_text.getEditableText().clear();
            Toast.makeText(MainActivity.this, "no location", Toast.LENGTH_LONG).show();
        }
    }

}
