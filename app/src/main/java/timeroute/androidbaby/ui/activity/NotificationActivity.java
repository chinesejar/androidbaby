package timeroute.androidbaby.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Like;
import timeroute.androidbaby.ui.adapter.ViewPagerFgAdapter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.fragment.LikeCommentFragment;
import timeroute.androidbaby.ui.presenter.NotificationPresenter;
import timeroute.androidbaby.ui.view.INotificationView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

public class NotificationActivity extends IBaseActivity<INotificationView, NotificationPresenter> implements INotificationView {

    private static final String TAG = "NotificationActivity";

    private SharedPreferenceUtils sharedPreferenceUtils;
    private List<IBaseFragment> fragmentList;

    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewPagerNotification)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");

        fragmentList = new ArrayList<>();
        LikeCommentFragment commentFragment = new LikeCommentFragment();
        commentFragment.setType("comment");
        LikeCommentFragment likeFragment = new LikeCommentFragment();
        likeFragment.setType("like");
        fragmentList.add(commentFragment);
        //fragmentList.add(likeFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(), fragmentList));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected NotificationPresenter createPresenter() {
        return new NotificationPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_notification;
    }
}
