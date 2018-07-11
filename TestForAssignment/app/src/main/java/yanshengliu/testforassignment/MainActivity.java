package yanshengliu.testforassignment;

import android.Manifest;
import android.content.Context;
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

import static android.os.Environment.getExternalStorageState;

public class MainActivity extends AppCompatActivity {

    private Button numBtn;
    private TextView numTextView,conditionTextView;
    private int count;
    private String mFileName,folderName;
    private String line,prev;
    private StringBuilder mStringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int permissionCheck = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.i("permisiion", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //check external Storage Status:
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.i("ExternalState","Works well.");
        }else{

        }

        //check permissions:
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.e("Permission: ", String.valueOf(permissionCheck));
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }else{

        }
        numBtn= (Button) findViewById(R.id.numBtn);
        numTextView=(TextView) findViewById(R.id.numTextView);
        conditionTextView=(TextView) findViewById(R.id.conditionTextVoew);
        count=0;
    }
    @Override
    protected void onResume(){
        super.onResume();

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            for (int j = 0; j < 5; j++) {
                System.out.println("test=it works so far~~~");
            }
        }

        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        folderName=path+"/csv/";
//        folderName=path;

        for (int j=0;j<5;j++){
            System.out.println("TEST====external storage direction!===>"+folderName);
        }
        conditionTextView.setText(path);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"csv");
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"");

        //file.mkdirs();
        if(file.mkdirs()){
            Log.i("yes we can","!");
            conditionTextView.setText(folderName+"\n make direction succeed!");
        }
        else{
            conditionTextView.setText("There is something wrong here!");
            Log.e("Something wrong here","file");
        }




        mFileName=folderName+"test.csv";
        File testFile=new File(mFileName);
        if(!testFile.exists()){
            try {
                testFile.createNewFile();
                conditionTextView.setText("YES WE CAN!!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        numBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                String temp="Next number: "+count;
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
