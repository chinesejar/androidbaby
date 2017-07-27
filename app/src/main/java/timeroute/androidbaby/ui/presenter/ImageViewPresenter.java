package timeroute.androidbaby.ui.presenter;

import android.content.Context;

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
}
