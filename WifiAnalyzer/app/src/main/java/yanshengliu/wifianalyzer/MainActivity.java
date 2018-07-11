package yanshengliu.wifianalyzer;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button wifiScanner;
    private Button freqAndSpeed;
    private ListView listView;
    private TextView textView;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiScanner = (Button) findViewById(R.id.button1);
        freqAndSpeed=(Button) findViewById(R.id.button2);
        listView=(ListView) findViewById(R.id.listView);
        textView=(TextView) findViewById(R.id.textView1);

        wifiScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiList=GetWifiList();
                ListAdapter listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,mWifiList);
                listView.setAdapter(listAdapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textView.setText(mWifiList.get(i).toString());
            }
        });

        freqAndSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiInfo=mWifiManager.getConnectionInfo();
                double freq=mWifiInfo.getFrequency()/1000.0;
                int speed=mWifiInfo.getLinkSpeed();
                textView.setText("Frequency: "+freq+"GHz ; "+"Speed: "+speed+" Mbps");

            }
        });




    }

    private List<ScanResult> GetWifiList() {
        List<ScanResult> list;
        mWifiManager.startScan();
        list=mWifiManager.getScanResults();
        mWifiInfo=mWifiManager.getConnectionInfo();
        double freq=mWifiInfo.getFrequency()/1000.0;
        int speed=mWifiInfo.getLinkSpeed();
        if(freq>5){
            textView.setText("This device supports 5G Wifi.");
        }else{
            textView.setText("This device does not support 5G Wifi.");
        }
//        if(mWifiManager.is5GHzBandSupported()){
//            textView.setText("This device supports 5G Wifi.");
//        }
//        else{
//            textView.setText("This device does not support 5G Wifi.");
//        }


        return list;
    }


}
