package timeroute.androidbaby.ui.activity;

import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;

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

    @Bind(R.id.content_viewPager)
    NoScrollViewPager content_viewPager;
    @Bind(R.id.navigation_bar)
    BottomNavigationView navigation_bar;

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

    private void initNavigationView() {

        fragmentList = new ArrayList<>();
        fragmentList.add(new FeedFragment());
        fragmentList.add(new DiscoveryFragment());
        fragmentList.add(new MineFragment());
        content_viewPager.setOffscreenPageLimit(3);
        // 禁止左右滑动，改为淡入淡出
//        content_viewPager.setPageTransformer(true, (view, position) -> {
//            view.setTranslationX(view.getWidth() * -position);
//
//            if(position <= -1.0F || position >= 1.0F) {
//                view.setAlpha(0.0F);
//            } else if( position == 0.0F ) {
//                view.setAlpha(1.0F);
//            } else {
//                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
//                view.setAlpha(1.0F - Math.abs(position));
//            }
//        });
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
