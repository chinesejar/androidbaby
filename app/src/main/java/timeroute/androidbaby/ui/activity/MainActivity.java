package timeroute.androidbaby.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.adapter.ViewPagerFgAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.fragment.DiscoveryFragment;
import timeroute.androidbaby.ui.fragment.FeedFragment;
import timeroute.androidbaby.ui.fragment.MineFragment;
import timeroute.androidbaby.util.SharedPreferenceUtils;
import timeroute.androidbaby.widget.NoScrollViewPager;

public class MainActivity extends IBaseActivity {

    private static final String TAG = "MainActivity";

    private SharedPreferenceUtils sharedPreferenceUtils;

    @Bind(R.id.content_viewPager)
    NoScrollViewPager content_viewPager;
    @Bind(R.id.navigation_bar)
    BottomNavigationView navigation_bar;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private List<IBaseFragment> fragmentList;

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    sharedPreferenceUtils.setString("alias", String.valueOf(sharedPreferenceUtils.getInt("id")));
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigationView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initNavigationView() {

        setTracker(TAG);
        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");
        initAndSetAlias();

        fragmentList = new ArrayList<>();
        fragmentList.add(new FeedFragment());
        fragmentList.add(new DiscoveryFragment());
        fragmentList.add(new MineFragment());
        content_viewPager.setOffscreenPageLimit(3);
        content_viewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(), fragmentList));
        content_viewPager.addOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigation_bar.setSelectedItemId(R.id.navigation_home);
                        mToolbar.setTitle(getString(R.string.app_name));
                        break;
                    case 1:
                        navigation_bar.setSelectedItemId(R.id.navigation_discovery);
                        mToolbar.setTitle(getString(R.string.tab_discovery));
                        break;
                    case 2:
                        navigation_bar.setSelectedItemId(R.id.navigation_profile);
                        mToolbar.setTitle(getString(R.string.tab_profile));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        navigation_bar.setOnNavigationItemSelectedListener(item -> {
            content_viewPager.setCurrentItem(item.getOrder());
            if(item.getOrder() != 0){
                floatingActionButton.setVisibility(View.INVISIBLE);
            }else {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
            return true;
        });
    }

    private void initAndSetAlias() {
        //JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
        String alias = String.valueOf(sharedPreferenceUtils.getInt("id"));
        String cur_alias = sharedPreferenceUtils.getString("alias");
        if(cur_alias != null){
            if(cur_alias.equals(alias)){
                return;
            }
        }
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };
}
