package timeroute.androidbaby.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import timeroute.androidbaby.widget.OrientedViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends IBaseFragment<IDiscoveryView, DiscoveryPresenter> implements IDiscoveryView {

    private static final String TAG = "DiscoveryFragment";

    private LinearLayoutManager mLayoutManager;

    private ContentFragmentAdapter contentFragmentAdapter;
    private List<Fragment> fragments = new ArrayList<>();

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
        mLayoutManager = new LinearLayoutManager(getContext());

        mPresenter.getHotFeed();

    }

    @Override
    public void setDataRefresh(boolean refresh) {

    }

    @Override
    public void setFeedList(FeedTimeLine feedTimeLine) {
        for(int i=0;i<feedTimeLine.getFeeds().size();i++){
            fragments.add(CardFragment.newInstance(feedTimeLine.getFeeds().get(i)));
        }
        contentFragmentAdapter = new ContentFragmentAdapter(getFragmentManager(), fragments);
        orientedViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
        orientedViewPager.setOffscreenPageLimit(4);
        orientedViewPager.setPageTransformer(true, new VerticalStackTransformer(getContext()));
        orientedViewPager.setAdapter(contentFragmentAdapter);
    }
}
