package timeroute.androidbaby.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.DiscoveryPresenter;
import timeroute.androidbaby.ui.presenter.FeedPresenter;
import timeroute.androidbaby.ui.view.IDiscoveryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends IBaseFragment<IDiscoveryView, DiscoveryPresenter> {

    private LinearLayoutManager mLayoutManager;

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
    }

}
