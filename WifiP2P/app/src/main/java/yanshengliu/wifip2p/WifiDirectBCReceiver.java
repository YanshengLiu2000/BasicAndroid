package yanshengliu.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by ylxh5 on 8/20/17.
 */

public class WifiDirectBCReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WifiDirectBCReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,MainActivity activity){
        super();
        this.mManager=manager;
        this.mChannel=channel;
        this.mActivity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action =intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            //check wifi status and tell an activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //Wifi p2p is opened.
            }else{
                //Wifi p2p is closed.
            }
        }else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            //use wifip2pmanager.requestpeers() get listview
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            //connected or disconnected
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //reply this.device
        }
    }
}
