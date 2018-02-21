package com.ISD.diy_ecards.ecards;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class FBFriendsBdayService extends Service {
    String TAG = "ME";
    int mId = 500;
    private final String DB_NAME = "DIYEcards";

    public FBFriendsBdayService() {
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        SQLiteDatabase myDB = null;
        String result = "";
        String date = "";
        try{
            myDB = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
            Cursor c = myDB.rawQuery("SELECT * FROM FBFriendsList", null);
            c.moveToNext();
            date = c.getString(c.getColumnIndex("birthday_date"));
            result = (c.getString(c.getColumnIndex("name")))+" is his/her Birthday Today";
        }
        catch ( Throwable t ){t.printStackTrace();}
        finally{if (myDB != null) myDB.close();}

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Birthday Reminder")
                .setContentText("Send an E-card Now!");
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        //Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Birthday details:");
        // Moves events into the big view
            inboxStyle.addLine(date);
            inboxStyle.addLine(result);
            inboxStyle.addLine("Send them an E-card Now!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, EcardDesigner.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(EcardDesigner.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Moves the big view style object into the notification object.
        mBuilder.setStyle(inboxStyle);
        // Issue the notification here.
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }
}
