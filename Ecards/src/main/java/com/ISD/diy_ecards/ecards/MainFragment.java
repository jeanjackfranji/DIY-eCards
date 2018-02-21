package com.ISD.diy_ecards.ecards;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import fr.castorflex.android.flipimageview.library.FlipImageView;
import android.database.sqlite.SQLiteDatabase;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MainFragment extends Fragment {

    // used for debugging purposes
    private static final String TAG = "MainFragment";
    private int sync = 0;
    private UiLifecycleHelper uiHelper;
    private View view;
    private boolean isFBLoggedIn = false;
    private final String DB_NAME = "DIYEcards";
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */

    public static MainFragment newInstance() {
        return (new MainFragment());
    }
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    // Override Already Android Implemented Methods but add the uiHelper to handle authentication issues
    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    // Modified By Jean-Jack: Fragment Added to the Main Activity
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        // Return the OnActivityResults to the Fragment instead of the Activity
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        // ADDED TO FETCH DATA
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_birthday", "friends_birthday"));

        // Flip Image View control flips the welcome text to login facebook text
        final FlipImageView fiv = (FlipImageView) view.findViewById(R.id.imageview);
        //Countdown Timer used for animation purposes
        new CountDownTimer(1500, 1000) {

            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                fiv.setClickable(false);
                fiv.toggleFlip();
            }
        }.start();
        new CountDownTimer(2500, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                try {
                    ImageView myImageView1 = (ImageView) view.findViewById(R.id.ImgViewPromotion1);
                    ImageView myImageView2 = (ImageView) view.findViewById(R.id.ImgViewPromotion2);
                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
                    myImageView1.setVisibility(View.VISIBLE);
                    myImageView2.setVisibility(View.VISIBLE);
                    myImageView1.startAnimation(myFadeInAnimation); //Set animation to your ImageView1
                    myImageView2.startAnimation(myFadeInAnimation); //Set animation to your ImageView2
                    if (fiv.isFlipped()) fiv.toggleFlip(false);
                }catch(NullPointerException npe){npe.printStackTrace();};
            }
        }.start();

        Button skipBtn = (Button) view.findViewById(R.id.btn_Skip);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setUserConfiguration();
                Intent i = new Intent(getActivity(), menuActivity.class);
                startActivity(i);
                // close this activity
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // Method used to determine if the FB session is Opened or Closed
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        final TextView welcome = (TextView) view.findViewById(R.id.txtViewDisplayName);
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            isFBLoggedIn = true;
            setUserConfiguration();
            // make request to the /me API
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        welcome.setText("Hello " + user.getName() + "!");
                    }
                }
            }).executeAsync();
            if(sync == 0){
                getUserFriendsBirthday();
                sync = 1;
            }
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            isFBLoggedIn = false;
            setUserConfiguration();
            welcome.setText("");
        }
    }
    // Method that's Responds to the FB Sessions Changes
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    //HELPER METHOD
    private void buildUserFriendsBirthdayDB(GraphObject users) {
        SQLiteDatabase myDB = null;
        try{
            JSONObject  jso = users.getInnerJSONObject();
            JSONArray   arr = jso.getJSONArray( "data" );
            myDB = getActivity().openOrCreateDatabase(DB_NAME,getActivity().MODE_PRIVATE,null);
            myDB.execSQL("DROP TABLE IF EXISTS FBFriendsList;");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS FBFriendsList "
                    + " (uid BIGINT PRIMARY KEY, name NVARCHAR, birthday_date NVARCHAR);");
            Log.i(TAG, "ArraySize: " + arr.length());
            for ( int i = 0; i < arr.length(); i++ )
            {
                JSONObject json_obj = arr.getJSONObject(i);
                long id     =  Long.parseLong(json_obj.getString("uid"));
                String name   = json_obj.getString("name");
                String Birthday = json_obj.getString("birthday_date");
                if(!Birthday.equals("null")) {
                        /* Insert data to a Table*/
                        myDB.execSQL("INSERT INTO FBFriendsList "
                                + " (uid, name, birthday_date)"
                                + " VALUES ('"+id+"','"+name+"','"+Birthday+"');");
                }
            }
            Intent i = new Intent(getActivity(), menuActivity.class);
            startActivity(i);
            getActivity().finish(); // close this activity
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
        }
        finally{
            if (myDB != null)
                myDB.close();
        }
    }

    private void getUserFriendsBirthday() {
                String fqlQuery = "SELECT uid, name, birthday_date FROM user \n" +
                        "WHERE uid in (SELECT uid2 FROM friend WHERE uid1 = me()) \n" +
                        "AND birthday_date != 'null' \n" +
                        "ORDER BY birthday_date DESC";
                Bundle params = new Bundle();
                params.putString("q", fqlQuery);
                Session session = Session.getActiveSession();
                Request request = new Request(session,"/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {
                        Log.i(TAG, "Result: " + response.toString());
                        buildUserFriendsBirthdayDB(response.getGraphObject());
                    }
                });
        Request.executeBatchAsync(request);
    }
    private void insertDataIntoDB(String TableName, String SQLStmt) {
        SQLiteDatabase myDB = null;
        try {
            myDB = getActivity().openOrCreateDatabase(DB_NAME, getActivity().MODE_PRIVATE, null);
            /* Insert data to a Table*/
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (isPermSkipped, isFBLoggedIn)"
                    + " VALUES ("+SQLStmt+");");
        }catch (Exception e) {
            Log.e("Error", "Error", e);
        }finally {
            if (myDB != null)
                myDB.close();
        }
    }
    // Insert User Configuration into the Database.
    private void setUserConfiguration() {
        CheckBox chkBox_Skip = (CheckBox) view.findViewById(R.id.chkBox_Skip);
        int fbLoggedIn = 0;
        if(isFBLoggedIn)  fbLoggedIn = 1;
        if(chkBox_Skip.isChecked())
            insertDataIntoDB("UserConfig","1,"+fbLoggedIn);
        else
            insertDataIntoDB("UserConfig","0,"+fbLoggedIn);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
