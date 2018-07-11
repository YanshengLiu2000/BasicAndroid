package yanshengliu.bluetoothzhazha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private TextView check_text;
    private ListView mlistView;
    private Button check;
    private Button search;
    private BluetoothAdapter btAdapter;
    private List<String> mArrayAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);


        registerReceiver(mBluetoothReceiver, intentFilter);

        check_text = (TextView) findViewById(R.id.check_text);
        Button check = (Button) findViewById(R.id.check);
        Button search = (Button) findViewById(R.id.search);
        mlistView = (ListView) findViewById(R.id.b_list);
        btAdapter = BluetoothAdapter.getDefaultAdapter();


        check.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (btAdapter == null) {
                    check_text.setText(String.valueOf("there is no Bluetooth interface"));
                }
                if (!btAdapter.isEnabled()) {
                    btAdapter.enable();

                }
                check_text.setText(String.valueOf("Bluetooth is enabled"));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                mArrayAdapter.clear();
                boolean isDiscovering = btAdapter.startDiscovery();

            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver(){
        @Override

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!mArrayAdapter.contains(device.getName() + "\n"
                        +device.getAddress())){
                    mArrayAdapter.add(device.getName() + "\n"
                            +device.getAddress());
                }


            }
            mlistView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    mArrayAdapter));

        }
    };
}
