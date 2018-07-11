package yanshengliu.ap_scanner;

import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ScanResult> mWifiList;
    private List<String> showList;
    private Button apScanner;
    private ListView listView;
    private WifiManager mWifiManager;
    //private ArrayList resultList;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private TextView textindialog;

    private TextView textView;
    public String userNameString;
    public String passWordString;
    private List<WifiConfiguration> mWifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        apScanner=(Button) findViewById(R.id.btnScan);
        listView=(ListView) findViewById(R.id.listViewWifi);
        //test
        textView=(TextView) findViewById(R.id.textView);
        userNameEditText = (EditText) findViewById(R.id.username);
        passWordEditText = (EditText) findViewById(R.id.password);
        textindialog =(TextView) findViewById(R.id.textView1);
        textView.setText("ESSID : BSSID : Signal strength : encryption");
        //test

        if (!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }

        apScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiList=getMWifiList();
                //showList=TransfertoShowList();
                mWifiConfiguration=mWifiManager.getConfiguredNetworks();
                ListAdapter listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,mWifiList);
                listView.setAdapter(listAdapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LoginWifi(i);
            }
        });

    }

    private List<String> TransfertoShowList() {
        List<String> list=null;
        for (int i=0; i<mWifiList.size();i++){
            String tempString=mWifiList.get(i).SSID+" : "+mWifiList.get(i).BSSID+" : "+mWifiList.get(i).level+" : ";
            if (mWifiList.get(i).capabilities!=null)
                tempString+="Open";
            else
                tempString+="Locked";
            list.add(tempString);
        }
        return list;
    }

    //Original Login program
    private void LoginWifi(int i) {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater =MainActivity.this.getLayoutInflater();
        AlertDialog dialog = builder.setView(inflater.inflate(R.layout.login_layout,null))
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        userNameEditText = (EditText) findViewById(R.id.username);
//                        passWordEditText = (EditText) findViewById(R.id.password);

//                        String test=userNameEditText.getText().toString();
//                        String test2=passWordEditText.getText().toString();
//                        System.out.println(test+test2);

                        //assume the username and password has been stored in String userNameString &
                        //passWordString
                        //here is the log in function.

                        userNameString="z5124787";
                        passWordString="Asd012zxc";
                        String SSID="UNIWIDE";
                        int type=3;
                        int index=i;
                        APConnector(SSID,userNameString,passWordString,type,index);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("zz");
                        //dialog.dismiss();
                    }
                }).create();
        dialog.show();


    }

    private void APConnector(String SSID, String userNameString, String passWordString, int type, int index) {
        //1.no password;2 wep;3wpa
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempconfig = this.IsExsits(SSID);
        if(tempconfig != null){
            mWifiManager.removeNetwork(tempconfig.networkId);
        }

        if (type ==1){
            config.wepKeys[0]="";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        else if(type ==2){
            config.hiddenSSID=true;
            config.wepKeys[0]="\"" + passWordString + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex=0;
        }
        else{
            config.hiddenSSID=true;
            config.preSharedKey="\"" + passWordString + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status=WifiConfiguration.Status.ENABLED;
        }
        //return config;
        //wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("XXX", "XXX", 3));  
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,true);
    }

    private WifiConfiguration IsExsits(String SSID){
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig:existingConfigs){
            if (existingConfig.SSID.equals("\"" +SSID+"\""))
                return existingConfig;
        }
        return null;
    }

//    public String[] SaveUserNameAndPassWord(){
//        String[] temp={userNameEditText.getText().toString(),passWordEditText.getText().toString()};
//        textView.setText(userNameEditText.getText().toString()+" : "+passWordEditText.getText().toString());
//        return temp;
//    }


//    private void LoginWifi(int i){
//        String title=mWifiList.get(i).SSID;
//    }

    public List<ScanResult> getMWifiList(){
        List<ScanResult> list;
        mWifiManager.startScan();
        list=mWifiManager.getScanResults();
        ScanResult temp ;
        for (int i=0; i<list.size();i++)
            for ( int j =i; j<list.size();j++){
                if(list.get(i).level>list.get(j).level) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
//        int count=0;
//        HashMap<String , Integer> map= new HashMap();
//        while(count!=list.size()){
//            for (int i=0; i<list.size();i++,count=i){
//                String key = list.get(i).SSID;
//                if(map.containsKey(key)){
//                    if (map.get(key) < 5){
//                        int temp1=map.get(key);
//                        temp1++;
//                        map.remove(key);
//                        map.put(key,temp1);
//                    }
//                    else{
//                        String ttemp=list.remove(i).SSID;
//                        for(int j=0;j<10;j++){
//                            System.out.println("Has removed!!!!"+ttemp);
//                        }
//                        break;
//                    }
//                }
//                else{
//                    map.put(key,1);
//                }
//            }
//        }
        return list;
    }


//    public void printMWifiList(List<ScanResult> list){
////        for(int i=0;i<list.size();i++){
////            if (list.get(i).capabilities.length()!=0) {
////                String temp = list.get(i).SSID + " " + list.get(i).level + " : " + list.get(i).capabilities;
////                resultList.add(temp);
////            }
////            else{
////                String temp = list.get(i).SSID + " " + list.get(i).level + " : Open" ;
////                resultList.add(temp);
////            }
////        }
////        ListAdapter listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,resultList);
//
//        ListAdapter listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list);
//        listView.setAdapter(listAdapter);
//
////        resultList.setText("SSID -LEVEL : Encryption \n");
////        if (list==null)
////            resultList.append("There is no Wifi nearby.");
////        else
////            for (int i = 0; i<list.size(); i++){
////                resultList.append(list.get(i).SSID+" "+list.get(i).level+" : "+list.get(i).capabilities+"\n");
////            }
//    }

}


