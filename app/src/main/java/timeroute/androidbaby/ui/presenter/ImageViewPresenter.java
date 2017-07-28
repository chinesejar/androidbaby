package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IImageViewView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class ImageViewPresenter extends BasePresenter<IImageViewView> {

    private static final String TAG = "ImageViewPresenter";

    private Context context;
    private IImageViewView iImageViewView;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public ImageViewPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void saveImage(Drawable drawable) {
        Bitmap bitmap = null;
        if(drawable instanceof BitmapDrawable){
            Log.d(TAG, "is bitmapdrawable");
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            if(bitmapDrawable.getBitmap()!=null){
                bitmap = bitmapDrawable.getBitmap();
                try {
                    FileOutputStream fos = new FileOutputStream(getOutputMediaFile());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    Toast.makeText(context, "图片已保存", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Download");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmSSS").format(new Date());
        File mediaFile;
        String mImageName="AndroidBaby_"+ timeStamp +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
