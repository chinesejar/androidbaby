package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IImageViewView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class ImageViewPresenter extends BasePresenter<IImageViewView> {

    private static final String TAG = "ImageViewPresenter";

    IImageViewView imageViewView;
    private Context context;
    private SharedPreferenceUtils sharedPreferenceUtils;
    private ThinDownloadManager thinDownloadManager;

    public ImageViewPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void saveImage(String image_url) {
        imageViewView = getView();
        Log.d(TAG, "image url: "+image_url);
        String[] recs = image_url.split("/");
        String filename = recs[recs.length-1];
        Log.d(TAG, "name: "+filename);

        String filepath = getOutputMediaFilePath(filename);
        if(filepath == null){
            return;
        }
        Uri downloadUri = Uri.parse(image_url);
        Uri destinationUri = Uri.parse(filepath);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Log.d(TAG, "complete");
                        imageViewView.setDisplayProgress(false);
                        thinDownloadManager.release();
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        imageViewView.setDisplayProgress(false);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                        Log.d(TAG, "progress: "+progress);
                        imageViewView.setProgressIndex(progress);
                    }
                });
        thinDownloadManager = new ThinDownloadManager();
        imageViewView.setProgressIndex(0);
        imageViewView.setDisplayProgress(true);
        thinDownloadManager.add(downloadRequest);
    }

    /** Create a File for saving an image or video */
    private  String getOutputMediaFilePath(String mImageName){
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
        Log.d(TAG, mediaStorageDir.getPath() + File.separator + mImageName);
        if(new File(mediaStorageDir.getPath() + File.separator + mImageName).exists()){
            Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        return mediaStorageDir.getPath() + File.separator + mImageName;
    }
}
