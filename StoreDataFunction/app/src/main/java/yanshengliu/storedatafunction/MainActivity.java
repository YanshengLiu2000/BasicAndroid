package yanshengliu.storedatafunction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button recordBtn,checkBtn,showRecordBtn,saveBtn;
    private EditText editText;
    private TextView textView,textView2;
    private ListView listView;
    private String infor,mFileName,mfolderName;
    private StringBuilder mStringBuilder;
    private Location location;
    private LocationManager mLocationManager;
    private WifiManager mWifiManager;
    private List<String> showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            statusTextView.setText("No works");
            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permission", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //check permissions:
        if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.e("Permission: ", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }else{
            Log.i("write permission","works well");
        }

        recordBtn=(Button) findViewById(R.id.recordBtn);
        checkBtn=(Button) findViewById(R.id.checkBtn);
        showRecordBtn=(Button) findViewById(R.id.showRecordBtn);
        saveBtn=(Button) findViewById(R.id.saveBtn);
        listView=(ListView) findViewById(R.id.listView);
        editText=(EditText) findViewById(R.id.editText);
        textView=(TextView) findViewById(R.id.textView);
        textView2=(TextView) findViewById(R.id.textView2);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mWifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
        mStringBuilder= new StringBuilder();


    }

    @Override
    protected void onResume(){
        super.onResume();
//Task One

        editText.clearFocus();
        ShowTheMsg();
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=editText.getText().toString();
                SharedPreferences sharedPreferences= getSharedPreferences("fileName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("string",msg);
                editor.commit();
                ShowTheMsg();
            }
        });
//Task Two!!!!
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg="";
                if(isSDcardExist()){
                    msg="SD card exists.\n";
                    File direction = Environment.getExternalStorageDirectory();
                    StatFs statFs = new StatFs(direction.getPath());
                    Long blockSize = statFs.getAvailableBlocksLong();
                    Long blockCount=statFs.getBlockCountLong();
                    Long availCount=statFs.getAvailableBlocksLong();
                    float total=blockSize*blockCount/(1024*1024*1024);
                    total=total/1024;
                    float available=blockSize*availCount/(1024*1024*1024);
                    available=available/1024;
                    msg+= "Total: "+total+"GB\nAvailable: "+available+"GB";
                }
                else{
                    msg="No SD card!";
                }
                textView2.setText(msg);
            }
        });

//Task Three!
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        mfolderName = path + "/csv/";

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
        //build & check today yymmdd.csv file:

        mFileName=mfolderName+"lab10"+".csv";
        File currentFile=new File(mFileName);
        if(!currentFile.exists()){
            try{
                currentFile.createNewFile();
                Log.i("File: ","File First time created!");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("File: ","Error Occurred During File Is Creating");
            }
        }else{
            Log.i("File: ","File has already created sometime!");
        }

        showList =  getShowList();


        showRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> infoList=ReadDataFromFile(mFileName);
                ListAdapter listAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,infoList);
                listView.setAdapter(listAdapter);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp="Data saved in "+mFileName;
                Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
                WriteDataToFile(showList,mFileName);
            }
        });
    }

    public static void closeSoftKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private List<String> ReadDataFromFile(String currentFileDirection){
        List<String> resultList= new ArrayList<>();
        try {
            InputStreamReader read = new InputStreamReader( new FileInputStream(currentFileDirection));
            BufferedReader bufferedReader= new BufferedReader(read);
            String line =bufferedReader.readLine();
            while(line!= null){

                System.out.println("test=read section==>"+line);
                System.out.println("test=read section==>"+line);
                System.out.println("test=read section==>"+line);

                resultList.add(line);
                line =bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private void WriteDataToFile(List<String> showList, String currentFileDirection) {
        for(int i=0;i<showList.size();i++){
            String temp=showList.get(i)+"\n";
            mStringBuilder.append(temp);
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(currentFileDirection));
            out.write(mStringBuilder.toString());
            out.flush();
            //out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> getShowList() {

        mWifiManager.startScan();
        List<ScanResult> list = mWifiManager.getScanResults();
        ScanResult temp;
        for (int i = 0; i < list.size(); i++){
            for (int j = i; j < list.size(); j++) {
                if (list.get(i).level < list.get(j).level) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        List<String> tempList= new ArrayList<>();
        String certainWifiInfo="";
        Date now= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDay = dateFormat.format(now);
        String locationInfo=getLocationInfo();
        for(int i=0;i<list.size();i++){
            certainWifiInfo=currentDay+" , "+locationInfo+" , "+list.get(i).SSID+" , "+list.get(i).level;
            tempList.add(certainWifiInfo);
//            System.out.println("test!!!!!!===========>"+certainWifiInfo);
        }
        return tempList;
    }

    private String getLocationInfo() {
        Log.i("Location","Enter Location Function!");
        String latitude = "" + location.getLatitude();
        String longitude = "" + location.getLongitude();
        String msg=latitude+" , "+longitude;
        return msg;
    }


    private void ShowTheMsg() {
        SharedPreferences sharedPreferences=getSharedPreferences("fileName",Context.MODE_PRIVATE);
        String recordedInfo=sharedPreferences.getString("string","");
        if(recordedInfo ==""){
            textView.setText("There is nothing in memory.");
        }else{
            String temp="The information below is stored in memory:\n"+recordedInfo;
            textView.setText(temp);
        }
    }

    private boolean isSDcardExist() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

}
