package yanshengliu.gpsfunctioncheck;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView, realTimeTextView;
    private LocationManager mLocationManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            statusTextView.setText("No works");
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        statusTextView = (TextView) findViewById(R.id.statusTextView);
        realTimeTextView = (TextView) findViewById(R.id.realTimeTextView);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        statusTextView.setText("works");
        if (location == null) {
            statusTextView.setText("zhazha there's no last location!");
        } else {
            String provider=location.getProvider();
            String latitude = "" + location.getLatitude();
            String longitude = "" + location.getLongitude();
            String time = "" + location.getTime();
            String accuracy = "" + location.getAccuracy();
            String speed = "" + location.getSpeed();
            String msg = "Date/Time: " + time + "\n" + "Provider: "+ provider+ "\nAccuracy: " + accuracy + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Speed: " + speed + "\n";
            statusTextView.setText(msg);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            statusTextView.setText("No works");
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

//        This is the real time location function
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateView(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                realTimeTextView.setText("Searching``````````\n Please wait.\n on StatusChanged!!!!");
            }

            @Override
            public void onProviderEnabled(String s) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                updateView(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }

            @Override
            public void onProviderDisabled(String s) {

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
            String provider=""+location.getProvider();
            msg = "provider: "+provider+"\nDate/Time: " + time + "\n"  + "Accuracy: " + accuracy + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + "Speed: " + speed + "\n";
            realTimeTextView.setText(msg);
        }
        else{
            realTimeTextView.setText("No MSG");
        }
    }
}
