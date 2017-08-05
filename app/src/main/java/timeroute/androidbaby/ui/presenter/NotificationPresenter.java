package timeroute.androidbaby.ui.presenter;

import android.content.Context;

import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.INotificationView;
import timeroute.androidbaby.ui.view.ISettingView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-25.
 */

public class NotificationPresenter extends BasePresenter<INotificationView> {

    private static final String TAG = "SettingPresenter";

    private Context context;
    private ISettingView settingView;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public NotificationPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }
}
