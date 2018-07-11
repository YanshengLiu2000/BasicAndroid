package yanshengliu.wifiactivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private Button stopBtn;
    private Button checkBtn;
    private Button simpleDialogBtn;
    private Button loginDialog;
    private WifiManager mWifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = (Button) findViewById(R.id.startWifi);
        stopBtn = (Button) findViewById(R.id.stopWifi);
        checkBtn = (Button) findViewById(R.id.checkWifi);
        simpleDialogBtn = (Button) findViewById(R.id.simpleDialogBtn);
        loginDialog = (Button) findViewById(R.id.customDialog);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
                mWifiManager.setWifiEnabled(true);
                System.out.print("Wifi state--->"+mWifiManager.getWifiState());
                TextView txtResult=(TextView) findViewById(R.id.txt1);
                txtResult.setText("Wifi state (start): "+mWifiManager.getWifiState());
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
                mWifiManager.setWifiEnabled(false);
                System.out.print("Wifi state (stop)--->"+mWifiManager.getWifiState());
                TextView txtResult=(TextView) findViewById(R.id.txt1);
                txtResult.setText("Wifi state (stop): "+mWifiManager.getWifiState());
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
                System.out.print("Wifi state (check)--->"+mWifiManager.getWifiState());
                TextView txtResult=(TextView) findViewById(R.id.txt1);
                txtResult.setText("Wifi state (check): "+mWifiManager.getWifiState());
            }
        });
        simpleDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adialog();
            }
        });
        loginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick();
            }
        });

    }

    public void myClick(){
        TextView txtResult=(TextView) findViewById(R.id.txt1);
        txtResult.setText("This is login button!!!!");
        startActivities(new Intent[]{new Intent(MainActivity.this, LoginActivity.class)});

    }

    protected void Adialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Test Dialog");
        builder.setMessage("Thi is a test for simple dialog.");
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
    }

}

