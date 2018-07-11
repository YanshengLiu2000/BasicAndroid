package yanshengliu.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ylxh5 on 8/21/17.
 */

public class MyBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Received in MyBroadcastReceiver",Toast.LENGTH_SHORT).show();
    }
}
