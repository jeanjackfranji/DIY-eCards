package com.ISD.diy_ecards.ecards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;

public class ChooseFriendBday extends Activity {

    private  LayoutInflater inflater = null;
    private  String arr_images[];
    private int counter = 0;
    private String[] fbFriends = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend_bday);
        inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        ImageButton btnExit = (ImageButton) findViewById(R.id.Btn_ClosePopUpWindow);
        btnExit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Find the ListView resource.
        final ListView mainListView = (ListView) findViewById( R.id.LV_FBFriends );
        String frIDs = "";
        Intent i = getIntent();
        if(i.hasExtra("FriendsIDs")) {
            frIDs = i.getStringExtra("FriendsIDs").toString();
            frIDs = frIDs.substring(0,frIDs.length()-1);
        }
        arr_images = new String[frIDs.split(",").length];
        fbFriends = new String[frIDs.split(",").length];
        String fqlQuery = "SELECT uid, name, pic FROM user \n" +
                    "WHERE uid in (SELECT uid2 FROM friend WHERE uid1 = me()) \n" +
                    "AND uid in ("+frIDs+");";
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.openActiveSession(this,true,callback);
        Request request = new Request(session, "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                        public void onCompleted(Response response) {
                            Log.i("ME:", "Result: " + response.toString());
                            buildListViewImages(response.getGraphObject());
                            mainListView.setAdapter(new MyAdapter(getBaseContext(), R.layout.row_fb_friends, fbFriends));
                            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(ChooseFriendBday.this, EcardDesigner.class);
                                    i.putExtra("FrName",mainListView.getAdapter().getItem(position).toString());
                                    startActivity(i);
                                    // close this activity
                                    finish();
                                }
                            });
                        }
                    }
            );
            Request.executeBatchAsync(request);
    }
    // Method that's Responds to the FB Sessions Changes
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    // Method used to determine if the FB session is Opened or Closed
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("ME:", "Logged in...");
        }
        else if (state.isClosed()) {
            Log.i("ME:", "Logged out...");
        }
    }
    private void buildListViewImages(GraphObject users) {
        try {
            JSONObject jso = users.getInnerJSONObject();
            JSONArray arr = jso.getJSONArray("data");
            for ( int i = 0; i < arr.length(); i++ ) {
                JSONObject json_obj = arr.getJSONObject(i);
                String Pic = json_obj.getString("pic");
                String Name = json_obj.getString("name");
                fbFriends[counter] = Name;
                arr_images[counter] = Pic;
                counter++;
            }
        }catch (Exception e ){e.printStackTrace();}
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    // Custom Adapter used for the ComboBox
    private class MyAdapter extends ArrayAdapter<String>{
        String [] strings;
        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
            strings = objects;
        }
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row= inflater.inflate(R.layout.row_fb_friends, parent, false);
            TextView sub=(TextView)row.findViewById(R.id.txt_View_ColorName);
            sub.setText(strings[position]);
            ImageView icon=(ImageView)row.findViewById(R.id.ImgView_Text_color);
            icon.setTag(position);
            new DownloadImageTask(icon).execute(arr_images[position]);
            return row;
        }
    }
}
