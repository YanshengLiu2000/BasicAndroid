package yanshengliu.testfornewphone;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.Manifest;

import static android.os.Environment.getExternalStorageState;

public class MainActivity extends AppCompatActivity {

    private Button numBtn;
    private TextView numTextView,conditionTextView;
    private int count;
    private String mFileName,folderName;
    private String line,prev;
    private StringBuilder mStringBuilder;
    private String infor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numBtn= (Button) findViewById(R.id.numBtn);
        numTextView=(TextView) findViewById(R.id.numTextView);
        conditionTextView=(TextView) findViewById(R.id.conditionTextVoew);
        count=0;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            for (int j = 0; j < 5; j++) {
                System.out.println("test=it works so far~~~");
            }
        }
        infor="";
//check permission:
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.e("Permission: ", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }else{
            //Log.e("Permission: ", String.valueOf(permissionCheck));

            infor=infor+"permission part works~~~~\n";
            conditionTextView.setText(infor);
        }

//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
//        path = path+"/test.txt";
//        Log.i("Path: ",path);
//        File fileTest= new File(path);
//        if(!fileTest.exists()){
//            try {
//                fileTest.createNewFile();
//                Log.e("filetest:","File test works.");
//                infor=infor+"file created!\n";
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else{
//            infor=infor+"file exists!\n";
//            Log.e("filetest:","File test exists.");
//        }
//        conditionTextView.setText(infor);


        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        folderName = path + "/csv";
        for (int j = 0; j < 5; j++) {
            System.out.println("TEST====external storage direction!===>" + folderName);
        }
        conditionTextView.setText(path);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "csv");
//        file.mkdirs();
        if (file.mkdirs()) {
            Log.i("yes we can", "!");
            infor=infor+"folder first time created!~~~~~\n";
            conditionTextView.setText(infor);
        } else {
            if(file.exists()){
                Log.i("folder","Folder has already exists!\n");
                infor=infor+"folder has already exists!\n";
            }
            else{
                Log.e("folder", "folder can not be created normally!\n");
                infor=infor+"folder can not be created normally!\n";

            }
        }
        conditionTextView.setText(infor);

        mFileName=folderName+"/test.csv";
        File testFile=new File(mFileName);
        if(!testFile.exists()){
            try {
                testFile.createNewFile();
                infor=infor+"test.csv has been created first time!\n";

            } catch (IOException e) {
                e.printStackTrace();
                infor=infor+"can not create test.csv\n";
            }
        }else{
            infor=infor+"test.csv has already exists!\n";
        }
        conditionTextView.setText(infor);




        numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                String temp = "Next number: " + count;
                numTextView.setText(temp);
//read

                try {
                    InputStreamReader read = new InputStreamReader( new FileInputStream(mFileName));
                    BufferedReader bufferedReader= new BufferedReader(read);
                    prev="The information in the file:\n";
                    line =bufferedReader.readLine();
                    mStringBuilder=new StringBuilder();
                    while(line != null ){
                        //line=lin;
                        System.out.println("test=read section==>"+line);
                        mStringBuilder.append(line);
                        mStringBuilder.append("\r\n");
                        line =bufferedReader.readLine();

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//write
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(mFileName));
                    temp=""+count+",\n";
                    mStringBuilder.append(temp);
                    //mStringBuilder.append("\r\n");

                    out.write(mStringBuilder.toString());
                    out.flush();
                    //out.newLine();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}