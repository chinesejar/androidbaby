package timeroute.androidbaby.ui.activity;

import android.os.Bundle;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.AboutPresenter;
import timeroute.androidbaby.ui.view.IAboutView;

public class AboutActivity extends IBaseActivity<IAboutView, AboutPresenter> implements IAboutView {

    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected AboutPresenter createPresenter(){
        return new AboutPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }
}
