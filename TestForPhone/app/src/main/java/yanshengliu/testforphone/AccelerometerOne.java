package yanshengliu.testforphone;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.security.KeyStore;
import java.util.List;

public class AccelerometerOne extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Button taskThreeBtn;
    private TextView textViewWithG, textViewWithoutG;
    private float[] g=new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_one);

        taskThreeBtn = (Button) findViewById(R.id.taskThreeBtn);
        textViewWithG = (TextView) findViewById(R.id.textViewWithG);
        textViewWithoutG = (TextView) findViewById(R.id.textViewWithoutG);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(mAccelerometerListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mAnotherAccelerometerListener,mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_NORMAL);

        taskThreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccelerometerOne.this, AccelerometerTwo.class);
                startActivityForResult(intent,1);
            }
        });

        g[0]=0;g[1]=0;g[2]=0;


        Sensor testSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        System.out.println("Test+++++++++++++++++++++++test!!");
        for (int j =0;j<10;j++){
            if(testSensor!=null){
                System.out.println(testSensor);
            }
            else{System.out.println("NOPE");}

        }

    }

    final SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            String X=""+sensorEvent.values[0];
            String Y=""+sensorEvent.values[1];
            String Z=""+sensorEvent.values[2];
            textViewWithG.setText("X: "+X+"\nY: "+Y+"\nZ: "+Z);

//            final float alpha= (float)0.25;
//
//            g[0]=alpha*g[0]+(1-alpha)*sensorEvent.values[0];
//            g[1]=alpha*g[1]+(1-alpha)*sensorEvent.values[1];
//            g[2]=alpha*g[2]+(1-alpha)*sensorEvent.values[2];
//
//            X=""+g[0];
//            Y=""+g[1];
//            Z=""+g[2];
//            textViewWithoutG.setText("X: "+X+"\nY: "+Y+"\nZ: "+Z);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            textViewWithG.setText("onAccuracyChanged!");
        }
    };
    
    final SensorEventListener mAnotherAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            String X=""+sensorEvent.values[0];
            String Y=""+sensorEvent.values[1];
            String Z=""+sensorEvent.values[2];
            textViewWithoutG.setText("X: "+X+"\nY: "+Y+"\nZ: "+Z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            textViewWithoutG.setText("onAccuracyChanged!");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerListener);
        mSensorManager.unregisterListener(mAnotherAccelerometerListener);
    }
}
