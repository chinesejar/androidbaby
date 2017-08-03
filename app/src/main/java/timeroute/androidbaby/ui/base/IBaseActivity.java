package timeroute.androidbaby.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import timeroute.androidbaby.MyApp;
import timeroute.androidbaby.R;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-13.
 */

public abstract class IBaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;
    private Tracker mTracker;
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //允许为空，不是所有都要实现MVP模式
        if(createPresenter()!=null) {
            mPresenter = createPresenter();
            mPresenter.attachView((V) this);
        }
        setContentView(provideContentViewId());//布局
        ButterKnife.bind(this);

        mTracker = getDefaultTracker();

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null && mAppBar != null) {
            setSupportActionBar(mToolbar); //把Toolbar当做ActionBar给设置
            if (canBack()) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null) {
            mPresenter.detachView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 此时android.R.id.home即为返回箭头
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public void setTracker(String name){
        mTracker.setScreenName("Fragment: " + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 判断当前 Activity 是否允许返回
     * 主界面不允许返回，次级界面允许返回
     *
     * @return false
     */
    public boolean canBack() {
        return false;
    }

    protected abstract T createPresenter();

    abstract protected int provideContentViewId();//用于引入布局文件

}
