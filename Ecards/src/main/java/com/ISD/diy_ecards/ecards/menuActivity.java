package com.ISD.diy_ecards.ecards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class menuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //start the service from here //MyService is your service class name
        startService(new Intent(this, FBFriendsBdayService.class));
    }
    public void Btn_ExitClicked(View v) {
        stopService(new Intent(this, FBFriendsBdayService.class));
        finish();
    }
    public void Btn_MakeEcard_Clicked(View v)
    {
        Intent i = new Intent(menuActivity.this, EcardDesigner.class);
        startActivity(i);
        // close this activity
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about){
            Intent i = new Intent(this, AboutApplication.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
