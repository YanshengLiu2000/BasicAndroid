package yanshengliu.testforassignment_ii;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final static String GET_IP = "https://ifconfig.co/json";
    private WifiManager mWifiManager;
    private TextView statusTextView,taskTextView;
    private NetworkChangeReceiver networkchangeReceiver;
    private int bNum=0;
    private String info="";
    private String taskInfo="";
    private int count=0;
    private Button timerBtn,threadBtn;
    private Thread thread;
    private Date time,previousTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permission", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        statusTextView = (TextView) findViewById(R.id.textView1);
        taskTextView= (TextView) findViewById(R.id.taskTextView);
        timerBtn=(Button) findViewById(R.id.timerBtn);
        threadBtn=(Button)findViewById(R.id.threadBtn);

        //        L2 receive a boardcast about the AP change:
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        intentFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        networkchangeReceiver =new NetworkChangeReceiver();
        registerReceiver(networkchangeReceiver, intentFilter);
//        new TimeCount(30000,100).start();
    }


    @Override
    protected void onResume(){
        super.onResume();

        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                thread.stop();
                taskInfo="";
                new TimeCount(30000,1).start();
            }
        });

        threadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time=new Date();
                if(previousTime==null){
                    info=info+"\n"+"Initialization Complete!"+time;
                    previousTime=time;
                }
                else{
                    long delta=time.getTime()-previousTime.getTime();
                    previousTime=time;
                    info=info+"\n"+"spend "+delta+" ms";
                }
                statusTextView.setText(info);
            }
        });

    }

    public class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            bNum++;
            if(bNum==30){
                bNum=0;
                info="";
            }
//            Toast.makeText(context, "network changes!", Toast.LENGTH_SHORT).show();

            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
//                statusTextView.setText("NetWork state changed!");
                Toast.makeText(context, "NetWork state changed!", Toast.LENGTH_SHORT).show();
//                System.out.println("========>NetWork state changed!");
                info=info+"\n"+bNum+" :"+"NSC:";
            }
            else if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                info=info+"\n"+bNum+" :"+"WCA:";
                Toast.makeText(context, "WiFi state changed!", Toast.LENGTH_SHORT).show();
            }
            else if (WifiManager.NETWORK_IDS_CHANGED_ACTION.equals(intent.getAction())){
                Toast.makeText(context,"NetWork IDs Changed!",Toast.LENGTH_SHORT).show();
                info=info+"\n"+bNum+" :"+"NIC:";
            }
            else if("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())){
                info=info+"\n"+bNum+" : "+"WASC";
                Toast.makeText(context,"WIFI_AP_STATE_CHANGED!",Toast.LENGTH_SHORT).show();
            }
            else{
//                statusTextView.setText("Connectivity action!!!");
                Toast.makeText(context, "Connectivity action!!!", Toast.LENGTH_SHORT).show();
//                System.out.println("========>Connectivity action!!!");
                info=info+"\n"+bNum+" :"+"CA:";

            }
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
//            System.out.println("+++++++++++++++++++"+mWifiInfo.getIpAddress());
            info+=getIpAddress(mWifiInfo)+":"+mWifiInfo.getBSSID();
            statusTextView.setText(info);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkchangeReceiver);
    }

    private static String getIpAddress(WifiInfo wifiInfo) {
        String result;
        int ip = wifiInfo.getIpAddress();
        result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return result;
    }

    private class TimeCount extends CountDownTimer {

        private TimeCount(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            count++;
            if(count==15){
                count=0;
                taskInfo="";
            }
            taskInfo+="*";
            taskTextView.setText(taskInfo);

        }

        @Override
        public void onFinish() {
            PopupDialog();
        }
    }
    private void PopupDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Finish!");
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                new TimeCount(10000,100).start();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

//    private void CreateThread() {
//        thread= new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (this){
//                    try {
//                        wait(100);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                count++;
//                                if(count==15){
//                                    count=0;
//                                    taskInfo="";
//                                    System.out.println("test==========> Keep running!");
//                                }
//                                taskInfo+="+";
//                                taskTextView.setText(taskInfo);
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

}

