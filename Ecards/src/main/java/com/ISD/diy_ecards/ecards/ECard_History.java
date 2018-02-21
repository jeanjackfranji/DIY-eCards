package com.ISD.diy_ecards.ecards;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ISD.custom_ListView.EcardHistoyCustomAdapter;

public class ECard_History extends Activity {
    String [] buildOptions= {""};
    String [] prgmImages = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecard__history);
        final String DB_NAME = "DIYEcards";
        String buildOption;
        SQLiteDatabase myDB = null;
        final ListView mainListView=(ListView) findViewById(R.id.LV_EcardHistory);
        try{
            myDB = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
            Cursor c = myDB.rawQuery("SELECT * FROM ECardHistory ORDER BY ID DESC limit 10", null);
            if(c.getCount() > 0 ) {
                prgmImages = new String[c.getCount()];
                buildOptions = new String[c.getCount()];
                int count = 0;
                while(c.moveToNext())
                {
                    prgmImages[count] = c.getString(c.getColumnIndex("image"));
                    buildOption = c.getString(c.getColumnIndex("title"))+":"+c.getString(c.getColumnIndex("body"))+":"+c.getInt(c.getColumnIndex("backroundImgPosition"))+":"+c.getInt(c.getColumnIndex("bodyImage"));
                    buildOptions[count] = buildOption;
                    count++;
                }
            }
            else
                mainListView.setVisibility(View.INVISIBLE);
        }
        catch ( Throwable t ){
            t.printStackTrace();
            mainListView.setVisibility(View.INVISIBLE);
        }
        finally{if (myDB != null) myDB.close();}

        mainListView.setAdapter(new EcardHistoyCustomAdapter(getBaseContext(),prgmImages));
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ECard_History.this,EcardDesigner.class);
                i.putExtra("ECardOptions",buildOptions[position]);
                startActivity(i);
                finish(); // close this activity
            }
        });
    }
    // Implemented By Jean-Jack (navigate back to the main menu)
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ECard_History.this, menuActivity.class);
        startActivity(i);
        finish(); // close this activity
    }
}
