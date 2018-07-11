package yanshengliu.ap_scanner_gamma;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private List<ScanResult> bridgeList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        mWifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
        listView=(ListView) findViewById(R.id.listView);

    }

    @Override
    protected void onResume(){
        super.onResume();

        List<ScanResult> mWifiList=getWifiList();
        ListAdapter listAdapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,mWifiList);
        listView.setAdapter(listAdapter);
        new TimeCount(60000,1000).start();

    }
    private class TimeCount extends CountDownTimer {

        private TimeCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            getWifiList();
        }

        @Override
        public void onFinish() {
            getWifiList();
        }
    }
    private List<ScanResult> getWifiList() {
        List<ScanResult> list;
        mWifiManager.startScan();
        list=mWifiManager.getScanResults();
        ScanResult temp ;

        for(int i=0;i<10;i++){
            System.out.println(list.isEmpty());
        }
        for (int i=0; i<list.size();i++)
            for ( int j =i; j<list.size();j++){
                if(list.get(i).level<list.get(j).level) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        return list;
    }
}
