package yanshengliu.accelerometer_function;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccelerometerTwo extends AppCompatActivity {

    private TextView textViewStatus;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_two);


        textViewStatus=(TextView) findViewById(R.id.textViewStatus);
        float textSize=65;
        textViewStatus.setText("ZHAZHA");
        textViewStatus.setTextSize(textSize);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mAccelerometerListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);


    }

    final SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float X=sensorEvent.values[0];
            float Y=sensorEvent.values[1];
            float Z=sensorEvent.values[2];

            if(Z>8.5){textViewStatus.setText("On the table");}
            else if (Y>8.5){textViewStatus.setText("Default");}
            else if (Y<-8.5){textViewStatus.setText("Upside Down");}
            else if (X<-8.5){textViewStatus.setText("Right");}
            else if(X>8.5){textViewStatus.setText("Left");}
            else{textViewStatus.setText("On The Move!");}
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            int j=0;
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerListener);
    }

}
