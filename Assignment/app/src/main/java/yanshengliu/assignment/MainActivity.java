package yanshengliu.assignment;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView conditionTextView;
    private Button recordStatusBtn;
    private WifiManager mWifiManager;
    private List<ScanResult> mWifiList;
    private WifiInfo mWifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        recordStatusBtn = (Button) findViewById(R.id.recordStatusBtn);
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);


    }

    @Override
    protected void onResume(){
        super.onResume();

        recordStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordLinkLayerInfor();
            }
        });

    }

    private void RecordLinkLayerInfor() {

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

    }

}
