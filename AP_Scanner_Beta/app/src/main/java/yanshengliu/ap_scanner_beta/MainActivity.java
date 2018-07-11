package yanshengliu.ap_scanner_beta;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private Button apScanner;
    private ListView listView;
    private TextView textView;
    private List<ScanResult> bridgeList;
    private View view;
    private EditText userName;
    private EditText passWord;
    private List<WifiConfiguration> wifiConfigurations;
    private WifiInfo mWifiInfo;
    private int IPADDRESS;
    private int btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        IPADDRESS=0;
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        List temp_wifiConfigurations = mWifiManager.getConfiguredNetworks();
        wifiConfigurations=temp_wifiConfigurations;
        apScanner = (Button) findViewById(R.id.button1);
        listView = (ListView)findViewById(R.id.listView1);
        textView=(TextView)findViewById(R.id.textView1);
//        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
//        view = inflater.inflate(R.layout.login_layout, null, false);
//        userName=(EditText) view.findViewById(R.id.username);
//        passWord=(EditText) view.findViewById(R.id.password);
        textView.setText("SHOW THE Result:");

//        if (!mWifiManager.isWifiEnabled()){
//            mWifiManager.setWifiEnabled(true);
//        }//give permission to open WiFi

        apScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ScanResult> mWifiList=getWifiList();

                wifiConfigurations=mWifiManager.getConfiguredNetworks();

                bridgeList=mWifiList;
                List<String> tempList=showList();

                ListAdapter listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,tempList);
                listView.setAdapter(listAdapter);
            }
        });//get WifiLists

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LoginWifi(adapterView,i);
            }
        });//click button and login wifi

        Button getIp=(Button)findViewById(R.id.button2);
        getIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adialog();
            }
        });
//        btn=0;
//        TimerTask task=new TimerTask() {
//            @Override
//            public void run() {
//                mWifiInfo=mWifiManager.getConnectionInfo();
//                IPADDRESS=mWifiInfo.getIpAddress();
//                if(IPADDRESS!=0){
//                    Adialog();
//                    btn=1;
//                }
//            }
//        };
//
//        Timer timer =new Timer();
//        timer.schedule(task,1000);
//        while(btn==0){
//            if(btn==1){
//                timer.cancel();
//            }
//            else{
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    private List<String> showList() {
        List<String> temp=new ArrayList<>();
        String certainWifiInfor="";
        for(int i=0;i<bridgeList.size();i++){
            certainWifiInfor=bridgeList.get(i).SSID+" : "+bridgeList.get(i).BSSID+" : "+bridgeList.get(i).level+" : ";
            if(bridgeList.get(i).capabilities!=null){
                certainWifiInfor+="Locked";
            }
            else{
                certainWifiInfor+="Open";
            }
            temp.add(certainWifiInfor);
        }
        return temp;

    }

    private void LoginWifi(AdapterView<?> adapterView, final int index) {

        String temp2=bridgeList.get(index).SSID+" : "+bridgeList.get(index).BSSID+" : "+bridgeList.get(index).level;
        String temp3="";
        if(bridgeList.get(index).capabilities!=null) {
             temp3 = ": locked";
        }
        else{
             temp3 = ": open";
        }
        temp2+=temp3;
        textView.setText(bridgeList.get(index).toString());

        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        view = inflater.inflate(R.layout.login_layout, null);
        userName=(EditText) view.findViewById(R.id.username);
        passWord=(EditText) view.findViewById(R.id.password);

        AlertDialog dialog = builder.setView(view).setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userNameString="";
                String passWordString="";
                userNameString=userName.getText().toString();
                passWordString=passWord.getText().toString();

                userNameString="z5124787";
                passWordString="Asd012zxc";

                for(int k=0;k<10;k++){
                    System.out.println("Get the user information!!!!"+userNameString+passWordString);
                }
                ConnectToCertainWifi(index,userNameString,passWordString);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j=0;j<10;j++){
                    System.out.println("Wifi Login has been Canceled!");
                }
            }
        }).create();
        dialog.show();
    }



    private void ConnectToCertainWifi(int index,String userNameString,String passWordString) {
        String SSID= bridgeList.get(index).SSID;

        for(int i=0;i<10;i++){
            System.out.println("Processing the ConnectToCertainWifi Part!!!"+userNameString+passWordString);
        }

        WifiConfiguration conf = new WifiConfiguration();
        String testcase="uniwide_test";

        conf.hiddenSSID=true;
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.status=WifiConfiguration.Status.ENABLED;

        WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
        conf = new WifiConfiguration();
        conf.SSID = "\""+SSID+"\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        enterpriseConfig.setIdentity(userNameString);
        enterpriseConfig.setPassword(passWordString);
        enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        conf.enterpriseConfig = enterpriseConfig;

        int netId=mWifiManager.addNetwork(conf);
        mWifiManager.enableNetwork(netId,true);
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();

        int IpAddress=mWifiInfo.getIpAddress();
        for(int k=0;k<10;k++){
            System.out.println("THIS IS IPADDRESS: "+IpAddress);
        }
    }

    protected void Adialog(){
        mWifiInfo=mWifiManager.getConnectionInfo();
        int IpAddress=mWifiInfo.getIpAddress();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mWifiInfo.getSSID());
        builder.setMessage("IP : "+String.valueOf(mWifiInfo.getIpAddress()));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
        for(int i=0;i<10;i++){
            System.out.println(mWifiInfo);
        }
    }

    private WifiConfiguration IsExists(String SSID){
        for(WifiConfiguration existingConfig : wifiConfigurations){
            if(existingConfig.SSID.equals("\""+SSID+"\"")){
                return existingConfig;
            }
        }
        return null;
    }

    private List<ScanResult> getWifiList() {
        List<ScanResult> list;
        mWifiManager.startScan();
        list=mWifiManager.getScanResults();
        ScanResult temp ;

        for (int i=0; i<list.size();i++)
            for ( int j =i; j<list.size();j++){
                if(list.get(i).level<list.get(j).level) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        for(int k=0;k<10;k++)
        {
            System.out.println(list.getClass());
        }
        list=RemoveSomeResults(list);
        return list;
    }

    private List<ScanResult> RemoveSomeResults(List<ScanResult> list) {
        //List<ScanResult> processedList;
        for(int i=0;i<5;i++){
            System.out.println("Enter RemoveSomeResults");
        }
        int count=0;
        HashMap<String,Integer> map=new HashMap<>();
        while(count!=list.size()){
            count=0;
            map=new HashMap<>();
            for(int i=0;i<list.size();i++,count++){
                String key=list.get(i).SSID;
                if(map.containsKey(key)){
                    if(map.get(key)<5){
                        int temp1=map.get(key);
                        temp1++;
                        map.remove(key);
                        map.put(key,temp1);
                    }
                    else{
                        list.remove(i);
                        break;
                    }
                }
                else{
                    map.put(key,1);
                }

            }
        }
        return list;

    }


}
