package com.ISD.diy_ecards.ecards;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private int isPermanentlySkipped = 0;
    private int isFacebookLoggedIn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                checkMainSetupCompletion();
                Intent i;
                if(isPermanentlySkipped == 1 || isFacebookLoggedIn == 1 )
                    i = new Intent(SplashScreen.this, menuActivity.class);
                else
                    i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkMainSetupCompletion() {
        SQLiteDatabase myDB = null;
        Cursor c= null;
        try {
            myDB = this.openOrCreateDatabase("DIYEcards",MODE_PRIVATE, null);
            String TableName = "UserConfig";
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, isPermSkipped boolean, isFBLoggedIn boolean );");
            /*retrieve data from database */
            c = myDB.rawQuery("SELECT * FROM UserConfig", null);

            int isPermSkipped = c.getColumnIndex("isPermSkipped");
            int isFBLoggedIn = c.getColumnIndex("isFBLoggedIn");
            // Check if our result was valid.
            c.moveToLast();
            if (c.getCount() != 0) {
                    int isSk = c.getInt(isPermSkipped);
                    int isFBLI = c.getInt(isFBLoggedIn);
                    if(isSk == 1)  isPermanentlySkipped = 1;
                    if(isFBLI == 1) isFacebookLoggedIn = 1 ;
            }
        }catch (Exception e) {
            Log.e("Error", "Error", e);
        }finally {
            if (c != null || !c.isClosed())
                c.close();
            if (myDB != null)
                myDB.close();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
