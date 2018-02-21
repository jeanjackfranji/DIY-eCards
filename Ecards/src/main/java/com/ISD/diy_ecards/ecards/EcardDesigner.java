package com.ISD.diy_ecards.ecards;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import com.ISD.custom_ListView.CustomAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class EcardDesigner extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static NavigationDrawerFragment mNavigationDrawerFragment;
    private static View rootView;
    private static final int SELECT_PHOTO = 100;
    private static int maxX=0,maxY=0;
    private static int prX = 0; // Previous X Coordinate on TextView
    private static int prY = 0; // Previous Y Coordinate on TextView
    private static Context currentContext;
    private static TextView txt_view_Title;
    private static TextView txt_view_Body;
    private static ImageView img_view_Image;
    private static int textColorPosition = 0,bodyTextColorPosition = 0;
    private static int textSize = 70,bodyTextSize = 24;
    private static LayoutInflater inflater;
    private static String[] strings = { "Black", "White", "Orange", "VioletRed",
            "Green", "Blue", "Yellow" };
    private static int arr_images[] = { R.color.Black,R.color.Cornsilk, R.color.OrangeRed,
                                 R.color.MediumVioletRed,R.color.Green, R.color.RoyalBlue, R.color.Gold };
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecard_designer);
        currentContext = getBaseContext();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        Fragment temp = PlaceholderFragment.newInstance(position + 1);
        fragmentManager.beginTransaction()
               .replace(R.id.container, temp, "EcardDesignerFragment")
                .commit();
    }

    public void onSectionAttached(int number) { mTitle = "";}

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.ecard_designer, menu);
            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);
            // Fetch and store ShareActionProvider
            mShareActionProvider = (ShareActionProvider) item.getActionProvider();
            mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
                @Override
                public boolean onShareTargetSelected(ShareActionProvider arg0, Intent arg1) {
                    try {
                        if(txt_view_Title.getText().length() == 0)
                            txt_view_Title.setText(" ");
                        if(txt_view_Body.getText().length() == 0)
                            txt_view_Body.setText(" ");
                        View content = rootView;
                        content.setDrawingCacheEnabled(true);
                        Bitmap bitmap = content.getDrawingCache();
                        File file, f= null;
                        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                            file = new File(android.os.Environment.getExternalStorageDirectory(), "DIY_Ecards_Cache");
                            if (!file.exists())
                                file.mkdirs();
                            f = new File(file.getAbsolutePath() + file.separator + "DIYEcard" + ".png");
                        }
                        FileOutputStream ostream = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                        ostream.close();
                        if(txt_view_Title.getText().equals(" "))
                            txt_view_Title.setText("");
                        if(txt_view_Body.getText().equals(" "))
                            txt_view_Body.setText("");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            Intent share = new Intent(Intent.ACTION_SEND);
            // If you want to share a png image only, you can do:
            // setType("image/png"); OR for jpeg: setType("image/jpeg");
            share.setType("image/png");
            // Make sure you put example png image named myImage.png in your directory
            String imagePath = Environment.getExternalStorageDirectory() + "/DIY_Ecards_Cache/DIYEcard.png";
            File imageFileToShare = new File(imagePath);
            Uri uri = Uri.fromFile(imageFileToShare);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            //Set the share Intent
            setShareIntent(share);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static void createAndShowTitlePopUpWindow() {
        // Create and populate a List of greeting card titles.
        String[] greetingTitles = new String[] {"Congratulations!","Greetings","Happy Thanksgiving!","Happy Holidays!","Happy Valentines Day!", "Happy Birthday!", "My Condolences",
                "My Dearest Friend","My Sincerest Apologies","Get Well Soon!","Good Morning","Good Afternoon","Good Evening"};
        LayoutInflater layoutInflater
                = (LayoutInflater) currentContext
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_set_title, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final Spinner sp =  ((Spinner)popupView.findViewById(R.id.Spn_TxtColor));
        sp.post(new Runnable() {
            @Override
            public void run() {
                sp.setSelection(textColorPosition);
            }
        });
        final SeekBar sk =(SeekBar) popupView.findViewById(R.id.seekBar_Size);
        if(textSize == 60)
            sk.setProgress(0);
        else if(textSize == 80)
            sk.setProgress(2);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView txtSz = (TextView)popupView.findViewById(R.id.txt_View_TextSize);
                if(progress == 0)
                    txtSz.setText("Small");
                else if(progress == 1)
                    txtSz.setText("Medium");
                else
                    txtSz.setText("Large");
            }
        });
        Button btnSave = (Button) popupView.findViewById(R.id.Btn_Save);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            final EditText et = (EditText)popupView.findViewById(R.id.editText_Title);
            @Override
            public void onClick(View v) {
                ImageView selectedTextColor = (ImageView)(sp.getSelectedView()).findViewById(R.id.ImgView_Text_color);
                //Get Needed Controls From Pop Up View
                if(et.getText().toString().length() != 0)
                txt_view_Title.setText(" "+et.getText());
                textColorPosition = Integer.parseInt(selectedTextColor.getTag()+"");
                txt_view_Title.setTextColor(currentContext.getResources().getColor(arr_images[textColorPosition]));
                if(sk.getProgress() == 0)
                    txt_view_Title.setTextSize(textSize = 60);
                else if(sk.getProgress() == 1)
                    txt_view_Title.setTextSize(textSize = 70);
                else
                    txt_view_Title.setTextSize(textSize = 80);
                popupWindow.dismiss();
            }
        });
        ImageButton btnExit = (ImageButton) popupView.findViewById(R.id.Btn_ClosePopUpWindow);
        btnExit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // Find the ListView resource.
        final ListView mainListView = (ListView) popupView.findViewById( R.id.LV_Titles );
        ArrayList<String> titlesList = new ArrayList<String>();
        titlesList.addAll( Arrays.asList(greetingTitles));
        // Create ArrayAdapter using the Titles list.
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(currentContext, R.layout.simplerow, titlesList);
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView selectedTextColor = (ImageView)(sp.getSelectedView()).findViewById(R.id.ImgView_Text_color);
                textColorPosition = Integer.parseInt(selectedTextColor.getTag()+"");
                txt_view_Title.setTextColor(currentContext.getResources().getColor(arr_images[textColorPosition]));
                if(sk.getProgress() == 0)
                    txt_view_Title.setTextSize(textSize = 60);
                else if(sk.getProgress() == 1)
                    txt_view_Title.setTextSize(textSize = 70);
                else
                    txt_view_Title.setTextSize(textSize = 80);
                String Item = mainListView.getItemAtPosition(position).toString();
                txt_view_Title.setText(Item);
                popupWindow.dismiss();
            }
        });
        inflater = (LayoutInflater) currentContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        Spinner mySpinner = (Spinner)popupView.findViewById(R.id.Spn_TxtColor);
        mySpinner.setAdapter(new MyAdapter(currentContext, R.layout.row_spinner, strings));

        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(txt_view_Title, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }
    private static PopupWindow popupWindow;
    private static void createAndShowImagePopUpWindow() {
        LayoutInflater layoutInflater
                = (LayoutInflater) currentContext
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_set_image, null);
        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final int [] prgmImages={R.drawable.img_birthday_cake,R.drawable.img_have_a_good_day,R.drawable.img_get_well_soon,R.drawable.img_happy_thanksgiving,R.drawable.img_happyvalentines,
                                 R.drawable.img_snowman,R.drawable.img_tree,R.drawable.img_my_condolences,R.drawable.img_graduation};
        final ListView mainListView=(ListView) popupView.findViewById(R.id.LV_Images);
        mainListView.setAdapter(new CustomAdapter(currentContext,prgmImages));
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(position == 3)
                   img_view_Image.setImageBitmap(decodeSampledBitmapFromResource(currentContext.getResources(), prgmImages[position], 300, 200));
               else
                    img_view_Image.setImageBitmap(decodeSampledBitmapFromResource(currentContext.getResources(), prgmImages[position], 200, 150));
                popupWindow.dismiss();
            }
        });
        ImageButton btnExit = (ImageButton) popupView.findViewById(R.id.Btn_ClosePopUpWindow);
        btnExit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(txt_view_Title, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }
    private static void createAndShowBodyPopUpWindow() {
        LayoutInflater layoutInflater
                = (LayoutInflater) currentContext
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_set_body_text, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText et = (EditText)popupView.findViewById(R.id.edit_text_body);
        if(txt_view_Body.getText().length() != 0)
            et.setText(txt_view_Body.getText());
        final Spinner sp =  ((Spinner)popupView.findViewById(R.id.Spn_TxtColor));
        sp.post(new Runnable() {
            @Override
            public void run() {
                sp.setSelection(bodyTextColorPosition);
            }
        });
        final SeekBar sk =(SeekBar) popupView.findViewById(R.id.seekBar_Size);
        if(bodyTextSize == 24)
            sk.setProgress(0);
        else if(bodyTextSize == 36)
            sk.setProgress(2);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView txtSz = (TextView)popupView.findViewById(R.id.txt_View_TextSize);
                if(progress == 0)
                    txtSz.setText("Small");
                else if(progress == 1)
                    txtSz.setText("Medium");
                else
                    txtSz.setText("Large");
            }
        });
        Button btnSave = (Button) popupView.findViewById(R.id.Btn_Save);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView selectedBodyTextColor = (ImageView)(sp.getSelectedView()).findViewById(R.id.ImgView_Text_color);
                //Get Needed Controls From Pop Up View
                if(et.getText().toString().length() != 0)
                    txt_view_Body.setText(" "+et.getText());
                bodyTextColorPosition = Integer.parseInt(selectedBodyTextColor.getTag()+"");
                txt_view_Body.setTextColor(currentContext.getResources().getColor(arr_images[bodyTextColorPosition]));
                if(sk.getProgress() == 0)
                    txt_view_Body.setTextSize(bodyTextSize = 24);
                else if(sk.getProgress() == 1)
                    txt_view_Body.setTextSize(bodyTextSize = 30);
                else
                    txt_view_Body.setTextSize(bodyTextSize = 36);
                popupWindow.dismiss();
            }
        });
        ImageButton btnExit = (ImageButton) popupView.findViewById(R.id.Btn_ClosePopUpWindow);
        btnExit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        inflater = (LayoutInflater) currentContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        Spinner mySpinner = (Spinner)popupView.findViewById(R.id.Spn_TxtColor);
        mySpinner.setAdapter(new MyAdapter(currentContext, R.layout.row_spinner, strings));

        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(txt_view_Title, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }

    public void ImgView_ChooseImage_Clicked(View v)
    {
       Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
       photoPickerIntent.setType("image/*");
       startActivityForResult(photoPickerIntent, SELECT_PHOTO);
       popupWindow.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        img_view_Image.setImageBitmap(decodeUri(selectedImage));
                    }catch (FileNotFoundException fnfe){fnfe.printStackTrace();}
                }
        }
    }

     /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnTouchListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private TextView  _Textview;
        private ImageView _ImageView;
        private TextView _BodyTextView;
        private int _xDelta;
        private int _yDelta;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public PlaceholderFragment() {
        }
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    prX = X; prY = Y;
                    break;
                case MotionEvent.ACTION_UP:
                    if((prX >= X-2 && prX <= X+2) && (prY >= Y-2 && prY <= Y+2))
                        if(view instanceof TextView) {
                            TextView textView = (TextView) view;
                            if(textView.getId() == R.id.txt_View_Titles)
                                createAndShowTitlePopUpWindow();
                            else
                                createAndShowBodyPopUpWindow();
                        }
                        else
                            createAndShowImagePopUpWindow();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    if(X - _xDelta  > 0 && X < maxX)
                        layoutParams.leftMargin = X - _xDelta;
                    if(Y - _yDelta  > 0 && Y  < maxY)
                        layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootView.invalidate();
            return true;
        }
         @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_ecard_designer, container, false);
            rootView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            // Note that system bars will only be "visible" if none of the LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                getActivity().getActionBar().show();
                            } else {
                                getActivity().getActionBar().hide();
                            }
                        }
                    });
            return rootView;
        }
        @Override
        public void onViewCreated(View v, Bundle savedInstanceState) {
            super.onViewCreated(v, savedInstanceState);
            mNavigationDrawerFragment.setFragmentBackground();
            _Textview = (TextView)rootView.findViewById(R.id.txt_View_Titles);
            _Textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
            _ImageView = (ImageView)rootView.findViewById(R.id.imgView_EcardPicture);
            _BodyTextView = (TextView)rootView.findViewById(R.id.txt_view_BodyText);
            if(txt_view_Title != null) {
                if (txt_view_Title.getText().toString().length() != 0)
                    _Textview.setText(txt_view_Title.getText());
                _Textview.setTextSize(textSize);
                _Textview.setTextColor(currentContext.getResources().getColor(arr_images[textColorPosition]));
                if(txt_view_Title.getTag().equals(currentContext.getResources().getConfiguration().orientation))
                    _Textview.setLayoutParams(txt_view_Title.getLayoutParams());
            }
            if(img_view_Image != null) {
                _ImageView.setImageDrawable(img_view_Image.getDrawable());
                if(img_view_Image.getTag().equals(currentContext.getResources().getConfiguration().orientation))
                    _ImageView.setLayoutParams(img_view_Image.getLayoutParams());
            }
            if(txt_view_Body != null) {
                if (txt_view_Body.getText().toString().length() != 0)
                    _BodyTextView.setText(txt_view_Body.getText());
                _BodyTextView.setTextSize(bodyTextSize);
                _BodyTextView.setTextColor(currentContext.getResources().getColor(arr_images[bodyTextColorPosition]));
                if(txt_view_Body.getTag().equals(currentContext.getResources().getConfiguration().orientation))
                    _BodyTextView.setLayoutParams(txt_view_Body.getLayoutParams());
            }
            txt_view_Title = _Textview;
            txt_view_Body = _BodyTextView;
            img_view_Image = _ImageView;

            _Textview.setOnTouchListener(this);
            _BodyTextView.setOnTouchListener(this);
            _ImageView.setOnTouchListener(this);
            _Textview.setTag(getResources().getConfiguration().orientation);
            _BodyTextView.setTag(getResources().getConfiguration().orientation);
            _ImageView.setTag(getResources().getConfiguration().orientation);
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Dragon_is_coming.otf");
            Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(),"Shardee.ttf");
            txt_view_Title.setTypeface(font);
            txt_view_Body.setTypeface(font2);

            Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
            Point mdispSize = new Point();
            mdisp.getSize(mdispSize);
            maxX = mdispSize.x-300;
            maxY = mdispSize.y-200;
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((EcardDesigner) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    // Custom Adapter used for the ComboBox
    public static class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
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
            View row=inflater.inflate(R.layout.row_spinner, parent, false);
            TextView sub=(TextView)row.findViewById(R.id.txt_View_ColorName);
            sub.setText(strings[position]);
            ImageView icon=(ImageView)row.findViewById(R.id.ImgView_Text_color);
            icon.setTag(position);
            icon.setImageResource(arr_images[position]);
            return row;
        }
    }
    // Implemented By Jean-Jack (navigate back to the main menu)
    @Override
    public void onBackPressed() {
        Intent i = new Intent(EcardDesigner.this, menuActivity.class);
        startActivity(i);
        finish(); // close this activity
    }
    // Destroy Static Variables that are not being cleaned by Java's AGC
    @Override
    public void onDestroy() {
        super.onDestroy();
        mNavigationDrawerFragment = null;
        rootView = null;
        currentContext = null;
        txt_view_Title = null;
        txt_view_Body = null;
        img_view_Image = null;
    }
    // Method for Bitmap Handling
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
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 320;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
