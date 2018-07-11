package yanshengliu.uniwidedetect;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button linkLayerRecordBtn;
    private EditText linkLayerEditText;
    private TextView statusTextView,processTextView;
    private int checkPoint=0;
    private String infor,mFileNameLLA,mfolderName,mFileNameL4,mFileNameL3,mFileNameL2;
    private StringBuilder mStringBuilder;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
//    private NetworkChangeReceiver networkchangeReceiver;
    private Date time,previousTime;
    //use to set UI.
    private int bNum=0;
    private int count=0;
    private String taskInfo="";
    //use for store
    private int currentIPadrress;
    private String currentBSSID;
    private Date L2HOS,L2HOE;//L2 handoff start & L2 handoff end
    private Date L3HOS,L3HOE;//L3 handoff start & L3 handoff end
    //use for switch
    private int L2HandOffSwitch=0;
    private int L3HandOffSwitch=0;
    private int refreshTime=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkLayerRecordBtn=(Button) findViewById(R.id.linkLayerRecordBtn);
        linkLayerEditText=(EditText) findViewById(R.id.linkLayerEditText);
        statusTextView= (TextView) findViewById(R.id.statusTextView);
        processTextView = (TextView) findViewById(R.id.processTextView);
        linkLayerEditText= (EditText) findViewById(R.id.linkLayerEditText);
        mWifiManager= (WifiManager) getSystemService(WIFI_SERVICE);

        checkPoint=0;
        infor="";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //check external Storage Status:
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.i("ExternalState","Works well.");
        }else{
            Log.e("ExternalState","Status error!!!!");
        }
        //check permissions:
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.e("Permission: ", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }else{
            infor=infor+"LinkLayer: permission part works~~~~\n";
            statusTextView.setText(infor);
        }

        //build & check the folder director:
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        mfolderName = path + "/csv/";
        infor= infor+"The path of the folder is:"+mfolderName+"\n";
        statusTextView.setText(infor);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"csv");
        if(file.mkdirs()){
            Log.i("Folder: ","First Time Create the CSV Folder.");
            infor=infor+"LinkLayer: Folder first time created.\n";
        }else{
            if(file.exists()){
                Log.i("Folder: ","Folder has already existed.");
                infor=infor+"LinkLayer: Folder exists.\n";
            }else{
                Log.e("Folder: ","Folder Part Error!!!");
                infor=infor+"LinkLayer: Folder Part Error!!!\n";
            }
        }
        statusTextView.setText(infor);
        //build & check today yymmdd.csv file:
        Date now= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String currentDay = dateFormat.format(now);
        mFileNameLLA=mfolderName+currentDay+"LLA.csv";
        mFileNameL2=mfolderName+currentDay+"L2.csv";
        mFileNameL3=mfolderName+currentDay+"L3.csv";
        mFileNameL4=mfolderName+currentDay+"L4.csv";
        CreateOrCheckFile(mFileNameLLA);
        CreateOrCheckFile(mFileNameL2);
        CreateOrCheckFile(mFileNameL3);
        CreateOrCheckFile(mFileNameL4);
        new TimeCount(60000,refreshTime).start();
    }

    private void CreateOrCheckFile(String FileName) {
        File currentFile=new File(FileName);
        if(!currentFile.exists()){
            try{
                currentFile.createNewFile();
                String temp="";
                if(FileName.equals(mFileNameLLA)){
                    temp="Check Point, TimePointer, Comment, Frequency(GHz), Speed, Signal Strength, AP Density";
                }
                else if(FileName.equals(mFileNameL2)){
                    temp="TimePointer, Comment, L2HOE, L2HOS, L2Period, currentBSSID, tempBSSID";
                }
                else if(FileName.equals(mFileNameL3)){
                    temp="TimePointer, Comment, L3HOE, L3HOS, L3Period, currentIP, tempIP";
                }
                else{
                    temp="Have no idea about this tcp link check!";
                }
                checkPoint=0;
                WriteDataTofile(temp, FileName);
                infor=infor+"LinkLayer: First Time File Is Created\n";
                Log.i("File: ","File First time created!");
            } catch (IOException e) {
                e.printStackTrace();
                infor=infor+"LinkLayer: Error Occurred During File Creating\n";
                Log.e("File: ","Error Occurred During File Is Creating");
            }
        }else{
            infor=infor+"LinkLayer: Today's file has already created \n";
            Log.i("File: ","File has already created sometime!");
        }
        statusTextView.setText(infor);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //set Btn Behaviour:
        linkLayerRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkLayerData=CheckPointLinkLayerInfor();
                WriteDataTofile(linkLayerData,mFileNameLLA);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkchangeReceiver);
    }

    private void WriteDataTofile(String usefulData,String currentFileDirection) {
        //read from file first:
        try {
            InputStreamReader read = new InputStreamReader( new FileInputStream(currentFileDirection));
            BufferedReader bufferedReader= new BufferedReader(read);
            String line =bufferedReader.readLine();
            mStringBuilder=new StringBuilder();
            while(line != null ){
                System.out.println("test=read section==>"+line);
                mStringBuilder.append(line);
                mStringBuilder.append("\r\n");
                line =bufferedReader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write all data into file:
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(currentFileDirection));
            String temp=usefulData+",\n";
            mStringBuilder.append(temp);
            //mStringBuilder.append("\r\n");
            out.write(mStringBuilder.toString());
            out.flush();
            //out.newLine();
            out.close();
            temp= "LinkLayer: write into file>>>"+usefulData;
            infor=infor+temp+"\n";
            statusTextView.setText(infor);
            Log.i("Write:","LinkLayer infor write succeed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read WiFi info and recorded in the resultData;
    private String CheckPointLinkLayerInfor() {
       // String temp="Check Point, Comment, Frequency(GHz), Speed, Signal Strength, AP Density";
        String resultData="";
        mWifiManager.startScan();
        List<ScanResult> list = mWifiManager.getScanResults();
        mWifiInfo= mWifiManager.getConnectionInfo();
        double freq=mWifiInfo.getFrequency()/1000.0;
        int speed=mWifiInfo.getLinkSpeed();
        int apdensity=list.size();
        int signalStrength= mWifiInfo.getRssi();
        resultData=resultData+checkPoint+" , "+TimePointer()+" , "+linkLayerEditText.getText().toString()+" , "+freq+" , "+speed+" , "+signalStrength+" , "+apdensity+" , ";
        checkPoint++;
        String temp="Comment: "+checkPoint;
        linkLayerEditText.setText(temp);
        return resultData;
    }

    private String TimePointer() {
        DateFormat df= new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
    }

//    public class NetworkChangeReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent){
//            bNum++;
//            if(bNum==30){
//                bNum=0;
//                info="";
//            }
////            Toast.makeText(context, "network changes!", Toast.LENGTH_SHORT).show();
//
//            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
////                statusTextView.setText("NetWork state changed!");
//                Toast.makeText(context, "NetWork state changed!", Toast.LENGTH_SHORT).show();
////                System.out.println("========>NetWork state changed!");
//                info=info+"\n"+bNum+" :"+"NSC:";
//            }
//            else if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
//                info=info+"\n"+bNum+" :"+"WCA:";
//                Toast.makeText(context, "WiFi state changed!", Toast.LENGTH_SHORT).show();
//            }
//            else if (WifiManager.NETWORK_IDS_CHANGED_ACTION.equals(intent.getAction())){
//                Toast.makeText(context,"NetWork IDs Changed!",Toast.LENGTH_SHORT).show();
//                info=info+"\n"+bNum+" :"+"NIC:";
//            }
//            else if("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())){
//                info=info+"\n"+bNum+" : "+"WASC";
//                Toast.makeText(context,"WIFI_AP_STATE_CHANGED!",Toast.LENGTH_SHORT).show();
//            }
//            else{
////                statusTextView.setText("Connectivity action!!!");
//                Toast.makeText(context, "Connectivity action!!!", Toast.LENGTH_SHORT).show();
////                System.out.println("========>Connectivity action!!!");
//                info=info+"\n"+bNum+" :"+"CA:";
//
//            }
//            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
////            System.out.println("+++++++++++++++++++"+mWifiInfo.getIpAddress());
//            info+=getIpAddress(mWifiInfo)+":"+mWifiInfo.getBSSID();
//            statusTextView.setText(info);
//        }
//    }

    private static String getIpAddress(WifiInfo wifiInfo) {
        String result;
        int ip = wifiInfo.getIpAddress();
        result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
        return result;
    }
    private static String IPFormat(int insertIP){
        String result;
        int ip = insertIP;
        result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
        return result;
    }

    private class TimeCount extends CountDownTimer {

        private TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            CheckL2Handoff();
            CheckL3Handoff();
            count++;
            if (count == 50) {
                count = 0;
                taskInfo = "";
            }
            taskInfo += "*";
            processTextView.setText(taskInfo);
        }

        @Override
        public void onFinish() {
            infor="";
            new TimeCount(60000,refreshTime).start();
            Toast.makeText(MainActivity.this, "Test restart!", Toast.LENGTH_SHORT).show();
        }
    }


    private void CheckL3Handoff() {// ip change!
        String resultInfo="";
        long L3Period=-2;
        mWifiInfo=mWifiManager.getConnectionInfo();
        int tempIPaddress=mWifiInfo.getIpAddress();
//       "TimePointer, Comment, L3HOE, L3HOS, L3Period, currentIP, tempIP";
        if(L3HandOffSwitch==0 && currentIPadrress==0 && tempIPaddress!=0){// 1st time enter campus zone!
            Toast.makeText(MainActivity.this, "L3 1st Enter!", Toast.LENGTH_SHORT).show();
            L3HandOffSwitch=1;
            L3Period=-1;
            resultInfo= TimePointer()+", "+"1st Time Connect Uniwide"+", "+L3HOE+", "+L3HOS+", "+L3Period+", "+IPFormat(currentIPadrress)+", "+IPFormat(tempIPaddress);
            currentIPadrress=tempIPaddress;
        }
        else if(L3HandOffSwitch==1 && currentIPadrress!= 0 && tempIPaddress==0){// disconnect because of low signal
            Toast.makeText(MainActivity.this, "L3 Disconnected!", Toast.LENGTH_SHORT).show();

//            infor=infor+"disconnet!";
            L3HOS=new Date();
            L3Period=-1;
//            statusTextView.setText(info);
            resultInfo= TimePointer()+", "+"Disconnected Because of Low Signal"+", "+L3HOE+", "+L3HOS+", "+L3Period+", "+IPFormat(currentIPadrress)+", "+IPFormat(tempIPaddress);
            currentIPadrress=tempIPaddress;
            L4Handoff(0);
        }
        else if(L3HandOffSwitch==1 && currentIPadrress== 0 && tempIPaddress!=0 ){
            Toast.makeText(MainActivity.this, "L3 Reconnected!", Toast.LENGTH_SHORT).show();
            L3HOE=new Date();
            L3Period=L3HOE.getTime()-L3HOS.getTime();
//            infor=infor+"reconnect!:"+L3Period;
//            statusTextView.setText(info);
            resultInfo= TimePointer()+", "+"Reconnected"+", "+L3HOE+", "+L3HOS+", "+L3Period+", "+IPFormat(currentIPadrress)+", "+IPFormat(tempIPaddress);
            currentIPadrress=tempIPaddress;
            L4Handoff(1);
        }
        if(L3Period!=-2){
            resultInfo=resultInfo+" , "+checkPoint;
            WriteDataTofile(resultInfo, mFileNameL3);
        }
    }

    private void CheckL2Handoff() {
        long L2Period =-2;
        mWifiInfo=mWifiManager.getConnectionInfo();
        String tempBSSID=mWifiInfo.getBSSID();
        String resultInfo="";
        if(L2HandOffSwitch==0 && currentBSSID==null && tempBSSID!=null){// 1st time enter campus zone!
            Toast.makeText(MainActivity.this, "L2 1st enter wifi", Toast.LENGTH_SHORT).show();
            L2HandOffSwitch=1;
            L2Period=-1;
            resultInfo= TimePointer()+", "+"1st Time Connect Uniwide"+", "+L2HOE+", "+L2HOS+", "+L2Period+", "+currentBSSID+", "+tempBSSID;
            currentBSSID=tempBSSID;
//            record L2Period and location and comments(1st enter wifi zone) into file.
        }
        else if(L2HandOffSwitch==1 && currentBSSID!=null && tempBSSID==null){// disconnect because of low signal
            Toast.makeText(MainActivity.this, "L2 Lost BSSID", Toast.LENGTH_SHORT).show();
            L2HOS=new Date();
            L2Period=-1;
            resultInfo= TimePointer()+", "+"Disconnected because of low signal"+", "+L2HOE+", "+L2HOS+", "+L2Period+", "+currentBSSID+", "+tempBSSID;
            currentBSSID=null;
           // L4Handoff(0);
//          record location and comments(lose signal) into file.
        }
        else if(L2HandOffSwitch==1 && currentBSSID==null && tempBSSID!=null){//reconnect wifi in campus
            Toast.makeText(MainActivity.this, "L2 ReFind BSSID", Toast.LENGTH_SHORT).show();
            L2HOE=new Date();
            L2Period=L2HOE.getTime()-L2HOS.getTime();
            resultInfo= TimePointer()+", "+"Reconnected Uniwide"+", "+L2HOE+", "+L2HOS+", "+L2Period+", "+currentBSSID+", "+tempBSSID;
            currentBSSID=tempBSSID;
            //L4Handoff(1);
//          record location ,L2Period, L2HOE,L2HOS and comments(lose signal) into file.
        }
        else if(L2HandOffSwitch==1 && currentBSSID!=null && tempBSSID!=null && currentBSSID.compareTo(tempBSSID)!=0){// From one AP to Another directly
            Toast.makeText(MainActivity.this, "L2 TP online!", Toast.LENGTH_SHORT).show();
            L2Period=0;
            resultInfo= TimePointer()+", "+"Connected Other AP without Disconnecting"+", "+L2HOE+", "+L2HOS+", "+L2Period+", "+currentBSSID+", "+tempBSSID;
            currentBSSID=tempBSSID;
            L4Handoff(2);
        }

        else{
            Log.i("L2","enter else part!");
        }
        if(L2Period!=-2){
            resultInfo=resultInfo+" , "+checkPoint;
            WriteDataTofile(resultInfo, mFileNameL2);
        }
    }
    private void L4Handoff(int status){//0 disconnect ;1 reconnected with different socket; 2 keep
        String resultInfo="";
        String tcpStatus="";
        if(status==0){
            tcpStatus="TCP break";
        }else if(status==1){
            tcpStatus="connected with different socket!";
        }else if(status==2) {
            tcpStatus="keep alive!";
        }else{
            tcpStatus="connected with same socket!";
        }
        resultInfo=TimePointer()+", "+tcpStatus;
        resultInfo=resultInfo+" , "+checkPoint;
        WriteDataTofile(resultInfo, mFileNameL4);
    }

}
