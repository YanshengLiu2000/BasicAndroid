package yanshengliu.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView deviceCondition;
    private Button checkWiFiDirectBtn;
    private Button peerDiscoveryBtn;
    private ListView listView1;
    private WifiP2pManager mManager;
    private IntentFilter mIntentFilter;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private List<WifiP2pDevice> peers= new ArrayList<WifiP2pDevice>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentFilter = new IntentFilter();
        //  Indicates a change in the Wi-Fi P2P status.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        deviceCondition=(TextView) findViewById(R.id.textView1);
        checkWiFiDirectBtn=(Button) findViewById(R.id.checkWiFiDirectBtn);
        peerDiscoveryBtn=(Button) findViewById(R.id.peerDiscoveryBtn);
        listView1=(ListView) findViewById(R.id.listView1);

        mManager=(WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);
        mReceiver=new WifiDirectBCReceiver(mManager,mChannel,this);

        for(int i=0;i<10;i++){
            System.out.println(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        }


        if(mManager.WIFI_P2P_STATE_ENABLED!=2){
            deviceCondition.setText("Wifi-P2P function is closed.");
        }




        checkWiFiDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mManager.WIFI_P2P_STATE_ENABLED!=2){
                    deviceCondition.setText("Wifi-P2P function is closed.");
                }else{
                    deviceCondition.setText("Wifi Direct is Available!");
                }

                int test=mManager.WIFI_P2P_STATE_ENABLED;
                for(int i=0;i<10;i++){
                    System.out.println("~~~~~~~~~~~~~~~~~~~"+test);
                }

                //onReceive(context, intent);
            }
        });
        peerDiscoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this gonna get a list view of p2p list!
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //nothing happens too
                        for(int i=0;i<10;i++){
                            System.out.println("~~~~Success~~~~Success~~~~~~Success!!!");
                        }
                    }

                    @Override
                    public void onFailure(int i) {
                        //nothing happens
                        for(int j=0;j<10;j++){
                            System.out.println("~~~~failure~~~~failure~~~~~~failure!!!");
                        }
                    }
                });

                WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener(){

                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        List<WifiP2pDevice> refreshedPeers= (List<WifiP2pDevice>) peerList.getDeviceList();
                        for(int i=0;i<10;i++){
                            System.out.println("Here is the onPeersAvailable process!!!!!");
                        }
                        ListAdapter listAdapter=new ArrayAdapter<WifiP2pDevice>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,refreshedPeers);
                        listView1.setAdapter(listAdapter);
                    }
                };
            }
        });



    }
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }

}


















