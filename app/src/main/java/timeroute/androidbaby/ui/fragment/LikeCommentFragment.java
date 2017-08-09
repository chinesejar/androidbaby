package timeroute.androidbaby.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.ui.activity.FeedDetailActivity;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.LikeCommentPresenter;
import timeroute.androidbaby.ui.view.ILikeCommentView;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeCommentFragment extends IBaseFragment<ILikeCommentView, LikeCommentPresenter> implements ILikeCommentView {

    private static final String TAG = "LikeCommentFragment";

    private boolean mIsRequestDataRefresh = false;
    private LinearLayoutManager mLayoutManager;
    private String type = null;

    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout abSwipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView list;

    @Override
    protected LikeCommentPresenter createPresenter() {
        return new LikeCommentPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.scrollRecycleView();
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_like_comment;
    }

    @Override
    protected void initView(View rootView) {
        setTracker(TAG);
        mLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(mLayoutManager);
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
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
            if(type.equals("like")){
                mPresenter.getLatestLikeNotification();
            }else if(type.equals("comment")) {
                mPresenter.getLatestCommentNotification();
            }
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String getType(){
        return type;
    }

    @Override
    public void goToFeedDetail(Feed feed) {
        Intent intent = new Intent(getContext(), FeedDetailActivity.class);
        intent.putExtra("feed", feed);
        getContext().startActivity(intent);
    }
}
