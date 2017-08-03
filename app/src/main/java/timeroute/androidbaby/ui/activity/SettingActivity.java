package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.SettingPresenter;
import timeroute.androidbaby.ui.view.ISettingView;

public class SettingActivity extends IBaseActivity<ISettingView, SettingPresenter> implements ISettingView {

    private static final String TAG = "SettingActivity";

    @Bind(R.id.setting_about)
    LinearLayout layoutAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTracker(TAG);
        layoutAbout.setOnClickListener(view -> {
            startActivity(new Intent(this, AboutActivity.class));
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
