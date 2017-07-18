package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.user.ImageToken;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IMineView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    private Context context;
    private IMineView mineView;

    private SharedPreferenceUtils sharedPreferenceUtils;

    public MinePresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void getToken(){
        mineView = getView();
        String token = sharedPreferenceUtils.getString("token");
        if(mineView != null){
            userApi.getImageToken("Token "+token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imageToken -> {
                        displayToken(imageToken);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void displayToken(ImageToken imageToken) {
        UploadManager uploadManager = new UploadManager();
        File data = mineView.getAvatar();
        File compressedImage = null;
        try {
            compressedImage = new Compressor(this.context)
                    .setMaxWidth(400)
                    .setMaxHeight(400)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(data);
        }catch (IOException e){

        }
        if(compressedImage != null){
            String name = String.valueOf(sharedPreferenceUtils.getInt("id"))+"_"+System.currentTimeMillis()+".jpg";
            uploadManager.put(compressedImage, name, imageToken.getToken(), (key, info, response) -> {
                if(info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                    Profile profile = new Profile();
                    profile.setAvatar(baseAvatarUrl+name);
                    int id = sharedPreferenceUtils.getInt("id");
                    String token = sharedPreferenceUtils.getString("token");
                    userApi.putProfile("Token "+token, id, profile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(avoid -> {
                                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                                sharedPreferenceUtils.setString("avatar", baseAvatarUrl+name);
                                mineView.setAvatar(baseAvatarUrl+name);
                            }, this::loadError);
                } else {
                    Log.i("qiniu", "Upload Fail");
                    Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }, null);
        }
    }

}
