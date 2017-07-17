package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.SplashPresenter;
import timeroute.androidbaby.ui.view.ISplashView;
import timeroute.androidbaby.util.SharedPreferenceUtils;
import timeroute.androidbaby.widget.SplashView;

public class SplashActivity extends IBaseActivity<ISplashView, SplashPresenter> implements ISplashView {

    private static final String TAG = "SplashActivity";

    private Handler handler = new Handler();
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
        if (token != null) {
            long cur_timestamp = System.currentTimeMillis();
            long last_login = sharedPreferenceUtils.getLong("last_login");
            Log.d(TAG, String.valueOf(last_login));
            Log.d(TAG, String.valueOf(cur_timestamp));
            if(last_login != 0 && (last_login - cur_timestamp)/1000<3600){
                isLogin = true;
                new Handler().postDelayed(()->{
                    onLoadingDataEnded();
                }, 1500);
            }else{
                String username = sharedPreferenceUtils.getString("username");
                String password = sharedPreferenceUtils.getString("password");
                mPresenter.getToken(username, password);
            }
        } else {
            new Handler().postDelayed(() -> {
                onLoadingDataEnded();
            }, 1500);
        }
    }

    private void onLoadingDataEnded() {
        // start the splash animation
        splash_view.splashAndDisappear(new SplashView.ISplashListener() {
            @Override
            public void onStart() {
                // log the animation start event
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction) {
                // log animation update events
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd() {
                // log the animation end event
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "splash ended");
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
