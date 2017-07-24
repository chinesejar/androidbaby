package timeroute.androidbaby.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.PostActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.FeedPresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

import static timeroute.androidbaby.ui.fragment.MineFragment.PERMISSION_CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends IBaseFragment<IFeedView, FeedPresenter> implements IFeedView {

    private static final String TAG = "FeedFragment";

    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout abSwipeRefreshLayout;
    @Bind(R.id.feed_list)
    RecyclerView feed_list;
    @Bind(R.id.fab_feed)
    FloatingActionButton floatingActionButton;

    @Override
    protected FeedPresenter createPresenter() {
        return new FeedPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_feed;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        feed_list.setLayoutManager(mLayoutManager);
        floatingActionButton.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(getActivity(), PostActivity.class));
            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_CAMERA
                );
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getLatestFeed();
        mPresenter.scrollRecycleView();
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return feed_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }
}
