package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IImageViewView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class ImageViewPresenter extends BasePresenter<IImageViewView> {

    private static final String TAG = "ImageViewPresenter";

    private Context context;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public ImageViewPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void saveImage(String image_url) {
        Log.d(TAG, "image url: "+image_url);
        String[] recs = image_url.split("/");
        String filename = recs[recs.length-1];
        Log.d(TAG, "name: "+filename);

        byte[] bs = new byte[1024];
        int len;
        try{
            //通过文件地址构建url对象
            URL url = new URL(image_url);
            //获取链接
            //URLConnection conn = url.openConnection();
            //创建输入流
            InputStream is = url.openStream();
            //获取文件的长度
            //int contextLength = conn.getContentLength();
            //输出的文件流
            OutputStream os = new FileOutputStream(getOutputMediaFile(filename));
            //开始读取
            while((len = is.read(bs)) != -1){
                os.write(bs,0,len);
            }
            //完毕关闭所有连接
            os.close();
            is.close();
            Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
        }catch(MalformedURLException e){
            Toast.makeText(context, "图片 URL 格式错误", Toast.LENGTH_SHORT).show();
        }catch(FileNotFoundException e){
            Toast.makeText(context, "无法加载文件", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            Toast.makeText(context, "获取连接失败", Toast.LENGTH_SHORT).show();
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(String mImageName){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        Log.d(TAG, "/Download");
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
        File mediaFile;
        Log.d(TAG, "file: "+mediaStorageDir.getPath() + File.separator + mImageName);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
