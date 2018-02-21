package com.ISD.diy_ecards.ecards;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ISD.slidingmenu.model.NavDrawerItem;
import com.ISD.slidingmenu.model.NavDrawerListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment ECardDesignersFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    // slide menu items
    String[] navMenuTitles;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);


    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ArrayList<NavDrawerItem> navDrawerItems;
        TypedArray navMenuIcons;
        NavDrawerListAdapter adapter;
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //mDrawerListView = (ListView) getActivity().findViewById(R.id.BackgroundListView);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        //adding nav drawer items to array Background List
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[13], navMenuIcons.getResourceId(13, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[14], navMenuIcons.getResourceId(14, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[15], navMenuIcons.getResourceId(15, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[16], navMenuIcons.getResourceId(16, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[17], navMenuIcons.getResourceId(17, -1)));
        navMenuIcons.recycle();
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getActivity().getApplicationContext(),navDrawerItems);
        mDrawerListView.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.ic_drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
   }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_FullScreen) {
            final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            ECardDesignersFragment = getActivity().getFragmentManager().findFragmentByTag("EcardDesignerFragment");
            ECardDesignersFragment.getView().setSystemUiVisibility(uiOptions);
            Toast.makeText(getActivity(), "Full Screen Mode", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(item.getItemId() == R.id.action_exit) {
            getActivity().finish();
            return true;
        }
        else if(item.getItemId() == R.id.action_save)
        {
            try {
                TextView txt_title = (TextView)ECardDesignersFragment.getView().findViewById(R.id.txt_View_Titles);
                TextView txt_body = (TextView)ECardDesignersFragment.getView().findViewById(R.id.txt_view_BodyText);
                if(txt_title.getText().length() == 0)
                    txt_title.setText(" ");
                if(txt_body.getText().length() == 0)
                    txt_body.setText(" ");
                View content = ECardDesignersFragment.getView();
                content.setDrawingCacheEnabled(true);
                Bitmap bitmap = content.getDrawingCache();
                File file, f= null;
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    file = new File(android.os.Environment.getExternalStorageDirectory(), "DIY_Ecards");
                    if (!file.exists())
                        file.mkdirs();
                    Date d = new Date();
                    CharSequence s  = DateFormat.format("mm-yy h-mm-ss", d.getTime());
                    f = new File(file.getAbsolutePath() + file.separator + "DIYEcard_"+s.toString()+ ".png");
                }
                FileOutputStream ostream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                ostream.close();
                if(txt_title.getText().equals(" "))
                    txt_title.setText("");
                if(txt_body.getText().equals(" "))
                    txt_body.setText("");
                Toast.makeText(getActivity(),"Saved In Gallery",Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(getActivity(),"Error In Saving Process",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFragmentBackground()
    {
        try {
            ECardDesignersFragment = getActivity().getFragmentManager().findFragmentByTag("EcardDesignerFragment");
            setEcardBackgroundTitle(navMenuTitles[mCurrentSelectedPosition]);
        }catch (NullPointerException npe){ npe.printStackTrace();}
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.Background);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private void setEcardBackgroundTitle(String val)
    {
        try {
            if (val.equals("Black"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_black, 480, 270)));
            if (val.equals("Gray"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_gray, 480, 270)));
            else if (val.equals("White"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_white, 480, 270)));
            else if (val.equals("Beige"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_beige, 480, 270)));
            else if (val.equals("Brown"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_brown, 480, 270)));
            else if (val.equals("Violet"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_violet, 480, 270)));
            else if (val.equals("Blue Violet"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_blue_violet, 480, 270)));
            else if (val.equals("Dark Blue"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_dark_blue, 480, 270)));
            else if (val.equals("Blue"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_blue, 480, 270)));
            else if (val.equals("Light Blue"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_light_blue, 480, 270)));
            else if (val.equals("Dark Green"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_dark_green_cyan, 480, 270)));
            else if (val.equals("Green"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_green, 480, 270)));
            else if (val.equals("Pure Green"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_pure_green, 480, 270)));
            else if (val.equals("Yellow"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_yellow, 480, 270)));
            else if (val.equals("Dark Red"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_dark_red, 480, 270)));
            else if (val.equals("Red"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_red, 480, 270)));
            else if (val.equals("Orange"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_orange, 480, 270)));
            else if (val.equals("Pink"))
                ECardDesignersFragment.getView().setBackground(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.background_pink, 480, 270)));
        }catch(Exception npe){npe.printStackTrace();}
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
