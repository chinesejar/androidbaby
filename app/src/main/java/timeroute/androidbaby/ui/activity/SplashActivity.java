package timeroute.androidbaby.ui.activity;

import android.content.Intent;
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
import timeroute.androidbaby.widget.SplashView;

public class SplashActivity extends IBaseActivity {

    private static final String TAG = "SplashActivity";

    private Handler handler = new Handler();

    @Bind(R.id.splash_view)
    SplashView splash_view;
    @Bind(R.id.tv_splash_info)
    TextView tv_splash_info;

    @Override
    protected BasePresenter createPresenter() {
        return null;
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
        Random random = new Random();
        handler.postDelayed(this::onLoadingDataEnded, 1000 + random.nextInt(2000));
    }

    private void onLoadingDataEnded(){
        // start the splash animation
        splash_view.splashAndDisappear(new SplashView.ISplashListener(){
            @Override
            public void onStart(){
                // log the animation start event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction){
                // log animation update events
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd(){
                // log the animation end event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash ended");
                }
                // free the view so that it turns into garbage
                splash_view = null;
                goToMain();
            }
        });
    }

    public void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,0);
    }
}
