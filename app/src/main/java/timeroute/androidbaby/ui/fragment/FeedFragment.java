package timeroute.androidbaby.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.ui.activity.FeedDetailActivity;
import timeroute.androidbaby.ui.activity.ImageViewActivity;
import timeroute.androidbaby.ui.activity.UserActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.FeedPresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends IBaseFragment<IFeedView, FeedPresenter> implements IFeedView {

    private static final String TAG = "FeedFragment";
    private static final int REQUEST_REFRESH = 1;

    private boolean mIsRequestDataRefresh = false;

    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout abSwipeRefreshLayout;
    @Bind(R.id.feed_list)
    RecyclerView feed_list;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.scrollRecycleView();
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
        if (abSwipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            abSwipeRefreshLayout.setRefreshing(false);
        } else {
            abSwipeRefreshLayout.setRefreshing(true);
            mPresenter.getLatestFeed();
        }
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
    public void goToUser(int user_id, String nickname, String assignment, String avatar) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        intent.putExtra("assignment", assignment);
        intent.putExtra("avatar", avatar);
        startActivity(intent);
    }

    @Override
    public void goToImageView(int i, String[] images){
        Intent intent = new Intent(getContext(), ImageViewActivity.class);
        intent.putExtra("index", i);
        intent.putExtra("images", images);
        startActivity(intent);
    }

    @Override
    public void goToFeedDetail(Feed feed){
        Intent intent = new Intent(getContext(), FeedDetailActivity.class);
        intent.putExtra("feed", feed);
        startActivityForResult(intent, REQUEST_REFRESH);
        //getContext().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REFRESH:
                setDataRefresh(true);
                break;
        }
    }
}
