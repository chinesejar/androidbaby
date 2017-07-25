package timeroute.androidbaby.ui.presenter;

import android.content.Context;

import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ISettingView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {

    private static final String TAG = "SettingPresenter";

    private Context context;
    private ISettingView settingView;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public SettingPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }
}
