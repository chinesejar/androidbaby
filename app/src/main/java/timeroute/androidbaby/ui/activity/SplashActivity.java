package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.appcompat.BuildConfig;
import android.widget.TextView;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.SplashPresenter;
import timeroute.androidbaby.ui.view.ISplashView;
import timeroute.androidbaby.util.SharedPreferenceUtils;
import timeroute.androidbaby.widget.SplashView;

public class SplashActivity extends IBaseActivity<ISplashView, SplashPresenter> implements ISplashView {

    private static final String TAG = "SplashActivity";

    private boolean isLogin = false;

    @Bind(R.id.splash_view)
    SplashView splash_view;
    @Bind(R.id.tv_splash_info)
    TextView tv_splash_info;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onStart() {
        super.onStart();

        startLoadingData();
    }

    private void startLoadingData() {
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");
        String token = sharedPreferenceUtils.getString("token");
        String username = sharedPreferenceUtils.getString("username");
        String password = sharedPreferenceUtils.getString("password");
        new Handler().postDelayed(() -> {
            if (username != null && password != null) {
                if(token != null) {
                    mPresenter.refreshToken(token);
                    //mPresenter.getToken(username, password);
                }else {
                    mPresenter.getToken(username, password);
                }
            } else {
                onLoadingDataEnded();
            }
        }, 1500);
    }

    private void onLoadingDataEnded() {
        // start the splash animation
        splash_view.splashAndDisappear(new SplashView.ISplashListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onUpdate(float completionFraction) {
                // log animation update events
                if (BuildConfig.DEBUG) {
                }
            }

            @Override
            public void onEnd() {
                // log the animation end event
                if (BuildConfig.DEBUG) {
                }
                // free the view so that it turns into garbage
                splash_view = null;
                goToMain();
            }
        });
    }

    public void goToMain() {
        if (isLogin) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void toMain() {
        isLogin = true;
        onLoadingDataEnded();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, 0);
    }
}
