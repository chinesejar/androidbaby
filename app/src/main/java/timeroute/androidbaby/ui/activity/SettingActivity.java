package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import timeroute.androidbaby.MyApp;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.SettingPresenter;
import timeroute.androidbaby.ui.view.ISettingView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

public class SettingActivity extends IBaseActivity<ISettingView, SettingPresenter> implements ISettingView {

    private static final String TAG = "SettingActivity";
    private SharedPreferenceUtils sharedPreferenceUtils;

    @Bind(R.id.switchNotify)
    Switch switchNotify;
    @Bind(R.id.setting_about)
    LinearLayout layoutAbout;
    @Bind(R.id.setting_logout)
    LinearLayout layoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");
        setTracker(TAG);
        switchNotify.setChecked(sharedPreferenceUtils.getBoolean("push_service"));
        switchNotify.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                if(JPushInterface.isPushStopped(getApplicationContext())){
                    JPushInterface.init(getApplicationContext());
                    sharedPreferenceUtils.setBoolean("push_service", true);
                }
            }else {
                if(!JPushInterface.isPushStopped(getApplicationContext())){
                    JPushInterface.stopPush(getApplicationContext());
                    sharedPreferenceUtils.setBoolean("push_service", false);
                }
            }
        });
        layoutAbout.setOnClickListener(view -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
        layoutLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.logout_title))
                    .setMessage(getString(R.string.logout_message))
                    .setNegativeButton(getString(R.string.cancel), ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }))
                    .setPositiveButton(getString(R.string.sure), ((dialogInterface, i) -> {
                        sharedPreferenceUtils.clearAll();
                        MyApp.getInstance().exit();
                    }))
                    .show();
        });
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }
}
