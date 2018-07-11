package yanshengliu.bluetooth_fucntions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button checkBtn;
    private Button discoveryBtn;
    private ListView listView;

    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter intentFilter;
    private List<String> bluetoothList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.textView);
        checkBtn=(Button) findViewById(R.id.checkBtn);
        discoveryBtn=(Button) findViewById(R.id.discoveryBtn);
        listView=(ListView) findViewById(R.id.listView);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                if(mBluetoothAdapter.isEnabled()){
                    textView.setText("Bluetooth is enabled.");
                }else{
                    textView.setText("zhazha!!!! There is no bluetooth module in this device!!!!!");
                }
            }
        });

        discoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothList=new ArrayList<String>();

                intentFilter=new IntentFilter();
                intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
                intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                registerReceiver(myReceiver,intentFilter);
                mBluetoothAdapter.startDiscovery();

                for(int j=0;j<10;j++){
                    System.out.println("=====================>Finish registration !");
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for(int j=0;j<10;j++){
                    System.out.println("=====================>"+bluetoothList.get(i));
                }
            }
        });



    }

    private BroadcastReceiver myReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Copy That!!", Toast.LENGTH_SHORT).show();
//            for(int j=0;j<10;j++){
//                System.out.println("=====================>Received msg!");
//            }
            String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String info=device.getName()+" : "+device.getAddress();
                if (!bluetoothList.contains(info)){
                    bluetoothList.add(info);
                    ListAdapter listAdapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,bluetoothList);
                    listView.setAdapter(listAdapter);
                    textView.setText("Found "+bluetoothList.size()+" devices available.");
                }
            }
            else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch(device.getBondState()){
                    case BluetoothDevice.BOND_BONDING:
                        for(int j=0;j<10;j++){
                        System.out.println("=====================>Bluetooth is peering!!!");
                        }
                }
            }
        }
    };

}
