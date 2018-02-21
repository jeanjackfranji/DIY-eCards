package com.ISD.Background_Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ISD.diy_ecards.ecards.FBFriendsBdayService;

/**
 * Created by Jean-Jack on 6/8/2014.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Intent myIntent = new Intent( context, FBFriendsBdayService.class );
            context.startService(myIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}