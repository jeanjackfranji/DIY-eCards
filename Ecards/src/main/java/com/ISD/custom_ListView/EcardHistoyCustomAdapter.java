package com.ISD.custom_ListView;

/**
 * Created by Jean-Jack on 5/26/2014.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ISD.diy_ecards.ecards.R;

import java.io.File;

public class EcardHistoyCustomAdapter extends BaseAdapter{
    private Context context;
    private String[] imageId;
    private ImageView img;
    private static LayoutInflater inflater=null;

    public EcardHistoyCustomAdapter(Context mainActivity, String[] prgmImages) {
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.row_ecard_history_images, null);
        img=(ImageView) rowView.findViewById(R.id.ImgView_ImagePresets);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "DIY_Ecards"),EcardImg;
        Bitmap bitmap;
        if(file.exists()) {
            EcardImg = new File(file.getAbsolutePath() + file.separator +imageId[position] + ".png");
            if(EcardImg.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath() + file.separator + imageId[position] + ".png", options);
                img.setImageBitmap(bitmap);
            }
            else
                img.setBackgroundResource(R.drawable.background_white);
        }
        return rowView;
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
}
