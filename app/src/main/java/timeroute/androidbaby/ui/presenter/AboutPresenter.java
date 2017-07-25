package timeroute.androidbaby.ui.presenter;

import android.content.Context;

import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IAboutView;
import timeroute.androidbaby.ui.view.ISettingView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class AboutPresenter extends BasePresenter<IAboutView> {

    private static final String TAG = "AboutPresenter";

    private Context context;
    private IAboutView aboutView;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public AboutPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }
}
