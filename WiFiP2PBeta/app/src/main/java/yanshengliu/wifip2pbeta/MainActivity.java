package yanshengliu.wifip2pbeta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;

public class MainActivity extends AppCompatActivity {

    private TextView deviceCondition;
    private Button checkWiFiDirectBtn;
    private Button peerDiscoveryBtn;
    private ListView listView1;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private IntentFilter intentFilter;
    private WiFiP2PReceiver wiFiP2PReceiver;
    private List<WifiP2pDevice> peerList= new ArrayList<WifiP2pDevice>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceCondition=(TextView) findViewById(R.id.textView1);
        checkWiFiDirectBtn=(Button) findViewById(R.id.checkWiFiDirectBtn);
        peerDiscoveryBtn=(Button) findViewById(R.id.peerDiscoveryBtn);
        listView1=(ListView) findViewById(R.id.listView1);

        mManager=(WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

//        intentFilter=new IntentFilter();
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        wiFiP2PReceiver =new WiFiP2PReceiver();
//        registerReceiver(wiFiP2PReceiver, intentFilter);

        checkWiFiDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mManager.WIFI_P2P_STATE_ENABLED!=2){
                    deviceCondition.setText("Wifi-P2P function is closed.");
                }else{
                    deviceCondition.setText("Wifi Direct is Available!");
                }
            }
        });

        peerDiscoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //test
                        for(int j=0;j<10;j++){
                            System.out.println("~~~~Success~~~~Success~~~~~~Success!!!");
                            intentFilter=new IntentFilter();
                            intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                            wiFiP2PReceiver =new WiFiP2PReceiver();
                            registerReceiver(wiFiP2PReceiver, intentFilter);
                        }
                        //test
                    }

                    @Override
                    public void onFailure(int i) {
                        //test
                        for(int j=0;j<10;j++){
                            System.out.println("~~~~failure~~~~failure~~~~~~failure!!!");
                        }
                        //test
                    }
                });
            }
        });




    }



    class WiFiP2PReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Copy That!!", Toast.LENGTH_SHORT).show();
            for(int j=0;j<10;j++){
                System.out.println("Receiver copy that!Receiver copy that!Receiver copy that!Receiver copy that!");
            }
            if(null!= mManager){
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        List tempList=new ArrayList();
                        tempList.addAll(peers.getDeviceList());
                        if(tempList.size()==0){
                            tempList.add("There is no available devices nearby~");

                        }
                        ListAdapter listadapter= new ArrayAdapter<WifiP2pDevice>(MainActivity.this,android.R.layout.simple_expandable_list_item_1, tempList);
                        listView1.setAdapter(listadapter);

                        for(int j=0;j<10;j++){
                            System.out.println("+++++++++++++++++peers list size: "+peers.getDeviceList().size());
                        }
                        for(int j=0;j<10;j++){
                            for(WifiP2pDevice device: peers.getDeviceList()){
                                System.out.println("++++++++++++++device address:"+device.deviceName+"+++name:"+device.deviceName);
                            }
                        }

                    }
                });
            }


        }
    }


}
