package timeroute.androidbaby.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.anim.VerticalStackTransformer;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.ui.adapter.ContentFragmentAdapter;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.DiscoveryPresenter;
import timeroute.androidbaby.ui.view.IDiscoveryView;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;
import timeroute.androidbaby.widget.OrientedViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends IBaseFragment<IDiscoveryView, DiscoveryPresenter> implements IDiscoveryView {

    private static final String TAG = "DiscoveryFragment";

    private boolean mIsRequestDataRefresh = false;

    private LinearLayoutManager mLayoutManager;

    private ContentFragmentAdapter contentFragmentAdapter;
    private List<Fragment> fragments = new ArrayList<>();

    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.view_pager)
    OrientedViewPager orientedViewPager;

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initView(View rootView) {
        setTracker(TAG);
        mLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public void setRefresh(boolean requestDataRefresh) {
        if (swipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(true);
            mPresenter.getHotFeed();
        }
    }

    @Override
    public void setFeedList(FeedTimeLine feedTimeLine) {
        fragments.clear();
        for(int i=0;i<feedTimeLine.getFeeds().size();i++){
            if(feedTimeLine.getFeeds().get(i).getFeedPic().size() != 0) {
                Fragment fragment = CardFragment.newInstance(feedTimeLine.getFeeds().get(i));
                fragments.add(fragment);
            }
        }
        contentFragmentAdapter = new ContentFragmentAdapter(getFragmentManager(), fragments);
        orientedViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
        orientedViewPager.setOffscreenPageLimit(4);
        orientedViewPager.setPageTransformer(true, new VerticalStackTransformer(getContext()));
        orientedViewPager.setAdapter(contentFragmentAdapter);
        mPresenter.scrollSwipeLayout();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public OrientedViewPager getViewPager() {
        return orientedViewPager;
    }

    @Override
    public ABSwipeRefreshLayout getSwipeLayout() {
        return swipeRefreshLayout;
    }
}
