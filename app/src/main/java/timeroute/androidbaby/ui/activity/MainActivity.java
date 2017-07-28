package timeroute.androidbaby.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.adapter.ViewPagerFgAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.fragment.DiscoveryFragment;
import timeroute.androidbaby.ui.fragment.FeedFragment;
import timeroute.androidbaby.ui.fragment.MineFragment;
import timeroute.androidbaby.widget.NoScrollViewPager;

public class MainActivity extends IBaseActivity {

    private static final String TAG = "MainActivity";
    private RxPermissions rxPermissions;

    @Bind(R.id.content_viewPager)
    NoScrollViewPager content_viewPager;
    @Bind(R.id.navigation_bar)
    BottomNavigationView navigation_bar;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private List<IBaseFragment> fragmentList;

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

        rxPermissions = new RxPermissions(this);
        floatingActionButton.setOnClickListener(view -> {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if(granted){
                            startActivity(new Intent(this, PostActivity.class));
                        }else {
                            Toast.makeText(this, "未开启权限", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

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
            return true;
        });
    }
}
