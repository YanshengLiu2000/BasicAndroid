package yanshengliu.testforphone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Button showAvilabaleSensorsBtn,taskTwoBtn;
    private ListView listView;
    private AlertDialog.Builder builder;
    private String tempInfo;
    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAvilabaleSensorsBtn=(Button) findViewById(R.id.showAvilabaleSensorsBtn);
        taskTwoBtn=(Button) findViewById(R.id.taskTwoBtn);
        listView=(ListView) findViewById(R.id.listView);

        taskTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccelerometerOne.class);
                startActivityForResult(intent,1);
            }
        });

        showAvilabaleSensorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
                sensorList=mSensorManager.getSensorList(Sensor.TYPE_ALL);
                for(int j=0;j<10;j++){
                    System.out.println(sensorList);
                }
                List tempList=new ArrayList();
                for (int j=0;j<sensorList.size();j++){
                    int seqNum=j+1;
                    String num=""+seqNum+") \n";
                    tempList.add(num+"Name: "+ sensorList.get(j).getName()+"\nVendor: "+sensorList.get(j).getVendor()+"\nVersion: "+sensorList.get(j).getVersion()+"\nMaximumRange: "+sensorList.get(j).getMaximumRange()+"\nMiniDelay: "+sensorList.get(j).getMinDelay());
                }
                ListAdapter listAdapter=new ArrayAdapter<Sensor>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,tempList);

                listView.setAdapter(listAdapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopUpDialog(i);
            }
        });



    }

    private void PopUpDialog(int i) {
        int j=i;
        builder=new AlertDialog.Builder(this);
        builder.setMessage("Name: "+ sensorList.get(j).getName()+"\nVendor: "+sensorList.get(j).getVendor()+"\nVersion: "+sensorList.get(j).getVersion()+"\nMaximumRange: "+sensorList.get(j).getMaximumRange()+"\nMiniDelay: "+sensorList.get(j).getMinDelay());
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

}