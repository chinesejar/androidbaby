package timeroute.androidbaby.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.PostActivity;
import timeroute.androidbaby.ui.activity.UserActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.FeedPresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends IBaseFragment<IFeedView, FeedPresenter> implements IFeedView {

    private static final String TAG = "FeedFragment";
    private RxPermissions rxPermissions;

    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout abSwipeRefreshLayout;
    @Bind(R.id.feed_list)
    RecyclerView feed_list;
    //@Bind(R.id.fab_feed)
    //FloatingActionButton floatingActionButton;

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
        rxPermissions = new RxPermissions(getActivity());
//        floatingActionButton.setOnClickListener(view -> {
//            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(granted -> {
//                        if(granted){
//                            startActivity(new Intent(getActivity(), PostActivity.class));
//                        }else {
//                            Toast.makeText(getActivity(), "未开启权限", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
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

    @Override
    public void goToUser(int user_id, String nickname) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }
}
